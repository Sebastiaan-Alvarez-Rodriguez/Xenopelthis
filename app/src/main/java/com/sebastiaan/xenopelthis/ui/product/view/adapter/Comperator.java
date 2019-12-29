package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

public class Comperator extends com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator<product> {

    public Comperator(@NonNull Adapter<product> adapter, @NonNull Adapter.SortBy strategy) {
        super(adapter, strategy);
    }

    @Override
    public int compare(product o1, product o2) {
        if (strategy == Adapter.SortBy.DATE) {
            return Long.compare(o1.getId(), o2.getId());
        }
        return o1.getName().compareTo(o2.getName());
    }

    @Override
    public boolean areContentsTheSame(product oldItem, product newItem) {
        return oldItem.getName().equals(newItem.getName()) && oldItem.getHasBarcode() == newItem.getHasBarcode();
    }

    @Override
    public boolean areItemsTheSame(product item1, product item2) {
        return item1.getId() == item2.getId();
    }
}
