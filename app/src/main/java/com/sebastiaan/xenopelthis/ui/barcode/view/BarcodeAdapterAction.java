package com.sebastiaan.xenopelthis.ui.barcode.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.sebastiaan.xenopelthis.db.entity.barcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Log.e("OOOF", "List size: "+list.size());
        for (barcode b: list)
            Log.e("OOOF", "    "+b.getTranslation());
        Log.e("OOOF", "Selected");
        for (barcode b: selected_barcodes)
            Log.e("OOOF", "    "+b.getTranslation());
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
        notifyItemRemoved(list.indexOf(b));
        list.remove(b);
        selected_barcodes.remove(b);
        actionMode = false;
        ((ActionListener) onClickListener).onActionModeChange(false);
    }

    @Override
    public void onViewRecycled(@NonNull BarcodeViewHolder holder) {
        super.onViewRecycled(holder);
        Log.e("OOF", "I recycled something: "+ holder.getOldPosition());
    }

    public void remove(Collection<barcode> barcodes) {
        List<Integer> positions = barcodes.stream().map(barcode -> list.indexOf(barcode)).collect(Collectors.toList());
        for (int i = 0; i < barcodes.size(); i++) {
            notifyItemRemoved(positions.get(i));
        }
        list.removeAll(barcodes);
        selected_barcodes.clear();
        actionMode = false;
        ((ActionListener) onClickListener).onActionModeChange(false);
    }
}
