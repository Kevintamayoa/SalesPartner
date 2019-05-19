package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

public class ViewModelEditOrden extends ViewModel {
    private List<Ensambles> ensamblesList;


    public List<Ensambles> getEnsamblesList() {
        return ensamblesList;
    }

    public void setEnsamblesList(List<Ensambles> ensamblesList) {
        this.ensamblesList = ensamblesList;
    }
}
