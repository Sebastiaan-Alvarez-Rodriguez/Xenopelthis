package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;

public class InventoryEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private TextView productName;
    private EditText amount;

    private boolean editMode = false;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_inventory_edit);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
    }

    private void findGlobalViews() {
        productName = findViewById(R.id.inventory_edit_productName);
        amount = findViewById(R.id.inventory_edit_amount);
    }

    //TODO: implement
    private void setupGlobalViews() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product") && intent.hasExtra("amount")) {
            editMode = true;

        }

    }

    //TODO: implement
    private void setupActionBar() {

    }
}
