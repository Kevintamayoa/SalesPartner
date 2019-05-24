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
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.stetho.Stetho;

import java.util.List;
import java.util.zip.Inflater;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesActivity;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesActivity;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesActivity;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.Reportes.ReportesActivity;
import avanzadas.herramientas.sales_partner.Vendedores.Vendedores;
import avanzadas.herramientas.sales_partner.Vendedores.vendedoresDao;

public class MenuActivity extends AppCompatActivity{

    private static final int DURATION_ANIMATION = 380;

    private ImageButton productosImageButton;
    private ImageButton ensamblesImageButton;
    private ImageButton clientesImageButton;
    private ImageButton ordenesImageButton;
    private ImageButton reportesImageButton;
    private LinearLayout linearActivity;
    private final int requestcode=11;
    private Vendedores vendedor;
    private OrdenesDao ordenesDao;

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



        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
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
