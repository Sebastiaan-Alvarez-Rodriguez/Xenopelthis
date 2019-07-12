package com.sebastiaan.xenopelthis.ui.supplier.view;

import com.sebastiaan.xenopelthis.db.entity.supplier;

public interface OnClickListener {
    void onClick(supplier s);
    boolean onLongClick(supplier s);
}
