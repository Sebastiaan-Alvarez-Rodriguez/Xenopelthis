package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class InventoryConstant {
    private DAOInventory dbInterface;

    public InventoryConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOInventory();
    }

    /**
     * Determines whether a given product is in the inventory
     * @param productID id of product to check
     * @param listener result callback
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
