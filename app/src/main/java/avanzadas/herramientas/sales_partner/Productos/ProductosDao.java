package avanzadas.herramientas.sales_partner.Productos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductosDao {

    @Insert
    public void insertProductId(Productos productos);

    @Insert
    public void insertCategoryIdProductos(Productos productos);

    @Insert
    public void insertDescriptionProdcutos(Productos productos);

    @Insert
    public void insertPrecioProducto(Productos productos);

    @Insert
    public void insertCantidadProducto(Productos productos);



   @Query("select * from products where category_id = :category")
    public List<Productos> getProductosByCategory(int category);

    @Query("select * from products /*Order By description ASC*/")
    public List<Productos> getAllProductos();

    @Query("select * from products p Order By p.description ASC")
    public List<Productos> getAllProductosOrderByTextASC();

    @Query("select * from products where description LIKE  :description")
    public List<Productos> getProductosByText(String description);

    @Query("Select * from products p where p.category_id=:Id and description Like :description")
    public List<Productos> getProductosByTextAndCategory(String description, int Id);

    @Query("SELECT  SUM (oa.qty * ap.qty * p.price)/100.00 AS total\n" +
            "FROM customers c\n" +
            "INNER JOIN orders o ON (c.id = o.customer_id)\n" +
            "INNER JOIN order_assemblies oa ON (o.id = oa.id)\n" +
            "INNER JOIN assembly_products ap ON (oa.assembly_id = ap.id)\n" +
            "INNER JOIN products p ON (ap.product_id = p.id)\n" +
            "where o.id=:id")
    public double IngresosById (int id);

    //llamas a esta funcion y recibe el id assemblie product
    @Query("SELECT * FROM products p Inner join assembly_products ap on(p.id=ap.product_id) where ap.id=:id")
    public List<Productos> getProductosByEnsambles(int id);

    @Query("SELECT (required_qty - qty) AS to_buy_qty\n" +
            "FROM products p\n" +
            "INNER JOIN (SELECT p.id, SUM(oa.qty*ap.qty) AS required_qty\n" +
            "            FROM order_assemblies oa\n" +
            "            INNER JOIN orders o ON (oa.id = o.id)\n" +
            "            INNER JOIN assembly_products ap ON (oa.assembly_id = ap.id)\n" +
            "            INNER JOIN products p ON (ap.product_id = p.id)\n" +
            "            WHERE o.status_id = 0 OR o.status_id = 2\n" +
            "            GROUP BY p.id\n" +
            "            ) AS tmp ON (p.id = tmp.id) where p.id = :idProduct;")
    public int getAllmissingProducts(int idProduct);







}
