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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.R;

public class
AdapterEditOrdenes  extends RecyclerView.Adapter<AdapterEditOrdenes.ViewHolder> implements View.OnClickListener {

    public List<String> cantidades=new ArrayList<>();
    //

    private static List<Ensambles> ensamblesList;
    public static Ordenes ordenes;


    public AdapterEditOrdenes(List<Ensambles> ensamblesList,Ordenes ordenes){
        this.ensamblesList=ensamblesList;
        this.ordenes=ordenes;
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
    public AdapterEditOrdenes.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.agregarorden_rc, viewGroup, false);
        view.setOnClickListener(this);
       // db = AppDataBase.getAppDataBase(viewGroup.getContext());
        return new AdapterEditOrdenes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEditOrdenes.ViewHolder viewHolder, int i) {

        //vh= viewHolder;
        viewHolder.Bind(ensamblesList.get(i));
    }

    @Override
    public int getItemCount() {
        return ensamblesList.size();
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
        private int n=0;

        private AppDataBase db;

        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            db= AppDataBase.getAppDataBase(itemView.getContext());
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


            double precio = (double) db.ensamblesDao().getCostoinEnsamble(ensambles.getId()) / 100;

            DescripcionEnsamble.setText(ensambles.getDescription());
            numOfProductsEnsamble.setText("Productos: "+db.productosDao().getProductosByEnsambles(ensambles.getId()).size());
            costoTotalEnsamble.setText("$"+precio);


            try {
                n=db.ordenesensamblesDao().
                        getQtydeCadaEnsamblesByOrden(ordenes.getId(),ensambles.getId()).getQty();
                cantidad_ensambles_add.setText(String.valueOf(db.ordenesensamblesDao().
                        getQtydeCadaEnsamblesByOrden(ordenes.getId(),ensambles.getId()).getQty()));
            }
            catch(Exception e){
                n=1;
                cantidad_ensambles_add.setText(String.valueOf(1));
            }


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