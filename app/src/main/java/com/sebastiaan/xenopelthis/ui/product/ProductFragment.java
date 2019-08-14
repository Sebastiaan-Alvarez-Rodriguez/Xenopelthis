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
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.Fragment;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFragment extends Fragment<product> implements ActionListener<product> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
    }

    @NonNull
    @Override
    protected List<product> filter(List<product> list, @NonNull String query) {
        List<product> returnlist = new ArrayList<>();
        for (product item : list)
            if (item.getName().toLowerCase().contains(query))
                returnlist.add(item);
        return returnlist;
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
    protected void prepareFAB(View view, boolean actionMode) {
        if (actionMode) {
            fab.setOnClickListener(v -> model.deleteByID(adapter.getSelected().stream().map(product::getId).collect(Collectors.toList())));
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        } else {
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ProductEditActivity.class);
                startActivityForResult(intent, REQ_ADD);
            });
            fab.setImageResource(android.R.drawable.ic_menu_add);
        }
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