package avanzadas.herramientas.sales_partner.Ordenes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Productos.ProductAdapter;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.R;

public class AdapterDetallesRc extends RecyclerView.Adapter<AdapterDetallesRc.ViewHolder> implements View.OnClickListener {

    static AppDataBase db;
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


        private TextView producto_rc_detalle;
        private TextView cantidad_prducto_detalles;
        //private RecyclerView recyclerViewAdapter;

        private Productos productos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            producto_rc_detalle = itemView.findViewById(R.id.producto_rc_detalle);
            cantidad_prducto_detalles = itemView.findViewById(R.id.cantidad_prducto_detalles);
            //recyclerViewAdapter = itemView.findViewById(R.id.recyclerViewAdapter);

            final View parent = itemView;

        }

        public void Bind(Productos productos){


            producto_rc_detalle.setText("Producto: "+ productos.getDescription());

           cantidad_prducto_detalles.setText("Cnatidad: "+ productos.getCantidad());

        }
    }

    private List<Productos> productosList;
    public AdapterDetallesRc(List<Productos> productosList){
        this.productosList = productosList;
    }

    @NonNull
    @Override
    public AdapterDetallesRc.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detalles_rc, viewGroup, false);
        view.setOnClickListener(this);
        db = AppDataBase.getAppDataBase(viewGroup.getContext());
        return new AdapterDetallesRc.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetallesRc.ViewHolder viewHolder, int i) {
        viewHolder.Bind(productosList.get(i));
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

}

