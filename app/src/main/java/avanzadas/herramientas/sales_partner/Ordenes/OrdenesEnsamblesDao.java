package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

@Dao
public interface OrdenesEnsamblesDao {

    @Insert
    public void InsertOrdenesEnsamble(OrdenesEnsambles ordenesEnsamblesensambles);

    @Delete
    public void DeleatEnsamble(OrdenesEnsambles ensamble);

    @Query("Update order_assemblies SET qty= qty-1 where a=:ensamble;")
    public void UdapteOrdenEnsamble(int ensamble);

    @Query("Delete  from order_assemblies where id=:ensamble;")
    public void DeleleOrderAssemblieById(int ensamble);
    //CHECAR CHECAR CHECAR
    //@Query("Select * from  order_assemblies;")
    //public List<Orde> getAllOrdenesEnsambles();

    @Query("Select * from  order_assemblies;")
    public List<OrdenesEnsambles> getAllOrdenesEnsambles();

    @Query("SELECT * from order_assemblies oa INNER JOIN orders o ON(oa.id=o.id) where oa.id=:id")
    public List<OrdenesEnsambles> getEnsamblesByOrder(int id);

    @Query("SELECT * from order_assemblies oa where oa.id=:id and oa.assembly_id=:id_assemblie ")
    public OrdenesEnsambles getQtydeCadaEnsamblesByOrden(int id, int id_assemblie);

    @Query("SELECT * FROM order_assemblies oa where oa.id=:id and oa.assembly_id=:id_ensamble")
    public OrdenesEnsambles getOrdenEnsambleByOrdenyEnsamble(int id,int id_ensamble);

    @Query("Select * FROM order_assemblies oa where id=:id")
    public List<OrdenesEnsambles> getCantidadDeEnsamblesByOrder(int id);

    @Query("select * from order_assemblies order by id")
    public List<OrdenesEnsambles> getAllOrdenesEnsamblesOrderById();


}
