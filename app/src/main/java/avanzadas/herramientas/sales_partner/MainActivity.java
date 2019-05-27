package avanzadas.herramientas.sales_partner;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesDao;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Ensambles.ProductosEnsamblesDao;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsambles;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsamblesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductCategory;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.Productos.ProductsCategoryDao;
import avanzadas.herramientas.sales_partner.Vendedores.Vendedores;
import avanzadas.herramientas.sales_partner.Vendedores.vendedoresDao;


public class MainActivity extends AppCompatActivity{


    vendedoresDao vendedoresDao;


    AppDataBase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
        List<Vendedores> vendedoresList= vendedoresDao.getAllVendedores();

        vendedoresDao = db.vendedoresDao();
        Vendedores vendedor= null;
          for(Vendedores v: vendedoresList){
            if(v.getOnline()==1){
              vendedor= v;
          }}
        if(vendedor==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            super.finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("vendedor", vendedor);
            startActivity(intent);
            super.finish();
        }
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
