package avanzadas.herramientas.sales_partner.Productos;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.R;


public class ProductosActivity extends AppCompatActivity {

    private Spinner BuscarSpinner;
    private EditText BuscarEditText;
    private RecyclerView recyclerView;
    ProductosDao productosDao;
    List<Productos> productosArrayList;
    List<ProductCategory> productCategories;
    ViewModelProducts model;

    @Override
    public void onBackPressed() {

        setResult(37);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        findView();
        getSupportActionBar().setTitle("Productos");


        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
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
