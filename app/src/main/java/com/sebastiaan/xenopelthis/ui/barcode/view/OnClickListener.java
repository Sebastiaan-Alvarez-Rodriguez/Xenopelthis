package com.sebastiaan.xenopelthis.ui.barcode.view;

import com.sebastiaan.xenopelthis.db.entity.barcode;

public interface OnClickListener {
    void onClick(barcode b);
    boolean onLongClick(barcode b);
}
