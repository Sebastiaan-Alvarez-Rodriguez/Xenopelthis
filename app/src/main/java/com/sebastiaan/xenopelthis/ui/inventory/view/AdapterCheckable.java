package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

import java.util.List;

public class AdapterCheckable extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterCheckable<ProductAndAmount> {

    public AdapterCheckable() {
        super();
    }

    public AdapterCheckable(List<ProductAndAmount> initialSelected) {
        super(initialSelected);
    }

    public AdapterCheckable(List<ProductAndAmount> initialSelected, OnClickListener<ProductAndAmount> onClickListener) {
        super(initialSelected, onClickListener);
    }
    @NonNull
    @Override
    public ViewHolder<ProductAndAmount> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(InventoryViewHolder.layoutResource, parent, false), this);
    }
}
