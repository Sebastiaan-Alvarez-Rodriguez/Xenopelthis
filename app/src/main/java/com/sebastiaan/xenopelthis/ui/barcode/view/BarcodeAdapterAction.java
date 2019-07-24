package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.view.View;

import androidx.annotation.Nullable;

import com.sebastiaan.xenopelthis.db.entity.barcode;

import java.util.ArrayList;
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

    public boolean add(barcode b) {
        if (!list.contains(b)) {
            list.add(b);
            notifyItemInserted(list.size());//TODO: This, or -1?
            return true;
        }
        return false;
    }

    public void remove(barcode b) {
        List<barcode> copy = new ArrayList<>(list);
        copy.remove(b);
        this.onChanged(copy);
    }

    public void remove(Collection<barcode> barcodes) {
        List<barcode> copy = new ArrayList<>(list);
        copy.removeAll(barcodes);
        this.onChanged(copy);
    }

    @Override
    public void onChanged(@Nullable List<barcode> barcodes) {
        actionMode = false;
        ((ActionListener) onClickListener).onActionModeChange(false);
        super.onChanged(barcodes);
    }
}
