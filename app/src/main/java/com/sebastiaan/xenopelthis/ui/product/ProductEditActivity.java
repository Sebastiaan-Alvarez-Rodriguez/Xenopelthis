package com.sebastiaan.xenopelthis.ui.product;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.supplier.SupplierViewModel;
import com.sebastiaan.xenopelthis.ui.supplier.view.SupplierAdapterCheckable;

import java.util.ArrayList;

//TODO: WIP: Make MVVM for supplier list. Make something to have checkable adapter functionality (new class)
public class ProductEditActivity extends AppCompatActivity {
    private EditText name, description;
    private RecyclerView supplierlist;

    private SupplierAdapterCheckable adapter;
    private SupplierViewModel model;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(SupplierViewModel.class);
        setContentView(R.layout.activity_product_edit);
        findGlobalViews();
        prepareList();
        setupActionBar();
    }


    private void prepareList() {
        adapter = new SupplierAdapterCheckable();
        model.getAll().observe(this, adapter);
        supplierlist.setLayoutManager(new LinearLayoutManager(this));
        supplierlist.setAdapter(adapter);
        supplierlist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
            actionbar.setTitle("Edit");
        }
    }


    private ProductStruct getProduct() {
        return new ProductStruct(name.getText().toString(), description.getText().toString());
    }

    private boolean checkInput(ProductStruct p) {
        if (p.name.isEmpty() || p.description.isEmpty() || adapter.getSelectedCount() == 0) {
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

        if (adapter.hasSelected()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.product_edit_layout),"Give at least 1 supplier", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
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
                    Intent result = new Intent();
                    result.putExtra("result-product", p);
                    result.putExtra("result-relations", new ArrayList<>(adapter.getSelectedIDs())); //TODO: This is serializable -> Slow. Improve
                    setResult(RESULT_OK);
                    finish();
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
