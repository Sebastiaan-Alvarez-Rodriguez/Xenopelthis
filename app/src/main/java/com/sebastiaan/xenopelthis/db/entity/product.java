package com.sebastiaan.xenopelthis.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class product {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name, productDescription;

    private boolean hasBarcode;

    public product(String name, String productDescription, boolean hasBarcode) {
        this.name = name;
        this.productDescription = productDescription;
        this.hasBarcode = hasBarcode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean getHasBarcode() {
        return hasBarcode;
    }

    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) 
            return true;
        if (!(obj instanceof product))
            return false;

        product other = (product) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
}
