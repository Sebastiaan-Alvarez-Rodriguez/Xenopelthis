package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Comperator;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class Adapter extends com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter<product> {
    public Adapter() {
        super();
    }

    public Adapter(OnClickListener<product> onClickListener) {
        super(onClickListener);
    }

    @NonNull
    @Override
    protected SortedList<product> getSortedList(Comperator<product> comperator) {
        return new SortedList<>(product.class, comperator);
    }

    @NonNull
    @Override
    protected Comperator<product> getComperator() {
        return new com.sebastiaan.xenopelthis.ui.product.view.adapter.Comperator(this, SortBy.NAME);
    }

    @NonNull
    @Override
    public ViewHolder<product> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(ProductViewHolder.layoutResource, parent,false), this);
    }
}
