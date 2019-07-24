package com.sebastiaan.xenopelthis.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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