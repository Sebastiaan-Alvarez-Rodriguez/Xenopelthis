package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;
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
    protected SortedList<supplier> getSortedList(Comperator<supplier> comperator) {
        return new SortedList<>(supplier.class, comperator);
    }

    @NonNull
    @Override
    protected Comperator<supplier> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.supplier.view.adapter.Comperator(this, SortBy.NAME);
    }

    @NonNull
    @Override
    public ViewHolder<supplier> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(parent.getContext()).inflate(SupplierViewHolder.layoutResource, parent,false), this);
    }
}
