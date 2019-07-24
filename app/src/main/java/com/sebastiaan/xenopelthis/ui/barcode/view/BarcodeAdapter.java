package com.sebastiaan.xenopelthis.ui.barcode.view;

import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.barcode;

import java.util.ArrayList;
import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeViewHolder> implements Observer<List<barcode>>, InternalClickListener {
    protected List<barcode> list;
    protected OnClickListener onClickListener;

    public BarcodeAdapter() {
        this(null);
    }

    public BarcodeAdapter(OnClickListener onClickListener) {
        list = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    public List<barcode> getItems() {
        return new ArrayList<>(list);
    }

    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BarcodeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(BarcodeViewHolder.layoutResource, viewGroup,false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder viewHolder, int position) {
        barcode item = list.get(position);
        viewHolder.set(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onClick(View view, int pos) {
        if (onClickListener != null)
            onClickListener.onClick(list.get(pos));
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        if (onClickListener != null)
            return onClickListener.onLongClick(list.get(pos));
        return true;
    }

    @Override
    public void onChanged(@Nullable List<barcode> barcodes) {
        list = barcodes;
        notifyDataSetChanged();
    }
}
