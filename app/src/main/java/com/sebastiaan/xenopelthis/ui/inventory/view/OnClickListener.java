package com.sebastiaan.xenopelthis.ui.inventory.view;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;

public interface OnClickListener {
    void onClick(inventory_item i);
    boolean onLongClick(inventory_item i);
}
