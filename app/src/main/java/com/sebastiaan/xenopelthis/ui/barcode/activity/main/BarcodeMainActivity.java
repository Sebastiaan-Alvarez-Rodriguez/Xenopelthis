package com.sebastiaan.xenopelthis.ui.barcode.activity.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.activity.edit.InventoryEditActivity;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.ProductViewHolder;

public class BarcodeMainActivity extends AppCompatActivity {

    private ViewStub viewStub;
    private Button toInventoryButton, unassignButton;

    private product p;
    private String barcodeString;

    private BarcodeMainViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        long productID = intent.getLongExtra("product-id", -42);
        ProductStruct productStruct = intent.getParcelableExtra("product");
        p = productStruct.toProduct(productID);
        barcodeString = intent.getStringExtra("barcode");

        model = ViewModelProviders.of(this).get(BarcodeMainViewModel.class);
        setContentView(R.layout.activity_barcode_singleproduct);
        findGlobalViews();
        setupActionBar();
        inflateViews();
        setupButtons();
    }

    private void findGlobalViews() {
        viewStub = findViewById(R.id.barcode_inflate_view);
        toInventoryButton = findViewById(R.id.barcode_main_to_inventory);
        unassignButton = findViewById(R.id.barcode_main_unassign);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_main_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionbar.setTitle("What to do?");
        }
    }
    private void inflateViews() {
        viewStub.setLayoutResource(R.layout.product_list_item);
        ProductViewHolder productViewHolder = new ProductViewHolder(viewStub.inflate());
        productViewHolder.set(p);
    }

    private void setupButtons() {
        toInventoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryEditActivity.class);
            intent.putExtra("product", new ProductStruct(p));
            intent.putExtra("product-id", p.getId());
            intent.putExtra("perhaps-new", true);
            startActivity(intent);
            finish();
        });
        unassignButton.setOnClickListener(v -> {
            //TODO: Maybe make confirm button before delete?

            model.delete(new BarcodeStruct(barcodeString), p.getId());
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
