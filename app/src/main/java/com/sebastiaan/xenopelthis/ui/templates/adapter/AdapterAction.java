package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public abstract class AdapterAction<T> extends AdapterCheckable<T> {
    private boolean actionMode = false;

    public AdapterAction() {
        this(null);
    }

    public AdapterAction(ActionListener<T> actionListener) {
        super(null, actionListener);
    }

    @Override
    public void onClick(View view, int pos) {
        if (actionMode) {
            super.onClick(view, pos);

            if (actionMode && !hasSelected()) {
                actionMode = false;
                ((ActionListener) onClickListener).onActionModeChange(false);
            }
        } else {
            onClickListener.onClick(list.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        if (!actionMode) {
            actionMode = true;
            ((ActionListener) onClickListener).onActionModeChange(true);
        }
        return super.onLongClick(view, pos);
    }

    public boolean isActionMode() {
        return actionMode;
    }

    @Override
    public void onChanged(@Nullable List<T> newList) {
        actionMode = false;
        ((ActionListener) onClickListener).onActionModeChange(false);
        super.onChanged(newList);
    }
}
