package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndID;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;

@Dao
public interface DAOInventory {
    @Insert
    void add(inventory_item... i);

    @Update
    void update(inventory_item... i);

    @Delete
    void delete(inventory_item... i);

    @Query("DELETE FROM inventory_item WHERE productID=:ids")
    void deleteByID(Long... ids);

    @Query("SELECT amount FROM inventory_item WHERE productID = :id")
    Long getAmount(long id);

    @Query("SELECT product.*, amount FROM product, inventory_item WHERE product.id = inventory_item.productID")
    LiveData<List<ProductAndID>> getAllLive();

    @Query("SELECT product.*, amount FROM product, inventory_item WHERE productID = product.id AND inventory_item.productID = :id")
    ProductAndID get(long id);
}
