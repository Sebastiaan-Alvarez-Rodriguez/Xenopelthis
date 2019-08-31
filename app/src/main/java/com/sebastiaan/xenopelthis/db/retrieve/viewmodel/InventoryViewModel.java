package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InventoryViewModel extends com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel<ProductAndAmount> {
    private DAOInventory inventoryInterface;


    public InventoryViewModel(Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        inventoryInterface = db.getDAOInventory();
        liveList = inventoryInterface.getAllLive();
    }

    @Override
    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.deleteByID(ids.toArray(new Long[]{})));
    }

    public void add(inventory_item item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.add(item));
        Log.e("Edit", "Placed new inventory item");
    }

    public void update(inventory_item item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.update(item));
    }

    /**
     * @return all products which are currently not in the inventory system
     */
    public LiveData<List<product>> getUnusedLive() {
        return inventoryInterface.getUnusedLive();
    }

}

