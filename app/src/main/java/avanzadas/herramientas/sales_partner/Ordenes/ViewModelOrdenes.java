package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ViewModelOrdenes extends ViewModel {

    private List<Ordenes> ordenesList;

    public List<Ordenes> getOrdenesList() {
        return ordenesList;
    }

    public void setOrdenesList(List<Ordenes> ordenesList) {
        this.ordenesList = ordenesList;
    }
}
