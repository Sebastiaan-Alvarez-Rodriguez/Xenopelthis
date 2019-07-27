package com.sebastiaan.xenopelthis.ui.templates.adapter;

public interface ActionListener<T> extends OnClickListener<T> {
        void onActionModeChange(boolean actionMode);
    }
