package avanzadas.herramientas.sales_partner.Reportes;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsambles;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesEnsamblesDao;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;

public class SimuladorActivity extends AppCompatActivity {

    private RadioButton pClienteRadioButton;
    private RadioButton pFechaRadioButton;
    private RadioButton pMontoVentaRadioButton;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private LinearLayout linearLayout;

    private List<OrdenesEnsambles> ordenesEnsamblesList;
    private List<Clientes> clientesList;
    private List<Ordenes> ordenesList;
    private List<Productos> productosList;
    private List<Productos> productosListSimulacion;
    private List<EnsamblesProducts> ensamblesProductsList;

    private Ordenes ordenes;
    private Clientes clientes;
    private OrdenesEnsambles ordenesEnsambles;

    private OrdenesDao oDao;
    private ClientesDao cDao;
    private OrdenesEnsamblesDao oeDao;


    private AppDataBase db;

    SimuladorAdapter adapter;

    @Override
    public void onBackPressed() {
        setResult(101);

        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador);

        findView();
        getSupportActionBar().setTitle("Simulación");

        db = AppDataBase.getAppDataBase(getApplicationContext());

        getList();
        productosListSimulacion = productosList;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.pClienteRadioButton:
                        //Toast.makeText(SimuladorActivity.this, "Procesar por cliente", Toast.LENGTH_SHORT).show();
                        Adapter(pCliente());
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(SimuladorActivity.this, "Clic", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.pFechaRadioButton:
                        //Toast.makeText(SimuladorActivity.this, "Procesar por fecha", Toast.LENGTH_SHORT).show();
                        Adapter(pFecha());
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(SimuladorActivity.this, "Clic", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.pVentaRadioButton:
                        //Toast.makeText(SimuladorActivity.this, "Procesar por monto de venta", Toast.LENGTH_SHORT).show();
                        Adapter(pMontoVenta());
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(SimuladorActivity.this, "Clic", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }
        });
    }

    private List<Ordenes> pCliente() {
        ordenesList = oDao.getAllOrdersOrderByClienteId();
        return ordenesList;
    }

    private List<Ordenes> pFecha() {
        ordenesList = oDao.getAllOrdenesByDate2();
        return ordenesList;
    }

    private List<Ordenes> pMontoVenta() {
        ordenesList = oDao.getAllOrdersOrderByMontoVenta();
        return ordenesList;
    }

    private void Adapter(List<Ordenes> ordenesList) {
        //---
        ProductosDao pDao = db.productosDao();
        productosList = pDao.getAllProductos();
        //---
        adapter = new SimuladorAdapter(ordenesList);
        recyclerView.setAdapter(adapter);
    }

    private void getList(){

        clientesList = new ArrayList<>();
        productosList = new ArrayList<>();
        ordenesList = new ArrayList<>();


        oeDao = db.ordenesensamblesDao();
        ordenesEnsamblesList = oeDao.getAllOrdenesEnsamblesOrderById();

        cDao = db.clientesDao();
        clientesList = cDao.getAllClientesOrderByIdDesc();

        oDao = db.orderDao();
        //ordenesList = oDao.getAllOrdenes();
    }

    private void ProductosFaltantes(View v){
        for(Ordenes o : ordenesList){
            if(o.getStatus_id() ==0){
                for(EnsamblesProducts ep : ensamblesProductsList){
                    if(ep.getProduct_id() == o.getId()){
                        for(Productos p : productosList){
                            productosListSimulacion.add(p);
                        }
                    }
                }
            }
        }
    }

    private void findView() {

        linearLayout = findViewById(R.id.linearLayout);
        pClienteRadioButton = findViewById(R.id.pClienteRadioButton);
        pFechaRadioButton = findViewById(R.id.pFechaRadioButton);
        pMontoVentaRadioButton = findViewById(R.id.pVentaRadioButton);
        recyclerView = findViewById(R.id.recyclerView);
        radioGroup = findViewById(R.id.radioGroup);
    }
}