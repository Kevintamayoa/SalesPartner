package avanzadas.herramientas.sales_partner.Clientes;

import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ViewModelClientes extends ViewModel {

    private List<Clientes> clientesList;

    public List<Clientes> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Clientes> clientesList) {
        this.clientesList = clientesList;
    }
}
