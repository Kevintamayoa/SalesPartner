package avanzadas.herramientas.sales_partner.Reportes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import avanzadas.herramientas.sales_partner.R;

public class ReportesActivity extends AppCompatActivity {

    private ImageButton productosFaltantesImageButton;
    private ImageButton simuladorImageButton;
    private ImageButton resumenVentasImageButton;
    private LinearLayout linearActivity;
    private final int requestcode=18;

    private static final int DURATION_ANIMATION = 380;

    @Override
    public void onBackPressed() {

        setResult(37);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==requestcode&&resultCode==101){
            productosFaltantesImageButton.setVisibility(View.VISIBLE);
            simuladorImageButton.setVisibility(View.VISIBLE);
            resumenVentasImageButton.setVisibility(View.VISIBLE);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        findView();
        getSupportActionBar().setTitle("Reportes");


        productosFaltantesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(ReportesActivity.this, ProductosFaltantesActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });

        simuladorImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(ReportesActivity.this, SimuladorActivity.class);
                startActivityForResult(intent,requestcode);            }
        });

        resumenVentasImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationStart(v);
                Intent intent = new Intent(ReportesActivity.this, ResumenVentasActivity.class);
                startActivityForResult(intent,requestcode);
            }
        });
    }

    private void animationStart(View view){
        //PONER RECURSOS DRAWABLE Y CAMIARLOS
        if(productosFaltantesImageButton==view){
            productosFaltantesImageButton.setVisibility(View.INVISIBLE);
            simuladorImageButton.setVisibility(View.INVISIBLE);
            resumenVentasImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.productos_faltantes));
        }else if(simuladorImageButton==view){
            productosFaltantesImageButton.setVisibility(View.INVISIBLE);
            simuladorImageButton.setVisibility(View.INVISIBLE);
            resumenVentasImageButton.setVisibility(View.INVISIBLE);

            linearActivity.setForeground(getDrawable(R.drawable.simulador));
        }else if(resumenVentasImageButton==view){
            productosFaltantesImageButton.setVisibility(View.INVISIBLE);
            simuladorImageButton.setVisibility(View.INVISIBLE);
            resumenVentasImageButton.setVisibility(View.INVISIBLE);
            linearActivity.setForeground(getDrawable(R.drawable.resumen_de_ventas));
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
        productosFaltantesImageButton = findViewById(R.id.productosFaltantesImageButton);
        simuladorImageButton = findViewById(R.id.simuladorImageButton);
        resumenVentasImageButton = findViewById(R.id.resumenVentasImageButton);
        linearActivity = findViewById(R.id.linearActivity);
    }
}
