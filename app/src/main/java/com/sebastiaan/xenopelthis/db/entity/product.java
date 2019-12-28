package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.Nullable;

/**
 * Class representing product entries
 */

@SuppressWarnings("unused")
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

    /**
     * Getter for associated id
     * @return object's id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for associated id
     * @param id new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for associated name
     * @return object's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for associated name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for associated product description
     * @return object's product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Setter for associated product description
     * @param productDescription new product description
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Getter for hasBarcode
     * @return true if object has a barcode, else false
     */
    public boolean getHasBarcode() {
        return hasBarcode;
    }

    /**
     * Setter for barcode
     * @param hasBarcode has barcode
     */
    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    /**
     * Equality override
     * @param obj object to be compared to this product
     * @return true if obj is the same, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) 
            return true;
        if (!(obj instanceof product))
            return false;

        product other = (product) obj;
        return this.id == other.id;
    }

    /**
     * @see Long#hashCode()
     * @return hashCode of object's id
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
}
