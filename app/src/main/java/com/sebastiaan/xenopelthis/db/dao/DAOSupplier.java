package com.sebastiaan.xenopelthis.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;

@Dao
public interface DAOSupplier {
    @Insert
    long add(supplier s);

    @Insert
    List<Long> add(supplier... s);

    @Update
    void update(supplier... s);

    @Delete
    void delete(supplier... ids);

    @Query("DELETE FROM supplier WHERE id=:ids")
    void deleteByID(Long... ids);

    @Query("SELECT * FROM supplier")
    LiveData<List<supplier>> getAllLive();

    @Query("SELECT * FROM supplier")
    List<supplier> getAll();

    @Query("SELECT * FROM supplier where id = :id")
    LiveData<supplier> get(long id);

    @Query("SELECT * FROM supplier where name LIKE :name")
    List<supplier> find(String name);

    @Query("SELECT * FROM supplier where name = :name")
    supplier findExact(String name);

    @Query("SELECT COUNT(*) from supplier")
    long count();
}