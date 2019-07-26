package com.sebastiaan.xenopelthis.ui.supplier.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.SupplierViewHolder;


public class OverrideDialog {
    private TextView textExists, textRelations;
    private ViewStub conflictItem;
    private Button cancelButton, overrideButton;

    private Dialog dialog;

    private Activity parent;

    public OverrideDialog(Activity activity) { parent = activity; }

    public void showDialog(SupplierStruct conflicting, long conflictID, OverrideListener onOverrideListener) {
        dialog = new Dialog(parent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_override);

        findGlobalViews(dialog);
        prepareList(conflictID);

        setupButtons(dialog, onOverrideListener);

        inflateViews(conflicting, conflictID);
        textExists.setText("Supplier with name"+conflicting.name+" already exists. See below:");
        textRelations.setText("The following product-relations exists. See below:");
    }

    private void findGlobalViews(Dialog dialog) {
        textExists = dialog.findViewById(R.id.dialog_conflict_text);
        textRelations = dialog.findViewById(R.id.dialog_conflict_text_relations);
        conflictItem = dialog.findViewById(R.id.dialog_conflict_item);
        cancelButton = dialog.findViewById(R.id.dialog_conflict_cancel);
        overrideButton = dialog.findViewById(R.id.dialog_conflict_override);
    }

    private void inflateViews(SupplierStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.supplier_list_item);
        SupplierViewHolder supplierViewHolder = new SupplierViewHolder(conflictItem.inflate());
        supplierViewHolder.set(conflict.toSupplier(conflictID));
    }

    private void prepareList(long conflictID) {
        RelationConstant relationConstant = new RelationConstant(parent);
        relationConstant.getProductsForSupplier(conflictID, productList -> {
            RecyclerView list = dialog.findViewById(R.id.dialog_conflict_relationlist);
            if (productList.isEmpty()) {
                list.setVisibility(View.GONE);
                textRelations.setVisibility(View.GONE);
            } else {
                Adapter adapter = new Adapter(null);
                adapter.onChanged(productList);
                list.setLayoutManager(new LinearLayoutManager(parent));
                list.setAdapter(adapter);
                list.addItemDecoration(new DividerItemDecoration(parent, LinearLayoutManager.VERTICAL));
            }
            parent.runOnUiThread(dialog::show);
        });
    }

    private void setupButtons(Dialog dialog, OverrideListener onOverrideListener) {
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        overrideButton.setOnClickListener(v -> {
            dialog.dismiss();
            onOverrideListener.onOverride();
        });
    }

}
