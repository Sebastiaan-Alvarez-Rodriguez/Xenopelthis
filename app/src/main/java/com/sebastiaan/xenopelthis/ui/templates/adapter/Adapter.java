package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

//Maybe need extends RecyclerView.Adapter<ViewHolder>
public abstract class Adapter<T> extends RecyclerView.Adapter<ViewHolder<T>> implements Observer<List<T>>, InternalClickListener {
    protected List<T> list;
    protected OnClickListener<T> onClickListener;

    public Adapter() {
        this(null);
    }

    public Adapter(OnClickListener<T> onClickListener) {
        list = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

//    @NonNull
//    @Override
//    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(ProductViewHolder.layoutResource, parent,false), this);
//    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        T item = list.get(position);
        holder.set(item);
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
    public void onChanged(@Nullable List<T> newList) {
        List<T> removed = ListUtil.getRemoved(list, newList);
        List<T> added = ListUtil.getAdded(list, newList);

        for (T t : removed) {
            int index = list.indexOf(t);
            notifyItemRemoved(index);
            list.remove(index);
        }
        if (newList != null)
            for (T t : added) {
                int index = newList.indexOf(t);
                list.add(index, t);
                notifyItemInserted(index);
            }
        list = newList;
    }
}
