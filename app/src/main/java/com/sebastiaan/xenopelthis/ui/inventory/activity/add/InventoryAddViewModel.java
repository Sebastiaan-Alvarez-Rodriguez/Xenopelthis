package com.sebastiaan.xenopelthis.ui.inventory.activity.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.repository.InventoryRepository;

import java.util.List;

public class InventoryAddViewModel extends AndroidViewModel {
    private InventoryRepository repository;

    private LiveData<List<product>> cachedList;

    public InventoryAddViewModel(@NonNull Application application) {
        super(application);
        repository = new InventoryRepository(application);
        cachedList = null;
    }

    /**
     * @return all products which are currently not in the inventory system
     */
    public LiveData<List<product>> getUnusedLive() {
        if (cachedList == null)
            cachedList = repository.getUnusedLive();
        return cachedList;
    }

    public void add(inventory_item item) {
        repository.add(item);
    }
}
