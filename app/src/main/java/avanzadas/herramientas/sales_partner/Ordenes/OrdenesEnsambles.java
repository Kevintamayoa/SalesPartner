package avanzadas.herramientas.sales_partner.Ordenes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

@Entity(tableName = "order_assemblies")
public class OrdenesEnsambles {

    @PrimaryKey//(autoGenerate = true)
    @ColumnInfo(name = "a")
    private  int a;

    @ColumnInfo(name = "id")
    //@ForeignKey(entity = Ordenes.class, parentColumns = "id", childColumns = "id")
    private int id;
    @ColumnInfo(name = "assembly_id")
    //@ForeignKey(entity = Ensambles.class, parentColumns = "id", childColumns = "assembly_id")
    private int assembly_id;
    @ColumnInfo(name = "qty")
    private int qty;

    public OrdenesEnsambles(int a, int id, int assembly_id, int qty) {
        this.a = a;
        this.id = id;
        this.assembly_id = assembly_id;
        this.qty = qty;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssembly_id() {
        return assembly_id;
    }

    public void setAssembly_id(int assembly_id) {
        this.assembly_id = assembly_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
