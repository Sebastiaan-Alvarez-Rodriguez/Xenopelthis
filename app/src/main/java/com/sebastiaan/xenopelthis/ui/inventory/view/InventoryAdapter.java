package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.util.ListUtil;


import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> implements Observer<List<inventory_item>>, InternalClickListener {

    protected  List<inventory_item> list;
    protected OnClickListener listener;

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

    @Override
    public int getItemCount() { return list.size(); }

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
    public void onChanged(@Nullable List<inventory_item> items) {
        List<inventory_item> removed = ListUtil.getRemoved(list, items);
        List<inventory_item> added = ListUtil.getAdded(list, items);

        for (inventory_item i : removed) {
            int index = list.indexOf(i);
            notifyItemRemoved(index);
            list.remove(index);
        }
        if (items != null)
            for (inventory_item s : added) {
                int index = items.indexOf(s);
                list.add(index, s);
                notifyItemInserted(index);
            }
        list = items;
    }

}
