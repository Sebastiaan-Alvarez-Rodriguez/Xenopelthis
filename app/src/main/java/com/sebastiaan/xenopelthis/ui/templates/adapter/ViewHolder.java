package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
    protected InternalClickListener clickListener;

    public ViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    private ViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
    }

    public abstract void set(T t);

}
