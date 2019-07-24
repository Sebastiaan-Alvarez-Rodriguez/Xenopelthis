package com.sebastiaan.xenopelthis.ui.product;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.barcode.view.ActionListener;
import com.sebastiaan.xenopelthis.ui.barcode.view.BarcodeAdapterAction;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

import java.util.List;

public class ProductEditBarcodeActivity extends AppCompatActivity implements ActionListener {
    private final static int REQ_BARCODE = 1;
    private ImageButton scanButton, addButton;
    private TextView text;
    private FloatingActionButton actionDeleteButton;
    private RecyclerView list;

    private List<barcode> editOldBarcodes;

    private BarcodeAdapterAction adapter;

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
        text = findViewById(R.id.barcode_edit_translation);
        actionDeleteButton = findViewById(R.id.barcode_edit_action_delete);
        list = findViewById(R.id.barcode_edit_list);
    }

    void prepareList(long productID) {
        BarcodeConstant barcodeConstant = new BarcodeConstant(this);
        barcodeConstant.getAllForProduct(productID, barcodeList -> {
            adapter = new BarcodeAdapterAction(barcodeList, this);
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

            Intent intent = getIntent();
            long id = intent.getLongExtra("product-id", -42);
            if (adapter.add(barcodeStruct.toBarcode(id))) {
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
            actionbar.setTitle("Barcodes");
        }
    }

    private BarcodeStruct getBarcode() {
        return new BarcodeStruct(text.getText().toString());
    }

    private void store() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("product-id", -42);

        BarcodeConstant constant = new BarcodeConstant(this);
        List<barcode> selected = adapter.getItems();

        constant.isUnique(selected, id, conflictList -> {
            if (conflictList.isEmpty()) {
                model.update(editOldBarcodes, selected, id);

                Intent next = new Intent(this, ProductEditRelationActivity.class);
                next.putExtra("product-id", id);
                startActivity(next);
            } else {
                //TODO: Notify user of conflicts
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
        Log.e("OOOF", "Acton mode changed to: "+actionMode);
        //TODO: Decide between lower 2 thtings
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
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK) {
            //TODO: Get barcodestring from intent and place in edittext
        }
    }
}