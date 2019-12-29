package com.sebastiaan.xenopelthis.ui.product.activity.edit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.activity.barcode.ProductEditBarcodeActivity;
import com.sebastiaan.xenopelthis.ui.product.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideListener;

public class ProductEditActivity extends AppCompatActivity {

    private EditText name, description;

    private ProductEditViewModel model;
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
        model = ViewModelProviders.of(this).get(ProductEditViewModel.class);
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
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            if (editMode)
                actionbar.setTitle(R.string.product_edit_activity_actionbar_edit);
            else
                actionbar.setTitle(R.string.product_edit_activity_actionbar_add);
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
            if (conflictProduct != null)
                showOverrideDialog(new ProductStruct(conflictProduct), conflictProduct.getId(), () -> insertNew(p));
            else
                insertNew(p);
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
            updateExisting(p, clickedID);
        } else {
            ProductConstant checker = new ProductConstant(this);
            checker.isUnique(p.name, conflictProduct -> {
                if (conflictProduct != null)
                    showOverrideDialog(new ProductStruct(conflictProduct), conflictProduct.getId(), () -> updateExisting(p, clickedID));
                else
                    updateExisting(p, clickedID);
            });
        }
    }

    private void updateExisting(ProductStruct p, long id) {
        model.update(p, id, x -> {
            Intent next = new Intent(this, ProductEditBarcodeActivity.class);
            next.putExtra("product-id", id);
            startActivity(next);
            finish();
        });
    }

    private void showOverrideDialog(ProductStruct p, long conflictID, OverrideListener overrideListener) {
        runOnUiThread(() -> {
            OverrideDialog dialog = new OverrideDialog(this);
            dialog.showDialog(p, conflictID, () -> model.delete(p, conflictID, nothing -> overrideListener.onOverride()));
        });
    }

    private void showEmptyErrors(ProductStruct p) {
        if (p.name.isEmpty())
            name.setError(getString(R.string.product_edit_activity_empty));
        if (p.description.isEmpty())
            description.setError(getString(R.string.product_edit_activity_empty));
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
