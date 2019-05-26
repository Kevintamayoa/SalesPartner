package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import avanzadas.herramientas.sales_partner.AppController;
import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesActivity;
import avanzadas.herramientas.sales_partner.Ensambles.ViewModelEnsambles;
import avanzadas.herramientas.sales_partner.R;

public class AgregarNuevaOrdenActivity extends AppCompatActivity {

    private RecyclerView rc;
    private Spinner spinner;
    final static int ADDENSAMBLE = 125;
    List<Ensambles> ensamblesList;
    AppDataBase db;
    AdapterAddOrder adapterAddOrder;
    View view;
    AdapterAddOrder.ViewHolder viewHolder;
    Ordenes orden;
    ViewModelAddOrden model;
    int ids = 0;
    String customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nueva_orden);

        getSupportActionBar().setTitle("Nueva Orden");

        db = AppDataBase.getAppDataBase(getApplicationContext());
        model = ViewModelProviders.of(AgregarNuevaOrdenActivity.this).get(ViewModelAddOrden.class);
        //view = LayoutInflater.from(this).inflate(R.layout.agregarorden_rc,rc,false);

        rc = findViewById(R.id.agregarorden_rc);
        spinner = findViewById(R.id.SpinnerSelectCliente);

        rc.setLayoutManager(new LinearLayoutManager(this));
        spinner.setAdapter(AdapterSpinner(db.clientesDao().getAllClientesByIdASC()));
        if (model.getEnsamblesList() == null) {
            ensamblesList = new ArrayList<>();
        } else {
            ensamblesList = model.getEnsamblesList();
            AdapterAddOrder adapterAddOrder = new AdapterAddOrder(ensamblesList);
            rc.setAdapter(adapterAddOrder);
        }
    }

    public ArrayAdapter<String> AdapterSpinner(List<Clientes> clientes) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        for (Clientes pc : clientes) {
            arrayAdapter.add(pc.getId() + "," + pc.getFirst_name() + " " + pc.getLast_name());
        }
        return arrayAdapter;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editarorden_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.saveButton) {
            AddOrden();
            AlertDialog.Builder dialog = new AlertDialog.Builder(AgregarNuevaOrdenActivity.this);
            dialog.setTitle("Orden guardada");
            dialog.setMessage("La orden ha sido guardada correctamente"
            );
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AgregarNuevaOrdenActivity.super.onBackPressed();

                }
            });

            dialog.show();
            return true;
        }
        if (id == R.id.AddButton) {
            Intent intent = new Intent(this, AgregarEnsambleActivity.class);
            intent.putExtra("orden", orden);

            startActivityForResult(intent, ADDENSAMBLE);


        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch ((item.getItemId())) {

            case 0:
                AlertDialog.Builder dialog = new AlertDialog.Builder(AgregarNuevaOrdenActivity.this);
                dialog.setTitle("Estas seguro?");
                dialog.setMessage("¿En serio estas seguro que desea eliminar " +
                        ensamblesList.get(item.getGroupId()).getDescription()
                        + " de su pedido?");
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ensamblesList.remove(ensamblesList.get(item.getGroupId()));
                        adapterAddOrder = new AdapterAddOrder(ensamblesList);
                        rc.setAdapter(adapterAddOrder);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ADDENSAMBLE) {
            ensamblesList.add(db.ensamblesDao().getEnsamblesByID(resultCode));
            adapterAddOrder = new AdapterAddOrder(ensamblesList);


            rc.setAdapter(adapterAddOrder);
            model.setEnsamblesList(ensamblesList);
            //postAndNotifyAdapter(new Handler(),rc);

        }


        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AgregarNuevaOrdenActivity.this);
        dialog.setTitle("Estas seguro?");
        dialog.setMessage("¿Estas seguro de abandonar sin guardar?");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AgregarNuevaOrdenActivity.super.onBackPressed();

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

    private void AddOrden() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();

        final String fecha = dateFormat.format(date);
        List<Integer> a = new ArrayList<>();
        List<Integer> ause = new ArrayList<>();
        for (OrdenesEnsambles oe : db.ordenesensamblesDao().getAllOrdenesEnsambles()) {
            a.add(oe.getA());
        }
        for (int i = 0; i < db.ordenesensamblesDao().getAllOrdenesEnsambles().size() + 15; i++) {
            if (!a.contains(i)) {
                ause.add(i);

            }
        }

        for (int i = 0; i < ensamblesList.size(); i++) {

            if (i == 0) {
                ids = db.orderDao().getAllOrdenesByDate().size();
                customer_id = String.valueOf(spinner.getSelectedItemPosition());
                db.orderDao().InsrtOrdenes(new Ordenes(ids, 0, spinner.getSelectedItemPosition(), fecha, ""));
                //Aqui mando al servidor la orden ^

                String url = "192.168.43.235:3000/orders/add";


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("registra")) {

                            Toast.makeText(getApplicationContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se ha registrado ", Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ", "" + response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String id = String.valueOf(ids);
                        String date = fecha;
                        String change_log = "";
                        String customer_id = String.valueOf(spinner.getSelectedItemPosition());

                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("id", id);
                        parametros.put("date", date);
                        parametros.put("change_log", change_log);
                        parametros.put("customer_id", customer_id);

                        return parametros;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);

                viewHolder = (AdapterAddOrder.ViewHolder) rc.findViewHolderForAdapterPosition(i);

                view = viewHolder.itemView;

                TextView tv = (TextView) view.findViewById(R.id.cantidad_ensambles_add);


//
                db.ordenesensamblesDao().InsertOrdenesEnsamble(new OrdenesEnsambles(
                        ause.get(i), ids, ensamblesList.get(i).getId(), Integer.parseInt(tv.getText().toString())));
                //Aqui mando al servidor los ordenes asambles ^
            }

        }


    }
}