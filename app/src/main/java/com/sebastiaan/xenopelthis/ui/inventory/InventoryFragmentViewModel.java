package com.sebastiaan.xenopelthis.ui.inventory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.retrieve.repository.InventoryRepository;

import java.util.List;

public class InventoryFragmentViewModel extends AndroidViewModel {
    private InventoryRepository repository;
    private LiveData<List<ProductAndAmount>> cachedList;

    public InventoryFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new InventoryRepository(application);
    }

    public LiveData<List<ProductAndAmount>> getAllLive() {
        if (cachedList == null)
            cachedList = repository.getAllLive();
        return cachedList;
    }

    public void deleteByID(List<Long> ids) {
        repository.deleteByID(ids);
    }
}
