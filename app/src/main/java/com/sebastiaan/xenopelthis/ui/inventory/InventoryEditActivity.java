package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;


//TODO: change productName to a selector OR set all new products automatically to zero amount
//TODO: fix that adding works
public class InventoryEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private TextView productName;
    private EditText amount;
    private long productID;

    private InventoryViewModel model;
    private boolean editMode = false;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_edit);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
    }

    private void findGlobalViews() {
        productName = findViewById(R.id.inventory_edit_productName);
        amount = findViewById(R.id.inventory_edit_amount);
    }

    private void setupGlobalViews() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            editMode = true;
            ProductAndAmount clickedProduct = intent.getParcelableExtra("product");
            productID = clickedProduct.getP().getId();
            productName.setText(clickedProduct.getP().getName());
            amount.setText(String.valueOf(clickedProduct.getAmount()));
        }
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.inventory_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            if (editMode)
                actionbar.setTitle("Edit");
            else
                actionbar.setTitle("Add");
        }
    }

    private inventory_item getInput() {
        return new inventory_item(productID, Long.valueOf(amount.getText().toString()));
    }

    private void checkInput(inventory_item item) {
        if (item.getAmount() < 0) {
            amount.setError("Amount cannot be negative");
        } else {
            if (editMode)
                updateExisting(item);
            else
                insertNew(item);
        }
    }

    private void insertNew(inventory_item item) {
        model.add(item);
    }

    //TODO: for edit
    private void updateExisting(inventory_item item) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                inventory_item p = getInput();
                checkInput(p);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_RELATIONS && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_next, menu);
        return true;
    }
}
