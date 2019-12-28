package com.sebastiaan.xenopelthis.ui.supplier;

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
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.supplier.activity.edit.SupplierEditActivity;
import com.sebastiaan.xenopelthis.ui.supplier.search.Searcher;
import com.sebastiaan.xenopelthis.ui.supplier.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.Fragment;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierFragment extends Fragment<supplier> implements ActionListener<supplier> {
    private SupplierFragmentViewModel model;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(SupplierFragmentViewModel.class);
    }

    @Override
    protected void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new Searcher.EventListener<supplier>() {
            @NonNull
            @Override
            public List<supplier> onBeginSearch() {
                add.setVisibility(View.INVISIBLE);
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<supplier> initial) {
                adapter.replaceAll(initial);
                add.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceiveFilteredContent(List<supplier> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    @Override
    protected void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        adapter = new AdapterAction(this);
        model.getAll().observe(this, adapter);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void prepareAdd(View view, boolean actionMode) {
        if (actionMode)
            add.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(supplier::getId).collect(Collectors.toList())));
        else
            add.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), SupplierEditActivity.class), REQ_ADD));
        super.prepareAdd(view, actionMode);
    }

    @Override
    public void onClick(supplier s) {
        if (!adapter.isActionMode()) {
            Intent intent = new Intent(getContext(), SupplierEditActivity.class);
            SupplierStruct supplier = new SupplierStruct(s);
            intent.putExtra("supplier", supplier);
            intent.putExtra("supplier-id", (Long) s.getId());
            startActivityForResult(intent, REQ_UPDATE);
        }
    }

}
