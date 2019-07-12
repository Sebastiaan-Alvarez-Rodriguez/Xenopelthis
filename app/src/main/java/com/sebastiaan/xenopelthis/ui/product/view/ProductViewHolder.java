package com.sebastiaan.xenopelthis.ui.product.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.supplier.view.SupplierAdapter;

class ProductViewHolder extends RecyclerView.ViewHolder {
    static final @LayoutRes int layoutResource = R.layout.product_list_item;

    private TextView productName, productDescription;
    private ImageButton expandDetailButton;
    private RelativeLayout detailView;
    private RecyclerView productSupplierView;

    private InternalClickListener clickListener;

    private long observedID;
    private SupplierAdapter adapter;
    private boolean suppliersReceived;

    ProductViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    ProductViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        findViews();
        detailView.setVisibility(View.GONE);
        setupButton();
        setupClicks();
        adapter = new SupplierAdapter();
        suppliersReceived = false;
    }

    private void findViews() {
        productName = itemView.findViewById(R.id.product_list_name);
        expandDetailButton = itemView.findViewById(R.id.product_list_expand_collapse);
        productDescription = itemView.findViewById(R.id.product_list_detail_description);
        productSupplierView = itemView.findViewById(R.id.product_list_detail_supplierView);
        detailView = itemView.findViewById(R.id.product_list_detailview);
    }

    private void setupButton() {
        expandDetailButton.setOnClickListener(v -> {
            if (detailView.getVisibility() == View.GONE) {
                if (!suppliersReceived)
                    prepareList();
                expandDetailButton.setBackgroundResource(R.drawable.ic_arrow_up);
                detailView.setVisibility(View.VISIBLE);
            } else {
                expandDetailButton.setBackgroundResource(R.drawable.ic_arrow_down);
                detailView.setVisibility(View.GONE);
            }
        });
    }

    void set(product product) {
        productName.setText(product.getName());
        productDescription.setText(product.getProductDescription());
        observedID = product.getId();
    }

    private void setupClicks() {
        if (clickListener == null)
            return;
        itemView.setOnClickListener(v -> clickListener.onClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> clickListener.onLongClick(v, getAdapterPosition()));
    }

    private void prepareList() {
        productSupplierView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        productSupplierView.setAdapter(adapter);
        productSupplierView.addItemDecoration(new DividerItemDecoration(itemView.getContext(), LinearLayoutManager.VERTICAL));
    }

    SupplierAdapter getAdapter() {
        return adapter;
    }

    long getObservedID() {
        return observedID;
    }
}
