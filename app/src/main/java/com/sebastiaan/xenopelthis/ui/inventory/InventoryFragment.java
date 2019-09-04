package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.activity.add.InventoryAddActivity;
import com.sebastiaan.xenopelthis.ui.inventory.activity.edit.InventoryEditActivity;
import com.sebastiaan.xenopelthis.ui.inventory.search.Searcher;
import com.sebastiaan.xenopelthis.ui.inventory.view.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.Fragment;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryFragment extends Fragment<ProductAndAmount> implements ActionListener<ProductAndAmount> {
    private InventoryFragmentViewModel model;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(InventoryFragmentViewModel.class);
    }


    @Override
    protected void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new com.sebastiaan.xenopelthis.ui.templates.search.Searcher.EventListener<ProductAndAmount>() {
            @NonNull
            @Override
            public List<ProductAndAmount> onBeginSearch() {
                add.setVisibility(View.INVISIBLE);
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<ProductAndAmount> initial) {
                adapter.replaceAll(initial);
                add.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceiveFilteredContent(List<ProductAndAmount> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    @Override
    protected void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        adapter = new AdapterAction(this);
        model.getAllLive().observe(this, adapter);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void prepareAdd(View view, boolean actionMode) {
        //TODO: should not be delete by id I think? Also, can same product be made by 2 or more suppliers? Then, inventory must be redone
        if (actionMode)
            add.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(ProductAndAmount::toInventoryItem).map(inventory_item::getProductID).collect(Collectors.toList())));
        else
            add.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), InventoryAddActivity.class), REQ_ADD));
        super.prepareAdd(view, actionMode);
    }

    @Override
    public void onClick(ProductAndAmount i) {
        if (!adapter.isActionMode()) {
            Intent intent = new Intent(getContext(), InventoryEditActivity.class);
            intent.putExtra("product", new ProductStruct(i.getP()));
            intent.putExtra("product-id", i.getP().getId());
            startActivityForResult(intent, REQ_UPDATE);
        }
    }

}
