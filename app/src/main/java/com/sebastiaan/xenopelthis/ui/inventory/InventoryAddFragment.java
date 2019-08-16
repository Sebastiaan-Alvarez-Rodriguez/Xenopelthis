package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;

import java.util.List;


public class InventoryAddFragment extends Fragment implements ActionListener<product> {
    private int REQ_ADD = 0;

    private SearchView search;
    private ImageButton sort;
    private RecyclerView list;

    private AdapterAction adapter;
    private InventoryViewModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.searchview);
        sort = view.findViewById(R.id.sort_button);
        list = view.findViewById(R.id.list);

        prepareList(view);
        prepareSearch();
        prepareSort();
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

    private void prepareSort() {
        sort.setOnClickListener(v -> adapter.sort(adapter.getSortStrategy() == Adapter.SortBy.NAME ? Adapter.SortBy.DATE : Adapter.SortBy.NAME));
    }

    private void prepareList(View view) {
        list = view.findViewById(R.id.list);

        adapter = new AdapterAction(this);
        model.getUnusedLive().observe(this, adapter);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    private void insertNew(product p) {
        model.add(new inventory_item(p.getId(), 0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActionModeChange(boolean actionMode) { }

    @Override
    public void onClick(product p) {
        long defaultAmount = 0;
        insertNew(p);
        Intent intent = new Intent(getContext(), InventoryEditActivity.class);
        intent.putExtra("product", new ProductStruct(p));
        intent.putExtra("product-id", p.getId());
        intent.putExtra("amount", defaultAmount);
        startActivityForResult(intent, REQ_ADD);
    }

    @Override
    public boolean onLongClick(product p) { return true; }
}
