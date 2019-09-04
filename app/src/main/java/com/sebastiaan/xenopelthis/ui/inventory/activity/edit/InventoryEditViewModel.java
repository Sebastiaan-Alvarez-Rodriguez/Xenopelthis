package com.sebastiaan.xenopelthis.ui.inventory.activity.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.InventoryRepository;

class InventoryEditViewModel extends AndroidViewModel {
    private InventoryRepository repository;

    InventoryEditViewModel(@NonNull Application application) {
        super(application);
        repository = new InventoryRepository(application);
    }

    void getAmount(long id, ResultListener<Long> listener) {
        repository.getAmount(id, listener);
    }

    void upsert(inventory_item item) {
        repository.upsert(item);
    }

    void update(inventory_item item) {
        repository.update(item);
    }
}
