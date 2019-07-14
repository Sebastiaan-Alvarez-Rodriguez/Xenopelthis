package com.sebastiaan.xenopelthis.ui.supplier;

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
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.ui.product.view.ProductAdapterCheckable;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;

import java.util.ArrayList;
import java.util.List;

public class SupplierEditRelationActivity extends AppCompatActivity implements Observer<List<product>> {
    private TextView text;
    private RecyclerView list;

    private ProductViewModel model;
    private RelationViewModel relationModel;

    private ProductAdapterCheckable adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        relationModel = ViewModelProviders.of(this).get(RelationViewModel.class);
        findGlobalViews();
        prepareList();
        text.setText("Products for this supplier:");
        setupActionBar();
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        list = findViewById(R.id.relation_edit_list);
    }

    void prepareList() {
        adapter = new ProductAdapterCheckable();
        model.getAll().observe(this, adapter);
        //TODO: if mode == edit
//        relationmodel.getAllForSupplier(edit_id).observe(this, this);

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
            return false;
        }
        return true;
    }

    void showEmptyErrors() {
        Snackbar.make(findViewById(R.id.relation_edit_layout), "Please select at least 1 item", Snackbar.LENGTH_LONG).show();
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
                    SupplierStruct s = data.getParcelableExtra("result-supplier");
                    relationModel.addSupplierWithProducts(s, ids);
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
    public void onChanged(@Nullable List<product> products) {
        adapter.setSelectedProducts(products);
    }
}
