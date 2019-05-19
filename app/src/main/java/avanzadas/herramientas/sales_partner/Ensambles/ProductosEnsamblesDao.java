package avanzadas.herramientas.sales_partner.Ensambles;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import avanzadas.herramientas.sales_partner.Productos.Productos;

@Dao
public interface ProductosEnsamblesDao {
    @Insert
    public void InsertAssemblyProducts(EnsamblesProducts assemblyproducts);

    @Update
    public void UpdateAssemblyProducts(EnsamblesProducts assemblyproducts);

    @Delete
    public void DeleteAssemblyProducts(EnsamblesProducts assemblyproducts);

    //@Query("SELECT * FROM EnsamblesProducts ORDER BY id")
    //public List<EnsamblesProducts> getAllAssemblyProducts();

    @Query("select * from assembly_products order by id")
    public List<EnsamblesProducts> getAllAssemblyProducts();

    @Query("SELECT * FROM assembly_products ap INNER JOIN products p ON (ap.id=p.id) where ap.id=:id")
    public List<EnsamblesProducts> getProductsByEnsamble(int id);
}
