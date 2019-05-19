package avanzadas.herramientas.sales_partner.Clientes;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.MyAdapter;
import avanzadas.herramientas.sales_partner.R;
import avanzadas.herramientas.sales_partner.StateVO;

public class ClientesActivity extends AppCompatActivity  {

    private Spinner BuscarSpinner;
    private EditText BuscarEditText;
    private RecyclerView recyclerView;
    ClientesDao clientesDao;
    List<Clientes> clientesList;

    ClientesAdapter adapter;

    AppDataBase db;

    ArrayList<StateVO> stateVOArrayList;
    StateVO stateVO;

    ViewModelClientes model;

    @Override
    public void onBackPressed() {
        setResult(37);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 45637 && requestCode == 9635) {
            busquedaRecyclerView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        findView();

        model = ViewModelProviders.of(ClientesActivity.this).get(ViewModelClientes.class);


        getSupportActionBar().setTitle("Clientes");

        db = AppDataBase.getAppDataBase(getApplicationContext());
        clientesDao = db.clientesDao();
        clientesList = clientesDao.getAllClientes();

        if(savedInstanceState != null){
            clientesList = model.getClientesList();
            adapter = new ClientesAdapter(clientesList);
            recyclerView.setAdapter(adapter);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BuscarSpinner.setAdapter(buscarSpinner());

    }


    public MyAdapter buscarSpinner() {
        final String[] select_qualification = {
                "Select Item", "first_name", "last_name", "address", "phone",
                "e_mail"};

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        stateVOArrayList = listVOs;
        MyAdapter myAdapter = new MyAdapter(ClientesActivity.this, 0,
                listVOs);
        return myAdapter;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clientes_action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.AddButton) {
            Intent intent = new Intent(ClientesActivity.this, AddClientesAtivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.BuscarButton) {
            busquedaRecyclerView();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void busquedaRecyclerView() {

        clientesList = new ArrayList<>();

        clientesDao = db.clientesDao();
        List<StateVO> checkBoxes = new ArrayList<>();
        for (StateVO sv : stateVOArrayList) {
            if (sv.isSelected()) {
                checkBoxes.add(sv);
            }
        }

        if (BuscarEditText.getText().toString().contentEquals("")) {
            clientesList = clientesDao.getAllClientes();


        } else if ((checkBoxes.size() == 0 || checkBoxes.size() == 5) && BuscarEditText.getText().toString().contentEquals("")) {
            clientesList = clientesDao.getAllClientes();


        } else if (checkBoxes.size() != 0 && BuscarEditText.getText().toString().contentEquals("")) {
            clientesList = clientesDao.getAllClientes();


        } else if (checkBoxes.size() != 0 && !BuscarEditText.getText().toString().contentEquals("")) {
            //select * from customers where
            //select * from customers where (first_name like '%erika%') or (phone1 like '%erika%');
            String cadena = "select * from customers where ";
            int i = 1;
            for (StateVO sv : checkBoxes) {
                if (i == checkBoxes.size()) {
                    cadena += " (" + sv.getTitle() + " like " + "'%" + BuscarEditText.getText().toString() + "%');";
                } else {
                    cadena += "(" + sv.getTitle() + " like " + "'%" + BuscarEditText.getText().toString() + "%')" + " or ";
                }
                i++;
            }
            //clientesList = clientesDao.getAllClientesFindDinamyc(cadena);

            List<Clientes> liveUsers = clientesDao.getAllClientesFindDinamyc(
                    new SimpleSQLiteQuery(cadena));
            clientesList = liveUsers;
        }

        model.setClientesList(clientesList);
        adapter = new ClientesAdapter(clientesList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Clientes c;
        switch ((item.getItemId())) {
            case 0:
                //Toast.makeText(this, "DETALLES", Toast.LENGTH_SHORT).show();
                detallesContextMenu(item);
                return true;
            case 1:
                //Toast.makeText(this, "EDITAR", Toast.LENGTH_SHORT).show();
                c = clientesList.get(item.getGroupId());
                Intent intent = new Intent(ClientesActivity.this, AddClientesAtivity.class);
                int i = item.getGroupId();
                intent.putExtra("cliente", c);
                startActivityForResult(intent, 9635);
                return true;
            case 2:
                //Toast.makeText(this, "ELIMINAR", Toast.LENGTH_SHORT).show();
                AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
                ClientesDao clientesDao = db.clientesDao();
                c = clientesList.get(item.getGroupId());
                clientesDao.DeleteClientes(c);
                busquedaRecyclerView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
    }

    private void detallesContextMenu(MenuItem item) {
        String cadena = clientesList.get(item.getGroupId()).getPhone1();
        if (!TextUtils.isEmpty(clientesList.get(item.getGroupId()).getPhone2())) {
            cadena += (", " + clientesList.get(item.getGroupId()).getPhone2());
        }
        if (!TextUtils.isEmpty(clientesList.get(item.getGroupId()).getPhone3())) {
            cadena += (", " + clientesList.get(item.getGroupId()).getPhone3());
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(ClientesActivity.this);
        dialog.setTitle(
                clientesList.get(item.getGroupId()).getId() + ".- " +
                        clientesList.get(item.getGroupId()).getFirst_name() + " "
                        + clientesList.get(item.getGroupId()).getLast_name())
                .setMessage(
                        clientesList.get(item.getGroupId()).getDireccion()
                                + "\n" + "Tel. " + cadena
                                + "\n" + clientesList.get(item.getGroupId()).getEmail())
                .show();
    }

    private void findView() {
        BuscarSpinner = findViewById(R.id.busquedaSpinner);
        BuscarEditText = findViewById(R.id.busquedaEditText);
        recyclerView = findViewById(R.id.recyclerView);
    }
}