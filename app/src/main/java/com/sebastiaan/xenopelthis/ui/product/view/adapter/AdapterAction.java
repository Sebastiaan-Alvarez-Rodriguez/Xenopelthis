package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class AdapterAction extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction<product> {

    public AdapterAction() {
        super();
    }

    public AdapterAction(ActionListener<product> actionListener) {
        super(actionListener);
    }

    @NonNull
    @Override
    public ViewHolder<product> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(ProductViewHolder.layoutResource, parent,false), this);
    }
}
