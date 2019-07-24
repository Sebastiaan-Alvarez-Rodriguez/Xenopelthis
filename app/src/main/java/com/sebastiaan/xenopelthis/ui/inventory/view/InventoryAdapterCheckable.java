package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryAdapterCheckable extends InventoryAdapter {
    private Set<inventory_item> selected_items;

    public InventoryAdapterCheckable() { this(null, null); }

    public InventoryAdapterCheckable(List<inventory_item> initialSelected) { this(initialSelected, null); }

    public InventoryAdapterCheckable(List<inventory_item> initialSelected, OnClickListener onClickListener) {
        super(onClickListener);
        selected_items = initialSelected == null ? new HashSet<>() : new HashSet<>(initialSelected);
    }

    @Override
    public void onClick(View view, int pos) {
        inventory_item item = list.get(pos);
        if (selected_items.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        super.onClick(view, pos);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (selected_items.contains(list.get(position)))
            viewHolder.itemView.setBackgroundResource(R.color.colorAccent);
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        inventory_item item = list.get(pos);
        if (selected_items.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
    }

    @Override
    public void onViewRecycled(@NonNull InventoryViewHolder holder) {
        holder.itemView.setBackgroundResource(android.R.color.transparent);
        super.onViewRecycled(holder);
    }

    public boolean hasSelected() { return !selected_items.isEmpty(); }

    public Set<inventory_item> getSelected() { return selected_items; }

    @Override
    public void onChanged(@Nullable List<inventory_item> items) {
        selected_items.clear();
        super.onChanged(items);
    }
}
