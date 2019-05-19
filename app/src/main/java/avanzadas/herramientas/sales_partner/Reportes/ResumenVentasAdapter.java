package avanzadas.herramientas.sales_partner.Reportes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ordenes.Ordenes;
import avanzadas.herramientas.sales_partner.R;

public class ResumenVentasAdapter extends RecyclerView.Adapter<ResumenVentasAdapter.ViewHolder> implements View.OnClickListener {


    private View.OnClickListener listener;
    private List<Ordenes> ordenesList;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mes;
        private ImageView imageView;
        private LinearLayout recyclerViewAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mes = itemView.findViewById(R.id.mes);
            imageView = itemView.findViewById(R.id.mesImageView);


        }

        int indice = 0;

        public void Bind(Ordenes ordenes) {

            String[] meses = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBE", "NOVIEMBRE", "DICIEMBRE"};
            mes.setText(meses[indice]);

            indice++;
        }
    }


    public ResumenVentasAdapter(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
    }

    @NonNull
    @Override
    public ResumenVentasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resumen_ventas_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumenVentasAdapter.ViewHolder viewHolder, int i) {
        viewHolder.Bind(ordenesList.get(i));
    }

    @Override
    public int getItemCount() {
        return ordenesList.size();
    }
}
