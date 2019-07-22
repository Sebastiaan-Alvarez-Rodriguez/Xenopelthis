package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;

public class InventoryAdapterAction extends InventoryAdapterCheckable {
    private boolean actionMode = false;

    public InventoryAdapterAction() { this(null); }

    public InventoryAdapterAction(ActionListener actionListener) { super(null, actionListener); }

    @Override
    public void onClick(View view, int pos) {
        if (actionMode) {
            super.onClick(view, pos);

            if (actionMode && !hasSelected()) {
                actionMode = false;
                ((ActionListener) listener).onActionModeChange(false);
            }
        } else {
            listener.onClick(list.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view, int pos) {
        if (!actionMode) {
            actionMode = true;
            ((ActionListener) listener).onActionModeChange(true);
        }
        return super.onLongClick(view, pos);
    }

    public boolean isActionMode() { return actionMode; }

    @Override
    public void onChanged(@Nullable List<inventory_item> items) {
        ((ActionListener) listener).onActionModeChange(false);
        super.onChanged(items);
    }
}
