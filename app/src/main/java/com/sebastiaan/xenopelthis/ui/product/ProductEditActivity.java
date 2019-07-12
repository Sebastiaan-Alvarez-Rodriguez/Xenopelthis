package com.sebastiaan.xenopelthis.ui.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.ui.supplier.view.SupplierAdapter;

//TODO: WIP: Make MVVM for supplier list. Make something to have checkable adapter functionality (new class)
public class ProductEditActivity extends AppCompatActivity {
    private EditText name, description;
    private RecyclerView supplierlist;

    private SupplierAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        findGlobalViews();
        prepareList();
        setupActionBar();
    }


    private void prepareList() {
//        if (enabledList != null) {
//            supplierAdapter = new SupplierAdapter();
//        } else {
//            supplierAdapter = new supplierAdapterCheckable(list, null, this);
//        }
//        supplierlist.setLayoutManager(new LinearLayoutManager(this));
//        supplierlist.setAdapter(supplierAdapter);
//        supplierlist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void findGlobalViews() {
        name = findViewById(R.id.product_edit_name);
        description = findViewById(R.id.product_edit_description);
        supplierlist = findViewById(R.id.product_edit_supplierslist);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.product_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Edit"); //TODO: use string resource
        }
    }


    private ProductStruct getProduct() {
        return new ProductStruct(name.getText().toString(), description.getText().toString());
    }

    private boolean checkInput(ProductStruct p) {
        if (p.name.isEmpty() || p.description.isEmpty()) { //|| adapter.getSelectedCount() == 0) {
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

//        if (supplierAdapter.getSelectedCount() == 0) {
//            Snackbar snackbar = Snackbar.make(findViewById(R.id.product_edit_layout),"Give at least 1 supplier", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_done:
                ProductStruct p = getProduct();
                if (checkInput(p)) {
                    //TODO: store product with relations
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }
}
