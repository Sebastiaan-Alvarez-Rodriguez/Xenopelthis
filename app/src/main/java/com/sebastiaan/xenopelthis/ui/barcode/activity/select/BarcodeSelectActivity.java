package com.sebastiaan.xenopelthis.ui.barcode.activity.select;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;

public class BarcodeSelectActivity extends AppCompatActivity implements ActionListener<product> {
    private ImageView sort, delete;
    private SearchView search;
    private RecyclerView list;

    private AdapterAction adapter;
    private String barcodeString;

    private BarcodeSelectViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_multiproducts);

        Intent intent = getIntent();
        barcodeString = intent.getStringExtra("barcode");

        model = ViewModelProviders.of(this).get(BarcodeSelectViewModel.class);

        findGlobalViews();
        prepareList();
        setupActionBar();
        prepareSearch();
        prepareSort();
        prepareDelete();
    }

    private void findGlobalViews() {
        search = findViewById(R.id.searchview);
        sort = findViewById(R.id.sort_button);
        list = findViewById(R.id.list);
        delete = findViewById(R.id.add);
    }

    void prepareList() {
        adapter = new AdapterAction(this);
        model.getForBarcodeLive(barcodeString).observe(this, adapter);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_multi_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionbar.setTitle("Select a product");
        }
    }

    private void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new Searcher.EventListener<product>() {
            @NonNull
            @Override
            public List<product> onBeginSearch() {
                delete.setVisibility(View.INVISIBLE);
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<product> initial) {
                adapter.replaceAll(initial);
                delete.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceiveFilteredContent(List<product> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    void prepareSort() {
        sort.setOnClickListener(v -> adapter.sort(adapter.getSortStrategy() == Adapter.SortBy.NAME ? Adapter.SortBy.DATE : Adapter.SortBy.NAME));
    }

    private void prepareDelete() {
        delete.setImageResource(R.drawable.ic_delete);
        delete.setOnClickListener(v -> {
            final BarcodeStruct b = new BarcodeStruct(barcodeString);
            for (product p : adapter.getSelected()) {
                model.delete(b, p.getId());
            }
        });
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        delete.setVisibility(actionMode ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(product product) {
        if (!adapter.isActionMode()) {
            //TODO: go to main activity for clicked product
        }
    }

    @Override
    public boolean onLongClick(product product) {
        onClick(product);
        return false;
    }
}
