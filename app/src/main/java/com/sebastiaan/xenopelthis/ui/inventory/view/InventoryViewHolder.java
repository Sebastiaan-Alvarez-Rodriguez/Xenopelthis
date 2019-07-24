package com.sebastiaan.xenopelthis.ui.inventory.view;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    static final @LayoutRes
    int layoutResource = R.layout.inventory_list_item;

    private TextView productName;
    private EditText amount;
    private InternalClickListener clickListener;

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

    void set(inventory_item item) {
        productName.setText(String.valueOf(item.getProductID()));
        amount.setText(String.valueOf(item.getAmount()));
    }
}
