package com.sebastiaan.xenopelthis.ui.supplier;

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
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.SupplierViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.stream.Collectors;

public class SupplierFragment extends com.sebastiaan.xenopelthis.ui.templates.Fragment<supplier> implements ActionListener<supplier> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(SupplierViewModel.class);
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
            fab.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(supplier::getId).collect(Collectors.toList())));
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        } else {
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SupplierEditActivity.class);
                startActivityForResult(intent, REQ_ADD);
            });
            fab.setImageResource(android.R.drawable.ic_menu_add);
        }
    }

    @Override
    public void onClick(supplier s) {
        Log.e("Click", "Supplier with name '" + s.getName() + "' was clicked!");
        Intent intent = new Intent(getContext(), SupplierEditActivity.class);
        SupplierStruct supplier = new SupplierStruct(s);
        intent.putExtra("supplier", supplier);
        intent.putExtra("supplier-id", (Long)s.getId());
        startActivityForResult(intent, REQ_UPDATE);
    }

}
