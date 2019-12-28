package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import static androidx.room.ForeignKey.CASCADE;

/**
 * Represents supplier-product relation entries
 */
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


    /**
     * Function to get the associated supplierID
     * @return the object's supplierID
     */
    public long getSupplierID() {
        return supplierID;
    }

    /**
     * Function to set the associated supplierID
     * @param supplierID the long to which the object's supplierID should be set
     */
    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * Function to get the associated productID
     * @return the object's productID
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Function to set the associated productID
     * @param productID the long to which the object's productID should be set
     */
    public void setProductID(long productID) {
        this.productID = productID;
    }
}