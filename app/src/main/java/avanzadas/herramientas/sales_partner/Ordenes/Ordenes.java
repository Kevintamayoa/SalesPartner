package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "orders")
public class Ordenes implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "status_id")
    private int status_id;

    @ColumnInfo(name = "customer_id")
    private int customer_id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "change_log")
    private String change_log;

    public Ordenes(int id, int status_id, int customer_id, String date, String change_log) {
        this.id = id;
        this.status_id = status_id;
        this.customer_id = customer_id;

        this.date=date;
        this.change_log = change_log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getChange_log() {
        return change_log;
    }

    public void setChange_log(String change_log) {
        this.change_log = change_log;
    }
}
