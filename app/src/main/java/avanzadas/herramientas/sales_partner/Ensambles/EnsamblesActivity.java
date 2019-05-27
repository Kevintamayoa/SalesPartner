package avanzadas.herramientas.sales_partner.Ensambles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Productos.ProductAdapter;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.R;

public class EnsamblesActivity extends AppCompatActivity {

    private Spinner BuscarSpinner;
    private EditText BuscarEditText;
    private RecyclerView recyclerView;
    ProductosEnsamblesDao pedao;
    EnsamblesDao ensamblesdao;
    //List<EnsamblesDao> ensamblesDao;
    List<Ensambles> ensamblesList;
    List<EnsamblesProducts> ensamblesProducts;

    ViewModelEnsambles model;
    AppDataBase db;
    static List<Productos> productosArrayList=new ArrayList<>();

    @Override
    public void onBackPressed() {

        setResult(37);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensambles);

        getSupportActionBar().setTitle("Ensambles");

        findView();



        model = ViewModelProviders.of(EnsamblesActivity.this).get(ViewModelEnsambles.class);

         db = AppDataBase.getAppDataBase(getApplicationContext());

        ensamblesdao = db.ensamblesDao();
        ensamblesList = ensamblesdao.getAllEnsambles();

        pedao = db.enamblesProductsDao();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        BuscarSpinner.setAdapter(opcionesSpinner(ensamblesList));
        if(model.getEnsamblesList()!=null){

            EnsamblesAdapter adapter = new EnsamblesAdapter(model.getEnsamblesList());
            recyclerView.setAdapter(adapter);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(EnsamblesActivity.this, "funciona", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog(EnsamblesActivity.this);
                    dialog.setContentView(R.layout.dialog_ensambles);
                    TextView descripcion_ensamble=dialog.findViewById(R.id.descripcion_ensamble);
                    TextView costo_detalles=dialog.findViewById(R.id.costo_detalles);
                    final RecyclerView rc= dialog.findViewById(R.id.detalles_rc);

                    descripcion_ensamble.setText(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getDescription());
                    costo_detalles.setText("Costo del ensamble: $"+(double)db.ensamblesDao().getCostoinEnsamble(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId()) / 100);
                    ProductAdapter adapter1=
                            new ProductAdapter(db.productosDao()
                                    .getProductosByEnsambles(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId()));
                    rc.setAdapter(adapter1);
                    productosArrayList= db.productosDao()
                            .getProductosByEnsambles(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId());
                    adapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double precioDouble = (double) productosArrayList.get(rc.getChildAdapterPosition(v)).getPrecio() / 100;
                            //Toast.makeText(ProductosActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EnsamblesActivity.this);
                            dialog.setTitle("Precio:\n$" + precioDouble)
                                    .setMessage(
                                            "Categoría:\n" +db.productsCategoryDao().getCategoryById(rc.getChildAdapterPosition(v)).getDescription() +
                                                    "\n\n" +
                                                    "Descripción:\n" + productosArrayList.get(rc.getChildAdapterPosition(v)).getDescription().toUpperCase() +
                                                    "\n\n" +
                                                    "Cantidad en stock:\n" + productosArrayList.get(rc.getChildAdapterPosition(v)).getCantidad()
                                    )
                                    .show();
                        }
                    });

                    rc.setLayoutManager(new LinearLayoutManager(EnsamblesActivity.this));
                    dialog.show();
                }
            });
        }
    }

    public ArrayAdapter<String> opcionesSpinner(List<Ensambles> ensamble) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.add("Todas");
        for (Ensambles pc : ensamble) {
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
        EnsamblesAdapter adapter;
        ensamblesList = new ArrayList<>();


        if (BuscarSpinner.getSelectedItem() == "Todas" && BuscarEditText.getText().toString() == "") {
            ensamblesList = ensamblesdao.getAllEnsambles();
            //recyclerView.setAdapter(new EnsamblesAdapter(ensamblesdao.getAllEnsambles()));
        } else if (BuscarSpinner.getSelectedItem() != "Todas" && BuscarEditText.getText().toString() == "") {
            ensamblesList.add(ensamblesdao.getEnsamblesByCategory(BuscarSpinner.getSelectedItemPosition() - 1));
            //recyclerView.setAdapter(new EnsamblesAdapter(ensamblesdao.getEnsamblesByCategory(BuscarSpinner.getSelectedItemPosition()-1)));
        } else if (BuscarSpinner.getSelectedItem() == "Todas" && BuscarEditText.getText().toString() != "") {
            ensamblesList = ensamblesdao.getEnsamblesByText("%" + BuscarEditText.getText().toString() + "%");
            //recyclerView.setAdapter(new EnsamblesAdapter(ensamblesdao.getEnsamblesByText("%"+BuscarEditText.getText().toString()+"%")));
        } else if (BuscarSpinner.getSelectedItem() != "Todas" && BuscarEditText.getText().toString() != "") {
            ensamblesList = ensamblesdao.getEnsamblesyTextAndCategory("%" + BuscarEditText.getText().toString() + "%", BuscarSpinner.getSelectedItemPosition() - 1);
            //recyclerView.setAdapter(new EnsamblesAdapter(ensamblesdao.getEnsamblesyTextAndCategory("%"+BuscarEditText.getText().toString()+"%", BuscarSpinner.getSelectedItemPosition()-1)));
        }
        model.setEnsamblesList(ensamblesList);

        adapter = new EnsamblesAdapter(ensamblesList);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(EnsamblesActivity.this, "funciona", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(EnsamblesActivity.this);
               dialog.setContentView(R.layout.dialog_ensambles);
                TextView descripcion_ensamble=dialog.findViewById(R.id.descripcion_ensamble);
                TextView costo_detalles=dialog.findViewById(R.id.costo_detalles);
                final RecyclerView rc= dialog.findViewById(R.id.detalles_rc);

                descripcion_ensamble.setText(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getDescription());
               costo_detalles.setText("Costo del ensamble: $"+(double)db.ensamblesDao().getCostoinEnsamble(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId()) / 100);
                ProductAdapter adapter1=
                        new ProductAdapter(db.productosDao()
                                .getProductosByEnsambles(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId()));
                rc.setAdapter(adapter1);
               productosArrayList= db.productosDao()
                       .getProductosByEnsambles(ensamblesList.get(recyclerView.getChildAdapterPosition(v)).getId());
                adapter1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double precioDouble = (double) productosArrayList.get(rc.getChildAdapterPosition(v)).getPrecio() / 100;
                        //Toast.makeText(ProductosActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EnsamblesActivity.this);
                        dialog.setTitle("Precio:\n$" + precioDouble)
                                .setMessage(
                                        "Categoría:\n" +db.productsCategoryDao().getCategoryById(rc.getChildAdapterPosition(v)).getDescription() +
                                                "\n\n" +
                                                "Descripción:\n" + productosArrayList.get(rc.getChildAdapterPosition(v)).getDescription().toUpperCase() +
                                                "\n\n" +
                                                "Cantidad en stock:\n" + productosArrayList.get(rc.getChildAdapterPosition(v)).getCantidad()
                                )
                                .show();
                    }
                });

rc.setLayoutManager(new LinearLayoutManager(EnsamblesActivity.this));
                        dialog.show();
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
