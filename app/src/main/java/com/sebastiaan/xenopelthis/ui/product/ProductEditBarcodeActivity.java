package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.barcode.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.barcode.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;

public class ProductEditBarcodeActivity extends AppCompatActivity implements ActionListener<barcode> {
    private final static int REQ_BARCODE = 1;
    private ImageButton scanButton, addButton;
    private TextView translation;
    private FloatingActionButton actionDeleteButton;
    private RecyclerView list;

    private List<barcode> editOldBarcodes;

    private AdapterAction adapter;

    private BarcodeConstant barcodeConstant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_edit);
        barcodeConstant = new BarcodeConstant(this);

        findGlobalViews();
        setupButtons();
        setupActionBar();

        Intent intent = getIntent();
        prepareList(intent.getLongExtra("product-id", -42));
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        addButton = findViewById(R.id.barcode_edit_add);
        translation = findViewById(R.id.barcode_edit_translation);
        actionDeleteButton = findViewById(R.id.barcode_edit_action_delete);
        list = findViewById(R.id.barcode_edit_list);
    }

    void prepareList(long productID) {
        barcodeConstant = new BarcodeConstant(this);
        barcodeConstant.getAllForProduct(productID, barcodeList -> {
            adapter = new AdapterAction(this);
            adapter.onChanged(barcodeList);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            editOldBarcodes = barcodeList;
        });
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });
        addButton.setOnClickListener(v -> {
            BarcodeStruct barcodeStruct = getBarcode();
            if (adapter.getItems().stream().map(barcode::getTranslation).anyMatch(s -> barcodeStruct.translation.equals(s))) {
                Toast.makeText(v.getContext(), "Item already in list", Toast.LENGTH_SHORT).show();
                translation.setText("");
            } else {
                Intent intent = getIntent();
                long id = intent.getLongExtra("product-id", -42);
                barcodeConstant.isUnique(barcodeStruct.translation, id, unique -> {
                    if (unique) {
                        adapter.add(barcodeStruct.toBarcode(id));
                        translation.setText("");
                    } else {
                        showConflictDialog(barcodeStruct, id);
                    }
                });

            }
        });

        actionDeleteButton.setOnClickListener(v -> {
            Log.e("OOOF", "CLicked delete. Deleting items: "+adapter.getSelectedCount());
            adapter.remove(adapter.getSelected());
        });
        actionDeleteButton.hide();
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Barcodes");
        }
    }

    private BarcodeStruct getBarcode() {
        return new BarcodeStruct(translation.getText().toString());
    }

    private void showConflictDialog(BarcodeStruct barcode, long conflictID) {
        runOnUiThread(() -> {
            OverrideDialog dialog = new OverrideDialog(this, barcode.translation);
            dialog.showDialog(barcode, conflictID, () -> {
                adapter.add(barcode.toBarcode(conflictID));
                translation.setText("");
            });
        });
    }
    private void store() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("product-id", -42);

        List<barcode> items = adapter.getItems();

        barcodeConstant.updateList(editOldBarcodes, items, id);
        Intent next = new Intent(this, ProductEditRelationActivity.class);
        next.putExtra("product-id", id);
        startActivity(next);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                store();
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