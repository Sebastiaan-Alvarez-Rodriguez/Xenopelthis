package com.sebastiaan.xenopelthis.ui.supplier.view;

import android.support.annotation.NonNull;
import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;

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
    public void onBindViewHolder(@NonNull SupplierViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        if (selected_suppliers.contains(list.get(position)))
            viewHolder.itemView.setBackgroundResource(R.color.colorAccent);
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

    public void setSelectedSuppliers(List<supplier> suppliers) {
        selected_suppliers = new HashSet<>(suppliers);
        notifyDataSetChanged();
//        super.onChanged(list);
    }

    public void setSelectedIDs(List<Long> ids) {
        selected_suppliers = new HashSet<>();
        for (Long id : ids)
            for (supplier s : list)
                if (s.getId() == id)
                    selected_suppliers.add(s);
        notifyDataSetChanged();
//        super.onChanged(list);
    }
}
