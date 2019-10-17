package com.sebastiaan.xenopelthis.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NonNull String getTranslation() {
        return translation;
    }

    public void setTranslation(@NonNull String translation) {
        this.translation = translation;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof barcode))
            return false;

        barcode other = (barcode) obj;
        return this.translation.equals(other.translation);
    }

    @Override
    public int hashCode() {
        return translation.hashCode();
    }
}
