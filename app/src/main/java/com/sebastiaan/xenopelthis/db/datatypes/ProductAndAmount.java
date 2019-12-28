package com.sebastiaan.xenopelthis.db.datatypes;

import androidx.room.Embedded;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;

/**
 *  Class to save an Inventory Entity from the database to a combination of {@link product} and an amount
 */

public class ProductAndAmount {
    @Embedded
    product p;
    long amount;

    /**
     * Function to convert the ProductAndAmount type to an inventory_item
     * @return an inventory_item object
     */
    public inventory_item toInventoryItem() { return new inventory_item(p.getId(), amount); }

    /**
     * Function to get the associated product
     * @return the product p
     */
    public product getP() {
        return p;
    }

    /**
     * Function to set the associated product
     * @param p the product to which the object's product should be set
     */
    public void setP(product p) {
        this.p = p;
    }

    /**
     * Function to return the associated amount
     * @return the amount associated with the object
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Function to set the associated amount
     * @param amount the amount to which the object's amount should be set
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
