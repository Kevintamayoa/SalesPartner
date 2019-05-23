package avanzadas.herramientas.sales_partner;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;

public class LoginActivity extends AppCompatActivity {

    ClientesDao clientesDao;
    private EditText user;
    private EditText clave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Sales Partner");

        AppDataBase db = AppDataBase.getAppDataBase(getApplicationContext());
        clientesDao = db.clientesDao();
        user= findViewById(R.id.usuarioLogin);
        clave= findViewById(R.id.claveLogin);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setIcon(R.mipmap.icono2_round);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.loginButton) {
            //Toast.makeText(this, "Funciona", Toast.LENGTH_SHORT).show();
        Login(user.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public Clientes Login(String usuarioText){
        Clientes cl;
        if(clientesDao.GetClienteByUser(usuarioText)!=null){
            cl= clientesDao.GetClienteByUser(usuarioText);

            if(cl.getClave().equalsIgnoreCase(clave.getText().toString())){
                clientesDao.ChangeOnline(cl.getId());
                restartApp();
            }
            else{
                AlertDialog.Builder dialog= new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("Ooops");
                dialog.setMessage("La contraseña fue incorrecta");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
       }
        else {
            AlertDialog.Builder dialog= new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("Ooops");
            dialog.setMessage("El usuario no existe");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

       return null;
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
