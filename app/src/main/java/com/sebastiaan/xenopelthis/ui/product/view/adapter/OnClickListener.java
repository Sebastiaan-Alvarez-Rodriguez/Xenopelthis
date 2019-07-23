package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import com.sebastiaan.xenopelthis.db.entity.product;

public interface OnClickListener {
    void onClick(product p);
    boolean onLongClick(product p);
}
