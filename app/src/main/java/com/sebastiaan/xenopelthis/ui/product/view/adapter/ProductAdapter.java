package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Observer<List<product>>, InternalClickListener {
    protected List<product> list;
    protected OnClickListener onClickListener;

    public ProductAdapter() {
        this(null);
    }

    public ProductAdapter(OnClickListener onClickListener) {
        list = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(ProductViewHolder.layoutResource, viewGroup,false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position) {
        product item = list.get(position);
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
    public void onChanged(@Nullable List<product> products) {
        List<product> removed = ListUtil.getRemoved(list, products);
        List<product> added = ListUtil.getAdded(list, products);

        for (product p : removed) {
            int index = list.indexOf(p);
            notifyItemRemoved(index);
            list.remove(index);
        }
        if (products != null)
            for (product p : added) {
                int index = products.indexOf(p);
                list.add(index, p);
                notifyItemInserted(index);
            }
        list = products;
    }
}
