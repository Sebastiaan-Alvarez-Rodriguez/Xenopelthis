package com.sebastiaan.xenopelthis.ui.barcode.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.ui.barcode.view.adapter.BarcodeViewHolder;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;

import java.util.stream.Collectors;

public class OverrideDialog extends com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideDialog<BarcodeStruct> {
    private String barcode;

    public OverrideDialog(Activity activity, String barcode) {
        super(activity);
        this.barcode = barcode;
    }

    @Override
    protected void prepareList(long conflictID) {
        BarcodeConstant barcodeConstant = new BarcodeConstant(parent);
        barcodeConstant.getProducts(barcode, conflictProducts -> {
            super.setList(new Adapter(), conflictProducts.stream().filter(product -> product.getId() != conflictID).collect(Collectors.toList()));
        });
    }

    @Override
    protected void inflateViews(BarcodeStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.barcode_list_item);
        BarcodeViewHolder barcodeViewHolder = new BarcodeViewHolder(conflictItem.inflate());
        barcodeViewHolder.set(conflict.toBarcode(conflictID));
    }

    @Override
    protected void findGlobalViews(Dialog dialog) {
        super.findGlobalViews(dialog);
        TextView question = dialog.findViewById(R.id.dialog_conflict_question);
        question.setText(R.string.barcode_override_dialog_new_relation);
        TextView warning = dialog.findViewById(R.id.dialog_conflict_warning);
        warning.setText("");
        ImageView image = dialog.findViewById(R.id.dialog_conflict_header);
        image.setImageResource(R.drawable.ic_barcode_ok);
        image.setColorFilter(android.R.color.white, PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void setTextViews(BarcodeStruct conflict) {
        textExists.setText(R.string.barcode_override_dialog_exists);
        textRelations.setText(R.string.barcode_override_dialog_linked_products);

        overrideButton.setText(R.string.barcode_override_dialog_continue);
        overrideButton.setBackgroundResource(android.R.color.holo_blue_dark);
    }
}
