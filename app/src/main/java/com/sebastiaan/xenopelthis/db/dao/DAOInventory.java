package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;

@Dao
public interface DAOInventory extends com.sebastiaan.xenopelthis.db.dao.DAOEntity<inventory_item> {
    @Query("DELETE FROM inventory_item WHERE productID IN(:ids)")
    void deleteByID(Long... ids);

    @Query("SELECT amount FROM inventory_item WHERE productID = :id")
    Long getAmount(long id);

    @Query("SELECT product.*, amount FROM product, inventory_item WHERE product.id = inventory_item.productID")
    LiveData<List<ProductAndAmount>> getAllLive();

    @Query("SELECT product.*, amount FROM product, inventory_item WHERE productID = product.id AND inventory_item.productID = :id")
    ProductAndAmount get(long id);

    @Query("SELECT product.name FROM product WHERE product.id NOT IN (SELECT productID FROM inventory_item) ")
    List<String> getUnusedNames();
}
