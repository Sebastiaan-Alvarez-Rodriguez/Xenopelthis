package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class AdapterAction extends com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction<barcode> {

    public AdapterAction() {
        super();
    }

    public AdapterAction(ActionListener<barcode> actionListener) {
        super(actionListener);
    }

    @NonNull
    @Override
    public ViewHolder<barcode> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(BarcodeViewHolder.layoutResource,parent,false), this);
    }
}
