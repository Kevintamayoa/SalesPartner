package avanzadas.herramientas.sales_partner.Ensambles;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import avanzadas.herramientas.sales_partner.Productos.Productos;


@Entity(tableName = "assembly_products")    // , indices = {@Index(value = "id",unique = true)})
public class EnsamblesProducts {

    @PrimaryKey//(autoGenerate = true)
    @ColumnInfo(name = "a")
    private  int a;

    @ColumnInfo(name = "id")
   // @ForeignKey(entity = Ensambles.class, parentColumns = "id", childColumns = "id")
    private int id;
    @ColumnInfo(name = "product_id")
    //@ForeignKey(entity = Productos.class, parentColumns = "id", childColumns = "product_id")
    private int product_id;
    @ColumnInfo(name = "qty")
    private int qty;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getProduct_id() { return product_id; }

    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }

    public EnsamblesProducts(int a, int id, int product_id, int qty) {
        this.a = a;
        this.id = id;
        this.product_id = product_id;
        this.qty = qty;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}

