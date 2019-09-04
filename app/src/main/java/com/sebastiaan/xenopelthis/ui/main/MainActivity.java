package com.sebastiaan.xenopelthis.ui.main;

import android.content.Intent;
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
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.barcode.activity.assign.BarcodeAssignActivity;
import com.sebastiaan.xenopelthis.ui.barcode.activity.main.BarcodeMainActivity;
import com.sebastiaan.xenopelthis.ui.barcode.activity.select.BarcodeSelectActivity;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

public class MainActivity extends AppCompatActivity {
    private final static int REQ_BARCODE = 1;
    private BarcodeViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            String barcodeString = data.getStringExtra("barcode");

            model.constantQuery().getProductsCount(barcodeString, count -> {
                if (count == 0) {
                    Intent intent = new Intent(this, BarcodeAssignActivity.class);
                    intent.putExtra("barcode", barcodeString);
                    startActivity(intent);
                } else if (count == 1) {
                    model.constantQuery().getProducts(barcodeString, products -> {
                        Intent intent = new Intent(this, BarcodeMainActivity.class);
                        intent.putExtra("product", new ProductStruct(products.get(0)));
                        intent.putExtra("product-id", products.get(0).getId());
                        intent.putExtra("barcode", barcodeString);
                        startActivity(intent);
                    });
                } else {
                    Intent intent = new Intent(this, BarcodeSelectActivity.class);
                    intent.putExtra("barcode", barcodeString);
                    startActivity(intent);
                }
            });
        }
    }
}