package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.lifecycle.ViewModel;
import android.view.View;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

public class ViewModelAddOrden extends ViewModel {
    private List<Ensambles> ensamblesList;


    public List<Ensambles> getEnsamblesList() {
        return ensamblesList;
    }

    public void setEnsamblesList(List<Ensambles> ensamblesList) {
        this.ensamblesList = ensamblesList;
    }



}
