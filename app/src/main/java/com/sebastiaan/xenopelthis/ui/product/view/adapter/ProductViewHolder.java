package com.sebastiaan.xenopelthis.ui.product.view.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class ProductViewHolder extends ViewHolder<product> {
    private TextView productName, productDescription;
    private ImageButton expandDetailButton;
    private ImageView hasBarcodeView;

    private RelativeLayout detailView;

    public ProductViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    public ProductViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        findViews();
        detailView.setVisibility(View.GONE);
        setupButton();
        setupClicks();
    }

    private void findViews() {
        productName = itemView.findViewById(R.id.product_list_name);
        expandDetailButton = itemView.findViewById(R.id.product_list_expand_collapse);
        hasBarcodeView = itemView.findViewById(R.id.product_list_has_barcode);
        productDescription = itemView.findViewById(R.id.product_list_detail_description);
        detailView = itemView.findViewById(R.id.product_list_detailview);
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
    public void set(product product) {
        productName.setText(product.getName());
        productDescription.setText(product.getProductDescription());
        if (product.getHasBarcode())
            hasBarcodeView.setBackgroundResource(R.drawable.ic_barcode_ok);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.product_list_item;
    }
}
