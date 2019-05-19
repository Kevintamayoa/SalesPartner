package avanzadas.herramientas.sales_partner.Reportes;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avanzadas.herramientas.sales_partner.AppDataBase;
import avanzadas.herramientas.sales_partner.Clientes.ClientesDao;
import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;
import avanzadas.herramientas.sales_partner.Ensambles.EnsamblesProducts;
import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.Ordenes.OrdenesDao;
import avanzadas.herramientas.sales_partner.Ordenes.StatusOrdenesDao;
import avanzadas.herramientas.sales_partner.Productos.Productos;
import avanzadas.herramientas.sales_partner.Productos.ProductosDao;
import avanzadas.herramientas.sales_partner.R;

public class SimuladorAdapter extends RecyclerView.Adapter<SimuladorAdapter.ViewHolder> implements View.OnClickListener {

    static AppDataBase db;
    private List<Ordenes> ordenesList;
    private List<Productos> productosList;

    public SimuladorAdapter(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
        this.productosList = productosList;
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
    public SimuladorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ordenes_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimuladorAdapter.ViewHolder viewHolder, int i) {

        viewHolder.Bind(ordenesList.get(i));
    }

    @Override
    public int getItemCount() {
        return ordenesList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre;
        private TextView status;
        private TextView cantidad_ordenes;
        private TextView costo_ordenes;
        private LinearLayout linearLayoutOrdenes;

        private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);


            db = AppDataBase.getAppDataBase(itemView.getContext());
int porf=0;

            nombre = itemView.findViewById(R.id.Cliente_ordenes);
            status = itemView.findViewById(R.id.status_ordenes);
            cantidad_ordenes = itemView.findViewById(R.id.cantidad_ordenes);
            costo_ordenes = itemView.findViewById(R.id.costo_ordenes);
            linearLayoutOrdenes = itemView.findViewById(R.id.recyclerViewAdapter);
        }

        public void Bind(Ordenes ordenes) {

            List<Productos> productosListSimulacion = new ArrayList<>();
            List<EnsamblesProducts> ensamblesProductsList = db.enamblesProductsDao().getAllAssemblyProducts();
            List<Ensambles> ensambles = db.ensamblesDao().getEnsamblesByOrdenes(ordenes.getId());
            List<Productos> productosList = new ArrayList<>();
            int a=0;
            List<Integer> ints= new ArrayList<>();

            for(Ensambles e : ensambles){
                for(Productos o:db.productosDao().getProductosByEnsambles(e.getId())) {

                    productosList.add(o);


                }
            }
            for(Productos p : productosList){
                for(Productos inventory: db.productosDao().getAllProductos()){
                    if(p.getId()==inventory.getId()){
                        p.setCantidad(inventory.getCantidad()-p.getCantidad());
                        ints.add(inventory.getCantidad()-p.getCantidad());
                    }

                }

            }
            if(ints.contains(0)){
                a=10;
            }
            for(Productos p : productosList){
                if(p.getCantidad()<0){
                    a=5;
                    break;
                }
            }


            //if(ordenes.getStatus_id() == 0){
            //    for(EnsamblesProducts ep : ensamblesProductsList){
            //        if(ep.getProduct_id() == productosList.get(ordenes.getId()).getId()){
            //            for(Productos p : productosList){
            //                productosListSimulacion.add(p);
            //            }
            //        }
            //    }
            //}



            AppDataBase db = AppDataBase.getAppDataBase(itemView.getContext());
            ClientesDao clientesDao = db.clientesDao();
            StatusOrdenesDao statusOrdenesDao = db.statusorderDao();
            OrdenesDao ordenesDao = db.orderDao();
            ProductosDao productosDao = db.productosDao();

            //---------CHECAR---------

                if(a == 0){
                    //FALTAN PRODUCTOS
                    linearLayoutOrdenes.setBackgroundColor(Color.GREEN);
                } else if(a == 10){
                    //SE PUEDE HACER
                    linearLayoutOrdenes.setBackgroundColor(Color.YELLOW);
                }else if (a ==5){
                    //NEL
                    linearLayoutOrdenes.setBackgroundColor(Color.RED);
                }



            double costoOrdenes =(double) ordenesDao.getCostProductOfEachCliente(ordenes.getId()) /100;

            nombre.setText(clientesDao.getClienteById(ordenes.getCustomer_id()).getFirst_name() + " " +
                    clientesDao.getClienteById(ordenes.getCustomer_id()).getLast_name());
            status.setText(statusOrdenesDao.getStatusByid(ordenes.getStatus_id()).getDescription()
                    + "\n" + ordenes.getDate());
            cantidad_ordenes.setText("Cantidad de ordenes: " + ordenesDao.getCantidadDeOrdenesById(ordenes.getCustomer_id()));
            costo_ordenes.setText("Costo total de ordenes: " + costoOrdenes);


        }
    }
}