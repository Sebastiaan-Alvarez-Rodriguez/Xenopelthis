package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.ProductConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideListener;

public class ProductEditActivity extends AppCompatActivity {

    private EditText name, description;

    private ProductViewModel model;
    private boolean editMode = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        findGlobalViews();
        
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product-id") && intent.hasExtra("product")) {
            editMode = true;
            ProductStruct clickedProduct = intent.getParcelableExtra("product");
            name.setText(clickedProduct.name);
            description.setText(clickedProduct.description);
        }

        setupActionBar();
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
    }

    private void findGlobalViews() {
        name = findViewById(R.id.product_edit_name);
        description = findViewById(R.id.product_edit_description);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.product_edit_toolbar);
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

    private ProductStruct getProduct() {
        return new ProductStruct(name.getText().toString(), description.getText().toString());
    }

    private void checkInput(ProductStruct p) {
        if (p.name.isEmpty() || p.description.isEmpty()) {
            showEmptyErrors(p);
        } else {
            if (editMode)
                checkEdit(p);
            else
                checkNew(p);
        }
    }
    
    private void checkNew(ProductStruct p) {
        ProductConstant checker = new ProductConstant(this);
        checker.isUnique(p.name, conflictProduct -> {
            if (conflictProduct != null) {
                Log.e("Checker", "Situation: new but taken. 'This name is already taken'.");
                showOverrideDialog(new ProductStruct(conflictProduct), conflictProduct.getId(), () -> insertNew(p));
            } else {
                Log.e("Checker", "Situation: new and unique -> OK.");
                insertNew(p);
            }
        });
    }

    private void insertNew(ProductStruct p) {
        model.add(p, assignedID -> {
            Intent next = new Intent(this, ProductEditBarcodeActivity.class);
            next.putExtra("product-id", assignedID);
            startActivity(next);
            finish();
        });
    }

    private void checkEdit(ProductStruct p) {
        Intent intent = getIntent();
        ProductStruct clickedProduct = intent.getParcelableExtra("product");
        long clickedID = intent.getLongExtra("product-id", -42);

        if (p.name.equals(clickedProduct.name)) {
            Log.e("Checker", "Situation: edit and name did not change -> OK.");
            updateExisting(p, clickedID);
        } else {
            ProductConstant checker = new ProductConstant(this);
            checker.isUnique(p.name, conflictProduct -> {
                if (conflictProduct != null) {
                    Log.e("Checker", "Situation: edit and name changed but taken. 'This name is already taken'.");
                    showOverrideDialog(new ProductStruct(conflictProduct), conflictProduct.getId(), () -> updateExisting(p, clickedID));
                } else {
                    Log.e("Checker", "Situation: edit and name changed and unique -> OK.");
                    updateExisting(p, clickedID);
                }
            });
        }
    }

    private void updateExisting(ProductStruct p, long id) {
        model.update(p, id);
        Intent next = new Intent(this, ProductEditBarcodeActivity.class);
        next.putExtra("product-id", id);
        startActivity(next);
        finish();
    }

    private void showOverrideDialog(ProductStruct p, long conflictID, OverrideListener overrideListener) {
        runOnUiThread(() -> {
            OverrideDialog dialog = new OverrideDialog(this);
            dialog.showDialog(p, conflictID, () -> model.delete(p, conflictID, nothing -> overrideListener.onOverride()));
        });
    }

    private void showEmptyErrors(ProductStruct p) {
        if (p.name.isEmpty())
            name.setError("This field must be filled");

        if (p.description.isEmpty())
            description.setError("This field must be filled");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                ProductStruct p = getProduct();
                checkInput(p);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_next, menu);
        return true;
    }
}
