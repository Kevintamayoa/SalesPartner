package avanzadas.herramientas.sales_partner;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.facebook.stetho.Stetho;

import java.util.List;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.Vendedores.Vendedores;
import avanzadas.herramientas.sales_partner.Vendedores.vendedoresDao;


public class MainActivity extends AppCompatActivity{


    vendedoresDao vendedoresDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if()
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
        vendedoresDao = db.vendedoresDao();
       Vendedores vendedor= null;
        List<Vendedores> vendedoresList= vendedoresDao.getAllVendedores();
        for(Vendedores v: vendedoresList){
          if(v.getOnline()==1){
              vendedor= v;
          }

        }
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
