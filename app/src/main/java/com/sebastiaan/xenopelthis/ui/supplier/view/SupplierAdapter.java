package com.sebastiaan.xenopelthis.ui.supplier.view;

import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierViewHolder> implements Observer<List<supplier>>, InternalClickListener {
    protected List<supplier> list;
    protected OnClickListener listener;

    public SupplierAdapter() {
        this(null, null);
    }

    public SupplierAdapter(OnClickListener listener) {
        this(null, listener);
    }

    public SupplierAdapter(List<supplier> initial, OnClickListener listener) {
        list = initial == null ? new ArrayList<>() : initial;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SupplierViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(SupplierViewHolder.layoutResource, viewGroup,false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder viewHolder, int position) {
        supplier item = list.get(position);
        viewHolder.set(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onClick(View view, int pos) {
        if (listener != null)
            listener.onClick(list.get(pos));
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        if (listener != null)
            return listener.onLongClick(list.get(pos));
        return true;
    }

    @Override
    public void onChanged(@Nullable List<supplier> suppliers) {
        list = suppliers;
        notifyDataSetChanged();
    }
}
