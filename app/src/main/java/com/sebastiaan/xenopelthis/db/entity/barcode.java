package com.sebastiaan.xenopelthis.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Class that contains the information of the entity barcode
 */

//TODO: Must barcode be unique (only 1 product may have a given barcode)?
// @Entity(primaryKeys = {"id", "translation"}, indices = {@Index(value = {"translation"}, unique = true)})
// or set primary key to be translation
//Currently not unique:
@Entity(primaryKeys = {"id", "translation"})
public class barcode {
    @ForeignKey(entity = product.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)
    private long id;
    @NonNull
    private String translation;

    public barcode(@NonNull String translation) {
        this.translation = translation;
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
     * Function to get the associated translation string
     * @return the object's translation string
     */
    public @NonNull String getTranslation() {
        return translation;
    }

    /**
     * Function to set the associated translation string
     * @param translation the string to which the object's translation string should be set
     */
    public void setTranslation(@NonNull String translation) {
        this.translation = translation;
    }

    /**
     * Function to compare an object to this barcode on equality
     * @param obj the object to be compared to this barcode
     * @return true if there is equality, false on inequality or if the object is not a barcode
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof barcode))
            return false;

        barcode other = (barcode) obj;
        return this.translation.equals(other.translation);
    }

    /**
     * @see String#hashCode()
     * @return the hashCode of the object's translation string
     */
    @Override
    public int hashCode() {
        return translation.hashCode();
    }
}
