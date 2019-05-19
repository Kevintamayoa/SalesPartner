package avanzadas.herramientas.sales_partner.Reportes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import avanzadas.herramientas.sales_partner.R;

public class MesesAdapter extends RecyclerView.Adapter<MesesAdapter.ViewHolder> implements View.OnClickListener {

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

    private List<Meses> mesesList;
    public MesesAdapter(List<Meses> mesesList) {
        this.mesesList = mesesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mes;
        private ImageView imageView;
        Meses meses;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mes = itemView.findViewById(R.id.mes);
            imageView = itemView.findViewById(R.id.mesImageView);

        }
        String[] a = {"ENERO", "FEBRERO","MARZO", "ABRIL","MAYO", "JUNIO","JULIO", "AGOSTO","SEPTIEMBRE", "OCTUBE","NOVIEMBRE", "DICIEMBRE"};


        int indice = 0;
        public void Bind(Meses meses) {

            mes.setText(meses.getMonthString());

            if (meses.getMonthString().equalsIgnoreCase("ENERO")){
                imageView.setBackgroundResource(R.drawable.enero);
            }
            else if (meses.getMonthString().equalsIgnoreCase("FEBRERO")){
                imageView.setBackgroundResource(R.drawable.febrero);
            }
            else if (meses.getMonthString().equalsIgnoreCase("MARZO")){
                imageView.setBackgroundResource(R.drawable.marzo);
            }
            else if (meses.getMonthString().equalsIgnoreCase("ABRIL")){
                imageView.setBackgroundResource(R.drawable.abril);
            }
            else if (meses.getMonthString().equalsIgnoreCase("MAYO")){
                imageView.setBackgroundResource(R.drawable.mayo);
            }
            else if (meses.getMonthString().equalsIgnoreCase("JUNIO")){
                imageView.setBackgroundResource(R.drawable.junio);
            }
            else if (meses.getMonthString().equalsIgnoreCase("JULIO")){
                imageView.setBackgroundResource(R.drawable.julio);
            }
            else if (meses.getMonthString().equalsIgnoreCase("AGOSTO")){
                imageView.setBackgroundResource(R.drawable.agosto);
            }
            else if (meses.getMonthString().equalsIgnoreCase("SEPTIEMBRE")){
                imageView.setBackgroundResource(R.drawable.septiembre);
            }
            else if (meses.getMonthString().equalsIgnoreCase("OCTUBRE")){
                imageView.setBackgroundResource(R.drawable.octubre);
            }
            else if (meses.getMonthString().equalsIgnoreCase("NOVIEMBRE")){
                imageView.setBackgroundResource(R.drawable.noviembre);
            }
            else if (meses.getMonthString().equalsIgnoreCase("DICIEMBRE")){
                imageView.setBackgroundResource(R.drawable.diciembre);
            }

           // mes.setText(a[indice]);
            indice++;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resumen_ventas_rc, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.Bind(mesesList.get(i));
    }

    @Override
    public int getItemCount() {
        return mesesList.size();
    }
}
