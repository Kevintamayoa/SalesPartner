package avanzadas.herramientas.sales_partner.Reportes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Productos.ProductCategory;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.Productos.ProductsCategoryDao;
import avanzadas.herramientas.sales_partner.R;

public class ProductosFaltantesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductosFaltantesAdapter adapter;
    ProductosDao productosDao;
    List<Productos> productosList;
    List<ProductCategory> productCategories;

    @Override
    public void onBackPressed() {
        setResult(101);

        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_faltantes);

        findView();
        getSupportActionBar().setTitle("Productos Faltantes");

        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());

        productosDao = db.productosDao();
        ProductsCategoryDao productsCategoryDao = db.productsCategoryDao();
        productCategories = productsCategoryDao.getAllCategory();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShowRecyclerView();
    }

    private void ShowRecyclerView(){

        productosList = new ArrayList<>();
        adapter = new ProductosFaltantesAdapter(productosList);
        //CREAR MÉTODO-DAO QUE DEVUELVA PRODUCTOS FALTANTES
        //productosList = productosDao.getAllmissingProducts();
        productosList = productosDao.getAllProductos();
        final List<Productos> nuevaLista = new ArrayList<>();
        for(Productos p : productosList){
            if(productosDao.getAllmissingProducts(p.getId()) > 0){
                nuevaLista.add(p);
            }
        }

        adapter = new ProductosFaltantesAdapter(nuevaLista);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LLENAR
                Toast.makeText(ProductosFaltantesActivity.this, "" + nuevaLista.get(recyclerView.getChildLayoutPosition(v)).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
    }
}
