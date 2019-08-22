package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.InventoryEditActivity;

import java.util.Collections;

public class MainBarcodeSingleProductActivity extends AppCompatActivity  {
    private final static int REQ_ADD = 0, REQ_BARCODE = 1;

    private ImageButton scanButton, expandButton;
    private Button assignButton, unassignButton, addToInvButton;
    private TextView productNameView, productDescriptionView;
    private ImageView productHasBarcode;
    private RelativeLayout detailview;

    private BarcodeConstant barcodeConstant;
    private BarcodeViewModel model;

    private product product;
    private String barcodeString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeConstant = new BarcodeConstant(this);
        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        setContentView(R.layout.activity_barcode_singleproduct);
        Intent intent = getIntent();
        if (intent.hasExtra("barcode") && intent.hasExtra("product") && intent.hasExtra("product-id")) {
            barcodeString = intent.getStringExtra("barcode");
            ProductStruct productStruct = intent.getParcelableExtra("product");
            product = productStruct.toProduct(intent.getLongExtra("product-id", -42));
        }

        findGlobalViews();
        detailview.setVisibility(View.GONE);

        prepareViews();
        setupButtons();
        setupActionBar();
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        assignButton = findViewById(R.id.barcode_btn_assign);
        unassignButton = findViewById(R.id.barcode_btn_unassign);
        addToInvButton = findViewById(R.id.barcode_btn_addToInv);
        productNameView = findViewById(R.id.barcode_product_name);
        productDescriptionView = findViewById(R.id.barcode_detail_description);
        productHasBarcode = findViewById(R.id.barcode_product_has_barcode);
        detailview = findViewById(R.id.barcode_detailview);
        expandButton = findViewById(R.id.barcode_expand_collapse);
    }

    private void prepareViews() {
        productNameView.setText(product.getName());
        productDescriptionView.setText(product.getProductDescription());
        if (product.getHasBarcode())
            productHasBarcode.setBackgroundResource(R.drawable.ic_barcode_ok);
    }

    private void setupButtons() {
        expandButton.setOnClickListener(v -> {
            if (detailview.getVisibility() == View.GONE) {
                expandButton.setBackgroundResource(R.drawable.ic_arrow_up);
                detailview.setVisibility(View.VISIBLE);
            } else {
                expandButton.setBackgroundResource(R.drawable.ic_arrow_down);
                detailview.setVisibility(View.GONE);
            }
        });

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });

        assignButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainBarcodeAddActivity.class);
            intent.putExtra("barcode", barcodeString);
            startActivityForResult(intent, REQ_ADD);
        });

        long productID = product.getId();
        unassignButton.setOnClickListener(v -> {
            barcodeConstant.deleteBarcodeForProducts(Collections.singletonList(productID), barcodeString);
            ProductViewModel productModel = ViewModelProviders.of(this).get(ProductViewModel.class);
            barcodeConstant.getAllForProduct(productID, barcodes -> {
                if (barcodes.isEmpty())
                    productModel.setHasBarcode(false, productID);
            });
            Intent intent = new Intent(this, MainBarcodeNoProductActivity.class);
            intent.putExtra("barcode", barcodeString);
            startActivity(intent);
            finish();
        });

        addToInvButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryEditActivity.class);
            InventoryViewModel inventoryModel = ViewModelProviders.of(this).get(InventoryViewModel.class);
            inventoryModel.get(productID, p -> {
                if (p == null) {
                    inventoryModel.add(new inventory_item(productID, 0L));
                    intent.putExtra("amount", 0L);
                } else {
                    intent.putExtra("amount", p.getAmount());
                }

                intent.putExtra("product", new ProductStruct(product));
                intent.putExtra("product-id", productID);
                startActivity(intent);
            });
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionBar.setTitle("Barcode");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            barcodeString = data.getStringExtra("barcode");
            prepareViews();
        }

        barcodeConstant.getForBarcodeCount(barcodeString, count -> {
            Intent intent;
            switch (count) {
                case 0:
                    intent = new Intent(this, MainBarcodeNoProductActivity.class);
                    intent.putExtra("barcode", barcodeString);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    barcodeConstant.getProducts(barcodeString, barcodeProduct -> {
                        product = barcodeProduct.get(0);
                        prepareViews();
                    });
                    break;
                default:
                    intent = new Intent(this, MainBarcodeMultiProductsActivity.class);
                    intent.putExtra("barcode", barcodeString);
                    startActivity(intent);
                    finish();
                    break;
            }

        });
    }
}
