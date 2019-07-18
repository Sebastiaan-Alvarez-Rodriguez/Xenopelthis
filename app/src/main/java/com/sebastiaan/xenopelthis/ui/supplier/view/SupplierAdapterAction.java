package com.sebastiaan.xenopelthis.ui.supplier.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;

public class SupplierAdapterAction extends SupplierAdapterCheckable {
    private boolean actionMode = false;

    public SupplierAdapterAction() { this(null); }

    public SupplierAdapterAction(ActionListener actionListener) { super(null, actionListener); }

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
    public void onChanged(@Nullable List<supplier> products) {
        ((ActionListener) listener).onActionModeChange(false);
        super.onChanged(products);
    }

}
