package com.sebastiaan.xenopelthis.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Class representing barcode entries
 */

@SuppressWarnings("unused")
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
     * Getter for associated translation string
     * @return object's translation string
     */
    public @NonNull String getTranslation() {
        return translation;
    }

    /**
     * Setter for associated translation string
     * @param translation new translation
     */
    public void setTranslation(@NonNull String translation) {
        this.translation = translation;
    }

    /**
     * Equality overload for object
     * @param obj object to compare to this barcode
     * @return true if objects have same translation, false otherwise
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
     * @return hashCode of object's translation string
     */
    @Override
    public int hashCode() {
        return translation.hashCode();
    }
}
