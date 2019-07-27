package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AdapterCheckable<T> extends Adapter<T> {
    protected Set<T> selected_items;


    public AdapterCheckable() { this(null, null); }

    public AdapterCheckable(List<T> initialSelected) { this(initialSelected, null);}

    public AdapterCheckable(List<T> initialSelected, OnClickListener<T> onClickListener) {
        super(onClickListener);
        selected_items = initialSelected == null ? new HashSet<>() : new HashSet<>(initialSelected);
    }

    public void setSelected(Collection<T> items) {
        List<T> removed = ListUtil.getRemoved(selected_items, items);
        List<T> added = ListUtil.getAdded(selected_items, items);
        for (T t : removed)
            notifyItemChanged(list.indexOf(t));
        for (T t : added)
            notifyItemChanged(list.indexOf(t));
        selected_items.clear();
        selected_items.addAll(items);
    }

    @Override
    public void onClick(View view, int pos) {
        T item = list.get(pos);
        if (selected_items.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        super.onClick(view, pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> viewHolder, int position) {
        if (selected_items.contains(list.get(position)))
            viewHolder.itemView.setBackgroundResource(R.color.colorAccent);
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        T item = list.get(pos);
        if (selected_items.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_items.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder<T> holder) {
        holder.itemView.setBackgroundResource(android.R.color.transparent);
        super.onViewRecycled(holder);
    }

    public int getSelectedCount() {
        return selected_items.size();
    }

    public boolean hasSelected() {
        return !selected_items.isEmpty();
    }

    public Set<T> getSelected() {
        return selected_items;
    }

    @Override
    public void onChanged(@Nullable List<T> newList) {
        selected_items.clear();
        super.onChanged(newList);
    }
}
