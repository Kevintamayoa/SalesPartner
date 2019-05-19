package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

@Dao
public interface OrdenesDao {
    @Insert
    public void InsrtOrdenes(Ordenes ordenes);

    @Update
    public void UdapteOrdenStatus(Ordenes ordenes);

    @Query("SELECT * from orders ")
    public List<Ordenes> getAllOrdenes();

    @Query("Select * from orders where (status_id = 0) Order By (substr(date,6,4)) asc;")
    public List<Ordenes>getAllOrdenesByDate2();

    @Query("select * from orders o\n" +
            "inner join customers c on(o.customer_id = c.id)\n" +
            "where (status_id = 0)\n" +
            "order by c.id;")
    public List<Ordenes> getAllOrdersOrderByClienteId();

    @Query("SELECT SUM(oa.qty*ap.qty*p.price) AS earnings\n" +
            "FROM orders o \n" +
            "INNER JOIN order_assemblies oa ON (o.id=oa.id) \n" +
            "INNER JOIN assembly_products ap ON (oa.assembly_id==ap.id)  \n" +
            "INNER JOIN products p ON (ap.product_id==p.id) \n" +
            "WHERE o.status_id =0 and o.id=:id")
    public double getCostProductOfEachCliente(int id);


    @Query("SELECT o.id, o.status_id, o.customer_id, o.date, o.change_log, SUM(oa.qty*ap.qty*p.price) AS earnings\n" +
        "FROM orders o \n" +
        "INNER JOIN order_assemblies oa ON (o.id=oa.id) \n" +
        "INNER JOIN assembly_products ap ON (oa.assembly_id==ap.id)  \n" +
        "INNER JOIN products p ON (ap.product_id==p.id) \n" +
        "WHERE o.status_id =0\n" +
        "GROUP BY o.id \n" +
        "ORDER BY (oa.qty*ap.qty*p.price) asc;")
    public List<Ordenes> getAllOrdersOrderByMontoVenta();

    @Query("SELECT * from orders where id=:id")
    public Ordenes getOrdenById(int id);

    @Query("Select * from orders Order By customer_id DESC;")
    public List<Ordenes> getAllOrdenesByCustomerIdDesc();

    @Query("Select * from orders Order By date DESC;")
    public List<Ordenes> getAllOrdenesByDate();

    @Query("SELECT COUNT(*) from orders where customer_id=:id")
    public int getCantidadDeOrdenesById(int id);

    @Query("Select * from orders o INNER JOIN order_status os On (os.id=o.status_id) where os.description=:description")
    public List<Ordenes> getOrdenesByStatus(String description);

    @Query("Update orders SET change_log= :comentario where id=:id;")
    public void UpdateOrderByChangeLog(String comentario, int id);

    @Query("SELECT * from orders where customer_id=:id")
    public List<Ordenes> getOrdenesByClientesName(int id);

    @Query("select * from orders o \n" +
            "INNER JOIN order_status os On (os.id=o.status_id)\n" +
            "INNER JOIN customers c on(c.id=o.customer_id)\n" +
            "where os.description=:description and c.id=:id_cliente")
    public List<Ordenes> getOrdenesByStatusAndClientes(String description, int id_cliente);


}
