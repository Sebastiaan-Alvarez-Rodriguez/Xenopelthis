package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.db.retrieve.constant.SupplierConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.AdapterCheckable;

import java.util.ArrayList;
import java.util.List;

public class ProductEditRelationActivity extends AppCompatActivity  {
    private TextView text;
    private RecyclerView list;

    private AdapterCheckable adapter;
    private RelationViewModel relationModel;

    private List<supplier> editOldSuppliers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
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
        RelationConstant relationConstant = new RelationConstant(this);
        SupplierConstant supplierConstant = new SupplierConstant(this);
        supplierConstant.getAll( totalList -> {
            adapter.add(totalList);
            relationConstant.getSuppliersForProduct(clickedID, supplierlist -> {
                adapter.setSelected(supplierlist);
                list.setLayoutManager(new LinearLayoutManager(this));
                list.setAdapter(adapter);
                list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                editOldSuppliers = supplierlist;
            });

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

                Log.e("OOF", "Save happened");
                Log.e("OOF", "Old:");
                if (editOldSuppliers != null)
                    for (supplier s : editOldSuppliers)
                        Log.e("OOF", "    "+s.getName());
                Log.e("OOF", "New:");
                if (editOldSuppliers != null)
                    for (supplier s : selectedSuppliers)
                        Log.e("OOF", "    "+s.getName());

                if (!selectedSuppliers.equals(editOldSuppliers)) {
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
