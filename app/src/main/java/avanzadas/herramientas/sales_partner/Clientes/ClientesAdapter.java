package avanzadas.herramientas.sales_partner.Clientes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.R;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ViewHolder> implements View.OnClickListener {

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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private ImageView imageViewAdapter;
        private TextView fullNameAdapter;
        private TextView addressAdapter;
        private TextView tels1Adapter;
        private TextView emailAdapter;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAdapter = itemView.findViewById(R.id.imagenAdapter);
            fullNameAdapter = itemView.findViewById(R.id.fullNameAdapter);
            addressAdapter = itemView.findViewById(R.id.addressAdapter);
            tels1Adapter = itemView.findViewById(R.id.tels1Adapter);
            emailAdapter = itemView.findViewById(R.id.emailAdapter);
            linearLayout = itemView.findViewById(R.id.recyclerViewAdapter);
            linearLayout.setOnCreateContextMenuListener(this);

        }

        public void Bind(Clientes clientes) {
            String[] firstName = clientes.getFirst_name().split(" ");
            String[] lastName = clientes.getLast_name().split(" ");


            fullNameAdapter.setText(lastName[0] + " " + firstName[0]);
            tels1Adapter.setText("Tel. " + clientes.getPhone1());
            emailAdapter.setText(clientes.getEmail());
            addressAdapter.setText(clientes.getDireccion());
            //AGREGAR FOTOS A CLIENTES(MISMA FOTO)
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 0, 0, "DETALLES");
            menu.add(this.getAdapterPosition(), 1, 1, "EDITAR");
            menu.add(this.getAdapterPosition(), 2, 2, "ELIMINAR");
        }
    }

    private List<Clientes> clientesList;

    public ClientesAdapter(List<Clientes> clientesList) {
        this.clientesList = clientesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clientes_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.Bind(clientesList.get(i));
    }

    @Override
    public int getItemCount() {
        return clientesList.size();
    }


}