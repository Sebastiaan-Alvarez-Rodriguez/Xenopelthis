package com.sebastiaan.xenopelthis.ui.supplier.view;

import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.HashSet;
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
    public void onClick(View view, int pos) {
        supplier item = list.get(pos);
        if (selected_suppliers.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_suppliers.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        super.onClick(view, pos);
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        supplier item = list.get(pos);
        if (selected_suppliers.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_suppliers.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
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
