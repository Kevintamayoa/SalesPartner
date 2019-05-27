package avanzadas.herramientas.sales_partner.Ordenes;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import avanzadas.herramientas.sales_partner.AppController;
import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesDao;
import avanzadas.herramientas.sales_partner.R;

public class EditarOrdenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView clienteTV;
    Clientes cliente;
    Ordenes orden;
    AppDataBase db;
    List<Ensambles> ensamblesList;
    AdapterEditOrdenes adapterEditOrdenes;
    private final int ADDENSAMBLE=130;
    View view;
    AdapterEditOrdenes.ViewHolder viewHolder;
    ViewModelEditOrden model;
    List<Ensambles> ensambleParaEliminar;
    List<Ensambles> ensamblesParaGuardar;
    private String urlOrdenesAssemblies = "http://192.168.0.9:3000/orderassemblies";
    private static String TAG = EditarOrdenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_orden);

        ensambleParaEliminar= new ArrayList<>();
        ensamblesParaGuardar= new ArrayList<>();
        db = AppDataBase.getAppDataBase(getApplicationContext());
        model = ViewModelProviders.of(EditarOrdenActivity.this).get(ViewModelEditOrden.class);

        if(model.getEnsamblesParaEliminar()!=null){
            ensambleParaEliminar= model.getEnsamblesParaEliminar();
        }
        if(model.getEnsamblesParaGuardar()!=null){
            ensamblesParaGuardar= model.getEnsamblesParaGuardar();
        }
        FindView();
        clienteTV.setText(cliente.getFirst_name()+" "+cliente.getLast_name());
        makeJsonArrayRequestOrdersAssembly();
        //RecyclerView();

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch ((item.getItemId())) {

            case 0:
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditarOrdenActivity.this);
                dialog.setTitle("Estas seguro?");
                dialog.setMessage("¿En serio estas seguro que desea eliminar " +
                        ensamblesList.get(item.getGroupId()).getDescription()
                        +" de su pedido?");
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrdenesEnsambles oe=db.ordenesensamblesDao().getOrdenEnsambleByOrdenyEnsamble(orden.getId(),
                                ensamblesList.get(item.getGroupId()).getId());

                        ensamblesList.remove(ensamblesList.get(item.getGroupId()));
                    ensambleParaEliminar.add(ensamblesList.get(item.getGroupId()));
                       adapterEditOrdenes=new AdapterEditOrdenes(ensamblesList, orden);
                       recyclerView.setAdapter(adapterEditOrdenes);

                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editarorden_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == R.id.AddButton){
            Intent intent=new Intent(this,AgregarEnsambleActivity.class);
            intent.putExtra("orden",orden);
            startActivityForResult(intent,ADDENSAMBLE);
        }
        else if(id == R.id.saveButton){

        EditOrden();
            setResult(0);
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarOrdenActivity.this);
            dialog.setTitle("Orden guardada");
            dialog.setMessage("La orden ha sido editada correctamente"
            );
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditarOrdenActivity.super.onBackPressed();

                }
            });

            dialog.show();
            return true;





           //if(oe.getQty()==1){db.ordenesensamblesDao().DeleatEnsamble(oe);}
           //else{db.ordenesensamblesDao().UdapteOrdenEnsamble(oe.getA()); }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void FindView(){
        cliente=(Clientes) getIntent().getSerializableExtra("cliente");
        orden=(Ordenes)getIntent().getSerializableExtra("orden");
        clienteTV=findViewById(R.id.Cliente_edit_tv);
        recyclerView=findViewById(R.id.recyclerView_edit);
    }
    public void RecyclerView(){
        List<OrdenesEnsambles> oensambles = new ArrayList<>();
        EnsamblesDao d= db.ensamblesDao();
        List<Ensambles> a= new ArrayList<>();
        if(model.getEnsamblesList()==null) {
            ensamblesList= new ArrayList<>();
            OrdenesEnsamblesDao dao= db.ordenesensamblesDao();
            oensambles= dao.getAllOrdenesEnsambles();
            for(OrdenesEnsambles o: oensambles){
                if(o.getId()==orden.getId()) {
                    ensamblesList.add(d.getEnsamblesByCategory(o.getAssembly_id()));
                }
            }

        }
        else{
            ensamblesList=model.getEnsamblesList();}
        adapterEditOrdenes= new AdapterEditOrdenes(ensamblesList,orden);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterEditOrdenes);

    }
    public void RecyclerViewOff(){
        if(model.getEnsamblesList()==null) {
            ensamblesList = new ArrayList<>();


            for (Ensambles ensambles : db.ensamblesDao().getEnsamblesByOrdenes(orden.getId())) {
                ensamblesList.add(ensambles);

            }
        }
        else{ensamblesList=model.getEnsamblesList();}
        adapterEditOrdenes= new AdapterEditOrdenes(ensamblesList,orden);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterEditOrdenes);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==ADDENSAMBLE){


                ensamblesList.add(db.ensamblesDao().getEnsamblesByID(resultCode));
                ensamblesParaGuardar.add(db.ensamblesDao().getEnsamblesByID(resultCode));
                model.setEnsamblesList(ensamblesList);
                adapterEditOrdenes = new AdapterEditOrdenes(ensamblesList, orden);
                recyclerView.setAdapter(adapterEditOrdenes);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    private void EditOrden(){

        db.ordenesensamblesDao().DeleleOrderAssemblieById(orden.getId());

        List<Integer> a= new ArrayList<>();
        List<Integer> ause= new ArrayList<>();
        for(OrdenesEnsambles oe:db.ordenesensamblesDao().getAllOrdenesEnsambles()){
            a.add(oe.getA());
        }
        for(int i=0;i<db.ordenesensamblesDao().getAllOrdenesEnsambles().size()+recyclerView.getLayoutManager().getChildCount()+1;i++){
            if(!a.contains(i)){
                ause.add(i);
            }
        }

        for(int i=0;i<recyclerView.getLayoutManager().getChildCount();i++){
           viewHolder=(AdapterEditOrdenes.ViewHolder)recyclerView.findViewHolderForAdapterPosition(i);
           view=viewHolder.itemView;
           //int id=db.orderDao().getAllOrdenesByDate().size();
           TextView tv= (TextView) view.findViewById(R.id.cantidad_ensambles_add);
           //db.orderDao().InsrtOrdenes(new Ordenes(id,0,spinner.getSelectedItemPosition(),fecha,""));
           //db.ordenesensamblesDao().InsertOrdenesEnsamble(new OrdenesEnsambles(
           //        db.ordenesensamblesDao().getAllOrdenesEnsambles().size()+1,id,ensamblesList.get(i).getId(),Integer.parseInt(tv.getText().toString())
           //));
            db.ordenesensamblesDao().InsertOrdenesEnsamble(new OrdenesEnsambles(
                    ause.get(i),
                    orden.getId(),ensamblesList.get(i).getId(),Integer.parseInt(tv.getText().toString())));
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditarOrdenActivity.this);
        dialog.setTitle("Estas seguro?");
        dialog.setMessage("¿Estas seguro de abandonar sin guardar?");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditarOrdenActivity.super.onBackPressed();

            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();


        // super.onBackPressed();
    }
    private void makeJsonArrayRequestOrdersAssembly() {
    OrdenesEnsamblesDao d= db.ordenesensamblesDao();


        JsonArrayRequest req = new JsonArrayRequest(urlOrdenesAssemblies,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<OrdenesEnsambles> ordenesEnsambles= new ArrayList<>();
                        Log.d(TAG, response.toString());
                        try {
                            OrdenesEnsamblesDao Dao = db.ordenesensamblesDao();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                int a = person.getInt("a");
                                int id = person.getInt("id");
                                int id1 = person.getInt("assembly_id");
                                int id2 = person.getInt("qty");

                                ordenesEnsambles.add(new OrdenesEnsambles(a,id,id1,id2));

                            }
                            if(ordenesEnsambles!=Dao.getAllOrdenesEnsambles()){
                                for(OrdenesEnsambles c: Dao.getAllOrdenesEnsambles()){
                                    Dao.DeleatEnsamble(c);
                                }
                                for(OrdenesEnsambles c:ordenesEnsambles){
                                    Dao.InsertOrdenesEnsamble(c);
                                }

                            }


                            RecyclerView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            RecyclerView();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//RecyclerView();
                RecyclerView();
            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }
}
