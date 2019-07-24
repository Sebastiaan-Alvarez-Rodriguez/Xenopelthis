package com.sebastiaan.xenopelthis.ui.inventory;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.inventory.view.ActionListener;
import com.sebastiaan.xenopelthis.ui.inventory.view.InventoryAdapterAction;

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(inventory_item i) {

    }

    @Override
    public boolean onLongClick(inventory_item i) {
        return true;
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        prepareFAB(getView(), actionMode);
    }

}
