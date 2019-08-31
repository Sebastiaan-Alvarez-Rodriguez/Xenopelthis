package com.sebastiaan.xenopelthis.ui.barcode;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;

public class assignActivity extends AppCompatActivity implements ActionListener<product> {

    private SearchView search;
    private ImageButton sort;
    private RecyclerView list;
    private FloatingActionButton fab;

    private AdapterAction adapter;

    private BarcodeViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        search = findViewById(R.id.searchview);
        sort = findViewById(R.id.sort_button);
        list = findViewById(R.id.list);
        fab = findViewById(R.id.fab);

        model = ViewModelProviders.of(this).get(BarcodeViewModel.class);

        prepareList();
        prepareFAB(false);
        prepareSearch();
        prepareSort();
    }

    protected void prepareSort() {
        sort.setOnClickListener(v -> adapter.sort(adapter.getSortStrategy() == Adapter.SortBy.NAME ? Adapter.SortBy.DATE : Adapter.SortBy.NAME));
    }

    private void prepareList() {
        adapter = new AdapterAction(this);
        model.getAllLive().observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new Searcher.EventListener<product>() {
            @NonNull
            @Override
            public List<product> onBeginSearch() {
                fab.hide();
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<product> initial) {
                adapter.replaceAll(initial);
                fab.show();
            }

            @Override
            public void onReceiveFilteredContent(List<product> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    @Override
    public void onActionModeChange(boolean actionMode) {

    }

    @Override
    public void onClick(product product) {

    }

    @Override
    public boolean onLongClick(product product) {
        return false;
    }
}
