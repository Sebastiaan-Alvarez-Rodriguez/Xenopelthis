package com.sebastiaan.xenopelthis.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
