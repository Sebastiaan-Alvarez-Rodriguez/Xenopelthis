package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class Adapter extends com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter<supplier> {
    public Adapter() {
        this(null);
    }

    public Adapter(OnClickListener<supplier> onClickListener) {
        super(onClickListener);
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
