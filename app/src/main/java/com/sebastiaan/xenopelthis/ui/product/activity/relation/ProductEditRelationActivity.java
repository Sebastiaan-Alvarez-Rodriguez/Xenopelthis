package com.sebastiaan.xenopelthis.ui.product.activity.relation;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.constant.RelationConstant;
import com.sebastiaan.xenopelthis.db.retrieve.constant.SupplierConstant;
import com.sebastiaan.xenopelthis.ui.supplier.search.Searcher;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.AdapterCheckable;

import java.util.ArrayList;
import java.util.List;

public class ProductEditRelationActivity extends AppCompatActivity  {
    private TextView text;
    private SearchView search;
    private RecyclerView list;

    private AdapterCheckable adapter;
    private ProductEditRelationViewModel model;

    private List<supplier> editOldSuppliers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_edit);
        model = ViewModelProviders.of(this).get(ProductEditRelationViewModel.class);
        findGlobalViews();
        text.setText("Suppliers for this product:");
        setupActionBar();

        Intent intent = getIntent();
        prepareListEdit(intent.getLongExtra("product-id", -42));
        prepareSearch();
    }

    private void findGlobalViews() {
        text = findViewById(R.id.relation_edit_text);
        search = findViewById(R.id.relation_edit_searchview);
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

    private void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new Searcher.EventListener<supplier>() {
            @NonNull
            @Override
            public List<supplier> onBeginSearch() {
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<supplier> initial) {
                adapter.replaceAll(initial);
            }

            @Override
            public void onReceiveFilteredContent(List<supplier> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.relation_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
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

                if (!selectedSuppliers.equals(editOldSuppliers)) {
                    long editID = data.getLongExtra("product-id", -42);
                    model.updateProductWithSuppliers(editID, editOldSuppliers, selectedSuppliers);
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
