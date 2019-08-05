package com.sebastiaan.xenopelthis.ui.product.view.dialog;

import android.app.Activity;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.ProductViewHolder;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.Adapter;

public class OverrideDialog extends com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideDialog<ProductStruct> {
    public OverrideDialog(Activity activity) { super(activity); }

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
        textExists.setText("Product with name "+conflict.name+" already exists. See below:");
        textRelations.setText("The following supplier-relations exists. See below:");
    }
}
