package com.sebastiaan.xenopelthis.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;

import java.util.List;

@Dao
public interface DAOSupplierProduct {
    @Insert
    void add(supplier_product... s);

    @Update
    void update(supplier_product... s);

    @Delete
    void remove(supplier_product... s);


    @Query("SELECT product.* FROM product, supplier_product WHERE supplier_product.productID = :id AND supplier_product.supplierID = product.id")
    LiveData<List<product>> productsForSupplier(long id);

    @Query("SELECT supplier.* FROM supplier, supplier_product WHERE supplier_product.productID = :id AND supplier_product.supplierID = supplier.id")
    LiveData<List<supplier>> suppliersForProduct(long id);
}
