package avanzadas.herramientas.sales_partner.Ordenes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.Clientes;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Productos.ProductAdapter;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;

public class AdapterOrdenes extends RecyclerView.Adapter<AdapterOrdenes.ViewHolder> implements View.OnClickListener {



    private List<Ordenes> ordenesList;
    public AdapterOrdenes(List<Ordenes> ordenesList){
        this.ordenesList = ordenesList;
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
    public AdapterOrdenes.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ordenes_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrdenes.ViewHolder viewHolder, int i) {

        viewHolder.Bind(ordenesList.get(i));
    }

    @Override
    public int getItemCount() {
        return ordenesList.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private Ordenes ordenActual;
        private TextView nombre;
        private TextView status;
        private TextView cantidad_ordenes;
        private TextView costo_ordenes;
        private LinearLayout linearLayoutOrdenes;

        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);

            nombre = itemView.findViewById(R.id.Cliente_ordenes);
            status = itemView.findViewById(R.id.status_ordenes);
            cantidad_ordenes = itemView.findViewById(R.id.cantidad_ordenes);
            costo_ordenes = itemView.findViewById(R.id.costo_ordenes);
            linearLayoutOrdenes = itemView.findViewById(R.id.recyclerViewAdapter);
            linearLayoutOrdenes.setOnCreateContextMenuListener(this);
        }

        public void Bind(Ordenes ordenes){
            AppDataBase db = AppDataBase.getAppDataBase(itemView.getContext());
            ClientesDao clientesDao= db.clientesDao();
            StatusOrdenesDao statusOrdenesDao=db.statusorderDao();
            OrdenesDao ordenesDao=db.orderDao();
            OrdenesEnsamblesDao ordenesEnsamblesDao=db.ordenesensamblesDao();
            ProductosDao productosDao=db.productosDao();

            ordenActual=ordenes;

            nombre.setText(clientesDao.getClienteById(ordenes.getCustomer_id()).getFirst_name()+" "+
                    clientesDao.getClienteById(ordenes.getCustomer_id()).getLast_name());
            status.setText(statusOrdenesDao.getStatusByid(ordenes.getStatus_id()).getDescription());
            cantidad_ordenes.setText("Cantidad de ensambles: "+ ordenesEnsamblesDao.getCantidadDeEnsamblesByOrder(ordenes.getId()).size());
            costo_ordenes.setText("Costo total de ordenen: "+ productosDao.IngresosById(ordenes.getId()));

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AppDataBase db = AppDataBase.getAppDataBase(v.getContext());

            String next=db.statusorderDao()
                    .getStatusByid(ordenActual.getStatus_id()).getNext();
            String prev=db.statusorderDao()
                    .getStatusByid(ordenActual.getStatus_id()).getPrevious();

           // int itemid=item.getItemId();



            menu.add(this.getAdapterPosition(), 0,0,"DETALLES");




            if(prev.equalsIgnoreCase("5")){
                menu.add(this.getAdapterPosition(), 1,1,"REGRESAR ESTADO").setEnabled(false);
            }
            else{
                menu.add(this.getAdapterPosition(), 1,1,"REGRESAR ESTADO");
            }
            if(next.equalsIgnoreCase("5")){
                menu.add(this.getAdapterPosition(), 2,2,"AVANZAR ESTADO").setEnabled(false);
            }
            else{
                menu.add(this.getAdapterPosition(), 2,2,"AVANZAR ESTADO");
            }
            if(ordenActual.getStatus_id()==0){
                menu.add(this.getAdapterPosition(), 3,3,"EDITAR ORDEN");
            }
            else {menu.add(this.getAdapterPosition(), 3,3,"EDITAR ORDEN").setEnabled(false);}

        }
    }
}