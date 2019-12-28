package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Class that contains information on the entity inventory_item
 */

@Entity(primaryKeys = {"productID"})
public class inventory_item {
    @ForeignKey(entity = product.class, parentColumns = "id", childColumns = "productID", onDelete = CASCADE)
    private long productID;

    private long amount;

    public inventory_item(long productID, long amount) {
        this.productID = productID;
        this.amount = amount;
    }

    /**
     * Function to get the associated productID
     * @return the object's productID
     */
    public long getProductID() { return productID; }

    /**
     * Function to set the associated productID
     * @param productID the long to which the object's productID should be set
     */
    public void setProductID(long productID) { this.productID = productID; }

    /**
     * Function to get the associated amount
     * @return the object's amount
     */
    public long getAmount() { return amount; }

    /**
     * Function to set the associated amount
     * @param amount the long to which the object's amount should be set
     */
    public void setAmount(long amount) { this.amount = amount; }
}
