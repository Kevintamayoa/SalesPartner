package avanzadas.herramientas.sales_partner.Ordenes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;

public class AdapterAddOrder  extends RecyclerView.Adapter<AdapterAddOrder.ViewHolder> implements View.OnClickListener {

    public List<String> cantidades=new ArrayList<>();

    public ViewHolder vh;
    private List<Ensambles> ensambles;
    public AdapterAddOrder(List<Ensambles> ensambles){
        this.ensambles = ensambles;
    }

    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public AdapterAddOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.agregarorden_rc, viewGroup, false);
        view.setOnClickListener(this);

        return new AdapterAddOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddOrder.ViewHolder viewHolder, int i) {

        vh= viewHolder;
        viewHolder.Bind(ensambles.get(i));
    }

    @Override
    public int getItemCount() {
        return ensambles.size();
    }

    public String Gind() {


       return vh.cantidad_ensambles_add.getText().toString();

    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        Context context;
        private TextView DescripcionEnsamble;
        private TextView numOfProductsEnsamble;
        private TextView costoTotalEnsamble;
        public TextView cantidad_ensambles_add;
        private LinearLayout linearLayoutEnsambesAdd;
        private Button minus_button;
        private Button plus_button;
        int n=1;

        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context=itemView.getContext();
            id = itemView.findViewById(R.id.id);

            DescripcionEnsamble = itemView.findViewById(R.id.DescripcionEnsamble);
            numOfProductsEnsamble = itemView.findViewById(R.id.numOfProductsEnsamble);
            costoTotalEnsamble = itemView.findViewById(R.id.costoTotalEnsamble);
            cantidad_ensambles_add = itemView.findViewById(R.id.cantidad_ensambles_add);
            linearLayoutEnsambesAdd = itemView.findViewById(R.id.linearLayoutEnsambesAdd);
            minus_button = itemView.findViewById(R.id.minus_button);
            plus_button = itemView.findViewById(R.id.plus_button);

            linearLayoutEnsambesAdd.setOnCreateContextMenuListener(this);
        }

        public void Bind(Ensambles ensambles){
            AppDataBase db = AppDataBase.getAppDataBase(itemView.getContext());

            double precio = (double) db.ensamblesDao().getCostoinEnsamble(ensambles.getId()) / 100;

           DescripcionEnsamble.setText(ensambles.getDescription());
           numOfProductsEnsamble.setText("Productos: "+db.productosDao().getProductosByEnsambles(ensambles.getId()).size());
           costoTotalEnsamble.setText("$"+precio);

           cantidad_ensambles_add.setText(String.valueOf(n));

           minus_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  if(n==1){
                      Toast.makeText(context, "Debe haber al menos un ensamble", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      n = n - 1;
                      cantidad_ensambles_add.setText(String.valueOf(n));
                  }
               }
           });
           plus_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   n=n+1;

                   cantidad_ensambles_add.setText(String.valueOf(n));
               }
           });




        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AppDataBase db = AppDataBase.getAppDataBase(v.getContext());



            menu.add(this.getAdapterPosition(), 0,0,"ELIMINAR");

        }
    }
}