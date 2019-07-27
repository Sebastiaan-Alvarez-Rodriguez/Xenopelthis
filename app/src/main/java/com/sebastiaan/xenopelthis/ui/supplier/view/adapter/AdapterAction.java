package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

import java.util.List;

public class AdapterAction extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction<supplier> {

    public AdapterAction() {
        super();
    }

    public AdapterAction(ActionListener<supplier> actionListener) {
        super(actionListener);
    }

    @NonNull
    @Override
    public ViewHolder<supplier> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(parent.getContext()).inflate(SupplierViewHolder.layoutResource, parent,false), this);
    }

    @Override
    public void onChanged(@Nullable List<supplier> newList) {
        super.onChanged(newList);
    }
}
