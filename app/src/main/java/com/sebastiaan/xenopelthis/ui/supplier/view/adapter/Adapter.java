package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class Adapter extends com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter<supplier> {
    public Adapter() {
        super();
    }

    public Adapter(OnClickListener<supplier> onClickListener) {
        super(onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder<supplier> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(parent.getContext()).inflate(SupplierViewHolder.layoutResource, parent,false), this);
    }
}
