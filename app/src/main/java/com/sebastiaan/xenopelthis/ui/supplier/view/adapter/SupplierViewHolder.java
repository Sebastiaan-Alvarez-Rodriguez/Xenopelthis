package com.sebastiaan.xenopelthis.ui.supplier.view.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

public class SupplierViewHolder extends ViewHolder<supplier> {
    public static final @LayoutRes int layoutResource = R.layout.supplier_list_item;

    private TextView supplierName, supplierCity, supplierPostalCode, supplierStreet, supplierHouseNumber, supplierSite, supplierEmail, supplierPhone;
    private ImageButton expandDetailButton;
    private RelativeLayout detailView;

    public SupplierViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }


    public SupplierViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;

        findViews();
        detailView.setVisibility(View.GONE);
        setupButton();

        setupClicks();
    }

    private void findViews() {
        supplierName = itemView.findViewById(R.id.supplier_list_name);
        expandDetailButton = itemView.findViewById(R.id.supplier_list_expand_collapse_view);
        supplierCity = itemView.findViewById(R.id.supplier_list_detail_city);
        supplierPostalCode = itemView.findViewById(R.id.supplier_list_detail_postal_code);
        supplierStreet = itemView.findViewById(R.id.supplier_list_detail_street);
        supplierHouseNumber = itemView.findViewById(R.id.supplier_list_detail_housenumber);
        supplierSite = itemView.findViewById(R.id.supplier_list_detail_site);
        supplierEmail = itemView.findViewById(R.id.supplier_list_detail_email);
        supplierPhone = itemView.findViewById(R.id.supplier_list_detail_phone);

        detailView = itemView.findViewById(R.id.supplier_list_detail_view);

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

    public void set(supplier supplier) {
        supplierName.setText(supplier.getName());
        supplierCity.setText(supplier.getCity());
        supplierPostalCode.setText(supplier.getPostalcode());
        supplierStreet.setText(supplier.getStreetname());
        supplierHouseNumber.setText(supplier.getHousenumber());
        supplierSite.setText(supplier.getWebaddress());
        supplierEmail.setText(supplier.getEmailaddress());
        supplierPhone.setText(supplier.getPhonenumber());
    }
}