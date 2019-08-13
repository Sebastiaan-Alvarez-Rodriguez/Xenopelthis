package com.sebastiaan.xenopelthis.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.view.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.stream.Collectors;

public class InventoryFragment extends com.sebastiaan.xenopelthis.ui.templates.Fragment<ProductAndAmount> implements ActionListener<ProductAndAmount> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
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
    protected void prepareFAB(View view, boolean actionMode) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (actionMode) {
            fab.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(ProductAndAmount::toInventoryItem).map(inventory_item::getProductID).collect(Collectors.toList())));
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        } else {
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), InventoryAddActivity.class);
                startActivityForResult(intent, REQ_ADD);
            });
            fab.setImageResource(android.R.drawable.ic_menu_add);
        }
    }

    @Override
    public void onClick(ProductAndAmount i) {
        Intent intent = new Intent(getContext(), InventoryEditActivity.class);
        intent.putExtra("product", new ProductStruct(i.getP()));
        intent.putExtra("product-id", i.getP().getId());
        intent.putExtra("amount", i.getAmount());
        startActivityForResult(intent, REQ_UPDATE);
    }

}
