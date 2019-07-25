package com.sebastiaan.xenopelthis.ui.product.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.ProductViewHolder;
import com.sebastiaan.xenopelthis.ui.supplier.view.Adapter;

public class OverrideDialog {
    private TextView textExists, textRelations;
    private ViewStub conflictItem;
    private Button cancelButton, overrideButton;

    private Dialog dialog;

    private Activity parent;

    public OverrideDialog(Activity activity) {
        parent = activity;
    }

    public void showDialog(ProductStruct conflicting, long conflictID, OverrideListener onOverrideListener) {
        dialog = new Dialog(parent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_override);

        findGlobalViews(dialog);
        prepareList(conflictID);

        setupButtons(dialog, onOverrideListener);

        inflateViews(conflicting, conflictID);
        textExists.setText("Product with name "+conflicting.name+ " already exists. See below:");
        textRelations.setText("The following supplier-relations exists. See below:");
    }

    private void findGlobalViews(Dialog dialog) {
        textExists = dialog.findViewById(R.id.dialog_conflict_text);
        textRelations = dialog.findViewById(R.id.dialog_conflict_text_relations);
        conflictItem = dialog.findViewById(R.id.dialog_conflict_item);
        cancelButton = dialog.findViewById(R.id.dialog_conflict_cancel);
        overrideButton = dialog.findViewById(R.id.dialog_conflict_override);
    }

    private void inflateViews(ProductStruct conflict, long conflictID) {
        conflictItem.setLayoutResource(R.layout.product_list_item);
        ProductViewHolder productViewHolder = new ProductViewHolder(conflictItem.inflate());
        productViewHolder.set(conflict.toProduct(conflictID));
    }

    private void prepareList(long conflictID) {
        RelationConstant relationConstant = new RelationConstant(parent);
        relationConstant.getSuppliersForProduct(conflictID, supplierList -> {
            RecyclerView list = dialog.findViewById(R.id.dialog_conflict_relationlist);
            if (supplierList.isEmpty()) {
                list.setVisibility(View.GONE);
                textRelations.setVisibility(View.GONE);
            } else {
                Adapter adapter = new Adapter(null);
                adapter.onChanged(supplierList);
                list.setLayoutManager(new LinearLayoutManager(parent));
                list.setAdapter(adapter);
                list.addItemDecoration(new DividerItemDecoration(parent, LinearLayoutManager.VERTICAL));
            }
            parent.runOnUiThread(dialog::show);
        });
    }

    private void setupButtons(Dialog dialog, OverrideListener onOverrideListener) {
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        overrideButton.setOnClickListener( v-> {
            dialog.dismiss();
            onOverrideListener.onOverride();
        });
    }

}
