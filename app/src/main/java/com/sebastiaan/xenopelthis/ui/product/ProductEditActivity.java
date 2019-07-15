package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

//TODO: WIP: Make MVVM for supplier list. Make something to have checkable adapter functionality (new class)
public class ProductEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private EditText name, description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
    }

    private void findGlobalViews() {
        name = findViewById(R.id.product_edit_name);
        description = findViewById(R.id.product_edit_description);
    }

    private void setupGlobalViews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ProductStruct product = extras.getParcelable("product");
            if (product != null) {
                name.setText(product.name);
                description.setText(product.description);
            }
        }
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.product_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Edit");
        }
    }

    private ProductStruct getProduct() {
        return new ProductStruct(name.getText().toString(), description.getText().toString());
    }

    private boolean checkInput(ProductStruct p) {
        if (p.name.isEmpty() || p.description.isEmpty()) {
            showEmptyErrors(p);
            return false;
        }
        return true;
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
        Log.e("Edit", "ProductEditActivity receives");
        switch (requestCode) {
            case REQ_RELATIONS:
                if (resultCode == RESULT_OK && data != null && data.hasExtra("result-product") && data.hasExtra("result-relations")) {
                    setResult(RESULT_OK, data);
                    Log.e("Edit", "ProductEditActivity is done with success");
                    finish();
                }
                break;
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
                if (checkInput(p)) {
                    Intent next = new Intent(this, ProductEditRelationActivity.class);
                    next.putExtra("result-product", p);
                    Bundle extras = getIntent().getExtras();
                    next.putExtra("mode_edit", (extras != null));
                    if (extras != null)
                        next.putExtra("product_id", extras.getString("product_id"));
                    startActivityForResult(next, REQ_RELATIONS);
                }
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
