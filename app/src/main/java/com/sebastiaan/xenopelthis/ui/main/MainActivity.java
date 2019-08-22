package com.sebastiaan.xenopelthis.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.mainBarcode.MainBarcodeMultiProductsActivity;
import com.sebastiaan.xenopelthis.ui.mainBarcode.MainBarcodeNoProductActivity;
import com.sebastiaan.xenopelthis.ui.mainBarcode.MainBarcodeSingleProductActivity;

public class MainActivity extends AppCompatActivity {
    private final static int REQ_BARCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.main_tabs);
        tabs.setupWithViewPager(viewPager);
        setupActionBar();
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setTitle("Xenopelthis");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main_menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.main_menu_barcode:
                intent = new Intent(this, Recognitron.class);
                startActivityForResult(intent, REQ_BARCODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.main_menu_settings);
        Drawable icon = item.getIcon();
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        item.setIcon(icon);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            String barcodeString = data.getStringExtra("barcode");
            BarcodeConstant barcodeConstant = new BarcodeConstant(this);
            barcodeConstant.getForBarcodeCount(barcodeString, count -> {
                Intent intent;
                switch (count) {
                    case 0:
                        intent = new Intent(this, MainBarcodeNoProductActivity.class);
                        intent.putExtra("barcode", barcodeString);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(this, MainBarcodeSingleProductActivity.class);
                        intent.putExtra("barcode", barcodeString);
                        barcodeConstant.getProducts(barcodeString, products -> {
                            intent.putExtra("product", new ProductStruct(products.get(0)));
                            intent.putExtra("product-id", products.get(0).getId());
                            startActivity(intent);
                        });
                        break;
                    default:
                        intent = new Intent(this, MainBarcodeMultiProductsActivity.class);
                        intent.putExtra("barcode", barcodeString);
                        startActivity(intent);
                        break;
                }
            });
        }
    }
}