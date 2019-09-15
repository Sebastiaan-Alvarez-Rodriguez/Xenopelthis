package com.sebastiaan.xenopelthis.ui.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.repository.ProductRepository;

import java.util.List;

public class ProductFragmentViewModel extends AndroidViewModel {
    private ProductRepository repository;

    private LiveData<List<product>> cachedList = null;

    public ProductFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public LiveData<List<product>> getAll() {
        if (cachedList == null)
            cachedList = repository.getAll();
        return cachedList;
    }

    public void deleteByID(@NonNull List<Long> ids) {
       repository.deleteByID(ids);
    }
}
