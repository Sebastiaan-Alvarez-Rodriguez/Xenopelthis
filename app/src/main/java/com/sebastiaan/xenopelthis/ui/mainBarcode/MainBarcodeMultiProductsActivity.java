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

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.stream.Collectors;

public class MainBarcodeMultiProductsActivity extends AppCompatActivity implements ActionListener<product> {
    private final static int REQ_ADD = 0, REQ_BARCODE = 1;
    private ImageButton scanButton;
    private Button assignButton, unassignButton;
    private RecyclerView list;

    private AdapterAction adapter;
    private BarcodeConstant barcodeConstant;
    private BarcodeViewModel model;

    private String barcodeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_multiproducts);
        barcodeConstant = new BarcodeConstant(this);
        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");
        findGlobalViews();
        prepareList();
        setupButtons();
        setupActionBar();
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        assignButton = findViewById(R.id.barcode_btn_assign);
        unassignButton = findViewById(R.id.barcode_btn_unassign);
        list = findViewById(R.id.list);
    }

    private void prepareList() {
        if (barcodeString == null || barcodeString.isEmpty())
            return;

        adapter = new AdapterAction(this) {
            @Override
            public void onClick(View view, int pos) { onLongClick(view, pos); }
        };

        model.getForBarcodeLive(barcodeString).observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });

        assignButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainBarcodeAddActivity.class);
            intent.putExtra("barcode", barcodeString);
            startActivityForResult(intent, REQ_ADD);
        });

        unassignButton.setOnClickListener(v -> {
            List<Long> productIds = adapter.getSelected().stream().map(product::getId).collect(Collectors.toList());
            barcodeConstant.deleteBarcodeForProducts(productIds, barcodeString);
            ProductViewModel productModel = ViewModelProviders.of(this).get(ProductViewModel.class);
            for (Long id : productIds) {
                barcodeConstant.getAllForProduct(id, barcodes -> {
                    if (barcodes.isEmpty())
                        productModel.setHasBarcode(false, id);
                });
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
                        intent = new Intent(this, MainBarcodeSingleProductActivity.class);
                        intent.putExtra("barcode", barcodeString);
                        barcodeConstant.getProducts(barcodeString, products -> {
                            intent.putExtra("product", new ProductStruct(products.get(0)));
                            intent.putExtra("product-id", products.get(0).getId());
                            startActivity(intent);
                            finish();
                        });
                        break;
                }
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
    public void onActionModeChange(boolean actionMode) { }

    @Override
    public void onClick(product product) { }

    @Override
    public boolean onLongClick(product product) {
        return true;
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            barcodeString = data.getStringExtra("barcode");
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
                    intent = new Intent(this, MainBarcodeSingleProductActivity.class);
                    intent.putExtra("barcode", barcodeString);
                    barcodeConstant.getProducts(barcodeString, products -> {
                        intent.putExtra("product", new ProductStruct(products.get(0)));
                        intent.putExtra("product-id", products.get(0).getId());
                        startActivity(intent);
                        finish();
                    });
                    break;
                default: break;
            }
        });
        prepareList();
    }
}
