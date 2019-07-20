package com.sebastiaan.xenopelthis.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

@Dao
public interface DAOBarcode {
    @Insert
    long add(barcode b);

    @Insert
    List<Long> add(barcode... b);
    
    @Update
    void update(barcode... b);

    @Delete
    void delete(barcode... b);

    @Query("DELETE FROM barcode WHERE id=:ids")
    void deleteForProduct(Long... ids);

    @Query("SELECT * FROM barcode")
    LiveData<List<barcode>> getAllLive();

    @Query("SELECT product.* FROM barcode, product WHERE barcode.id = product.id")
    LiveData<List<product>> getAllProductsLive();

    @Query("SELECT * FROM barcode")
    List<barcode> getAll();

    @Query("SELECT * FROM barcode WHERE id = :id")
    List<barcode> getAllForProduct(long id);

    @Query("SELECT * FROM barcode WHERE id = :id")
    LiveData<List<barcode>> getAllForProductLive(long id);

    @Query("SELECT * FROM barcode WHERE translation LIKE :translation")
    List<barcode> find(String translation);

    @Query("SELECT * FROM barcode WHERE translation = :translation")
    barcode findExact(String translation);

    @Query("SELECT COUNT(*) from barcode")
    int count();

}