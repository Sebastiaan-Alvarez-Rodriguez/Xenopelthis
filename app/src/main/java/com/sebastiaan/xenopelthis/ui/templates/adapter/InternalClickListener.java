package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

public interface InternalClickListener {
    void onClick(View view, int pos);
    boolean onLongClick(View view, int pos);


}
