package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.support.annotation.NonNull;
import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndID;
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
        ProductAndID item = list.get(pos);
        if (selected_items.add(item.toInventoryItem())) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item.toInventoryItem());
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
        ProductAndID item = list.get(pos);
        if (selected_items.add(item.toInventoryItem())) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item.toInventoryItem());
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
    }

    public boolean hasSelected() { return !selected_items.isEmpty(); }

    public Set<inventory_item> getSelected() { return selected_items; }
}
