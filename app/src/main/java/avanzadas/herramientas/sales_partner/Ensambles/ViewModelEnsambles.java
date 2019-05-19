package avanzadas.herramientas.sales_partner.Ensambles;

import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ViewModelEnsambles extends ViewModel {

    private List<Ensambles> ensamblesList;

    public List<Ensambles> getEnsamblesList() {
        return ensamblesList;
    }

    public void setEnsamblesList(List<Ensambles> ensamblesList) {
        this.ensamblesList = ensamblesList;
    }
}
