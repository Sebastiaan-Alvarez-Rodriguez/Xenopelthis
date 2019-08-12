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
}
