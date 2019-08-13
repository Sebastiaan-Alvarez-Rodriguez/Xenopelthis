package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

public class Comperator extends com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator<supplier> {

    public Comperator(@NonNull Adapter<supplier> adapter, @NonNull Adapter.SortBy strategy) {
        super(adapter, strategy);
    }

    @Override
    public int compare(supplier o1, supplier o2) {
        switch (strategy) {
            case NAME:
                return o1.getName().compareTo(o2.getName());
            case DATE:
                return Long.compare(o1.getId(), o2.getId());
            default:
                return o1.getName().compareTo(o2.getName());
        }
    }

    @Override
    public boolean areContentsTheSame(supplier oldItem, supplier newItem) {
        return oldItem.getName().equals(newItem.getName()) &&
                oldItem.getStreetname().equals(newItem.getStreetname()) &&
                oldItem.getHousenumber().equals(newItem.getHousenumber()) &&
                oldItem.getCity().equals(newItem.getCity()) &&
                oldItem.getPostalcode().equals(newItem.getPostalcode()) &&
                oldItem.getPhonenumber().equals(newItem.getPhonenumber()) &&
                oldItem.getEmailaddress().equals(newItem.getEmailaddress()) &&
                oldItem.getWebaddress().equals(newItem.getWebaddress());
    }

    @Override
    public boolean areItemsTheSame(supplier item1, supplier item2) {
        return item1.getId() == item2.getId();
    }
}
