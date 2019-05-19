package avanzadas.herramientas.sales_partner.Productos;

import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ViewModelProducts extends ViewModel {
    private List<Productos> productosList;

    public List<Productos> getProductosList() {
        return productosList;
    }

    public void setProductosList(List<Productos> productosList) {
        this.productosList = productosList;
    }
}
