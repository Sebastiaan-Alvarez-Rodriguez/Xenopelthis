package com.sebastiaan.xenopelthis.ui.barcode.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BarcodeAdapterCheckable extends BarcodeAdapter {
    protected Set<barcode> selected_barcodes;

    public BarcodeAdapterCheckable() { this(null, null); }

    public BarcodeAdapterCheckable(List<barcode> initialSelected) { this(initialSelected, null);}

    public BarcodeAdapterCheckable(List<barcode> initialSelected, OnClickListener onClickListener) {
        super(onClickListener);
        selected_barcodes = initialSelected == null ? new HashSet<>() : new HashSet<>(initialSelected);
    }

    @Override
    public void onClick(View view, int pos) {
        barcode item = list.get(pos);
        if (selected_barcodes.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_barcodes.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        super.onClick(view, pos);
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (selected_barcodes.contains(list.get(position)))
            viewHolder.itemView.setBackgroundResource(R.color.colorAccent);
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        barcode item = list.get(pos);
        if (selected_barcodes.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_barcodes.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
    }

    @Override
    public void onViewRecycled(@NonNull BarcodeViewHolder holder) {
        holder.itemView.setBackgroundResource(android.R.color.transparent);
        super.onViewRecycled(holder);
    }

    public int getSelectedCount() {
        return selected_barcodes.size();
    }

    public boolean hasSelected() {
        return !selected_barcodes.isEmpty();
    }

    public Set<barcode> getSelected() {
        return selected_barcodes;
    }

    @Override
    public void onChanged(@Nullable List<barcode> barcodes) {
        selected_barcodes.clear();
        super.onChanged(barcodes);
    }
}
