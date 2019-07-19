package com.sebastiaan.xenopelthis.db.datatypes;

import android.arch.persistence.room.Embedded;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;

public class ProductAndID {
    @Embedded
    product p;
    long amount;

    public inventory_item toInventoryItem() { return new inventory_item(p.getId(), amount); }

    public product getP() {
        return p;
    }

    public void setP(product p) {
        this.p = p;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
