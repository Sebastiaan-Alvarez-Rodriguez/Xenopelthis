package com.sebastiaan.xenopelthis.ui.product.view;

import android.view.View;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.HashSet;
import java.util.Set;

public class ProductAdapterCheckable extends ProductAdapter {
    private Set<product> selected_products;

    public ProductAdapterCheckable() {
        this(null);
    }

    public ProductAdapterCheckable(OnClickListener onClickListener) {
        super(onClickListener);
        selected_products = new HashSet<>();
    }

    @Override
    public void onClick(View view, int pos) {
        product item = list.get(pos);
        if (selected_products.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_products.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        super.onClick(view, pos);
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        product item = list.get(pos);
        if (selected_products.add(item)) {
            view.setBackgroundResource(R.color.colorAccent);
        } else {
            selected_products.remove(item);
            view.setBackgroundResource(android.R.color.transparent);
        }
        return super.onLongClick(view, pos);
    }

    public int getSelectedCount() {
        return selected_products.size();
    }

    public boolean hasSelected() {
        return !selected_products.isEmpty();
    }

    public Set<product> getSelected() {
        return selected_products;
    }
}
