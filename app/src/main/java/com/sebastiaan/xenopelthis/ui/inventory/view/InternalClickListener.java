package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.view.View;

public interface InternalClickListener {
    void onClick(View view, int pos);
    boolean onLongClick(View view, int pos);
}
