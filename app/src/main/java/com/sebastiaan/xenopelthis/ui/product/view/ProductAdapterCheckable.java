package com.sebastiaan.xenopelthis.ui.product.view;

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
    public void onClick(product p) {
        if (!selected_products.add(p))
            selected_products.remove(p);
        super.onClick(p);
    }

    @Override
    public boolean onLongClick(product p) {
        if (!selected_products.add(p))
            selected_products.remove(p);
        return super.onLongClick(p);
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
