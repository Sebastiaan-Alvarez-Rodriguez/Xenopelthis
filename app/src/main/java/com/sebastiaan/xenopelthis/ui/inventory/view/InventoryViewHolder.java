package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class InventoryViewHolder extends ViewHolder<inventory_item> {
    public static final @LayoutRes int layoutResource = R.layout.inventory_list_item;
    private TextView productName;
    private EditText amount;

    InventoryViewHolder(@NonNull View itemView) { this(itemView, null); }

    InventoryViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;

        findViews();
        setupClicks();
    }

    private void findViews() {
        productName = itemView.findViewById(R.id.inventory_list_product);
        amount = itemView.findViewById(R.id.inventory_list_amount);
    }

    private void setupClicks() {
        if (clickListener == null)
            return;
        itemView.setOnClickListener(v -> clickListener.onClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> clickListener.onLongClick(v, getAdapterPosition()));
    }

    @Override
    public void set(inventory_item item) {
        productName.setText(String.valueOf(item.getProductID()));
        amount.setText(String.valueOf(item.getAmount()));
    }
}
