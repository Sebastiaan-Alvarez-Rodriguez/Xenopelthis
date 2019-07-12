package com.sebastiaan.xenopelthis.ui.product;

import android.arch.lifecycle.Observer;
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
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.product.view.ObserverListener;
import com.sebastiaan.xenopelthis.ui.product.view.ProductAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProductFragment extends Fragment implements ObserverListener {
    private ProductViewModel model;
    private static final int REQ_ADD = 0, REQ_UPDATE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
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
        prepareFAB();
    }
    
    void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        ProductAdapter adapter = new ProductAdapter(this);
        model.getAll().observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    @SuppressWarnings("ConstantConditions")
    void prepareFAB() {//TODO: if actionmode, stop actionmode first (or hide this button?)
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductEditActivity.class);
            startActivityForResult(intent, REQ_ADD);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
            case REQ_ADD:
                if (resultCode == RESULT_OK && data.getExtras() != null && data.hasExtra("result")) {
                    ProductStruct p = data.getExtras().getParcelable("result");
                    
                    model.add(p);
                }
                break;
            case REQ_UPDATE:
        }
    }

    @Override
    public void addObserver(Observer<List<supplier>> observer, long id) {
        model.getSuppliersForProduct(id).observe(this, observer);
    }

    @Override
    public void removeObserver(Observer<List<supplier>> observer, long id) {
        model.getSuppliersForProduct(id).removeObserver(observer);
    }
}
