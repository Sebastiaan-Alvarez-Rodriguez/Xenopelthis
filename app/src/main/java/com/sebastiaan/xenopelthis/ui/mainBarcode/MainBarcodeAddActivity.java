package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.OnClickListener;

import java.util.List;
import java.util.stream.Collectors;

public class MainBarcodeAddActivity extends AppCompatActivity implements ActionListener<product>, OnClickListener<product> {
    private SearchView search;
    private ImageButton sort;
    private RecyclerView list;
    private FloatingActionButton fab;

    private String barcodeString;

    private AdapterAction adapter;
    private BarcodeViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");
        setContentView(R.layout.activity_barcode_add);
        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);

        findGlobalViews();
        setupActionBar();
        prepareList();
        prepareFAB();
        prepareSort();
        prepareSearch();
    }

    private void findGlobalViews() {
        search = findViewById(R.id.searchview);
        sort = findViewById(R.id.sort_button);
        list = findViewById(R.id.list);
        fab = findViewById(R.id.fab);
    }

    private void setupActionBar() {
        Toolbar myToolbar;
        myToolbar = findViewById(R.id.barcode_add_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose Products for Barcode");
        }

    }

    private void prepareList() {
        fab.hide();
        fab.setImageResource(R.drawable.ic_arrow_right);

        adapter = new AdapterAction(this);

        if (barcodeString != null) {
            model.getUnassignedForBarcodeLive(barcodeString).observe(this, adapter);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            list.setAdapter(adapter);
        }
    }

    private void prepareFAB() {
        fab.setOnClickListener(v -> {
            List<Long> productIds = adapter.getSelected().stream().map(product::getId).collect(Collectors.toList());
            for (Long item : productIds)
                model.add(new BarcodeStruct(barcodeString), item);
            finish();
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(product product) { }

    @Override
    public boolean onLongClick(product product) { return false; }

    @Override
    public void onActionModeChange(boolean actionMode) {
        if (actionMode)
            fab.show();
        else
            fab.hide();
    }
}
