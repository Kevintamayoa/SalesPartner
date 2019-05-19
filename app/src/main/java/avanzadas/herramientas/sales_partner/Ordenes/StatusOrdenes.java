package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "order_status")
public class StatusOrdenes {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    //public boolean test = true;

    @ColumnInfo(name = "editable") //editable solo puede ser false 0 o true 1
    private int editable;

    @NonNull
    @ColumnInfo(name = "previous")
    private String previous;

    @NonNull
    @ColumnInfo(name = "next")
    private String next;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    @NonNull
    public String getDescription() { return description; }

    public void setDescription(@NonNull String description) { this.description = description; }

    public int getEditable() { return editable; }

    public void setEditable(int editable) { this.editable = editable; }

    @NonNull
    public String getPrevious() { return previous; }

    public void setPrevious(@NonNull String previous) { this.previous = previous; }

    @NonNull
    public String getNext() { return next; }

    public void setNext(@NonNull String next) { this.next = next; }

    public StatusOrdenes(int id, @NonNull String description, int editable, @NonNull String previous, @NonNull String next) {
        this.id = id;
        this.description = description;
        this.editable = editable;
        this.previous = previous;
        this.next = next;
    }
}
