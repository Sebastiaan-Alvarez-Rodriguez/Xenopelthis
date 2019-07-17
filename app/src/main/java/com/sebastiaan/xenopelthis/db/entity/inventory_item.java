package com.sebastiaan.xenopelthis.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"productID"})
public class inventory_item {
    @ForeignKey(entity = product.class, parentColumns = "productID", childColumns = "productID", onDelete = CASCADE)
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
