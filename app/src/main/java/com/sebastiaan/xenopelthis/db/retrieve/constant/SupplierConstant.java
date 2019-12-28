package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SupplierConstant {
    private DAOSupplier dbInterface;

    public SupplierConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOSupplier();
    }

    /**
     * Checks if a given name is unique in database
     * @param name name to check
     * @param listener result callback
     */
    public void isUnique(String name, ResultListener<supplier> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.findExact(name)));
    }

    /**
     * Get all suppliers in database
     * @param listener result callback
     */
    public void getAll(ResultListener<List<supplier>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAll()));
    }
}
