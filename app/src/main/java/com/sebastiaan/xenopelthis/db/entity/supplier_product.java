package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import static androidx.room.ForeignKey.CASCADE;


@Entity(primaryKeys = {"supplierID", "productID"})
public class supplier_product {
    @ForeignKey(entity = supplier.class, parentColumns = "id", childColumns = "supplierID", onDelete = CASCADE)
    private long supplierID;
    @ForeignKey(entity = product.class, parentColumns = "id", childColumns = "productID", onDelete = CASCADE)
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