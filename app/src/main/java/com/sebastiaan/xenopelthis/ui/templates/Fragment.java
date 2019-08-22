package com.sebastiaan.xenopelthis.ui.templates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.Adapter;
import com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction;

import static android.app.Activity.RESULT_OK;

public abstract class Fragment<T> extends androidx.fragment.app.Fragment implements ActionListener<T> {
    protected ViewModel<T> model;

    protected static final int REQ_ADD = 0, REQ_UPDATE = 1;

    protected SearchView search;
    protected ImageView add, sort;
    protected RecyclerView list;

    protected AdapterAction<T> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.searchview);
        sort = view.findViewById(R.id.sort_button);
        list = view.findViewById(R.id.list);
        add = view.findViewById(R.id.add);
        prepareList(view);
        prepareAdd(view, false);
        prepareSearch();
        prepareSort();
    }

    abstract protected void prepareSearch();

    protected void prepareSort() {
        sort.setOnClickListener(v -> adapter.sort(adapter.getSortStrategy() == Adapter.SortBy.NAME ? Adapter.SortBy.DATE : Adapter.SortBy.NAME));
    }
    abstract protected void prepareList(View view);

    @CallSuper
    protected void prepareAdd(View view, boolean actionMode) {
        add.setImageResource(actionMode ? R.drawable.ic_delete : R.drawable.ic_add);
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
                if (resultCode == RESULT_OK && v != null)
                    Snackbar.make(v, "Item edited", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(T t) {
        return true;
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        prepareAdd(getView(), actionMode);
        Log.e("Click", "Action mode changed to: "+actionMode);
    }

}
