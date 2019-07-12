package com.sebastiaan.xenopelthis.ui.product.view;

import android.arch.lifecycle.Observer;

import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;

public interface ObserverListener {
    void addObserver(Observer<List<supplier>> observer, long id);
    void removeObserver(Observer<List<supplier>> observer, long id);
}
