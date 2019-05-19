package avanzadas.herramientas.sales_partner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.zip.Inflater;

import avanzadas.herramientas.sales_partner.Clientes.ClientesActivity;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesActivity;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesActivity;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.Reportes.ReportesActivity;

public class MainActivity extends AppCompatActivity{

    private static final int DURATION_ANIMATION = 380;

    private ImageButton productosImageButton;
    private ImageButton ensamblesImageButton;
    private ImageButton clientesImageButton;
    private ImageButton ordenesImageButton;
    private ImageButton reportesImageButton;
    private LinearLayout linearActivity;
    private final int requestcode=11;

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
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        findView();
        getSupportActionBar().setTitle("Sales Partner");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());



        productosImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        ensamblesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MainActivity.this, EnsamblesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        clientesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        ordenesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MainActivity.this, OrdenesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
        reportesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(MainActivity.this, ReportesActivity.class);
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

    private void findView(){
        productosImageButton = findViewById(R.id.productosImageButton);
        ensamblesImageButton = findViewById(R.id.ensamblesImageButton);
        clientesImageButton = findViewById(R.id.clientesImageButton);
        ordenesImageButton = findViewById(R.id.ordenesImageButton);
        reportesImageButton = findViewById(R.id.reportesImageButton);
        linearActivity = findViewById(R.id.linearActivity);

    }
}
