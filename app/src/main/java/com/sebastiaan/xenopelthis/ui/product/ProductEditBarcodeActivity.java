package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.barcode.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.barcode.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

public class ProductEditBarcodeActivity extends AppCompatActivity implements ActionListener<barcode> {
    private final static int REQ_BARCODE = 1;
    private ImageButton scanButton, addButton;
    private TextView translation;
    private FloatingActionButton actionDeleteButton;
    private RecyclerView list;

    private AdapterAction adapter;

    private BarcodeViewModel model;

    private long productID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_barcode_edit);

        findGlobalViews();
        setupButtons();
        setupActionBar();

        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        Intent intent = getIntent();
        productID = intent.getLongExtra("product-id", -42);
        prepareList();
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        addButton = findViewById(R.id.barcode_edit_add);
        translation = findViewById(R.id.barcode_edit_translation);
        actionDeleteButton = findViewById(R.id.barcode_edit_action_delete);
        list = findViewById(R.id.barcode_edit_list);
    }

    void prepareList() {
        adapter = new AdapterAction(this);
        model.getForProductLive(productID).observe(this, adapter);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });

        addButton.setOnClickListener(v -> {
            BarcodeStruct barcodeStruct = getBarcode();

            model.constantQuery().isUnique(barcodeStruct.translation, unique -> {
                if (unique) {
                    model.add(barcodeStruct, productID);
                    translation.setText("");
                } else {
                    model.constantQuery().isUnique(barcodeStruct.translation, productID, onlySelfContains -> {
                        if (onlySelfContains) {
                            Toast.makeText(v.getContext(), "Item already in list", Toast.LENGTH_SHORT).show();
                            translation.setText("");
                        } else {
                            runOnUiThread(() -> showConflictDialog(barcodeStruct));
                        }
                    });
                }
            });
        });

        actionDeleteButton.setOnClickListener(v -> {
            Log.e("OOOF", "CLicked delete. Deleting items: "+adapter.getSelectedCount());
            model.delete(adapter.getSelected());
        });
        actionDeleteButton.hide();
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionbar.setTitle("Barcodes");
        }
    }

    private BarcodeStruct getBarcode() {
        return new BarcodeStruct(translation.getText().toString());
    }

    private void showConflictDialog(BarcodeStruct barcode) {
        OverrideDialog dialog = new OverrideDialog(this, barcode.translation);
        dialog.showDialog(barcode, productID, () -> {
            model.add(barcode, productID);
            translation.setText("");
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                Intent next = new Intent(this, ProductEditRelationActivity.class);
                next.putExtra("product-id", productID);
                startActivity(next);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_next, menu);
        return true;
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        if (actionMode)
            actionDeleteButton.show();
        else
            actionDeleteButton.hide();
    }

    @Override
    public void onClick(barcode b) {
    }

    @Override
    public boolean onLongClick(barcode b) {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("BARR", "Received barcode result");
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            Log.e("BARR", "result OK");
            String barcodeString = data.getStringExtra("barcode");
            translation.setText(barcodeString);
        }
    }
}