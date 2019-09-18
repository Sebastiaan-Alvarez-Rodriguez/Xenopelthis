package com.sebastiaan.xenopelthis.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.activity.edit.ProductEditActivity;
import com.sebastiaan.xenopelthis.ui.product.search.Searcher;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.Fragment;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.stream.Collectors;

public class ProductFragment extends Fragment<product> implements ActionListener<product> {
    private ProductFragmentViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ProductFragmentViewModel.class);
    }

    @Override
    protected void prepareSearch() {
        search.setOnQueryTextListener(new Searcher(new Searcher.EventListener<product>() {
            @NonNull
            @Override
            public List<product> onBeginSearch() {
                add.setVisibility(View.INVISIBLE);
                return adapter.getItems();
            }

            @Override
            public void onFinishSearch(List<product> initial) {
                adapter.replaceAll(initial);
                add.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceiveFilteredContent(List<product> filtered) {
                adapter.replaceAll(filtered);
                list.scrollToPosition(0);
            }
        }));
    }

    @Override
    protected void prepareList(View view) {
        adapter = new AdapterAction(this);
        model.getAll().observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void prepareAdd(View view, boolean actionMode) {
        if (actionMode)
            add.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(product::getId).collect(Collectors.toList())));
        else
            add.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), ProductEditActivity.class), REQ_ADD));
        super.prepareAdd(view, actionMode);
    }

    @Override
    public void onClick(product p) {
        if (!adapter.isActionMode()) {
            Intent intent = new Intent(getContext(), ProductEditActivity.class);
            ProductStruct product = new ProductStruct(p);
            intent.putExtra("product", product);
            intent.putExtra("product-id", p.getId());
            startActivityForResult(intent, REQ_UPDATE);
        }
    }
}