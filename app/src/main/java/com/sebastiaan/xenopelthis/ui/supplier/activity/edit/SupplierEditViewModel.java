package com.sebastiaan.xenopelthis.ui.supplier.activity.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.SupplierRepository;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

class SupplierEditViewModel extends AndroidViewModel {
    private SupplierRepository repository;

    SupplierEditViewModel(@NonNull Application application) {
        super(application);
        repository = new SupplierRepository(application);
    }

    void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallback) {
        repository.add(s, idCallback);
    }

    void update(@NonNull SupplierStruct s, long id) {
        repository.update(s, id);
    }

    void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        repository.delete(s, id, callback);
    }
}
