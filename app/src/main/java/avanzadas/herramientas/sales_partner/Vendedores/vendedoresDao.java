package avanzadas.herramientas.sales_partner.Vendedores;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import avanzadas.herramientas.sales_partner.Clientes.Clientes;
@Dao
public interface vendedoresDao {

    @Query("Select * from vendedores ")
    public List<Vendedores> getAllVendedores();

    @Query("Update vendedores SET online= 1 where id=:id")
    public void ChangeLogin(int id);

    @Query("Update vendedores SET online= 0 where id=:id")
    public void ChangeLogout(int id);

    @Query("Select * from  vendedores  where user_name=:user")
    public Vendedores GetClienteByUser(String user);

}
