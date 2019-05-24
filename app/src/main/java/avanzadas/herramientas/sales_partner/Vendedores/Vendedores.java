package avanzadas.herramientas.sales_partner.Vendedores;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "vendedores")
public class Vendedores implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "first_name")
    private String first_name;

    @ColumnInfo(name = "last_name")
    private String last_name;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "clave")
    private String clave;

    @ColumnInfo(name = "online")
    private Integer online;

    public Vendedores(int id, String first_name, String last_name, String user_name, String clave, Integer online) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.clave = clave;
        this.online = online;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
