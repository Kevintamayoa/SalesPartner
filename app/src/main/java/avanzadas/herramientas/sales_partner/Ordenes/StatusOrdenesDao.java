package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

@Dao
public interface StatusOrdenesDao {
    @Insert
    public void InsertStatusOrdenes(StatusOrdenes statusOrdenes);


    @Query("Select * from  order_status;")
    public List<StatusOrdenes> getAllStatusOrders();

    @Query("Select * from order_status where id=:id")
    public StatusOrdenes getStatusByid(int id);

}
