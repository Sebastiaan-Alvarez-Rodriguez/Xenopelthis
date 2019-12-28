package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.Nullable;

/**
 * Class that contains the information of the entity product
 */

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
     * Function to get the associated id
     * @return the object's id
     */
    public long getId() {
        return id;
    }

    /**
     * Function to set the associated id
     * @param id the long to which the object's id should be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Function to get the associated name
     * @return the object's name
     */
    public String getName() {
        return name;
    }

    /**
     * Function to set the associated name
     * @param name the String to which the object's name should be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function to get the associated product description
     * @return the object's product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Function to set the associated product description
     * @param productDescription the String to which the object's product description should be set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Function to retrieve whether this object has a barcode
     * @return true if the object has a barcode, else false
     */
    public boolean getHasBarcode() {
        return hasBarcode;
    }

    /**
     * Function to set whether this object has a barcode
     * @param hasBarcode the boolean to which the object's hasBarcode should be set
     */
    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    /**
     * Function to compare an object to this product on equality
     * @param obj the object to be compared to this product
     * @return true if there is equality, false on inequality or if the object is not a product
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
     * @return the hashCode of the object's id
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
}
