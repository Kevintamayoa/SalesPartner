package avanzadas.herramientas.sales_partner.Clientes;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import avanzadas.herramientas.sales_partner.R;

public class EditClientesActivity extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText apellidoEditText;
    private EditText direccionEditText;
    private EditText tel1EditText;
    private EditText tel2EditText;
    private EditText tel3EditText;
    private EditText emailEditText;
    private CheckBox tel2CheckBox;
    private CheckBox tel3CheckBox;
    private CheckBox emailCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clientes);

        findview();
        getSupportActionBar().setTitle("Editar Cliente");

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.clientes_action_bar, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == R.id.saveButton){
            Toast.makeText(this, "addButton", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void findview(){
        nombreEditText = findViewById(R.id.nombreEditText);
        apellidoEditText= findViewById(R.id.apellidoEditText);
        direccionEditText= findViewById(R.id.direccionEditText);
        tel1EditText= findViewById(R.id.tel1EditText);
        tel2EditText= findViewById(R.id.tel2EditText);
        tel3EditText= findViewById(R.id.tel3EditText);
        tel2CheckBox= findViewById(R.id.tel2CheckBox);
        tel3CheckBox= findViewById(R.id.tel3CheckBox);
        emailEditText= findViewById(R.id.emailEditText);
        emailCheckBox= findViewById(R.id.emailCheckBox);
    }
}
