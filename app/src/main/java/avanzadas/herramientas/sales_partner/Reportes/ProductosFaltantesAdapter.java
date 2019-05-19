package avanzadas.herramientas.sales_partner.Reportes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;


public class ProductosFaltantesAdapter extends RecyclerView.Adapter<ProductosFaltantesAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;

    private List<Productos> productosList;

    public ProductosFaltantesAdapter(List<Productos> productosList) {
        this.productosList = productosList;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagenAdapter;
        private TextView description;
        private TextView precio;
        private TextView idAdapter;
        private TextView qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenAdapter = itemView.findViewById(R.id.imagenAdapter);
            description = itemView.findViewById(R.id.descriptionAdapter);
            precio = itemView.findViewById(R.id.precioAdapter);
            idAdapter = itemView.findViewById(R.id.idAdapter);
            qty = itemView.findViewById(R.id.cantidadAdapter);
        }

        public void Bind(Productos productos) {

            AppDataBase db = AppDataBase.getAppDataBase(itemView.getContext());
            ProductosDao productosDao = db.productosDao();

            description.setText(productos.getDescription());
            idAdapter.setText("ID: " + productos.getId());
            qty.setText("Faltantes: " + productosDao.getAllmissingProducts(productos.getId()));

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

            setImage(productos);
        }
    }

    private void setImage(Productos productos) {
        if (productos.getCategory_id() == 0) {
            //METER UN DRAWABLE
            //imagenAdapter.setBackground();
        } else if (productos.getCategory_id() == 1) {

        } else if (productos.getCategory_id() == 2) {

        } else if (productos.getCategory_id() == 3) {

        } else if (productos.getCategory_id() == 4) {

        } else if (productos.getCategory_id() == 5) {

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productos_faltantes_rc, viewGroup, false);
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