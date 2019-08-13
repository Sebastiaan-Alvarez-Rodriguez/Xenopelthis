package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;

public class AdapterAction extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction<inventory_item> {

    public AdapterAction() {
        super();
    }

    public AdapterAction(ActionListener<inventory_item> actionListener) {
        super(actionListener);
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