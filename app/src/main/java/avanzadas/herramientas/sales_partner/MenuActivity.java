package avanzadas.herramientas.sales_partner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesActivity;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesActivity;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesDao;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Ensambles.ProductosEnsamblesDao;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesActivity;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsambles;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsamblesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductCategory;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.Productos.ProductsCategoryDao;
import avanzadas.herramientas.sales_partner.Reportes.ReportesActivity;
import avanzadas.herramientas.sales_partner.Vendedores.Vendedores;
import avanzadas.herramientas.sales_partner.Vendedores.vendedoresDao;

public class MenuActivity extends AppCompatActivity{

    private static final int DURATION_ANIMATION = 380;
    private static String TAG = MainActivity.class.getSimpleName();
    private String urlproducts = "http://192.168.0.9:3000/products";
   // private String urlproductscategorys = "http://192.168.0.12:3000/productcategories";
    private String urlAssemblies = "http://192.168.0.9:3000/assemblies";
    private String urlAssembliesProducts = "http://192.168.0.9:3000/assemblyproducts";
    private String urlOrdenes = "http://192.168.0.9:3000/orders";
    private String urlOrdenesAssemblies = "http://192.168.0.9:3000/orderassemblies";
    private String urlClientes = "http://192.168.0.9:3000/customers";

    private static String KEY_SUCCESS = "success";
    private static String KEY_USERID = "userid";
    private void makeJsonArrayRequestProductos(final List<Productos> productoslocales) {
        JsonArrayRequest req = new JsonArrayRequest(urlproducts,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ProductosDao productosDao = db.productosDao();
                            if(productosDao.getAllProductos()==null||productosDao.getAllProductos().size()==0){
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                int id = person.getInt("id");
                                int cat = person.getInt("category_id");
                                String desc = person.getString("description");
                                int price = person.getInt("price");
                                int qty = person.getInt("qty");

                                    productosDao.insertProductId(new Productos(id, cat, desc, price, qty));

                                    //productoslocales.remove(new Productos(id, cat, desc, price, qty));

                            }
                            for(Productos obj :productoslocales){
                                productosDao.deleteProductId(obj);
                            }}

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

    private void makeJsonArrayRequestEnsambles(final List<Ensambles> ensambleslocales) {
        JsonArrayRequest req = new JsonArrayRequest(urlAssemblies,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            EnsamblesDao ensamblesDao = db.ensamblesDao();
                            if(ensamblesDao.getAllEnsambles()==null||ensamblesDao.getAllEnsambles().size()==0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject person = (JSONObject) response.get(i);
                                    int id = person.getInt("id");
                                    String desc = person.getString("description");

                                        ensamblesDao.InsertEnsamble(new Ensambles(id, desc));

                                       // ensambleslocales.remove(new Ensambles(id, desc));

                                }
                                for (Ensambles obj : ensambleslocales) {
                                    ensamblesDao.DeleteEnsamble(obj);
                                }
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
    private void makeJsonArrayRequestEnsamblesProducts(final List<EnsamblesProducts> locales) {
        JsonArrayRequest req = new JsonArrayRequest(urlAssembliesProducts,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ProductosEnsamblesDao Dao = db.enamblesProductsDao();
                            if(Dao.getAllAssemblyProducts()==null||Dao.getAllAssemblyProducts().size()==0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject person = (JSONObject) response.get(i);
                                    int a = person.getInt("a");
                                    int id = person.getInt("id");
                                    int product_id = person.getInt("product_id");
                                    int qty = person.getInt("qty");

                                        Dao.InsertAssemblyProducts(new EnsamblesProducts(a, id, product_id, qty));

                                       // locales.remove(new EnsamblesProducts(a, id, product_id, qty));

                                }
                                for (EnsamblesProducts obj : locales) {
                                    Dao.DeleteAssemblyProducts(obj);
                                }
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
    private void makeJsonArrayRequestClientes(final List<Clientes> locales) {
        JsonArrayRequest req = new JsonArrayRequest(urlClientes,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Clientes> clientes= new ArrayList<>();
                        Log.d(TAG, response.toString());
                        try {
                            ClientesDao Dao = db.clientesDao();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                int id = person.getInt("id");
                                String id1 = person.getString("first_name");
                                String id2 = person.getString("last_name");
                                String id3 = person.getString("address");
                                String id4 = person.getString("phone1");
                                String id5 = person.getString("phone2");
                                String id6 = person.getString("phone3");
                                String id7 = person.getString("e_mail");
                                int id8 = person.getInt("state");
                                    clientes.add(new Clientes(id,id1,id2,id3,id4,id5,id6,id7,id8));
                                    //Dao.InsertClientes(new Clientes(id,id1,id2,id3,id4,id5,id6,id7));

                            }
                            if(clientes!=Dao.getAllClientes()){
                                for(Clientes c: Dao.getAllClientes()){
                                    Dao.DeleteClientes(c);
                                }
                                for(Clientes c:clientes){
                                    Dao.InsertClientes(c);
                                }
                            }
                           // for(Clientes obj :locales){
                           //     Dao.DeleteClientes(obj);
                           // }

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
    private void makeJsonArrayRequestOrders(final List<Ordenes> locales) {
        JsonArrayRequest req = new JsonArrayRequest(urlOrdenes,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Ordenes> ordenes= new ArrayList<>();
                        Log.d(TAG, response.toString());
                        try {
                            OrdenesDao Dao = db.orderDao();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                int id = person.getInt("id");
                                int id1 = person.getInt("status_id");
                                int id2 = person.getInt("customer_id");
                                String id3 = person.getString("date");
                                String id4 = person.getString("change_log");


                                    ordenes.add(new Ordenes(id,id1,id2,id3,id4));

                                   // locales.remove(new Ordenes(id,id1,id2,id3,id4));

                            }
                            if(ordenes!=Dao.getAllOrdenes()){
                                for(Ordenes c: Dao.getAllOrdenes()){
                                    Dao.DeleteOrden(c);
                                }
                                for(Ordenes c:ordenes){
                                    Dao.InsrtOrdenes(c);
                                }
                            }
                            //for(Ordenes obj :locales){
                            //    Dao.DeleteOrden(obj);
                            //}

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
    private void makeJsonArrayRequestOrdersAssembly(final List<OrdenesEnsambles> locales) {
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
    private ImageButton productosImageButton;
    private ImageButton ensamblesImageButton;
    private ImageButton clientesImageButton;
    private ImageButton ordenesImageButton;
    private ImageButton reportesImageButton;
    private LinearLayout linearActivity;
    private final int requestcode=11;
    private Vendedores vendedor;
    private OrdenesDao ordenesDao;
    AppDataBase db;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==requestcode&&resultCode==37){
            ensamblesImageButton.setVisibility(View.VISIBLE);
            clientesImageButton.setVisibility(View.VISIBLE);
            ordenesImageButton.setVisibility(View.VISIBLE);
            reportesImageButton.setVisibility(View.VISIBLE);
            productosImageButton.setVisibility(View.VISIBLE);


        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Stetho.initializeWithDefaults(this);
        findView();

        getSupportActionBar().setTitle("Sales Partner");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Intent intent= getIntent();
        vendedor= (Vendedores) intent.getSerializableExtra("vendedor");
        getSupportActionBar().setTitle(vendedor.getFirst_name() + " "+ vendedor.getLast_name());



         db = AppDataBase.getAppDataBase(getApplicationContext());
        ProductosDao productosDao = db.productosDao();
        List<Productos> productoslocales=productosDao.getAllProductos();
        makeJsonArrayRequestProductos(productoslocales);

     //   ProductsCategoryDao productsCategoryDao = db.productsCategoryDao();
     //   List<ProductCategory> productCategories=productsCategoryDao.getAllCategory();
     //   makeJsonArrayRequestProductCategories(productCategories);

        EnsamblesDao ensamblesDao = db.ensamblesDao();
        List<Ensambles> ensambles=ensamblesDao.getAllEnsambles();
        makeJsonArrayRequestEnsambles(ensambles);

        ProductosEnsamblesDao productosEnsamblesDao = db.enamblesProductsDao();
        List<EnsamblesProducts> ensamblesProducts=productosEnsamblesDao.getAllAssemblyProducts();
        makeJsonArrayRequestEnsamblesProducts(ensamblesProducts);

        ClientesDao clientesDao = db.clientesDao();
        List<Clientes> clientes=clientesDao.getAllClientes();
        makeJsonArrayRequestClientes(clientes);

        OrdenesDao ordenesDao = db.orderDao();
        List<Ordenes> ordeneslocales=ordenesDao.getAllOrdenes();
        makeJsonArrayRequestOrders(ordeneslocales);

        OrdenesEnsamblesDao ordenesEnsamblesDao = db.ordenesensamblesDao();
        List<OrdenesEnsambles> ordenesEnsambles=ordenesEnsamblesDao.getAllOrdenesEnsambles();
        makeJsonArrayRequestOrdersAssembly(ordenesEnsambles);
        ordenesDao = db.orderDao();
        //RequestQueue rq;
        //JsonObjectRequest jor;
        productosImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MenuActivity.this, ProductosActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        ensamblesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MenuActivity.this, EnsamblesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        clientesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MenuActivity.this, ClientesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        ordenesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MenuActivity.this, OrdenesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        reportesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MenuActivity.this, ReportesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });

    }

    private void animationStart(View view){
        //PONER RECURSOS DRAWABLE Y CAMIARLOS
        if(productosImageButton==view){
            ensamblesImageButton.setVisibility(View.INVISIBLE);
            clientesImageButton.setVisibility(View.INVISIBLE);
            ordenesImageButton.setVisibility(View.INVISIBLE);
            reportesImageButton.setVisibility(View.INVISIBLE);
            productosImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.productos));
        }
        else if(ensamblesImageButton==view){
            productosImageButton.setVisibility(View.INVISIBLE);
            clientesImageButton.setVisibility(View.INVISIBLE);
            ordenesImageButton.setVisibility(View.INVISIBLE);
            reportesImageButton.setVisibility(View.INVISIBLE);
            ensamblesImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.ensambles));
        }
        else if(clientesImageButton==view){
            ensamblesImageButton.setVisibility(View.INVISIBLE);
            productosImageButton.setVisibility(View.INVISIBLE);
            ordenesImageButton.setVisibility(View.INVISIBLE);
            reportesImageButton.setVisibility(View.INVISIBLE);
            clientesImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.clientes));
        }
        else if(ordenesImageButton==view){
            ensamblesImageButton.setVisibility(View.INVISIBLE);
            clientesImageButton.setVisibility(View.INVISIBLE);
            productosImageButton.setVisibility(View.INVISIBLE);
            reportesImageButton.setVisibility(View.INVISIBLE);
            ordenesImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.ordenes));
        }
        else if(reportesImageButton==view){
            reportesImageButton.setVisibility(View.INVISIBLE);
            ensamblesImageButton.setVisibility(View.INVISIBLE);
            clientesImageButton.setVisibility(View.INVISIBLE);
            ordenesImageButton.setVisibility(View.INVISIBLE);
            productosImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.reportes));
        }
        Animator animator = ViewAnimationUtils.createCircularReveal(
                linearActivity,
                linearActivity.getWidth()/2, linearActivity.getHeight()/2,
                0,linearActivity.getWidth()
        );
        animator.setDuration(DURATION_ANIMATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                linearActivity.setForeground(null);
            }
        });
        animator.start();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprincipal, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.logoutButton) {
            //Toast.makeText(this, "Funciona", Toast.LENGTH_SHORT).show();
            AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
            vendedoresDao vendedoresDao= db.vendedoresDao();
            vendedoresDao.ChangeLogout(vendedor.getId());
            restartApp();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    private void findView(){
        productosImageButton = findViewById(R.id.productosImageButton);
        ensamblesImageButton = findViewById(R.id.ensamblesImageButton);
        clientesImageButton = findViewById(R.id.clientesImageButton);
        ordenesImageButton = findViewById(R.id.ordenesImageButton);
        reportesImageButton = findViewById(R.id.reportesImageButton);
        linearActivity = findViewById(R.id.linearActivity);

    }
    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 50;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
