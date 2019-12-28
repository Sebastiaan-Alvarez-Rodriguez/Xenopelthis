package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Class representing inventory entries
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
     * Getter for associated productID
     * @return object's productID
     */
    public long getProductID() { return productID; }

    /**
     * Setter for associated productID
     * @param productID new productID
     */
    public void setProductID(long productID) { this.productID = productID; }

    /**
     * Getter for associated amount
     * @return object's amount
     */
    public long getAmount() { return amount; }

    /**
     * Setter for associated amount
     * @param amount new amount
     */
    public void setAmount(long amount) { this.amount = amount; }
}
