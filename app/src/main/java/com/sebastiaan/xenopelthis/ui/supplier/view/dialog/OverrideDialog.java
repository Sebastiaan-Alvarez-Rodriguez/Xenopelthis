package com.sebastiaan.xenopelthis.ui.supplier.view.dialog;

import android.app.Activity;
import android.content.Context;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.SupplierViewHolder;

public class OverrideDialog extends com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideDialog<SupplierStruct> {
    private Context context;

    public OverrideDialog(Activity activity) { super(activity); context = activity.getApplicationContext();}

    @Override
    protected void prepareList(long conflictID) {
        RelationConstant relationConstant = new RelationConstant(parent);
        relationConstant.getProductsForSupplier(conflictID, productList -> super.setList(new Adapter(null), productList));
    }

    @Override
    protected void inflateViews(SupplierStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.supplier_list_item);
        SupplierViewHolder supplierViewHolder = new SupplierViewHolder(conflictItem.inflate());
        supplierViewHolder.set(conflict.toSupplier(conflictID));
    }

    @Override
    protected void setTextViews(SupplierStruct conflict) {
        textExists.setText(context.getString(R.string.supplier_override_dialog_exists, conflict.name));
        textRelations.setText(R.string.supplier_override_dialog_relations);
    }

}
