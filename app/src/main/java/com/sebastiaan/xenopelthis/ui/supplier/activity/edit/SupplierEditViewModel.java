package com.sebastiaan.xenopelthis.ui.supplier.activity.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.SupplierRepository;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

public class SupplierEditViewModel extends AndroidViewModel {
    private SupplierRepository repository;

    public SupplierEditViewModel(@NonNull Application application) {
        super(application);
        repository = new SupplierRepository(application);
    }

    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallback) {
        repository.add(s, idCallback);
    }

    public void update(@NonNull SupplierStruct s, long id) {
        repository.update(s, id);
    }

    public void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        repository.delete(s, id, callback);
    }
}
