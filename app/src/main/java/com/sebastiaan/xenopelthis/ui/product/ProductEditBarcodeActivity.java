package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.barcode.view.ActionListener;
import com.sebastiaan.xenopelthis.ui.barcode.view.BarcodeAdapterAction;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;

public class ProductEditBarcodeActivity extends AppCompatActivity implements ActionListener {
    private final static int REQ_BARCODE = 1, REQ_CONTINUE = 2;
    private ImageButton scanButton, addButton;
    private TextView text;
    private FloatingActionButton actionDeleteButton;
    private RecyclerView list;

    private boolean editMode = false;
    private List<barcode> editOldBarcodes;

    private BarcodeAdapterAction adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_edit);
        findGlobalViews();
        setupButtons();
        setupActionBar();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product-id") && intent.hasExtra("result-product")) {
            editMode = true;
            prepareListEdit(intent.getLongExtra("product-id", -42));
        } else {
            prepareList();
        }
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        addButton = findViewById(R.id.barcode_edit_add);
        text = findViewById(R.id.barcode_edit_translation);
        actionDeleteButton = findViewById(R.id.barcode_edit_action_delete);
        list = findViewById(R.id.barcode_edit_list);
    }

    void prepareList() {
        adapter = new BarcodeAdapterAction(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    void prepareListEdit(long clickedID) {
        BarcodeConstant barcodeConstant = new BarcodeConstant(this);
        barcodeConstant.getAllForProduct(clickedID, barcodeList -> {
            adapter = new BarcodeAdapterAction(barcodeList, this);
            adapter = new BarcodeAdapterAction();
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            editOldBarcodes = barcodeList;
        });
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            //TODO: Take stream of pictures, use firebase to detect barcode... in other activity with callback/intent to here
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });
        addButton.setOnClickListener(v -> {
            BarcodeStruct barcodeStruct = getBarcode();
            //TODO: Check if string has correct format

            if (adapter.add(barcodeStruct.toBarcode())) {
                text.setText("");
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
            if (editMode)
                actionbar.setTitle("Edit");
            else
                actionbar.setTitle("Select");
        }
    }

    private BarcodeStruct getBarcode() {
        return new BarcodeStruct(text.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                //TODO: Store all barcodes in list
                Intent intent = getIntent();
                Intent next = new Intent(this, ProductEditRelationActivity.class);
                if (editMode) {
                    Long id = intent.getLongExtra("product-id", -42);
                    next.putExtra("product-id", id);
                }
                ProductStruct p = intent.getParcelableExtra("result-product");
                next.putExtra("result-product", p);
                startActivityForResult(next, REQ_CONTINUE);
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
        Log.e("OOOF", "Acton mode changed to: "+actionMode);
        //TODO: Decide between lower 2 thtings
//        ((View) actionDeleteButton).setVisibility(actionMode ? View.VISIBLE : View.GONE);
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
}