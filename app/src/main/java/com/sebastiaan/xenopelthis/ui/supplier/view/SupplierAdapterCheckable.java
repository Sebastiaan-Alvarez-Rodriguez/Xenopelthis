package com.sebastiaan.xenopelthis.ui.supplier.view;

import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SupplierAdapterCheckable extends SupplierAdapter {
    private Set<supplier> selected_suppliers;

    public SupplierAdapterCheckable() {
        this(null);
    }

    public SupplierAdapterCheckable(OnClickListener onClickListener) {
        super(onClickListener);
        selected_suppliers = new HashSet<>();
    }

    @Override
    public void onClick(supplier s) {
        if (!selected_suppliers.add(s))
            selected_suppliers.remove(s);
        super.onClick(s);
    }

    @Override
    public boolean onLongClick(supplier s) {
        if (!selected_suppliers.add(s))
            selected_suppliers.remove(s);
        return super.onLongClick(s);
    }

    public int getSelectedCount() {
        return selected_suppliers.size();
    }

    public boolean hasSelected() {
        return !selected_suppliers.isEmpty();
    }

    public Set<supplier> getSelected() {
        return selected_suppliers;
    }

    public Set<Long> getSelectedIDs() {
        HashSet<Long> tmp = new HashSet<>();
        for (supplier s : selected_suppliers)
            tmp.add(s.getId());
        return tmp;
    }
}
