package avanzadas.herramientas.sales_partner.Clientes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ClientesDao {

    @Insert
    public void InsertClientes(Clientes clientes);

    @Update
    public void UpdateCliente(Clientes clientes);

    @Delete
    public void DeleteClientes(Clientes cliente);


    @Query("Select * from  customers  Order By id Asc;")
    public List<Clientes> getAllClientesByIdASC();

    @Query("Select * from  customers  Order By last_name DESC;")
    public List<Clientes> getAllClientes();

    @Query("Update customers SET online= 1 where id=:id;")
    public void ChangeOnline(int id);

    @Query("Update customers SET online= 0 where id=:id;")
    public void ChangeOnlineOut(int id);

    @Query("Select * from customers where id=:id")
    public Clientes getClienteById(int id);

    @Query("Select * from  customers  Order By id DESC;")
    public List<Clientes> getAllClientesOrderByIdDesc();

    @Query("Select * from  customers  Order By id ASC;")
    public List<Clientes> getAllClientesOrderByIdASC();


    @Query("select * from customers where first_name like :f_name;")
    public List<Clientes> getAllClientesByNombre(String f_name);

    @Query("select * from customers where last_name like :l_name;")
    public List<Clientes> getAllClientesByApellido(String l_name);

    @Query("select * from customers where phone1 or phone2 or phone3 like :tel;")
    public List<Clientes> getAllClientesByTel(String tel);

    @Query("select * from customers where e_mail like :email;")
    public List<Clientes> getAllClientesByEmail(String email);

    @Query("select * from customers where address like :dir;")
    public List<Clientes> getAllClientesByDir(String dir);

    @Query("Select * from  customers  where user=:user")
    public Clientes GetClienteByUser(String user);

    @RawQuery(observedEntities = Clientes.class)
    List<Clientes> getAllClientesFindDinamyc(SupportSQLiteQuery query);

}
