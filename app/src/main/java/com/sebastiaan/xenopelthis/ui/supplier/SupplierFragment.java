package com.sebastiaan.xenopelthis.ui.supplier;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.supplier.view.SupplierAdapter;
import com.sebastiaan.xenopelthis.ui.supplier.view.OnClickListener;

import static android.app.Activity.RESULT_OK;

public class SupplierFragment extends Fragment {
    private SupplierViewModel model;
    private static final int REQ_ADD = 0, REQ_UPDATE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(SupplierViewModel.class);
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
        prepareFAB(view);
    }



    void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        SupplierAdapter adapter = new SupplierAdapter(new OnClickListener() {
            @Override
            public void onClick(supplier s) {
                Log.e("Click", "Supplier with name '" + s.getName() + "' was clicked!");
            }

            @Override
            public boolean onLongClick(supplier s) {
                Log.e("Click", "Supplier with name '" + s.getName() + "' was longclicked and click is consumed!");
                return true;
            }
        });
        model.getAll().observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
    }

    void prepareFAB(View view) {//TODO: if actionmode, stop actionmode first (or hide this button?)
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SupplierEditActivity.class);
            startActivityForResult(intent, REQ_ADD);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View v = getView();
        switch (requestCode) {
            case REQ_ADD:
                if (resultCode == RESULT_OK && v != null)
                    Snackbar.make(v, "New item added", Snackbar.LENGTH_SHORT).show();
                break;
            case REQ_UPDATE:
        }
    }
}
