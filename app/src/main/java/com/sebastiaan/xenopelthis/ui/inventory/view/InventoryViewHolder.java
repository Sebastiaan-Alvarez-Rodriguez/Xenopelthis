package com.sebastiaan.xenopelthis.ui.inventory.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class InventoryViewHolder extends ViewHolder<ProductAndAmount> {
    public static final @LayoutRes int layoutResource = R.layout.inventory_list_item;
    private TextView productName;
    private TextView amount;
    private TextView productDescription;
    private ImageButton expandDetailButton;
    private RelativeLayout detailView;

    private View itemView;

    InventoryViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        this.itemView = itemView;
        findViews();
        setupClicks();
        setupButton();
        detailView.setVisibility(View.GONE);
    }

    private void findViews() {
        productName = itemView.findViewById(R.id.inventory_list_product);
        amount = itemView.findViewById(R.id.inventory_list_amount);
        productDescription = itemView.findViewById(R.id.inventory_list_detail_description);
        expandDetailButton = itemView.findViewById(R.id.inventory_list_expand_collapse);
        detailView = itemView.findViewById(R.id.inventory_list_detailview);

    }

    private void setupButton() {
        expandDetailButton.setOnClickListener(v -> {
            if (detailView.getVisibility() == View.GONE) {
                expandDetailButton.setBackgroundResource(R.drawable.ic_arrow_up);
                detailView.setVisibility(View.VISIBLE);
            } else {
                expandDetailButton.setBackgroundResource(R.drawable.ic_arrow_down);
                detailView.setVisibility(View.GONE);
            }
        });
    }

    private void setupClicks() {
        if (clickListener == null)
            return;
        itemView.setOnClickListener(v -> clickListener.onClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> clickListener.onLongClick(v, getAdapterPosition()));
    }

    @Override
    public void set(ProductAndAmount item) {
        productName.setText(String.valueOf(item.getP().getName()));
        amount.setText(String.valueOf(item.getAmount()));
        productDescription.setText(String.valueOf(item.getP().getProductDescription()));
    }
}
