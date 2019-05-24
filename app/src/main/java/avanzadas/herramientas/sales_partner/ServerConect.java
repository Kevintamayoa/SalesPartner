package avanzadas.herramientas.sales_partner;

import android.content.Context;
import android.util.Log;
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

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;

import static android.content.ContentValues.TAG;

public class ServerConect {
    List<Clientes> clientes;
    List<Ensambles> ensambles ;
    List<Ordenes> ordenes;
    List<Productos> productos;
    private String jsonResponse;
    AppDataBase db;

    private String url = "http://192.168.0.27:3000/products";
  //  private String url = "http://192.168.0.27:3000/products";


    public ServerConect(Context context){
         db= AppDataBase.getAppDataBase(context);
       // final Context context= this.context;
        // Recuperamos todo del servidor
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //trying to test the response length but no results also
                 for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Integer id=jsonObject.getInt("id");
                        String description= jsonObject.getString("descrption");
                        Integer category_id=jsonObject.getInt("category_id");
                        Integer price=jsonObject.getInt("price");
                        Integer qty=jsonObject.getInt("qty");
                        Productos p=new Productos(id,category_id,description,price,qty);
                        ProductosDao productosDao=db.productosDao();
                        productosDao.insertProductId(p);
                  //     Toast.makeText(context., "conectando", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }



                //Log.v("Data from the web: " , response.toString());


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Mainactivity", error.getMessage());

            }
        });

    //    AppController.getInstance().addToRequestQueue(request);



    }
    public void SaveClientes(){

    }
    public void SaveEnsambles(){

    }
    public void SaveOrdenes(){

    }
    public void SaveProducts(){}




}
