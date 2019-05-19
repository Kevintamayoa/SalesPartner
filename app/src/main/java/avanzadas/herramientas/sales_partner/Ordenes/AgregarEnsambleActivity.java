package avanzadas.herramientas.sales_partner.Ordenes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesActivity;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesAdapter;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.R;

public class AgregarEnsambleActivity extends AppCompatActivity  {

    private EditText buscar;
    private RecyclerView rc;
    AppDataBase db;
    List<Ensambles> ensamblesList;
    Ordenes orden;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensamble);

        buscar=findViewById(R.id.busqueda_agregarEnsamble);
        rc=findViewById(R.id.agregarEnsamble_rc);
        getSupportActionBar().setTitle("Agregar Ensamble");


        db = AppDataBase.getAppDataBase(getApplicationContext());
         intent= getIntent();

        if((Ordenes) intent.getSerializableExtra("orden") != null){
            orden= (Ordenes) intent.getSerializableExtra("orden");
        }

        rc.setLayoutManager(new LinearLayoutManager(this));
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.productos_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == R.id.BuscarButton){
            busquedaRecyclerView();


            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
    private void busquedaRecyclerView() {
        AdapterAddEnsamble adapter;


        ensamblesList= db.ensamblesDao().getEnsamblesByTextOrderByDescription("%"+buscar.getText().toString()+"%");

        adapter = new AdapterAddEnsamble(ensamblesList);

        rc.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        final int id= item.getGroupId();
        switch ((item.getItemId())) {

            case 0:

                detallesContextMenu(ensamblesList.get(item.getGroupId()));
                return true;
            case 1:

                if((Ordenes) intent.getSerializableExtra("orden") != null){
                    List<Ensambles> e = db.ensamblesDao().getEnsamblesByOrdenes(orden.getId());
                    Ensambles en= ensamblesList.get(item.getGroupId());
                    int check=0;
                    for(Ensambles ensambles:e){
                        if(ensambles.getId()==en.getId()){
                            check++;
                        }
                    }
                    if(check!=0)
                    {
                        android.support.v7.app.AlertDialog.Builder dialog= new android.support.v7.app.AlertDialog.Builder(AgregarEnsambleActivity.this);
                        dialog.setTitle("Esta orden ya contiene el ensamble");
                        dialog.setMessage("Si desea aumentar la cantidad del ensamble, señalelo por medio del numberPicker");
                        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                    }
                    else{
                        AlertDialog.Builder dialog= new AlertDialog.Builder(AgregarEnsambleActivity.this);
                        dialog.setTitle(ensamblesList.get(item.getGroupId()).getDescription());
                        dialog.setMessage("¿Estas seguro que deseas agregar este ensamble a tu carrito?");
                        dialog.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(ensamblesList.get(id).getId());
                                List<Integer> a= new ArrayList<>();
                                List<Integer> ause= new ArrayList<>();

                                for(OrdenesEnsambles oe:db.ordenesensamblesDao().getAllOrdenesEnsambles()){
                                    a.add(oe.getA());
                                }
                                for(int i=0;i<db.ordenesensamblesDao().getAllOrdenesEnsambles().size()+10;i++){
                                    if(!a.contains(i)){
                                        ause.add(i);
                                    }
                                }

                               // db.ordenesensamblesDao().InsertOrdenesEnsamble(new OrdenesEnsambles(ause.get(0),orden.getId(),ensamblesList.get(item.getGroupId()).getId(),1));
                                Toast.makeText(AgregarEnsambleActivity.this, "El ensamble se ha agregado correctamente"
                                        , Toast.LENGTH_SHORT).show();
                                AgregarEnsambleActivity.super.onBackPressed();
                            }
                        });
                        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();}
                    return true;
                }
                else{
                    AlertDialog.Builder dialog= new AlertDialog.Builder(AgregarEnsambleActivity.this);
                    dialog.setTitle(ensamblesList.get(item.getGroupId()).getDescription());
                    dialog.setMessage("¿Estas seguro que deseas agregar este ensamble a tu carrito?");
                    dialog.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(ensamblesList.get(id).getId());
                            List<Integer> a= new ArrayList<>();
                            List<Integer> ause= new ArrayList<>();

                            for(OrdenesEnsambles oe:db.ordenesensamblesDao().getAllOrdenesEnsambles()){
                                a.add(oe.getA());
                            }
                            for(int i=0;i<db.ordenesensamblesDao().getAllOrdenesEnsambles().size()+10;i++){
                                if(!a.contains(i)){
                                    ause.add(i);
                                }
                            }

                           // db.ordenesensamblesDao().InsertOrdenesEnsamble(new OrdenesEnsambles(ause.get(0),orden.getId(),ensamblesList.get(item.getGroupId()).getId(),1));
                            Toast.makeText(AgregarEnsambleActivity.this, "El ensamble se ha agregado correctamente"
                                    , Toast.LENGTH_SHORT).show();

                            AgregarEnsambleActivity.super.onBackPressed();

                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();


                    return  true;
                }










            default:
                return super.onContextItemSelected(item);
        }


    }
    private void detallesContextMenu(Ensambles ensambles){



      final Dialog dialog = new Dialog(AgregarEnsambleActivity.this);
       dialog.setContentView(R.layout.dialogo_ensambles_detalles);
       TextView descripcion=dialog.findViewById(R.id.descripcion_detalles);
       TextView costo=dialog.findViewById(R.id.costo_detalles);
       final RecyclerView recyclerView=dialog.findViewById(R.id.detalles_rc);
        final double cost= db.ensamblesDao().getCostoinEnsamble(ensambles.getId())/100;
       descripcion.setText(ensambles.getDescription());
       costo.setText("$"+cost);

       //Agregar adapter para el rc
        final List<Productos> productos=db.productosDao().getProductosByEnsambles(ensambles.getId());
        AdapterDetallesRc adapter;
        adapter= new AdapterDetallesRc(productos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo2 = new  AlertDialog.Builder(AgregarEnsambleActivity.this);


                dialogo2.setTitle("Descripcion: "+ productos.get(recyclerView.getChildAdapterPosition(v)).getDescription())
                        .setMessage(
                                "Categoría: " +db.productsCategoryDao().getCategoryById(productos.get(recyclerView.getChildAdapterPosition(v)).getCategory_id()) +
                                        "\n\n" +
                                        "Precio " + productos.get(recyclerView.getChildAdapterPosition(v)).getPrecio() +
                                        "\n\n" +
                                        "Cantidad en stock:" + productos.get(recyclerView.getChildAdapterPosition(v)).getCantidad()
                        )
                        .show();
            }
        });
        dialog.show();

    }
}
