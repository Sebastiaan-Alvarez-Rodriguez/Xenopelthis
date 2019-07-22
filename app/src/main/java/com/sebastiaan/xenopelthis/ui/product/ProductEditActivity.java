package com.sebastiaan.xenopelthis.ui.product;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.ProductConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.product.view.dialog.OverrideListener;

public class ProductEditActivity extends AppCompatActivity {
    private static final int REQ_BARCODE = 0;

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
                //TODO: Could ask user whether he wants to override the conflicting item... Is that user-friendly?
                // In code we just need to give a "product-id" of conflicting item to next activity for override
                Snackbar.make(findViewById(R.id.product_edit_layout), "'"+p.name+"' is already in use", Snackbar.LENGTH_LONG).show();
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
            startActivityForResult(next, REQ_BARCODE);
        });
    }

    private void checkEdit(ProductStruct p) {
        Intent intent = getIntent();
        ProductStruct clickedProduct = intent.getParcelableExtra("product");
        long clickedID = intent.getLongExtra("product-id", -42);



        if (p.name.equals(clickedProduct.name)) {
            model.update(p, clickedID);
            Log.e("Checker", "Situation: edit and name did not change -> OK.");
            Intent next = new Intent(this, ProductEditBarcodeActivity.class);
            next.putExtra("product-id", clickedID);
            startActivityForResult(next, REQ_BARCODE);
        } else {
            ProductConstant checker = new ProductConstant(this);
            checker.isUnique(p.name, conflictProduct -> {
                if (conflictProduct != null) {
                    Log.e("Checker", "Situation: edit and name changed but taken. 'This name is already taken'.");
                    //TODO: Could ask user whether he wants to override the conflicting item... Is that user-friendly?
                    // In code we need to give a "product-id" of conflicting item to next activity for override
                    // AND we must somehow delete the existing item being edited in the database
                    Snackbar.make(findViewById(R.id.product_edit_layout), "'"+p.name+"' is already in use", Snackbar.LENGTH_LONG).show();
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
    }

    private void showOverrideDialog(ProductStruct p, long conflictID, OverrideListener overrideListener) {
        runOnUiThread(() -> {
            OverrideDialog dialog = new OverrideDialog(this);
            dialog.showDialog(p, conflictID, () -> {
                model.delete(p, conflictID);
                overrideListener.onOverride();
            });
        });
    }

    private void showEmptyErrors(ProductStruct p) {
        if (p.name.isEmpty())
            name.setError("This field must be filled");

        if (p.description.isEmpty())
            description.setError("This field must be filled");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK ) {
                setResult(RESULT_OK, data);
                finish();
        }
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
