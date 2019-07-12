package com.sebastiaan.xenopelthis.ui.supplier.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierViewHolder> implements Observer<List<supplier>>, OnClickListener {
    private List<supplier> list;
    private OnClickListener listener;

    public SupplierAdapter() {
        this(null);
    }

    public SupplierAdapter(OnClickListener listener) {
        list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SupplierViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(SupplierViewHolder.layoutResource, viewGroup,false));
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
    public void onClick(supplier s) {
        if (listener != null)
            listener.onClick(s);
    }

    @Override
    public boolean onLongClick(supplier s) {
        if (listener != null)
            return listener.onLongClick(s);
        return true;
    }

    @Override
    public void onChanged(@Nullable List<supplier> suppliers) {
        list = suppliers;
        notifyDataSetChanged();
    }
}
