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

    /**
     * Returns the cachedlist and construct it if required
     * @return the cachedlist
     */
    public LiveData<List<ProductAndAmount>> getAllLive() {
        if (cachedList == null)
            cachedList = repository.getAllLive();
        return cachedList;
    }

    /**
     * Delete the given inventory items
     * @param ids the ids of the inventory items
     */
    public void deleteByID(List<Long> ids) {
        repository.deleteByID(ids);
    }
}
