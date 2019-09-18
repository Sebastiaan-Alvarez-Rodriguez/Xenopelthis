package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

@Dao
public interface DAOInventory extends DAOEntity<inventory_item> {
    @Query("SELECT product.*, amount FROM product, inventory_item WHERE productID = product.id AND inventory_item.productID = :id")
    ProductAndAmount get(long id);

    @Query("SELECT product.*, amount FROM product, inventory_item WHERE product.id = inventory_item.productID")
    LiveData<List<ProductAndAmount>> getAllLive();

    @Query("DELETE FROM inventory_item WHERE productID IN(:ids)")
    void deleteByID(Long... ids);

    @Query("SELECT product.* FROM product WHERE product.id NOT IN (SELECT productID FROM inventory_item) ")
    LiveData<List<product>> getUnusedLive();

    @Query("SELECT COUNT(*) FROM inventory_item WHERE productID = :id")
    boolean contains(long id);
}
