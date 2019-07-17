package com.sebastiaan.xenopelthis.ui.inventory;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;

public class InventoryFragment extends Fragment {
    private InventoryViewModel model;
    private static final int REQ_ADD = 0, REQ_UPDATE = 1;

    //TODO: implement functions

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareList(view);
        prepareFAB(view);
    }

    void prepareList(View view) {
        RecyclerView list = view.findViewById(R.id.list);

        //InventoryAdapter adapter = new InventoryAdapter(new onClickListener() {

        //})



    }

    //TODO: implement
    void prepareFAB(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
