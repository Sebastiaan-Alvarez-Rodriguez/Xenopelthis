package com.sebastiaan.xenopelthis.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

@Dao
public interface  DAOProduct {
    @Insert
    long add(product p);

    @Insert
    List<Long> add(product... p);

    @Update
    void update(product... p);

    @Delete
    void delete(product... p);

    @Query("DELETE FROM product WHERE id=:ids")
    void deleteByID(Long... ids);

    @Query("SELECT * FROM product")
    LiveData<List<product>> getAllLive();

    @Query("SELECT * FROM product")
    List<product> getAll();

    @Query("SELECT * FROM product WHERE id = :id")
    LiveData<product> get(long id);

    @Query("SELECT * FROM product WHERE name LIKE :name")
    List<product> find(String name);

    @Query("SELECT * FROM product WHERE name = :name")
    product findExact(String name);

    @Query("SELECT COUNT(*) from product")
    int count();


}
