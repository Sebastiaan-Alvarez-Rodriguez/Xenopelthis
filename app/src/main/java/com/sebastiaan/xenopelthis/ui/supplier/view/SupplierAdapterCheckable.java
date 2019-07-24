package com.sebastiaan.xenopelthis.ui.supplier.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SupplierAdapterCheckable extends SupplierAdapter {
    private Set<supplier> selected_suppliers;

    public SupplierAdapterCheckable() {
        this(null, null);
    }

    public SupplierAdapterCheckable(List<supplier> initialSelected) {
        this(initialSelected, null);
    }

    public SupplierAdapterCheckable(List<supplier> initialSelected, OnClickListener onClickListener) {
        super(onClickListener);
        selected_suppliers = initialSelected == null ? new HashSet<>() : new HashSet<>(initialSelected);
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

    @Override
    public void onViewRecycled(@NonNull SupplierViewHolder holder) {
        holder.itemView.setBackgroundResource(android.R.color.transparent);
        super.onViewRecycled(holder);
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

    @Override
    public void onChanged(@Nullable List<supplier> suppliers) {
        selected_suppliers.clear();
        super.onChanged(suppliers);
    }
}
