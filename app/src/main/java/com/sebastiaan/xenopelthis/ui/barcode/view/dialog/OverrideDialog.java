package com.sebastiaan.xenopelthis.ui.barcode.view.dialog;

import android.app.Activity;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.ProductConstant;
import com.sebastiaan.xenopelthis.ui.barcode.view.adapter.BarcodeViewHolder;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;

import java.util.Collections;

public class OverrideDialog extends com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideDialog<BarcodeStruct> {

    public OverrideDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void prepareList(long conflictID) {
        ProductConstant productConstant = new ProductConstant(parent);
        productConstant.get(conflictID, conflictProduct -> {
            super.setList(new Adapter(null), Collections.singletonList(conflictProduct));
        });
    }

    @Override
    protected void inflateViews(BarcodeStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.product_list_item);
        BarcodeViewHolder productViewHolder = new BarcodeViewHolder(conflictItem.inflate());
        productViewHolder.set(conflict.toBarcode(conflictID));
    }

    @Override
    protected void setTextViews(BarcodeStruct conflict) {
        textExists.setText("The following barcode already exists.");
        textRelations.setText("The following products are already linked. See below:");
    }
}
