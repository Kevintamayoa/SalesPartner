package avanzadas.herramientas.sales_partner.Clientes;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.R;

public class AddClientesAtivity extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText nombreUsuario;
    private EditText claveUsuario;

    private EditText apellidoEditText;
    private EditText direccionEditText;
    private MaskEditText tel1EditText;
    private MaskEditText tel2EditText;
    private MaskEditText tel3EditText;
    private EditText emailEditText;
    private CheckBox tel2CheckBox;
    private CheckBox tel3CheckBox;
    private CheckBox emailCheckBox;

    private String user;
    private String clave;
    private String f_name;
    private String l_name;
    private String dir;
    private String tel1;
    private String tel2;
    private String tel3;
    private String email;
    private Boolean check = false;

    private boolean errorNombre = false;
    private boolean errorTel1 = false;
    private boolean errorEmail = false;

    Clientes c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clientes);


        findView();




        getSupportActionBar().setTitle("Nuevo cliente");



        try {
            check = setParamsEditCliente();
        }catch (Exception e){
        }





        tel2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tel2 = "";
                if (tel2CheckBox.isChecked()) {
                    tel2EditText.setEnabled(true);

                } else {
                    tel2EditText.setText("");
                    tel2EditText.setEnabled(false);
                }
            }
        });

        tel3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tel3 = "";
                if (tel3CheckBox.isChecked()) {
                    tel3EditText.setEnabled(true);

                } else {
                    tel3EditText.setText("");
                    tel3EditText.setEnabled(false);
                }
            }
        });

        emailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                email = "";
                if (emailCheckBox.isChecked()) {
                    emailEditText.setEnabled(true);
                } else {
                    emailEditText.setText("");
                    emailEditText.setEnabled(false);
                }
            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addcliente_action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        //
        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
        ClientesDao clientesDao = db.clientesDao();
        List<Clientes> clientesList = clientesDao.getAllClientes();



        if (id == R.id.saveButton) {

            checarCampos();

            if(errorEmail && !check){
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddClientesAtivity.this);
                dialog.setTitle("Ya existe un usuario con el mismo Email\n").show();
                errorEmail = false;
                return true;
            }
            if(errorNombre && !check){
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddClientesAtivity.this);
                dialog.setTitle("Ya existe un usuario con el mismo nombre completo\n").show();
                errorNombre = false;
                return true;
            }
            if(errorTel1 && !check){
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddClientesAtivity.this);
                dialog.setTitle("Ya existe un usuario con el mismo teléfono principal\n").show();
                errorTel1 = false;
                return true;
            }

            if(nombreEditText.getText().toString().contentEquals("") ||
                    apellidoEditText.getText().toString().contentEquals("") ||
                    tel1EditText.getText().toString().contentEquals("") ||
                    direccionEditText.getText().toString().contentEquals("") ||
                    (tel2CheckBox.isChecked() && tel2EditText.getText().toString().contentEquals("")) ||
                    (tel3CheckBox.isChecked() && tel3EditText.getText().toString().contentEquals("")) ||
                    (emailCheckBox.isChecked() && emailEditText.getText().toString().contentEquals(""))){
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddClientesAtivity.this);
                dialog.setMessage("Tiene que llenar los campos obligatorios").show();
                return false;
            }

            if(check){
                Clientes c = (Clientes) getIntent().getExtras().getSerializable("cliente");

                setResult(45637);

                c.setFirst_name(nombreEditText.getText().toString());
                c.setLast_name(apellidoEditText.getText().toString());
                c.setDireccion(direccionEditText.getText().toString());
                c.setPhone1(tel1EditText.getText().toString());
                c.setPhone2(tel2EditText.getText().toString());
                c.setPhone3(tel3EditText.getText().toString());
                c.setEmail(emailEditText.getText().toString());



                clientesDao.UpdateCliente(c);
                super.finish();
                return true;
            }

            saveData();

            c = new Clientes(clientesList.size() + 1, f_name, l_name, dir, tel1, tel2, tel3, email,1);

            clientesDao.InsertClientes(c);


            Toast.makeText(this, "Cliente Guardado, te toca comisión", Toast.LENGTH_SHORT).show();

            super.finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }



    private void saveData(){
        f_name = nombreEditText.getText().toString();
        l_name = apellidoEditText.getText().toString();
        dir = direccionEditText.getText().toString();
        tel1 = tel1EditText.getRawText();
        tel2 = tel2EditText.getText().toString();
        tel3 = tel2EditText.getText().toString();
        email = emailEditText.getText().toString();
        user= nombreUsuario.getText().toString();
        clave= claveUsuario.getText().toString();


    }

    private void checarCampos(){
        String fullName = nombreEditText.getText().toString() +  apellidoEditText.getText().toString();

        AppDataBase db = AppDataBase.getAppDataBase(AddClientesAtivity.this);
        ClientesDao cDao2 = db.clientesDao();
        List<Clientes> clientesList2 = cDao2.getAllClientes();

        for(Clientes c : clientesList2){
            String fullNameDB = "";
            fullNameDB = c.getFirst_name() + c.getLast_name();
            if(fullNameDB.contentEquals(fullName)){
                errorNombre = true;
            }
            if(tel1EditText.getRawText() == c.getPhone1()){
                errorTel1 = true;
            }
            if(emailEditText.getText().toString().contentEquals(c.getEmail()) && emailCheckBox.isChecked() && !emailEditText.getText().toString().contentEquals("")){
                errorEmail = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        setResult(45637);

        AlertDialog.Builder dialog = new AlertDialog.Builder(AddClientesAtivity.this);
        dialog.setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("¿Seguro desea salir?")
                .setMessage("\nSi sale no se guardán los cambios hechos");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean setParamsEditCliente() {
        Clientes c = (Clientes) getIntent().getExtras().getSerializable("cliente");
        if (c != null) {
            getSupportActionBar().setTitle("Editar Cliente");
            nombreEditText.setText(c.getFirst_name());
            apellidoEditText.setText(c.getLast_name());
            direccionEditText.setText(c.getDireccion());
            tel1EditText.setText(c.getPhone1());
            if (!TextUtils.isEmpty(c.getPhone2())) {
                tel2CheckBox.setChecked(true);
                tel2EditText.setEnabled(true);
                tel2EditText.setText(c.getPhone2());
            }
            if (!TextUtils.isEmpty(c.getPhone3())) {
                tel3CheckBox.setChecked(true);
                tel3EditText.setEnabled(true);
                tel3EditText.setText(c.getPhone3());
            }
            if (!TextUtils.isEmpty(c.getEmail())) {
                emailCheckBox.setChecked(true);
                emailEditText.setEnabled(true);
                emailEditText.setText(c.getEmail());
            }
            return true;
        }
        return false;
    }

    private void findView() {
        nombreEditText = findViewById(R.id.nombreEditText);
        apellidoEditText = findViewById(R.id.apellidoEditText);
        direccionEditText = findViewById(R.id.direccionEditText);
        tel1EditText = findViewById(R.id.tel1EditText);
        tel2EditText = findViewById(R.id.tel2EditText);
        tel3EditText = findViewById(R.id.tel3EditText);
        tel2CheckBox = findViewById(R.id.tel2CheckBox);
        tel3CheckBox = findViewById(R.id.tel3CheckBox);
        emailEditText = findViewById(R.id.emailEditText);
        emailCheckBox = findViewById(R.id.emailCheckBox);

    }
}