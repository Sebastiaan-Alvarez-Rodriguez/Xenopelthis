package com.sebastiaan.xenopelthis.ui.main.activity.main;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MainViewModel extends AndroidViewModel {
    private BarcodeRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new BarcodeRepository(application);
    }

    public void getProductsCount(String barcode, ResultListener<Long> listener) {
        repository.getProductsCount(barcode, listener);
    }

    public void getProducts(String barcode, ResultListener<List<product>> listener) {
        repository.getProducts(barcode, listener);
    }
}
