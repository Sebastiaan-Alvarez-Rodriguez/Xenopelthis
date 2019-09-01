package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InventoryConstant {
    private DAOInventory dbInterface;

    public InventoryConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOInventory();
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
            ProductAndAmount p = dbInterface.get(id);
            listener.onResult(p != null? p.getAmount() : 0);
        });
    }

    /**
     * Determines whether a given product is in the inventory
     * @param productID The id of the product to check
     * @param listener Result callback
     */
    public void contains(long productID, ResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.contains(productID)));
    }

    public void get(long id, ResultListener<ProductAndAmount> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.get(id)));
    }
}
