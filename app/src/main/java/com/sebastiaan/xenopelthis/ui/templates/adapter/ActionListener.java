package com.sebastiaan.xenopelthis.ui.templates.adapter;

/**
 * @see OnClickListener
 * Specialized template to additionally allow for actionmode change callbacks
 * @param <T> The type from items from the list
 */
public interface ActionListener<T> extends OnClickListener<T> {
        void onActionModeChange(boolean actionMode);
    }
