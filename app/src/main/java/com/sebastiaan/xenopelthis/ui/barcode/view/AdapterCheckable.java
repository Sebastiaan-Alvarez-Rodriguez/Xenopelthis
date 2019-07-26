package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

import java.util.List;

public class AdapterCheckable extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterCheckable<barcode> {

    public AdapterCheckable() {
        super();
    }

    public AdapterCheckable(List<barcode> initialSelected) {
        super(initialSelected);
    }

    public AdapterCheckable(List<barcode> initialSelected, OnClickListener<barcode> onClickListener) {
        super(initialSelected, onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder<barcode> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(BarcodeViewHolder.layoutResource,parent,false), this);
    }
}
