package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"productID"})
public class inventory_item {
    @ForeignKey(entity = product.class, parentColumns = "id", childColumns = "productID", onDelete = CASCADE)
    private long productID;

    private long amount;

    public inventory_item(long productID, long amount) {
        this.productID = productID;
        this.amount = amount;
    }

    public long getProductID() { return productID; }

    public void setProductID(long productID) { this.productID = productID; }

    public long getAmount() { return amount; }

    public void setAmount(long amount) { this.amount = amount; }
}
