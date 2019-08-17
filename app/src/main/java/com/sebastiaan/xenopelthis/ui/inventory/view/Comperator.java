package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

public class Comperator extends com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator<ProductAndAmount> {
    public Comperator(@NonNull Adapter<ProductAndAmount> adapter, @NonNull Adapter.SortBy strategy) {
        super(adapter, strategy);
    }

    @Override
    public int compare(ProductAndAmount o1, ProductAndAmount o2) {
        switch (strategy) {
            case NAME:
                return o1.getP().getName().compareTo(o2.getP().getName());
            case DATE:
                return Long.compare(o1.getAmount(), o2.getAmount());
            default:
                return Long.compare(o1.getAmount(), o2.getAmount());
        }
    }

    @Override
    public boolean areContentsTheSame(ProductAndAmount oldItem, ProductAndAmount newItem) {
        return oldItem.getAmount() == newItem.getAmount() && oldItem.getP().getId() == newItem.getP().getId();
    }

    @Override
    public boolean areItemsTheSame(ProductAndAmount item1, ProductAndAmount item2) {
        return item1.getP().getId() == item2.getP().getId();
    }
}
