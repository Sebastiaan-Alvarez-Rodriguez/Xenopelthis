package com.sebastiaan.xenopelthis.ui.product.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

public class ProductAdapterAction extends ProductAdapterCheckable {
    private boolean actionMode = false;

    public ProductAdapterAction() {
        this(null);
    }

    public ProductAdapterAction(ActionListener actionListener) {
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
    public void onChanged(@Nullable List<product> suppliers) {
        ((ActionListener) onClickListener).onActionModeChange(false);
        super.onChanged(suppliers);
    }
}
