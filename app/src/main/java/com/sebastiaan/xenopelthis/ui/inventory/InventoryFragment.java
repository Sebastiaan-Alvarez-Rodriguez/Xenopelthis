package com.sebastiaan.xenopelthis.ui.inventory;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndID;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.inventory.view.ActionListener;
import com.sebastiaan.xenopelthis.ui.inventory.view.InventoryAdapterAction;

import java.util.stream.Collectors;

public class InventoryFragment extends Fragment implements ActionListener {
    private InventoryViewModel model;
    private static final int REQ_ADD = 0, REQ_UPDATE = 1;

    private InventoryAdapterAction adapter;


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
        prepareList(view);
        prepareFAB(view, false);
    }

    void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        adapter = new InventoryAdapterAction(this);
        model.getAll().observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    //TODO: implement
    void prepareFAB(View view, boolean actionMode) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (actionMode) {
            fab.setOnClickListener(v -> {
                model.deleteByID(adapter.getSelected().stream().map(inventory_item::getProductID).collect(Collectors.toList()));
            });
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        } else {
            fab.setOnClickListener(v -> {
                //Intent intent = new Intent(v.getContext(), )
                //startActivityForResult(intent, REQ_ADD);
            });
            fab.setImageResource(android.R.drawable.ic_menu_add);
        }

    }

    //TODO: implement
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //TODO: implement
    @Override
    public void onClick(ProductAndID i) {

    }

    //TODO: implement
    @Override
    public boolean onLongClick(ProductAndID i) {
        return true;
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        prepareFAB(getView(), actionMode);
    }

}
