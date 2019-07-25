package com.sebastiaan.xenopelthis.ui.supplier.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.ProductViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

import java.util.List;

public class AdapterCheckable extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterCheckable<supplier> {

    public AdapterCheckable() {
        super();
    }

    public AdapterCheckable(List<supplier> initialSelected) {
        super(initialSelected);
    }

    public AdapterCheckable(List<supplier> initialSelected, OnClickListener<supplier> onClickListener) {
        super(initialSelected, onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder<supplier> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(parent.getContext()).inflate(ProductViewHolder.layoutResource, parent,false), this);
    }
}
