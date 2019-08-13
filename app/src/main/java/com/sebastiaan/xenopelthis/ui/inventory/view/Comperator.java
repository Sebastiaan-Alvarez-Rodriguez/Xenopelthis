package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

public class Comperator extends com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator<inventory_item> {
    public Comperator(@NonNull Adapter<inventory_item> adapter, @NonNull Adapter.SortBy strategy) {
        super(adapter, strategy);
    }

    @Override
    public int compare(inventory_item o1, inventory_item o2) {
        switch (strategy) {
            case NAME: //TODO: Sort on product name... How to get that here?
                return Long.compare(o1.getAmount(), o2.getAmount());
            case DATE:
                throw new RuntimeException("Not implemented");
            default:
                return Long.compare(o1.getAmount(), o2.getAmount());
        }
    }

    @Override
    public boolean areContentsTheSame(inventory_item oldItem, inventory_item newItem) {
        return oldItem.getAmount() == newItem.getAmount() && oldItem.getProductID() == newItem.getProductID();
    }

    @Override
    public boolean areItemsTheSame(inventory_item item1, inventory_item item2) {
        return item1.getProductID() == item2.getProductID();
    }
}
