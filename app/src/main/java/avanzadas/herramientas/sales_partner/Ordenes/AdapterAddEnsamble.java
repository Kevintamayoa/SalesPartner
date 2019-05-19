package avanzadas.herramientas.sales_partner.Ordenes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;

public class AdapterAddEnsamble  extends RecyclerView.Adapter<AdapterAddEnsamble.ViewHolder> implements View.OnClickListener {


    private List<Ensambles> EnsamblesList;
    private List<Ordenes> ordenesList;
    public AdapterAddEnsamble(List<Ensambles> EnsamblesList){
        this.EnsamblesList = EnsamblesList;
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
    public AdapterAddEnsamble.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ensambles_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddEnsamble.ViewHolder viewHolder, int i) {

        viewHolder.Bind(EnsamblesList.get(i));
    }

    @Override
    public int getItemCount() {
        return  EnsamblesList.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView descripcion;
        private TextView cantidadproductos;
        private TextView costo;
        private LinearLayout LinearLayout;
        Context context;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context= itemView.getContext();
            descripcion= itemView.findViewById(R.id.DescripcionEnsamble);
            cantidadproductos=itemView.findViewById(R.id.numOfProductsEnsamble);
            costo=itemView.findViewById(R.id.costoTotalEnsamble);
            LinearLayout = itemView.findViewById(R.id.recyclerViewAdapter);
            LinearLayout.setOnCreateContextMenuListener(this);
        }

        public void Bind(Ensambles ensambles){
            AppDataBase db= AppDataBase.getAppDataBase(context);
            EnsamblesDao ed= db.ensamblesDao();

            double cost= ed.getCostoinEnsamble(ensambles.getId())/100;
            descripcion.setText(ensambles.getDescription());
            //cantidadproductos.setText("Num of Products in assembly: "+ String.valueOf(ed.getCantidadProductosinEnsambles(ensambles.getId())));
            cantidadproductos.setText("Cantidad de productos: "+ ed.getCantidadProductosinEnsambles(ensambles.getId()));
            costo.setText("Costo: $"+ cost);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


            menu.add(this.getAdapterPosition(), 0,0,"DETALLES");
            menu.add(this.getAdapterPosition(), 1,1,"AGREGAR ");

        }
    }
}

