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

    public product(String name, String productDescription) {
        this.name = name;
        this.productDescription = productDescription;
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
    
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) 
            return true;
        if (!(obj instanceof product))
            return false;

        product other = (product) obj;
        return this.id == other.id;
    }
}
