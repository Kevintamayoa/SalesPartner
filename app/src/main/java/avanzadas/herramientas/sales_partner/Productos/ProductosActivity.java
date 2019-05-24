package avanzadas.herramientas.sales_partner.Productos;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import avanzadas.herramientas.sales_partner.AppController;
import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.R;
import avanzadas.herramientas.sales_partner.ServerConect;
import avanzadas.herramientas.sales_partner.Vendedores.CustomJSONObjectRequest;
import avanzadas.herramientas.sales_partner.Vendedores.VolleyController;


public class ProductosActivity extends AppCompatActivity {

    private Spinner BuscarSpinner;
    private EditText BuscarEditText;
    private RecyclerView recyclerView;
    ProductosDao productosDao;
    List<Productos> productosArrayList;
    List<ProductCategory> productCategories;
    ViewModelProducts model;
    List<Productos> productos;

    @Override
    public void onBackPressed() {

        setResult(37);
        super.onBackPressed();
    }

    private static String TAG = ProductosActivity.class.getSimpleName();
    private String url = "http://192.168.0.12:3000/products";
    private static String KEY_SUCCESS = "success";
    private static String KEY_USERID = "userid";
    AppDataBase db;

    private void makeJsonArrayRequest() {


        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                int id = person.getInt("id");

                                int cat = person.getInt("category_id");
                                String desc = person.getString("description");
                                int price = person.getInt("price");
                                int qty = person.getInt("qty");
                                ProductosDao productosDao = db.productosDao();
                                productosDao.insertProductId(new Productos(id, cat, desc, price, qty));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(req);


}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        findView();
      db = AppDataBase.getAppDataBase(getApplicationContext());
makeJsonArrayRequest();


        getSupportActionBar().setTitle("Productos");
        //Toast.makeText(this, productos.get(0).de, Toast.LENGTH_SHORT).show();

        model = ViewModelProviders.of(ProductosActivity.this).get(ViewModelProducts.class);
        productosDao = db.productosDao();
        //List<Productos> productosList = productosDao.getAllProductos();
        ProductsCategoryDao productsCategoryDao = db.productsCategoryDao();
        productCategories = productsCategoryDao.getAllCategory();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BuscarSpinner.setAdapter(opcionesSpinner(productCategories));
        if(model.getProductosList()!=null){
            ProductAdapter adapter= new ProductAdapter(model.getProductosList());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double precioDouble = (double) productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getPrecio() / 100;
                    //Toast.makeText(ProductosActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ProductosActivity.this);
                    dialog.setTitle("Precio:\n$" + precioDouble)
                            .setMessage(
                                    "Categoría:\n" + productCategories.get(productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getCategory_id()).getDescription().toUpperCase() +
                                            "\n\n" +
                                            "Descripción:\n" + productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getDescription().toUpperCase() +
                                            "\n\n" +
                                            "Cantidad en stock:\n" + productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getCantidad()
                            )
                            .show();
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    public ArrayAdapter<String> opcionesSpinner(List<ProductCategory> categories) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        arrayAdapter.add("Todas");
        for (ProductCategory pc : categories) {
            arrayAdapter.add(pc.getDescription());
        }
        return arrayAdapter;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productos_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.BuscarButton) {
            //Toast.makeText(this, "Funciona", Toast.LENGTH_SHORT).show();
            busquedaRecyclerView();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void busquedaRecyclerView() {
        ProductAdapter adapter;// = new ProductAdapter(null);
        productosArrayList = new ArrayList<>();

        if (BuscarSpinner.getSelectedItem().toString().contentEquals("Todas") && BuscarEditText.getText().toString().contentEquals("")) {
            productosArrayList = productosDao.getAllProductosOrderByTextASC();
        } else if (!BuscarSpinner.getSelectedItem().toString().contentEquals("Todas") && BuscarEditText.getText().toString().contentEquals("")) {
            productosArrayList = productosDao.getProductosByCategory(BuscarSpinner.getSelectedItemPosition() - 1);
        } else if (BuscarSpinner.getSelectedItem().toString().contentEquals("Todas") && !BuscarEditText.getText().toString().contentEquals("")) {
            String text= BuscarEditText.getText().toString();
            productosArrayList = productosDao.getProductosByText("%" + BuscarEditText.getText().toString() + "%");
        } else if (!BuscarSpinner.getSelectedItem().toString().contentEquals("Todas") && !BuscarEditText.getText().toString().contentEquals("")) {
            productosArrayList = productosDao.getProductosByTextAndCategory("%" + BuscarEditText.getText().toString() + "%", BuscarSpinner.getSelectedItemPosition() - 1);
        }
        model.setProductosList(productosArrayList);

        adapter = new ProductAdapter(productosArrayList);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double precioDouble = (double) productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getPrecio() / 100;
                //Toast.makeText(ProductosActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductosActivity.this);
                dialog.setTitle("Precio:\n$" + precioDouble)
                        .setMessage(
                                "Categoría:\n" + productCategories.get(productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getCategory_id()).getDescription().toUpperCase() +
                                        "\n\n" +
                                        "Descripción:\n" + productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getDescription().toUpperCase() +
                                        "\n\n" +
                                        "Cantidad en stock:\n" + productosArrayList.get(recyclerView.getChildAdapterPosition(v)).getCantidad()
                        )
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        BuscarSpinner = findViewById(R.id.BuscarSpinner);
        BuscarEditText = findViewById(R.id.BuscarEditText);
        recyclerView = findViewById(R.id.recyclerView);
    }
}
