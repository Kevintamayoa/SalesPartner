package avanzadas.herramientas.sales_partner.Productos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import avanzadas.herramientas.sales_partner.Ensambles.Ensambles;

@Dao
public interface ProductsCategoryDao {

    @Insert
    public void insertProductCategoryId(ProductCategory productos);

    @Insert
    public void insertProductCategoryDescription(ProductCategory productos);

    @Delete
    public void DeleteProductCategory(ProductCategory ensambles);

    @Query("select * from product_categories where description = :description ")
    public ProductCategory getCategory(String description);

    @Query("select * from product_categories")
    public List<ProductCategory> getAllCategory();

    @Query("select * from product_categories where id = :description ")
    public ProductCategory getCategoryById(int description);

}
