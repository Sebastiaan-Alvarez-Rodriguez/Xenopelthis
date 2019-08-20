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
    private DAOProduct productInterface;

    public InventoryViewModel(Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        inventoryInterface = db.getDAOInventory();
        productInterface = db.getDAOProduct();
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

    public void add(inventory_item item, @NonNull ResultListener<Long> idCallback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallback.onResult(inventoryInterface.add(item)));
    }

    public void update(inventory_item i) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.update(i));
    }

    public void findByName(String name, ResultListener<product> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(productInterface.findExact(name)));
    }

    public void get(long id, ResultListener<ProductAndAmount> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(inventoryInterface.get(id)));
    }

    public LiveData<List<product>> getUnusedLive() {
        return inventoryInterface.getUnusedLive();
    }

}

