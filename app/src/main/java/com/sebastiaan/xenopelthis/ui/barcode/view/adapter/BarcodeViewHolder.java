package com.sebastiaan.xenopelthis.ui.barcode.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.ui.templates.adapter.InternalClickListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ViewHolder;

@SuppressWarnings("WeakerAccess")
public class BarcodeViewHolder extends ViewHolder<barcode> {
    public static final @LayoutRes int layoutResource = R.layout.barcode_list_item;
    private TextView translation;

    public BarcodeViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    public BarcodeViewHolder(@NonNull View itemView, InternalClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        findViews();
        setupClicks();
    }

    /**
     * @see View#findViewById(int)
     */
    private void findViews() {
        translation = itemView.findViewById(R.id.barcode_list_translation);
    }

    /**
     * @see TextView#setText(CharSequence)
     */
    @Override
    public void set(barcode b) {
        translation.setText(b.getTranslation());
    }

    private void setupClicks() {
        if (clickListener == null)
            return;
        itemView.setOnClickListener(v -> clickListener.onClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> clickListener.onLongClick(v, getAdapterPosition()));
    }
}
