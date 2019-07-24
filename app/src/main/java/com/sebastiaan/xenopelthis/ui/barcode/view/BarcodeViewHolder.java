package com.sebastiaan.xenopelthis.ui.barcode.view;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;

class BarcodeViewHolder extends RecyclerView.ViewHolder {
    static final @LayoutRes int layoutResource = R.layout.barcode_list_item;

    private TextView translation;

    private InternalClickListener clickListener;


    BarcodeViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    BarcodeViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        findViews();
        setupClicks();
    }

    private void findViews() {
        translation = itemView.findViewById(R.id.barcode_list_translation);
    }

    void set(barcode b) {
        translation.setText(b.getTranslation());
    }

    private void setupClicks() {
        if (clickListener == null)
            return;
        itemView.setOnClickListener(v -> clickListener.onClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> clickListener.onLongClick(v, getAdapterPosition()));
    }
}
