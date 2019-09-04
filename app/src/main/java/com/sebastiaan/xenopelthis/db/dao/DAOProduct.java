package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

@Dao
public interface DAOProduct extends DAOEntity<product> {
    @Query("DELETE FROM product WHERE id IN(:ids)")
    void deleteByID(Long... ids);

    @Query("UPDATE product SET hasBarcode = :hasBarcode WHERE id IN(:ids)")
    void setHasBarcode(boolean hasBarcode, Long... ids);

    @Query("SELECT * FROM product")
    LiveData<List<product>> getAllLive();

    @Query("SELECT * FROM product")
    List<product> getAll();

    @Query("SELECT * FROM product WHERE id = :id")
    product get(long id);

    @Query("SELECT * FROM product WHERE id = :id")
    LiveData<product> getLive(long id);

    @Query("SELECT * FROM product WHERE name LIKE :name")
    List<product> find(String name);

    @Query("SELECT * FROM product WHERE name = :name")
    product findExact(String name);

    @Query("SELECT COUNT(*) from product")
    int count();
}
