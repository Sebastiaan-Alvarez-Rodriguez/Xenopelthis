package com.sebastiaan.xenopelthis.ui.product;

import android.arch.lifecycle.Observer;
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
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.SupplierViewModel;
import com.sebastiaan.xenopelthis.ui.supplier.view.SupplierAdapterCheckable;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductEditRelationActivity extends AppCompatActivity implements Observer<List<supplier>> {
    private TextView text;
    private RecyclerView list;

    private SupplierAdapterCheckable adapter;
    private SupplierViewModel model;
    private RelationViewModel relationModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        model = ViewModelProviders.of(this).get(SupplierViewModel.class);
        relationModel = ViewModelProviders.of(this).get(RelationViewModel.class);
        findGlobalViews();
        prepareList();
        text.setText("Suppliers for this product:");
        setupActionBar();
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        list = findViewById(R.id.relation_edit_list);
    }

    void prepareList() {
        adapter = new SupplierAdapterCheckable();
        model.getAll().observe(this, adapter);
        //TODO: if mode == edit
//        relationModel.getAllForProduct(edit_id).observe(this, this);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.relation_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Edit");
        }
    }

    boolean checkInput(ArrayList<Long> ids) {
        if (ids.isEmpty()) {
            showEmptyErrors();
            return true;
        }
        return false;
    }

    void showEmptyErrors() {
        Snackbar.make(findViewById(R.id.relation_edit_layout), "Please select at least 1 item", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_done:
                ArrayList<Long> ids = new ArrayList<>(adapter.getSelectedIDs());
                Intent data = getIntent();
                if (checkInput(ids)) {
                    ProductStruct p = data.getParcelableExtra("result-product");
                    relationModel.addProductWithSuppliers(p, ids);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_ok, menu);
        return true;
    }

    @Override
    public void onChanged(@Nullable List<supplier> suppliers) {
        adapter.setSelectedSuppliers(suppliers);
    }
}
