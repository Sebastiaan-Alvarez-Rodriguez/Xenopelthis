package com.sebastiaan.xenopelthis.ui.supplier;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.repository.SupplierRepository;

import java.util.List;

class SupplierFragmentViewModel extends AndroidViewModel {
    private SupplierRepository repository;

    private LiveData<List<supplier>> cachedList = null;

    SupplierFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new SupplierRepository(application);
    }

    LiveData<List<supplier>> getAll() {
        if (cachedList == null)
            cachedList = repository.getAll();
        return cachedList;
    }

    public void deleteByID(@NonNull List<Long> ids) {
        repository.deleteByID(ids);
    }
}
