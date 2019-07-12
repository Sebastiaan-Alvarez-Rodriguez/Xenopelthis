package com.sebastiaan.xenopelthis.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(primaryKeys = {"supplierID", "productID"})
public class supplier_product {
    @ForeignKey(entity = supplier.class, parentColumns = "supplierID", childColumns = "supplierID", onDelete = CASCADE)
    private long supplierID;
    @ForeignKey(entity = product.class, parentColumns = "productID", childColumns = "productID", onDelete = CASCADE)
    private long productID;

    public supplier_product(long supplierID, long productID) {
        this.supplierID = supplierID;
        this.productID = productID;
    }


    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }
}