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
public interface DAOBarcode extends DAOEntity<barcode> {
    @Query("DELETE FROM barcode WHERE id IN(:ids)")
    void deleteForProduct(Long... ids);

    @Query("DELETE FROM barcode WHERE id IN (:ids) AND translation = :translation")
    void deleteBarcodeForProducts(String translation, Long... ids);

    @Query("SELECT * FROM barcode")
    LiveData<List<barcode>> getAllLive();

    @Query("SELECT product.* FROM barcode, product WHERE barcode.id = product.id")
    LiveData<List<product>> getAllProductsLive();

    @Query("SELECT * FROM barcode")
    List<barcode> getAll();

    @Query("SELECT * FROM barcode WHERE id = :id")
    List<barcode> getAllForProduct(long id);

    @Query("SELECT product.* FROM barcode, product WHERE barcode.translation = :barcode AND barcode.id = product.id")
    List<product> getForBarcode(String barcode);

    @Query("SELECT * FROM barcode WHERE id = :id")
    LiveData<List<barcode>> getAllForProductLive(long id);

    @Query("SELECT product.* FROM product, barcode WHERE barcode.translation = :barcode AND barcode.id = product.id")
    LiveData<List<product>> getAllForBarcodeLive(String barcode);

    @Query("SELECT COUNT(*) FROM barcode WHERE barcode.translation = :barcode")
    long getProductsCount(String barcode);

    @Query("SELECT product.* FROM product WHERE product.id NOT IN (SELECT id FROM barcode WHERE translation = :translation)")
    LiveData<List<product>> getUnassignedForBarcodeLive(String translation);

    @Query("SELECT COUNT(*) from barcode")
    int count();

}