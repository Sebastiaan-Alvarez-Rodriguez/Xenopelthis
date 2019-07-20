package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.view.View;

interface InternalClickListener {
    void onClick(View view, int pos);
    boolean onLongClick(View view, int pos);
}
