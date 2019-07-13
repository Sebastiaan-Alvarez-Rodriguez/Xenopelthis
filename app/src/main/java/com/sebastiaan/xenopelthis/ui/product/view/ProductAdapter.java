package com.sebastiaan.xenopelthis.ui.product.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Observer<List<product>>, OnClickListener {
    private List<product> list;
    private OnClickListener onClickListener;

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
        return new ProductViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(ProductViewHolder.layoutResource, viewGroup,false));
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
    public void onClick(product p) {
        if (onClickListener != null)
            onClickListener.onClick(p);
    }

    @Override
    public boolean onLongClick(product p) {
        if (onClickListener != null)
            return onClickListener.onLongClick(p);
        return true;
    }

    @Override
    public void onChanged(@Nullable List<product> suppliers) {
        list = suppliers;
        notifyDataSetChanged();
    }
}
