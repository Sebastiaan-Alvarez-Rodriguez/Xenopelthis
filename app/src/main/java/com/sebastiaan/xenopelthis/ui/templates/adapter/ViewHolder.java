package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
    protected InternalClickListener clickListener;

    public static @LayoutRes int layoutResource;
    public ViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    private ViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        layoutResource = getLayoutRes();
    }

    public abstract void set(T t);

    protected abstract @LayoutRes int getLayoutRes();
}
