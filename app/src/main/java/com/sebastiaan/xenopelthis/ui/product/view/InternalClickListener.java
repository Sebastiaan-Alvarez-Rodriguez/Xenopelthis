package com.sebastiaan.xenopelthis.ui.product.view;

import android.view.View;

interface InternalClickListener {
    void onClick(View view, int pos);
    boolean onLongClick(View view, int pos);
}
