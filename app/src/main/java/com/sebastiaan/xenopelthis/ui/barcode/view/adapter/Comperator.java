package com.sebastiaan.xenopelthis.ui.barcode.view.adapter;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

public class Comperator extends com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator<barcode> {

    public Comperator(@NonNull Adapter<barcode> adapter, @NonNull Adapter.SortBy strategy) {
        super(adapter, strategy);
    }

    /**
     * @see String#compareTo(String)
     * @param o1 The first barcode
     * @param o2 The second barcode
     * @throws RuntimeException if the objects are dates
     */
    @Override
    public int compare(barcode o1, barcode o2) {
        switch (strategy) {
            case NAME:
                return o1.getTranslation().compareTo(o2.getTranslation());
            case DATE:
                throw new RuntimeException("Not implemented");
            default:
                return o1.getTranslation().compareTo(o2.getTranslation());
        }
    }

    @Override
    public boolean areContentsTheSame(barcode oldItem, barcode newItem) {
        return false;
    }

    @Override
    public boolean areItemsTheSame(barcode item1, barcode item2) {
        return false;
    }
}
