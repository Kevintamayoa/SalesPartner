package avanzadas.herramientas.sales_partner.Ensambles;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EnsamblesDao {

    @Insert
    public void InsertEnsamble(Ensambles ensambles);

    @Delete
    public void DeleteEnsamble(Ensambles ensambles);

    @Query("Select * from  assemblies;")
    public List<Ensambles> getAllEnsambles();

    @Query("select * from assemblies where id = :category")
    public Ensambles getEnsamblesByCategory(int category);

    @Query("select * from assemblies where description LIKE  :description")
    public List<Ensambles> getEnsamblesByText(String description);

    @Query("Select * from assemblies p where p.id=:Id and p.description Like :description")
    public List<Ensambles> getEnsamblesyTextAndCategory(String description, int Id);

    @Query("SELECT COUNT(*) FROM assembly_products ap INNER JOIN products p ON (ap.product_id=p.id) Where ap.id=:id;")
    public int getCantidadProductosinEnsambles(int id);

    @Query("SELECT sum(p.price) FROM assembly_products ap INNER JOIN products p ON (ap.product_id=p.id) Where ap.id=:id;")
    public int getCostoinEnsamble(int id);

    @Query("select * from assemblies where description LIKE  :description ORDER BY description ASC")
    public List<Ensambles> getEnsamblesByTextOrderByDescription(String description);

    @Query("SELECT * FROM assemblies where id=:id")
    public Ensambles getEnsamblesByID(int id);

    @Query("SELECT * FROM assemblies a inner join order_assemblies oa ON(a.id=oa.assembly_id) where a.id=:id")
    public List<Ensambles> getEnsamblesByOA_id(int id);

    @Query("SELECT a.id, a.description FROM assemblies a inner join order_assemblies oa ON(a.id=oa.assembly_id) inner join orders o On(o.id=oa.id)where o.id=:id")
    public List<Ensambles> getEnsamblesByOrdenes(int id);

}
