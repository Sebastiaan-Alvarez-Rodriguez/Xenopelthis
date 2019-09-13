package com.sebastiaan.xenopelthis.ui.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.ProductRepository;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;

class ProductFragmentViewModel extends AndroidViewModel {
    private ProductRepository repository;

    private LiveData<List<product>> cachedList = null;

    ProductFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    void add(@NonNull ProductStruct p, @NonNull ResultListener<Long> idCallback) {
        repository.add(p, idCallback);
    }

    void update(@NonNull ProductStruct p, long id) {
        repository.update(p, id);
    }

    void delete(@NonNull ProductStruct p, long id, ResultListener<Void> callback) {
        repository.delete(p, id, callback);
    }

    LiveData<List<product>> getAll() {
        if (cachedList == null)
            cachedList = repository.getAll();
        return cachedList;
    }

    public void deleteByID(@NonNull List<Long> ids) {
       repository.deleteByID(ids);
    }
}
