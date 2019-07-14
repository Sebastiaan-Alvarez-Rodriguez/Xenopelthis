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
import com.sebastiaan.xenopelthis.db.retrieve.constant.ProductConstant;
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
        setupActionBar();
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
            actionbar.setTitle("Edit");
        }
    }

    private ProductStruct getProduct() {
        return new ProductStruct(name.getText().toString(), description.getText().toString());
    }

    private void checkInput(ProductStruct p) {
        if (p.name.isEmpty() || p.description.isEmpty()) {
            showEmptyErrors(p);
        } else {
            ProductConstant checker = new ProductConstant(this);
            checker.isUnique(p.name, unique -> {
                if (!unique) {
                    Log.e("Checker", "This name is already taken.");
                    //TODO: In situation new and edit-but-changed-name, show user some sort of popup window,
                    // asking them whether they want to override item which has the name currently set.
                    // This is a complex situation (especially when edit-but-changed-name happened), so think thrice!
                } else {
                    //Unique name chosen. Very good
                    Intent next = new Intent(this, ProductEditRelationActivity.class);
                    next.putExtra("result-product", p);
                    startActivityForResult(next, REQ_RELATIONS);
                }
            });
        }
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
        if (requestCode == REQ_RELATIONS && resultCode == RESULT_OK ) {
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
