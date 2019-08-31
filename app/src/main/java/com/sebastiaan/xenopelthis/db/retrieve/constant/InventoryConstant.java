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

    public void getProductAndAmount(long id, ResultListener<ProductAndAmount> listener) {
        Executor myExetutor = Executors.newSingleThreadExecutor();
        myExetutor.execute(() -> {
            ProductAndAmount x = dbInterface.get(id);
            listener.onResult(x);
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
