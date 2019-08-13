package com.sebastiaan.xenopelthis.ui.barcode.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class Adapter extends com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter<barcode> {

    public Adapter() {
        super();
    }

    @NonNull
    @Override
    protected SortedList<barcode> getSortedList(Comperator<barcode> comperator) {
        return new SortedList<>(barcode.class, comperator);
    }

    @NonNull
    @Override
    protected Comperator<barcode> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.barcode.view.adapter.Comperator(this, SortBy.NAME);
    }

    public Adapter(OnClickListener<barcode> onClickListener) {
        super(onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder<barcode> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BarcodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(BarcodeViewHolder.layoutResource,parent,false), this);
    }
}
