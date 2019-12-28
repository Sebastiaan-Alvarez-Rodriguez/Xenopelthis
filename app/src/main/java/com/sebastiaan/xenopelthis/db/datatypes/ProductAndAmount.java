package com.sebastiaan.xenopelthis.db.datatypes;

import androidx.room.Embedded;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;

/**
 *  Utility datatype to combine {@link product} and an amount, for {@link inventory_item} queries
 */

public class ProductAndAmount {
    @Embedded
    private product p;
    private long amount;

    /**
     * Function to convert this object to an inventory_item
     * @return an inventory_item object
     */
    public inventory_item toInventoryItem() { return new inventory_item(p.getId(), amount); }

    /**
     * Getter for associated product
     * @return product p
     */
    public product getP() {
        return p;
    }

    /**
     * Setter for associated product
     * @param p product to which object's product should be set
     */
    public void setP(product p) {
        this.p = p;
    }

    /**
     * Getter for associated amount
     * @return amount associated with object
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Setter for associated amount
     * @param amount amount to which object's amount should be set
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
