package avanzadas.herramientas.sales_partner.Productos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "products")
public class Productos {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "category_id")
    private int  category_id;

    @ColumnInfo(name = "description")
    private String  description;

    @ColumnInfo(name = "price")
    private int  precio;

    @ColumnInfo(name = "qty")
    private int  cantidad;

    public Productos(int id, int category_id, String description, int precio, int cantidad) {
        this.id = id;
        this.category_id = category_id;
        this.description = description;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    public Productos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
