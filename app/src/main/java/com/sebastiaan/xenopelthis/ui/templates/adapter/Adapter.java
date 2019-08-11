package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Template to create an Adapter, which works with architecture LiveData
 * @param <T> The type of items of the list to be displayed
 */
public abstract class Adapter<T> extends RecyclerView.Adapter<ViewHolder<T>> implements Observer<List<T>>, InternalClickListener {
    protected List<T> list;
    protected OnClickListener<T> onClickListener;

    /**
     * Constructor which sets given onClickListener to send callbacks to, if the listener is not null
     * @param onClickListener Listener to send callbacks in case of item clicks
     */
    public Adapter(OnClickListener<T> onClickListener) {
        list = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    /**
     * @see #Adapter(OnClickListener)
     * Same function, but sets no listener
     */
    public Adapter() {
        this(null);
    }

    /**
     * Simple function to return the list of items
     * @return the list of items
     */
    public List<T> getItems() {
        return new ArrayList<>(list);
    }

    /**
     * Adds a single item to the list, and displays it to the user
     * @param item The item to be added
     * @return true if addition was successful, otherwise false
     */
    public boolean add(T item) {
        if (list.add(item)) {
            notifyItemInserted(list.indexOf(item));
            return true;
        }
        return false;
    }

    /**
     * @see #add(Object)
     * Function to add a collection of items to the list
     */
    public void add(Collection<T> items) {
        for (T item : items)
            add(item);
    }

    /**
     * Removes a single item from the list, if it is in the list. Does nothing otherwise
     * @param item item to be removed
     */
    public void remove(T item) {
        if (list.contains(item)) {
            int index = list.indexOf(item);
            notifyItemRemoved(index);
            list.remove(index);
        }
    }

    /**
     * @see #remove(Object)
     * Function to remove a list of items from the list
     */
    public void remove(Collection<T> items) {
        for (T item : items)
            remove(item);
    }

    /**
     * Assigns a viewholder an item from the list, depending on the position of the viewholder in the list
     * @param holder The viewholder to receive an item from the UI list
     * @param position The position of the viewholder in the UI list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        T item = list.get(position);
        holder.set(item);
    }

    /**
     * @return the amount of items in the list
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Propagates click callback from viewholder to listener.
     * @param view The clicked view
     * @param pos the clicked position
     */
    @Override
    public void onClick(View view, int pos) {
        if (onClickListener != null)
            onClickListener.onClick(list.get(pos));
    }

    /**
     * Propagates long-click callback from viewholder to listener.
     * @param view The clicked view
     * @param pos the clicked position
     */
    @Override
    public boolean onLongClick(View view, int pos) {
        if (onClickListener != null)
            return onClickListener.onLongClick(list.get(pos));
        return true;
    }

    /**
     * Callback receiver for list changes.
     * @param newList the new List
     */
    @Override
    public void onChanged(@Nullable List<T> newList) {
        List<T> removed = ListUtil.getRemoved(list, newList);
        List<T> added = ListUtil.getAdded(list, newList);
        remove(removed);
        add(added);
    }
}
