package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;

public class InventoryAddActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private Spinner productName;
    private EditText amount;

    private InventoryViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
        setContentView(R.layout.activity_inventory_add);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
    }

    private void findGlobalViews() {
        productName = findViewById(R.id.inventory_add_productName);
        amount = findViewById(R.id.inventory_add_amount);
    }

    private void setupGlobalViews() {
        model.getUnusedNames(items -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            productName.setAdapter(adapter);
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar;
        myToolbar = findViewById(R.id.inventory_add_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Add");
        }
    }

    private void checkInput() {
        long set_amount = Long.valueOf(amount.getText().toString());
        //TODO: empty errors (e.g. no available products)
        if (set_amount < 0)
            amount.setError("Amount cannot be negative");
        else
            insertNew(set_amount);

    }

    private void insertNew(long amount) {
        model.findByName(productName.getSelectedItem().toString(), product -> {
            model.add(new inventory_item(product.getId(), amount));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                checkInput();
                finish();
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
