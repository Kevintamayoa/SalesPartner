package avanzadas.herramientas.sales_partner.Ensambles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.MainActivity;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.R;

public class EnsamblesAdapter extends RecyclerView.Adapter<EnsamblesAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView cantidadproductos;
        private TextView costo;

        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context= itemView.getContext();
            descripcion= itemView.findViewById(R.id.DescripcionEnsamble);
            cantidadproductos=itemView.findViewById(R.id.numOfProductsEnsamble);
            costo=itemView.findViewById(R.id.costoTotalEnsamble);
            final View parent = itemView;
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
    }

    private List<Ensambles> EnsamblesList;
    public EnsamblesAdapter(List<Ensambles> EnsamblesList){
        this.EnsamblesList = EnsamblesList;
    }

    @NonNull
    @Override
    public EnsamblesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ensambles_rc, viewGroup, false);
        view.setOnClickListener(this);
        //return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_rc, viewGroup, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnsamblesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.Bind(EnsamblesList.get(i));
    }

    @Override
    public int getItemCount() {
        return EnsamblesList.size();
    }
}
