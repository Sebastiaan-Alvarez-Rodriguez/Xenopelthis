package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;

import java.util.List;

public class AdapterCheckable extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterCheckable<product> {

    public AdapterCheckable() {
        super();
    }

    public AdapterCheckable(List<product> initialSelected) {
        super(initialSelected);
    }

    public AdapterCheckable(List<product> initialSelected, OnClickListener<product> onClickListener) {
        super(initialSelected, onClickListener);
    }

    @NonNull
    @Override
    protected SortedList<product> getSortedList(Comperator<product> comperator) {
        return new SortedList<>(product.class, comperator);
    }

    @NonNull
    @Override
    protected Comperator<product> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.product.view.adapter.Comperator(this, SortBy.NAME);
    }

    @NonNull
    @Override
    public ViewHolder<product> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(ProductViewHolder.layoutResource, parent,false), this);
    }
}
