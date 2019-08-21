package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

public class MainBarcodeMultiProductsActivity extends AppCompatActivity implements ActionListener<product> {
    private final static int REQ_BARCODE = 1;
    private ImageButton scanButton;
    private Button assignButton, unassignButton;
    private RecyclerView list;

    private String barcodeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_multiproducts);
        findGlobalViews();
        setupButtons();
        setupActionBar();
        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");
        prepareList();
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        assignButton = findViewById(R.id.barcode_btn_assign);
        unassignButton = findViewById(R.id.barcode_btn_unassign);
        list = findViewById(R.id.list);
    }

    @Override
    public void onActionModeChange(boolean actionMode) {

    }

    @Override
    public void onClick(product product) {

    }

    @Override
    public boolean onLongClick(product product) {
        return false;
    }
}
