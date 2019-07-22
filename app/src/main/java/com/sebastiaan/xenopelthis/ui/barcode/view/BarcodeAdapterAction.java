package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.sebastiaan.xenopelthis.db.entity.barcode;

import java.util.Collection;
import java.util.List;

public class BarcodeAdapterAction extends BarcodeAdapterCheckable {
    private boolean actionMode = false;

    public BarcodeAdapterAction() {
        super(null, null);
    }

    public BarcodeAdapterAction(List<barcode> initialSelected) {
        super(initialSelected);
    }

    public BarcodeAdapterAction(ActionListener actionListener) {
        super(null, actionListener);
    }

    public BarcodeAdapterAction(List<barcode> initialSelected, ActionListener actionListener) {
        super(initialSelected, actionListener);
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
    public void onChanged(@Nullable List<barcode> barcodes) {
        ((ActionListener) onClickListener).onActionModeChange(false);
        super.onChanged(barcodes);
    }

    public boolean add(barcode b) {
        if (!list.contains(b)) {
            list.add(b);
            notifyItemInserted(list.size() - 1);
            return true;
        }
        return false;
    }

    public void remove(barcode b) {
        list.remove(b);
        notifyDataSetChanged();
    }

    public void remove(Collection<barcode> barcodes) {
        list.removeAll(barcodes);
        notifyDataSetChanged();
    }
}
