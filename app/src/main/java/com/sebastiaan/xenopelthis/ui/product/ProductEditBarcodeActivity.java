package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
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

    private BarcodeViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_edit);
        findGlobalViews();
        setupButtons();
        setupActionBar();

        Intent intent = getIntent();
        prepareList(intent.getLongExtra("product-id", -42));

        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        addButton = findViewById(R.id.barcode_edit_add);
        translation = findViewById(R.id.barcode_edit_translation);
        actionDeleteButton = findViewById(R.id.barcode_edit_action_delete);
        list = findViewById(R.id.barcode_edit_list);
    }

    void prepareList(long productID) {
        BarcodeConstant barcodeConstant = new BarcodeConstant(this);
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
            //TODO: Check if string has correct format?

            Intent intent = getIntent();
            long id = intent.getLongExtra("product-id", -42);
            if (adapter.add(barcodeStruct.toBarcode(id))) {
                translation.setText("");
                Snackbar.make(findViewById(R.id.barcode_edit_layout), "Translation added!", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.barcode_edit_layout), "This translation already is in the list", Snackbar.LENGTH_LONG).show();
            }
        });

        actionDeleteButton.setOnClickListener(v -> adapter.remove(adapter.getSelected()));
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

    private void store() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("product-id", -42);

        BarcodeConstant constant = new BarcodeConstant(this);
        List<barcode> selected = adapter.getItems();

        constant.isUnique(selected, id, conflictList -> {
            if (conflictList.isEmpty()) {
                model.updateList(editOldBarcodes, selected, id);
                Intent next = new Intent(this, ProductEditRelationActivity.class);
                next.putExtra("product-id", id);
                startActivity(next);
                finish();
            } else {
                for (barcode b : conflictList) {
                    OverrideDialog dialog = new OverrideDialog(this);
                    //TODO: Does this work? Could not work if update fails to change productID or... small mistakes
                    dialog.showDialog(new BarcodeStruct(b), id, () -> model.update(new BarcodeStruct(b), id));
                }
            }
        });
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
        Log.e("BARR", "Received barcode result");
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            Log.e("BARR", "result OK");
            String barcodeString = data.getStringExtra("barcode");
            translation.setText(barcodeString);
        }
    }
}