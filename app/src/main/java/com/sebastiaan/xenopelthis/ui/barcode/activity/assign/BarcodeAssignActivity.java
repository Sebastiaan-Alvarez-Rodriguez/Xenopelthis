package com.sebastiaan.xenopelthis.ui.barcode.activity.assign;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.BaseActivity;
import com.sebastiaan.xenopelthis.ui.barcode.activity.main.BarcodeMainActivity;
import com.sebastiaan.xenopelthis.ui.barcode.activity.select.BarcodeSelectActivity;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterCheckable;

import java.util.List;

public class BarcodeAssignActivity extends BaseActivity {

    private SearchView search;
    private RecyclerView list;
    private ImageView add, sort;

    private String barcodeString;

    private AdapterCheckable adapter;
    private BarcodeAssignViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");
        setContentView(R.layout.activity_barcode_assign);
        model = ViewModelProviders.of(this).get(BarcodeAssignViewModel.class);

        findGlobalViews();
        setupActionBar();
        prepareList();
        prepareAdd();
        prepareSort();
        prepareSearch();
    }

    private void findGlobalViews() {
        search = findViewById(R.id.searchview);
        sort = findViewById(R.id.sort_button);
        list = findViewById(R.id.list);
        add = findViewById(R.id.add);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_assign_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionbar.setTitle(R.string.barcode_assign_activity_title);
        }
    }

    private void prepareList() {
        adapter = new AdapterCheckable(null);
        if (barcodeString != null) {
            model.getUnassignedForBarcodeLive(barcodeString).observe(this, adapter);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            list.setAdapter(adapter);
        }
    }

    private void prepareAdd() {
        add.setVisibility(View.INVISIBLE);
    }

    private void prepareSort() {
        sort.setOnClickListener(v -> adapter.sort(adapter.getSortStrategy() == Adapter.SortBy.NAME ? Adapter.SortBy.DATE : Adapter.SortBy.NAME));
    }

    private void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new com.sebastiaan.xenopelthis.ui.templates.search.Searcher.EventListener<product>() {
            @NonNull
            @Override
            public List<product> onBeginSearch() {
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<product> initial) {
                adapter.replaceAll(initial);
            }

            @Override
            public void onReceiveFilteredContent(List<product> filtered) {
                adapter.replaceAll(filtered);
            }
        }));
    }

    private void store() {
        for (product p : adapter.getSelected())
            model.add(new BarcodeStruct(barcodeString), p.getId());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                switch (adapter.getSelectedCount()) {
                    case 0:
                        Snackbar.make(list.getRootView(), R.string.barcode_assign_activity_selector_zero, Snackbar.LENGTH_LONG).show();
                        break;
                    case 1:
                        store();
                        Intent intent = new Intent(this, BarcodeMainActivity.class);
                        product p = adapter.getSelected().iterator().next();
                        p.setHasBarcode(true);
                        intent.putExtra("product", new ProductStruct(p));
                        intent.putExtra("product-id", p.getId());
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                    default:
                        store();
                        Intent multiIntent = new Intent(this, BarcodeSelectActivity.class);
                        startActivity(multiIntent);
                        finish();
                        break;
                }
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

