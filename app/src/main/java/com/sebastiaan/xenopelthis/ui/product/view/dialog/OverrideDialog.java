package com.sebastiaan.xenopelthis.ui.product.view.dialog;

import android.app.Activity;
import android.content.Context;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.ProductViewHolder;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.Adapter;

public class OverrideDialog extends com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideDialog<ProductStruct> {
    private Context context;

    public OverrideDialog(Activity activity) { super(activity); context = activity.getApplicationContext(); }

    @Override
    protected void prepareList(long conflictID) {
        RelationConstant relationConstant = new RelationConstant(parent);
        relationConstant.getSuppliersForProduct(conflictID, supplierList -> {
            super.setList(new Adapter(null), supplierList);
        });
    }

    @Override
    protected void inflateViews(ProductStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.product_list_item);
        ProductViewHolder productViewHolder = new ProductViewHolder(conflictItem.inflate());
        productViewHolder.set(conflict.toProduct(conflictID));
    }

    @Override
    protected void setTextViews(ProductStruct conflict) {
        textExists.setText(context.getString(R.string.product_override_dialog_exists, conflict.name));
        textRelations.setText(R.string.product_override_dialog_relations);
    }
}
