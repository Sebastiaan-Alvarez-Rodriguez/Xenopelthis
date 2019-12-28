package com.sebastiaan.xenopelthis.ui.barcode.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;

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

    /**
     * @see SortedList#SortedList(Class, SortedList.Callback)
     */
    @NonNull
    @Override
    protected SortedList<barcode> getSortedList(Comperator<barcode> comperator) {
        return new SortedList<>(barcode.class, comperator);
    }

    /**
     * @see com.sebastiaan.xenopelthis.ui.barcode.view.adapter.Comperator#Comperator(Adapter, SortBy)
     */
    @NonNull
    @Override
    protected Comperator<barcode> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.barcode.view.adapter.Comperator(this, SortBy.NAME);
    }

    /**
     * @see BarcodeViewHolder#BarcodeViewHolder(View, InternalClickListener)
     */
    @NonNull
    @Override
    public ViewHolder<barcode> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(BarcodeViewHolder.layoutResource,parent,false), this);
    }
}
