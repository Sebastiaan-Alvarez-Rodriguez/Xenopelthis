package com.sebastiaan.xenopelthis.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;

@Dao
public interface DAOInventory {
    @Insert
    void add(inventory_item... i);

    @Update
    void update(inventory_item... i);

    @Delete
    void remove(inventory_item... i);

    @Query("SELECT amount FROM inventory_item WHERE productID = :id")
    Long getAmount(long id);

    @Query("SELECT * FROM inventory_item")
    LiveData<List<inventory_item>> getAllLive();
}
