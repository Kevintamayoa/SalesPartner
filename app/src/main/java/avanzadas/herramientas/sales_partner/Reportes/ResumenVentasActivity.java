package avanzadas.herramientas.sales_partner.Reportes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesDao;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsambles;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsamblesDao;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.R;

public class ResumenVentasActivity extends AppCompatActivity {
//673AB7

    List<Ordenes> ordenesList;
    List<Productos> productosList;
    List<EnsamblesProducts> ensamblesProductsList;
    OrdenesDao ordenesDao;
    List<Meses> mesesList;
    List<Ensambles> ensamblesList;
    List<OrdenesEnsambles> ordenesEnsamblesList;

    private RecyclerView recyclerView;
    private TextView currentYearTextView;
    private Button currentYearButton;
    private NumberPicker numberPicker;
    private Button buscarButton;

    private ResumenVentasAdapter adapter;

    private int MAX_VALUE_NUMBER_PICKER = 2020;
    private int MIN_VALUE_NUMBER_PICKER = 2010;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("year", numberPicker.getValue());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_ventas);

        findView();



        //currentYearTextView.setText(numberPicker.getValue());

        getSupportActionBar().setTitle("Resumen Ventas");

        AppDataBase db = AppDataBase.getAppDataBase(this);
        ordenesDao = db.orderDao();
        ordenesList = new ArrayList<>();
        ordenesList = ordenesDao.getAllOrdenes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentYearTextView.setText("" + Calendar.getInstance().get(Calendar.YEAR));
        numberPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));


        numberPicker.setMinValue(MIN_VALUE_NUMBER_PICKER);
        numberPicker.setMaxValue(MAX_VALUE_NUMBER_PICKER);

        //numberPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentYearTextView.setText("" + newVal);
            }
        });
        currentYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentYearTextView.setText("" + Calendar.getInstance().get(Calendar.YEAR));
                numberPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
            }
        });

        if(savedInstanceState != null){
            int numero = savedInstanceState.getInt("year");
            numberPicker.setValue(numero);
            currentYearTextView.setText(""+numero);
        }


        fillRecyclerView();
    }




    private void fillRecyclerView() {

        //adapter = new ResumenVentasAdapter(ordenesList);
        //ResumenVentasAdapter adapter =
        //ordenesList = new ArrayList<>();
        //ordenesList = ordenesDao.get
        //adapter.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //int i = 0;
        //        //for (Ordenes o : ordenesList){
        //        //    String []dateYear = o.getDate().split("-");
        //        //    String cadena = "";
        //        //    if(dateYear[2] == currentYearTextView.getText().toString() && ordenesList.get(i).getStatus_id() == 4){
        //        //        ordenesList.get(i).getCustomer_id();
        //        //    }
        //        //    i++;
        //        //}
//
//
        //        //ordenesList = ordenesDao.getAllOrdersByYear(currentYearTextView.getText().toString());
        //        AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        //        dialog.setTitle("")
        //                .setMessage("")
        //                .show();
        //    }
        //});
        //recyclerView.setAdapter(adapter);
        //String[] meses = {"ENERO", "FEBRERO","MARZO", "ABRIL","MAYO", "JUNIO","JULIO", "AGOSTO","SEPTIEMBRE", "OCTUBE","NOVIEMBRE", "DICIEMBRE"};

        MesesAdapter adapter;
        adapter = new MesesAdapter(addMeses());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResumenVentasActivity.this, "" + mesesList.get(recyclerView.getChildAdapterPosition(v)).getMonthString(), Toast.LENGTH_SHORT).show();
                DetallesDialog detallesDialog = new DetallesDialog();
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResumenVentasActivity.this);

                if(detallesDialog.getVentasCompletadas(v) == 0 && detallesDialog.getVentasCanceladas(v) == 0){
                    dialog.setTitle("No hubieron ventas en el mes de "
                            + mesesList.get(recyclerView.getChildLayoutPosition(v)).getMonthString())
                            .setPositiveButton(":(", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else{
                    dialog.setTitle(
                            "RESUMEN "
                                    + mesesList.get(recyclerView.getChildLayoutPosition(v)).getMonthString()
                                    + ": "
                                    + (recyclerView.getChildLayoutPosition(v) + 1)
                    )
                            .setMessage(
                                    "\n" + "VENTAS COMPLETADAS:  " + detallesDialog.getVentasCompletadas(v)
                                            + "\n" + "\n" + "VENTAS CANCELADAS:  " + detallesDialog.getVentasCanceladas(v)
                                            + "\n" + "\n" + "GANANCIA TOTAL:  $" + detallesDialog.getGananciaTotal(v)
                                            + "\n" + "\n" + "GANANCIA P/ENSAMBLE:\n" + detallesDialog.getGananciaEnsamble(v)
                            )
                            .show();
                }


            }
        });

        recyclerView.setAdapter(adapter);
    }

    private class DetallesDialog {

        int month;
        String year = "";
        //dd-mm-yyyy
        //05-10-2016

        private int getVentasCompletadas(View v){
            int ventasCompletas = 0;
            int i = 0;

            year = currentYearTextView.getText().toString();
            month = (Integer.parseInt(String.valueOf(recyclerView.getChildAdapterPosition(v))) + 1);

            for(Ordenes o : ordenesList){
                String[] date = ordenesList.get(i).getDate().split("-");
                if((year.contentEquals(date[2])) && (o.getStatus_id() == 4)){
                    if(month == Integer.parseInt(date[1])){
                        ventasCompletas++;
                    }
                }
                i++;
            }

            return ventasCompletas;
        }

        private int getVentasCanceladas(View v){
            int ventasCanceladas = 0;

            int i = 0;

            year = currentYearTextView.getText().toString();
            month = (Integer.parseInt(String.valueOf(recyclerView.getChildAdapterPosition(v))) + 1);

            for(Ordenes o : ordenesList){
                String[] date = ordenesList.get(i).getDate().split("-");
                if((year.contentEquals(date[2])) && (o.getStatus_id() == 1)){
                    if(month == Integer.parseInt(date[1])){
                        ventasCanceladas++;
                    }
                }
                i++;
            }

            return ventasCanceladas;
        }

        private double getGananciaTotal(View v){
            double gananciaTotal = 0;

            int i = 0;

            year = currentYearTextView.getText().toString();
            month = (Integer.parseInt(String.valueOf(recyclerView.getChildAdapterPosition(v))) + 1);

            AppDataBase db= AppDataBase.getAppDataBase(getApplicationContext());
            EnsamblesDao eDao = db.ensamblesDao();

            OrdenesEnsamblesDao oeDao = db.ordenesensamblesDao();
            ordenesEnsamblesList = oeDao.getAllOrdenesEnsambles();

            for(Ordenes o : ordenesList){
                String[] date = ordenesList.get(i).getDate().split("-");
                double costoEnsamble = 0;
                if((year.contentEquals(date[2])) && (o.getStatus_id() == 4)){
                    if(month == Integer.parseInt(date[1])){
                        //
                        for(OrdenesEnsambles oe : ordenesEnsamblesList){
                            if(oe.getId() == o.getId()){
                                costoEnsamble = eDao.getCostoinEnsamble(oe.getAssembly_id()) * oe.getQty();
                            }
                            gananciaTotal += costoEnsamble;
                            costoEnsamble = 0;
                        }

                    }
                }
                i++;
            }
            gananciaTotal = (gananciaTotal/100);

            return gananciaTotal;
        }

        private String getGananciaEnsamble(View v){
            double gananciaEnasamble = 0;
            String cadena = "";

            int i = 0;

            year = currentYearTextView.getText().toString();
            month = (Integer.parseInt(String.valueOf(recyclerView.getChildAdapterPosition(v))) + 1);

            AppDataBase db= AppDataBase.getAppDataBase(getApplicationContext());
            EnsamblesDao eDao = db.ensamblesDao();
            ensamblesList = eDao.getAllEnsambles();

            OrdenesEnsamblesDao oeDao = db.ordenesensamblesDao();
            ordenesEnsamblesList = oeDao.getAllOrdenesEnsambles();

            for(Ordenes o : ordenesList){
                String[] date = ordenesList.get(i).getDate().split("-");
                if((year.contentEquals(date[2])) && (o.getStatus_id() == 4)){
                    if(month == Integer.parseInt(date[1])){
                        //
                        for(OrdenesEnsambles oe : ordenesEnsamblesList){
                            if(oe.getId() == o.getId()){
                                for(Ensambles e : ensamblesList){
                                    if(oe.getAssembly_id() == e.getId()){
                                        gananciaEnasamble = (eDao.getCostoinEnsamble(oe.getAssembly_id()) * oe.getQty());
                                        cadena += e.getDescription() + ":  $"+ (gananciaEnasamble/100) + " (" + oe.getQty() + ")" + "\n";
                                    }
                                }
                            }
                        }

                    }
                }
                i++;
            }

            return cadena;
        }

    }


    private List<Meses> addMeses() {
        //Meses a, b, c, d, e, f, g, h, i, j, k, l;
        mesesList = new ArrayList<>();

        mesesList.add(new Meses("ENERO"));
        mesesList.add(new Meses("FEBRERO"));
        mesesList.add(new Meses("MARZO"));
        mesesList.add(new Meses("ABRIL"));
        mesesList.add(new Meses("MAYO"));
        mesesList.add(new Meses("JUNIO"));
        mesesList.add(new Meses("JULIO"));
        mesesList.add(new Meses("AGOSTO"));
        mesesList.add(new Meses("SEPTIEMBRE"));
        mesesList.add(new Meses("OCTUBRE"));
        mesesList.add(new Meses("NOVIEMBRE"));
        mesesList.add(new Meses("DICIEMBRE"));

        //a = new Meses("ENERO");
        //b = new Meses("FEBRERO");
        //c = new Meses("MARZO");
        //d = new Meses("ABRIL");
        //e = new Meses("MAYO");
        //f = new Meses("JUNIO");
        //g = new Meses("JULIO");
        //h = new Meses("AGOSTO");
        //i = new Meses("SEPTIEMBRE");
        //j = new Meses("OCTUBRE");
        //k = new Meses("NOVIEMBRE");
        //l = new Meses("DICIEMBRE");
        //mesesList.add(a);
        //mesesList.add(b);
        //mesesList.add(c);
        //mesesList.add(d);
        //mesesList.add(e);
        //mesesList.add(f);
        //mesesList.add(g);
        //mesesList.add(h);
        //mesesList.add(i);
        //mesesList.add(j);
        //mesesList.add(k);
        //mesesList.add(l);

        return mesesList;
    }

    @Override
    public void onBackPressed() {
        setResult(101);

        super.onBackPressed();
    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        currentYearTextView = findViewById(R.id.currentYearTextView);
        currentYearButton = findViewById(R.id.currentyearButton);
        numberPicker = findViewById(R.id.numberPicker);
        recyclerView = findViewById(R.id.recyclerView);
    }


}