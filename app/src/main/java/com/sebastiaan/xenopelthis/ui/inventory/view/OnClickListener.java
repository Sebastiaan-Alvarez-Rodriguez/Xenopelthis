package com.sebastiaan.xenopelthis.ui.inventory.view;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndID;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

public interface OnClickListener {
    void onClick(ProductAndID i);
    boolean onLongClick(ProductAndID i);
}
