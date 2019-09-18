package com.sebastiaan.xenopelthis.ui.product.activity.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.ProductRepository;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

public class ProductEditViewModel extends AndroidViewModel {
    private ProductRepository repository;

    public ProductEditViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public void add(@NonNull ProductStruct p, @NonNull ResultListener<Long> idCallback) {
         repository.add(p, idCallback);
    }

    public void update(@NonNull ProductStruct p, long id) {
         repository.update(p, id);
    }

    public void delete(@NonNull ProductStruct p, long id, ResultListener<Void> callback) {
         repository.delete(p, id, callback);
    }
}
