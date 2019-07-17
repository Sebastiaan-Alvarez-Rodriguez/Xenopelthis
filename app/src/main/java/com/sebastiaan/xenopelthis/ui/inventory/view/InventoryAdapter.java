package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;


import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> implements Observer<List<inventory_item>>, InternalClickListener {

    protected  List<inventory_item> list;
    private OnClickListener listener;

    public InventoryAdapter() { this(null); }

    public InventoryAdapter(OnClickListener listener) {
        list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InventoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(InventoryViewHolder.layoutResource, viewGroup, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder viewHolder, int position) {
        inventory_item item = list.get(position);
        viewHolder.set(item);
    }

}
