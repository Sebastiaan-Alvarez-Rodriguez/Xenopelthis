package com.sebastiaan.xenopelthis.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = {"translation"}, unique = true)})
public class barcode {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String translation;

    public barcode(String translation) {
        this.translation = translation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof barcode))
            return false;

        barcode other = (barcode) obj;
        return this.id == other.id;
    }
}
