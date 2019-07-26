package com.sebastiaan.xenopelthis.ui.supplier;

import android.content.Intent;
import android.os.Bundle;
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
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.ProductConstant;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.RelationViewModel;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterCheckable;

import java.util.ArrayList;
import java.util.List;

public class SupplierEditRelationActivity extends AppCompatActivity {
    private TextView text;
    private RecyclerView list;

    private RelationViewModel relationModel;
    private AdapterCheckable adapter;

    private List<product> editOldProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        relationModel = ViewModelProviders.of(this).get(RelationViewModel.class);
        findGlobalViews();
        text.setText("Products for this supplier:");
        setupActionBar();

        Intent intent = getIntent();
        prepareListEdit(intent.getLongExtra("supplier-id", -42));
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        list = findViewById(R.id.relation_edit_list);
    }

    void prepareListEdit(long clickedID) {
        RelationConstant relationConstant = new RelationConstant(this);
        ProductConstant productConstant = new ProductConstant(this);
        productConstant.getAll(productList -> {
            adapter.add(productList);
            relationConstant.getProductsForSupplier(clickedID, productlist -> {
                adapter = new AdapterCheckable(productlist);
                list.setLayoutManager(new LinearLayoutManager(this));
                list.setAdapter(adapter);
                list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                editOldProducts = productlist;
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
                ArrayList<product> selectedProducts = new ArrayList<>(adapter.getSelected());
                Intent data = getIntent();

                if (!selectedProducts.equals(editOldProducts) ) {
                    long editID = data.getLongExtra("supplier-id", -42);
                    relationModel.updateSupplierWithProducts(editID, editOldProducts, selectedProducts);
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
