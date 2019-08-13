package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;

import java.util.List;

public class AdapterCheckable extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterCheckable<inventory_item> {

    public AdapterCheckable() {
        super();
    }

    public AdapterCheckable(List<inventory_item> initialSelected) {
        super(initialSelected);
    }

    public AdapterCheckable(List<inventory_item> initialSelected, OnClickListener<inventory_item> onClickListener) {
        super(initialSelected, onClickListener);
    }

    @NonNull
    @Override
    protected SortedList<inventory_item> getSortedList(Comperator<inventory_item> comperator) {
        return new SortedList<>(inventory_item.class, comperator);
    }

    @NonNull
    @Override
    protected Comperator<inventory_item> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.inventory.view.Comperator(this, SortBy.NAME);
    }

    @NonNull
    @Override
    public ViewHolder<inventory_item> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(InventoryViewHolder.layoutResource, parent, false), this);
    }
}
