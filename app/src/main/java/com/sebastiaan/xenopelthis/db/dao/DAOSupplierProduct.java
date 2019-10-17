package com.sebastiaan.xenopelthis.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;

import java.util.List;

@Dao
public interface DAOSupplierProduct extends DAOEntity<supplier_product> {
    @Query("SELECT product.* FROM product, supplier_product WHERE supplier_product.supplierID = :id AND supplier_product.productID = product.id")
    List<product> productsForSupplier(long id);

    @Query("SELECT supplier.* FROM supplier, supplier_product WHERE supplier_product.productID = :id AND supplier_product.supplierID = supplier.id")
    List<supplier> suppliersForProduct(long id);

    @Query("SELECT supplier.id FROM supplier, supplier_product WHERE supplier_product.productID = :id AND supplier_product.supplierID = supplier.id")
    List<Long> supplierIDsForProduct(long id);
}
