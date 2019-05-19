package avanzadas.herramientas.sales_partner.Productos;

import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import avanzadas.herramientas.sales_partner.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements View.OnClickListener {

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

        private ImageView imagenAdapter;
        private TextView precioAdapter;
        private TextView descriptionAdapter;
        //private RecyclerView recyclerViewAdapter;

        private Productos productos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenAdapter = itemView.findViewById(R.id.imagenAdapter);
            precioAdapter = itemView.findViewById(R.id.precioAdapter);
            descriptionAdapter = itemView.findViewById(R.id.descriptionAdapter);
            //recyclerViewAdapter = itemView.findViewById(R.id.recyclerViewAdapter);

            final View parent = itemView;

        }

        public void Bind(Productos productos){
            descriptionAdapter.setText(productos.getDescription());

            double precioDouble = (double) productos.getPrecio() /100;
            precioAdapter.setText("$"+ precioDouble);
            if(productos.getCategory_id() == 0){
                //METER UN DRAWABLE
                //imagenAdapter.setBackground();
               imagenAdapter.setBackgroundResource(R.drawable.product_category_hdd);
            }else if(productos.getCategory_id() == 1){
                imagenAdapter.setBackgroundResource(R.drawable.product_category_memory);
            }else if(productos.getCategory_id() == 2){
                imagenAdapter.setBackgroundResource(R.drawable.product_category_screen);
            }else if(productos.getCategory_id() == 3){
                imagenAdapter.setBackgroundResource(R.drawable.product_category_procesador);
            }else if(productos.getCategory_id() == 4){
                imagenAdapter.setBackgroundResource(R.drawable.product_category_procesador);
            }else if(productos.getCategory_id() == 5){
                imagenAdapter.setBackgroundResource(R.drawable.product_category_tvideo);
            }
            else if(productos.getCategory_id() == 6){
                imagenAdapter.setBackgroundResource(R.drawable.produc_category_tsound);
            }
        }
    }

    private List<Productos> productosList;
    public ProductAdapter(List<Productos> productosList){
        this.productosList = productosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.Bind(productosList.get(i));
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

}
