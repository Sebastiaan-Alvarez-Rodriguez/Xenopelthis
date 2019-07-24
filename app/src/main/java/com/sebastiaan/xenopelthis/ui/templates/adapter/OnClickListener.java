package com.sebastiaan.xenopelthis.ui.templates.adapter;


public interface OnClickListener<T> {
    void onClick(T t);
    boolean onLongClick(T t);
}
