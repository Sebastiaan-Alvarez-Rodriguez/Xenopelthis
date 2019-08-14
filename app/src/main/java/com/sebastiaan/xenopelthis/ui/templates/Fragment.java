package com.sebastiaan.xenopelthis.ui.templates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;
import com.sebastiaan.xenopelthis.ui.templates.adapter.AdapterAction;

import static android.app.Activity.RESULT_OK;

public abstract class Fragment<T> extends androidx.fragment.app.Fragment implements ActionListener<T> {
    protected ViewModel<T> model;

    protected static final int REQ_ADD = 0, REQ_UPDATE = 1;

    protected SearchView search;
    protected ImageButton sort;
    protected RecyclerView list;
    protected FloatingActionButton fab;

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
        fab = view.findViewById(R.id.fab);
        prepareList(view);
        prepareFAB(view, false);
        prepareSearch();
    }

    abstract protected void prepareSearch();
    abstract protected void prepareList(View view);
    abstract protected void prepareFAB(View view, boolean actionMode);

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
        prepareFAB(getView(), actionMode);
        Log.e("Click", "Action mode changed to: "+actionMode);
    }

}
