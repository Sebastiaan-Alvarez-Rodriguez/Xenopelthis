package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;

@Dao
public interface DAOInventory extends com.sebastiaan.xenopelthis.db.dao.DAOEntity<inventory_item> {
    @Query("SELECT amount FROM inventory_item WHERE productID = :id")
    Long getAmount(long id);

    @Query("SELECT * FROM inventory_item")
    LiveData<List<inventory_item>> getAllLive();
}
