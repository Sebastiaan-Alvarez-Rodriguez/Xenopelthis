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
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;


//TODO: finish
public class InventoryEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private TextView productNameEdit;
    private EditText amount;
    private long productID;

    private InventoryViewModel model;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
        //TODO: throw error if intent has no extra?
        if (intent != null && intent.hasExtra("product")) {
            setContentView(R.layout.activity_inventory_edit);
            findGlobalViews();
            setupGlobalViews();
        }
        setupActionBar();
    }

    private void findGlobalViews() {
        productNameEdit = findViewById(R.id.inventory_edit_productName);
        amount = findViewById(R.id.inventory_edit_amount);
    }

    private void setupGlobalViews() {
        Intent intent = getIntent();
        ProductAndAmount clickedProduct = intent.getParcelableExtra("product");
        productID = clickedProduct.getP().getId();
        productNameEdit.setText(clickedProduct.getP().getName());
        amount.setText(String.valueOf(clickedProduct.getAmount()));
    }

    private void setupActionBar() {
        Toolbar myToolbar;
        myToolbar = findViewById(R.id.inventory_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Edit");
        }
    }

    private inventory_item getInput() {
        return new inventory_item(productID, Long.valueOf(amount.getText().toString()));
    }

    private void checkInput(inventory_item item) {
        //TODO: empty errors
        if (item.getAmount() < 0)
            amount.setError("Amount cannot be negative");
        else
            updateExisting(item);

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
