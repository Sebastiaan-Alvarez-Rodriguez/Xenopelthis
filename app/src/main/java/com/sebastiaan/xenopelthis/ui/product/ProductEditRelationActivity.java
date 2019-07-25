package com.sebastiaan.xenopelthis.ui.product;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.SupplierViewModel;
import com.sebastiaan.xenopelthis.ui.supplier.view.AdapterCheckable;

import java.util.ArrayList;
import java.util.List;

public class ProductEditRelationActivity extends AppCompatActivity  {
    private TextView text;
    private RecyclerView list;

    private AdapterCheckable adapter;
    private SupplierViewModel model;
    private RelationViewModel relationModel;

    private List<supplier> editOldSuppliers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        model = ViewModelProviders.of(this).get(SupplierViewModel.class);
        relationModel = ViewModelProviders.of(this).get(RelationViewModel.class);
        findGlobalViews();
        text.setText("Suppliers for this product:");
        setupActionBar();

        Intent intent = getIntent();
        prepareListEdit(intent.getLongExtra("product-id", -42));
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        list = findViewById(R.id.relation_edit_list);
    }

    private void prepareListEdit(long clickedID) {
        adapter = new AdapterCheckable();
        model.getAll().observe(this, adapter);

        RelationConstant relationConstant = new RelationConstant(this);
        relationConstant.getSuppliersForProduct(clickedID, supplierlist -> {
            adapter.setSelected(supplierlist);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            editOldSuppliers = supplierlist;
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.relation_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Relations");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_done:
                ArrayList<supplier> selectedSuppliers = new ArrayList<>(adapter.getSelected());
                Intent data = getIntent();

                if (editOldSuppliers != selectedSuppliers) {
                    long editID = data.getLongExtra("product-id", -42);
                    relationModel.updateProductWithSuppliers(editID, editOldSuppliers, selectedSuppliers);
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
