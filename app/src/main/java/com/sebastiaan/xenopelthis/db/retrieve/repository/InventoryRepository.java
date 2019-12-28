package com.sebastiaan.xenopelthis.db.retrieve.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Repository class containing queries related to {@link inventory_item}, to decouple database from database function callers
 */
public class InventoryRepository {
    private DAOInventory inventoryInterface;

    public InventoryRepository(Context applicationContext) {
        inventoryInterface = Database.getDatabase(applicationContext).getDAOInventory();
    }

    /**
     * @return all products which are currently not in the inventory system
     */
    public LiveData<List<product>> getUnusedLive() {
        return inventoryInterface.getUnusedLive();
    }

    public LiveData<List<ProductAndAmount>> getAllLive() {
        return inventoryInterface.getAllLive();
    }

    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.deleteByID(ids.toArray(new Long[]{})));
    }

    public void add(inventory_item item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.add(item));
    }

    public void update(inventory_item item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.update(item));
    }

    /**
     * Updates an inventory item if it exists, and inserts it otherwise
     * @param item The item to be updated/added
     */
    public void upsert(inventory_item item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            long affected = inventoryInterface.update(item);
            if (affected == 0)
                inventoryInterface.add(item);
        });
    }

    /**
     * Queries the amount of items in inventory for a given product id.
     * Returns 0 if the item in question is not in the inventory
     * @param id The product id to find the amount in inventory for
     * @param listener Result callback
     */
    public void getAmount(long id, ResultListener<Long> listener) {
        Executor myExetutor = Executors.newSingleThreadExecutor();
        myExetutor.execute(() -> {
            ProductAndAmount p = inventoryInterface.get(id);
            listener.onResult(p != null? p.getAmount() : 0);
        });
    }
}
