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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppController;
import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.MainActivity;
import avanzadas.herramientas.sales_partner.MyAdapter;
import avanzadas.herramientas.sales_partner.R;
import avanzadas.herramientas.sales_partner.StateVO;

public class ClientesActivity extends AppCompatActivity  {

    private Spinner BuscarSpinner;
    private EditText BuscarEditText;
    private RecyclerView recyclerView;
    ClientesDao clientesDao;
    private String urlClientes = "http://192.168.43.235:3000/customers";
    private static String TAG = ClientesActivity.class.getSimpleName();
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
            makeJsonArrayRequestClientes(clientesDao.getAllClientes());
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

                                clientes.add(new Clientes(id,id1,id2,id3,id4,id5,id6,id7));
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
}