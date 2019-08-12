package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class Adapter extends com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter<ProductAndAmount> {

    public Adapter() {
        super();
    }

    public Adapter(OnClickListener<ProductAndAmount> onClickListener) {
        super(onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder<ProductAndAmount> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(InventoryViewHolder.layoutResource, parent, false), this);
    }
}
