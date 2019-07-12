package com.sebastiaan.xenopelthis.ui.product.view;

import com.sebastiaan.xenopelthis.db.entity.product;

public interface OnClickListener {
    void onClick(product s);
    boolean onLongClick(product s);
}
