package avanzadas.herramientas.sales_partner.Ordenes;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.AddClientesAtivity;
import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesActivity;
import avanzadas.herramientas.sales_partner.Clientes.ClientesAdapter;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.MyAdapter;
import avanzadas.herramientas.sales_partner.Productos.ProductAdapter;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosActivity;
import avanzadas.herramientas.sales_partner.R;
import avanzadas.herramientas.sales_partner.StateVO;

public class OrdenesActivity extends AppCompatActivity {

    private Spinner filtrarporestado;
    private Spinner filtrarporcliente;
    private CheckBox fechainicial;
    private CheckBox fechafinal;
    private Calendar calendar;
    public List<Ordenes> ordenesfinal = new ArrayList<>();

    private MaskEditText TvfechaInicial;
    private MaskEditText TvfechaFinal;
    private RecyclerView rc_ordenes;
    List<Clientes> clientesList;
    AppDataBase db;
    List<Ordenes> ordenesArrayList;
    static final int RequestAdd = 15, RequesEdit = 35;
    StateVO stateVO;
    ArrayList<StateVO> stateVOArrayList;
    ViewModelOrdenes model;

    @Override
    public void onBackPressed() {

        setResult(37);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);
        getSupportActionBar().setTitle("Ordenes");
        findView();
        model= ViewModelProviders.of(OrdenesActivity.this).get(ViewModelOrdenes.class);
        fechainicial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fechainicial.isChecked()) {
                    TvfechaInicial.setEnabled(true);
                } else {
                    TvfechaInicial.setEnabled(false);
                }
            }
        });
        fechafinal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fechafinal.isChecked()) {
                    TvfechaFinal.setEnabled(true);
                } else {
                    TvfechaFinal.setEnabled(false);
                }
            }
        });
        db = AppDataBase.getAppDataBase(getApplicationContext());
        ClientesDao clDao = db.clientesDao();
        clientesList = clDao.getAllClientesOrderByIdASC();
        StatusOrdenesDao soDao = db.statusorderDao();

        List<StatusOrdenes> so = soDao.getAllStatusOrders();

        //if(this.getSystemService(this.WINDOW_SERVICE).toString() == )
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            rc_ordenes.setLayoutManager(new LinearLayoutManager(this));
        } else {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(OrdenesActivity.this, 2);
            rc_ordenes.setLayoutManager(layoutManager);
        }


        filtrarporestado.setAdapter(SpinnerEstado());
        filtrarporcliente.setAdapter(AdapterSpinnerCliente(clientesList));
        if(model.getOrdenesList()!=null){
            AdapterOrdenes adapter= new AdapterOrdenes(model.getOrdenesList());

            rc_ordenes.setAdapter(adapter);
        }

    }


    public ArrayAdapter<String> AdapterSpinnerCliente(List<Clientes> clientes) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item);
        //int a=categories.size();
        arrayAdapter.add("Todos");
        for (Clientes pc : clientes) {
            arrayAdapter.add(pc.getId() + ", " + pc.getFirst_name() + " " + pc.getLast_name());
        }
        return arrayAdapter;
    }

    public MyAdapter SpinnerEstado() {
        final String[] select_qualification = {
                "Select Item", "Pendiente", "Cancelado", "Confirmado", "En tránsito", "Finalizado"};

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            stateVO = new StateVO();

            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);

            listVOs.add(stateVO);
        }
        stateVOArrayList = listVOs;
        MyAdapter myAdapter = new MyAdapter(OrdenesActivity.this, 0,
                listVOs);
        return myAdapter;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clientes_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.AddButton) {
            Intent intent = new Intent(this, AgregarNuevaOrdenActivity.class);
            startActivityForResult(intent, RequestAdd);
        } else if (id == R.id.BuscarButton) {
            ordenesfinal= new ArrayList<>();
            busquedaRecyclerView();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int ordenid = ordenesArrayList.get(item.getGroupId()).getId();

        switch ((item.getItemId())) {

            case 0:
               // Ordenes or=checarporFecha(ordenesfinal).get(item.getGroupId());
                detallesContextMenu(ordenesfinal.get(item.getGroupId()));
                return true;
            case 1:
                Ordenes o = ordenesArrayList.get(item.getGroupId());
                db.orderDao().UdapteOrdenStatus(new Ordenes(o.getId(), o.getStatus_id() - 1, o.getCustomer_id(), o.getDate(), o.getChange_log()));

                Toast.makeText(this, "Ahora el status del pedido es: " + String.valueOf(db.statusorderDao().getStatusByid(ordenesArrayList
                        .get(item.getGroupId()).getStatus_id() - 1).getDescription()), Toast.LENGTH_SHORT).show();
                final Dialog dialog1 = new Dialog(OrdenesActivity.this);
                dialog1.setContentView(R.layout.comentarios_dialog);
                Button btn1 = dialog1.findViewById(R.id.btn_comentario);
                final EditText comentario1 = dialog1.findViewById(R.id.comentario);
                dialog1.setCancelable(false);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.orderDao().UpdateOrderByChangeLog(comentario1.getText().toString(), ordenid);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                busquedaRecyclerView();
                return true;
            case 2:
                Ordenes ord = ordenesArrayList.get(item.getGroupId());
                db.orderDao().UdapteOrdenStatus(new Ordenes(ord.getId(), ord.getStatus_id() + 1, ord.getCustomer_id(), ord.getDate(), ord.getChange_log()));

                Toast.makeText(this, "Ahora el status del pedido es: " + String.valueOf(db.statusorderDao().getStatusByid(ordenesArrayList
                        .get(item.getGroupId()).getStatus_id() + 1).getDescription()), Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(OrdenesActivity.this);
                dialog.setContentView(R.layout.comentarios_dialog);
                Button btn = dialog.findViewById(R.id.btn_comentario);
                final EditText comentario = dialog.findViewById(R.id.comentario);
                dialog.setCancelable(false);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.orderDao().UpdateOrderByChangeLog(comentario.getText().toString(), ordenid);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                busquedaRecyclerView();
                return true;
            case 3:
                if (ordenesArrayList.get(item.getGroupId()).getStatus_id() == 0) {
                    Intent intent = new Intent(OrdenesActivity.this, EditarOrdenActivity.class);
                    intent.putExtra("cliente", db.clientesDao().getClienteById(ordenesArrayList.get(item.getGroupId()).getCustomer_id()));
                    intent.putExtra("orden", ordenesArrayList.get(item.getGroupId()));
                    startActivityForResult(intent, RequesEdit);
                } else {
                    Toast.makeText(this, "La orden no puede ser editada", Toast.LENGTH_SHORT).show();
                }


                return true;
            default:
                return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
    }

    private void busquedaRecyclerView() {


        AdapterOrdenes adapter;// = new ProductAdapter(null);

        ordenesArrayList = new ArrayList<>();

        OrdenesDao ordenesDao = db.orderDao();
        List<StateVO> checkBoxes = new ArrayList<>();
        for (StateVO sv : stateVOArrayList) {
            if (sv.isSelected()) {
                checkBoxes.add(sv);
            }
        }
        if (checkBoxes.size() == 0 && filtrarporcliente.getSelectedItem() == "Todos") {
            ordenesArrayList = ordenesDao.getAllOrdenesByDate();
        } else if (checkBoxes.size() != 0 && filtrarporcliente.getSelectedItem() == "Todos") {

            for (StateVO sv : checkBoxes) {
                for (Ordenes o : ordenesDao.getOrdenesByStatus(sv.getTitle())) {
                    ordenesArrayList.add(o);
                }
            }
        } else if (checkBoxes.size() == 0 && filtrarporcliente.getSelectedItem() != "Todos") {
            //char[] chars=new char[filtrarporcliente.getSelectedItem().toString().length()];
            String id = new String();
            ordenesArrayList = ordenesDao.getOrdenesByClientesName(filtrarporcliente
                    .getSelectedItemPosition() - 1);
        } else if (checkBoxes.size() != 0 && filtrarporcliente.getSelectedItem() != "Todos") {
            for (StateVO sv : checkBoxes) {
                for (Ordenes o : ordenesDao.getOrdenesByStatusAndClientes(sv.getTitle(),
                        filtrarporcliente.getSelectedItemPosition() - 1)) {

                    ordenesArrayList.add(o);
                }
            }

        }

        List<Ordenes> ordenes=checarporFecha(ordenesArrayList);
        adapter = new AdapterOrdenes(ordenes);


        rc_ordenes.setAdapter(adapter);

    }

    private List<Ordenes> checarporFecha(List<Ordenes> ordenes) {

        List<Ordenes> ordenes1=ordenes;
        Calendar dateSolicitadaInicio = Calendar.getInstance();
        Calendar dateSolicitadaFinal = Calendar.getInstance();

        if (fechainicial.isChecked() && fechafinal.isChecked()) {
            String[] fsi = TvfechaInicial.getText().toString().split("/");
            String[] fsf = TvfechaFinal.getText().toString().split("/");
            try {
                dateSolicitadaInicio.set(Integer.parseInt(fsi[2]), Integer.parseInt(fsi[1]), Integer.parseInt(fsi[0]));
                dateSolicitadaFinal.set(Integer.parseInt(fsf[2]), Integer.parseInt(fsf[1]), Integer.parseInt(fsf[0]));
                if (Integer.parseInt(fsi[0]) > 31 || Integer.parseInt(fsi[1]) > 12 || Integer.parseInt(fsf[0]) > 31 || Integer.parseInt(fsf[1]) > 12) {
                    dialogAlert();
                } else {
                    for (Ordenes o : ordenes) {
                        String[] dateOrdenString = o.getDate().split("-");
                        Calendar dateOrden = Calendar.getInstance();
                        dateOrden.set(Integer.parseInt(dateOrdenString[2]),
                                Integer.parseInt(dateOrdenString[1]), Integer.parseInt(dateOrdenString[0]));
                        if (dateOrden.compareTo(dateSolicitadaFinal) < 0 && dateOrden.compareTo(dateSolicitadaInicio) > 0) {
                            ordenesfinal.add(o);
                        }
                    }
                }
            }
            catch (Exception e) {
                dialogAlert();
            }


        }
        else if (fechainicial.isChecked() && !fechafinal.isChecked()) {
            String[] fsi = TvfechaInicial.getText().toString().split("/");
            try {
                dateSolicitadaInicio.set(Integer.parseInt(fsi[2]), Integer.parseInt(fsi[1]), Integer.parseInt(fsi[0]));
                if (Integer.parseInt(fsi[0]) > 31 || Integer.parseInt(fsi[1]) > 12) {
                    dialogAlert();
                } else {
                    for (Ordenes o : ordenes) {
                        String[] dateOrdenString = o.getDate().split("-");
                        Calendar dateOrden = Calendar.getInstance();
                        dateOrden.set(Integer.parseInt(dateOrdenString[2]),
                                Integer.parseInt(dateOrdenString[1]), Integer.parseInt(dateOrdenString[0]));
                        if (dateOrden.compareTo(dateSolicitadaInicio) > 0) {
                            ordenesfinal.add(o);
                        }
                    }

                }
            }
            catch (Exception e) {
               dialogAlert();



            }

        }
        else if (!fechainicial.isChecked() && fechafinal.isChecked()) {
            String[] fsf = TvfechaFinal.getText().toString().split("/");
            try {
                dateSolicitadaFinal.set(Integer.parseInt(fsf[2]), Integer.parseInt(fsf[1]), Integer.parseInt(fsf[0]));
                if (Integer.parseInt(fsf[0]) > 31 || Integer.parseInt(fsf[1]) > 12) {
                    dialogAlert();
                } else {
                    for (Ordenes o : ordenes) {
                        String[] dateOrdenString = o.getDate().split("-");
                        Calendar dateOrden = Calendar.getInstance();
                        dateOrden.set(Integer.parseInt(dateOrdenString[2]),
                                Integer.parseInt(dateOrdenString[1]), Integer.parseInt(dateOrdenString[0]));
                        if (dateOrden.compareTo(dateSolicitadaFinal) < 0) {
                            ordenesfinal.add(o);
                        }
                    }



                }
            } catch (Exception e) {
                dialogAlert();
            }


        }
        else {
            ordenesfinal=ordenes;
            model.setOrdenesList(ordenesfinal);
            return ordenesfinal;
        }
        model.setOrdenesList(ordenesfinal);
        return ordenesfinal;
    }

    private void detallesContextMenu(Ordenes orden) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(OrdenesActivity.this);
        dialog.setTitle(
                db.clientesDao().getClienteById(orden.getCustomer_id()).getFirst_name() + " " +
                        db.clientesDao().getClienteById(orden.getCustomer_id()).getLast_name())
                .setMessage(
                        "id de orden: " + orden.getId()
                                + "\n" + "Status de la orden: " +

                                db.statusorderDao().getStatusByid(orden.getStatus_id()).getDescription() + "\n" +
                                "Ensambles ordenados: " + GetEnsamblesByOrden(orden)
                                + "\n" + "Fecha del pedido: " + orden.getDate() + "\n" + "Costo de la orden: " + GetCostoEnsamble(orden)

                )
                .show();
    }

    private void findView() {
        filtrarporestado = findViewById(R.id.SpinnerporEstado);
        filtrarporcliente = findViewById(R.id.SpinnerporCliente);
        fechainicial = findViewById(R.id.fechainicial);
        fechafinal = findViewById(R.id.fechafinal);

        TvfechaInicial = findViewById(R.id.TVfechaInicial);
        TvfechaFinal = findViewById(R.id.TVfechaFinal);
        rc_ordenes = findViewById(R.id.recyclerViewOrdenes);

        TvfechaInicial.setEnabled(false);
        TvfechaFinal.setEnabled(false);
    }

    private String GetEnsamblesByOrden(Ordenes ordenes) {
        String cadena = "";
        int a= db.ordenesensamblesDao().getEnsamblesByOrder(ordenes.getId()).size();
        List<OrdenesEnsambles> e=db.ordenesensamblesDao().getEnsamblesByOrder(ordenes.getId());
        for (int i = 0; i < db.ordenesensamblesDao().getEnsamblesByOrder(ordenes.getId()).size(); i++) {
            if (i == 0) {
                cadena = db.ensamblesDao().getEnsamblesByID(e.get(i).getAssembly_id()).getDescription();
            } else {
                cadena = cadena + ", " +
                        db.ensamblesDao().getEnsamblesByID(e.get(i).getAssembly_id()).getDescription();
            }

        }
        return cadena;
    }

    private double GetCostoEnsamble(Ordenes ordenes) {
        double costo = 0;
        for (int i = 0; i < db.ordenesensamblesDao().getEnsamblesByOrder(ordenes.getId()).size(); i++) {
            costo = costo + ((db.ensamblesDao().getCostoinEnsamble(
                    db.ordenesensamblesDao().getEnsamblesByOrder(ordenes.getId()).get(i).getId()
            )) / 100);
        }

        return costo;
    }

    private void dialogAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(OrdenesActivity.this);
        dialog.setTitle("Formato de fecha incorrecto");
        dialog.setMessage("El formato de fecha debe ser dd/mm/yyyy");
        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

   // @Override
   // protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
   //     if(requestCode==RequestAdd){
   //
   //
   //     }
   //
   //
   //
   //
   //
   //     super.onActivityResult(requestCode, resultCode, data);
   // }
}
