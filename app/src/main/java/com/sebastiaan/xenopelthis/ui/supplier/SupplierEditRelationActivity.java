package com.sebastiaan.xenopelthis.ui.supplier;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.product.view.ProductAdapterCheckable;

import java.util.ArrayList;
import java.util.List;

public class SupplierEditRelationActivity extends AppCompatActivity {
    private TextView text;
    private RecyclerView list;

    private ProductViewModel model;
    private RelationViewModel relationModel;

    private ProductAdapterCheckable adapter;

    private boolean editMode = false;
    private List<product> editOldProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        relationModel = ViewModelProviders.of(this).get(RelationViewModel.class);
        findGlobalViews();
        text.setText("Products for this supplier:");


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("supplier-id") && intent.hasExtra("result-supplier")) {
            editMode = true;
            prepareListEdit(intent.getLongExtra("supplier-id", -42));
        } else {
            prepareList();
        }

        setupActionBar();
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        list = findViewById(R.id.relation_edit_list);
    }

    void prepareList() {
        adapter = new ProductAdapterCheckable();
        model.getAll().observe(this, adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    void prepareListEdit(long clickedID) {
        RelationConstant relationConstant = new RelationConstant(this);
        relationConstant.getProductsForSupplier(clickedID, productlist -> {
            adapter = new ProductAdapterCheckable(productlist);
            model.getAll().observe(this, adapter);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            editOldProducts = productlist;
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.relation_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            if (editMode)
                actionbar.setTitle("Edit");
            else
                actionbar.setTitle("Select");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_done:
                ArrayList<product> selectedProducts = new ArrayList<>(adapter.getSelected());
                Intent data = getIntent();

                SupplierStruct s = data.getParcelableExtra("result-supplier");
                if (editMode) {
                    long editID = data.getLongExtra("supplier-id", -42);
                    relationModel.updateSupplierWithProducts(s, editID, editOldProducts, selectedProducts);
                } else {
                    relationModel.addSupplierWithProducts(s, selectedProducts);
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_ok, menu);
        return true;
    }

}
