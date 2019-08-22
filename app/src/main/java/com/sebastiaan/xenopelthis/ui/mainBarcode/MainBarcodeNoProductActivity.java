package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

public class MainBarcodeNoProductActivity extends AppCompatActivity {
    private final static int REQ_ADD = 0, REQ_BARCODE = 1;
    private ImageButton scanButton;
    private Button assignButton;

    private String barcodeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_noproduct);

        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");

        findGlobalViews();
        setupButtons();
        setupActionBar();
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        assignButton = findViewById(R.id.barcode_btn_assign);
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
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode"))
            barcodeString = data.getStringExtra("barcode");

        BarcodeConstant barcodeConstant = new BarcodeConstant(this);
        barcodeConstant.getForBarcodeCount(barcodeString, count -> {
            Intent intent;
            switch (count) {
                case 0: break;
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
