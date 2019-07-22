package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SupplierConstant {
    private DAOSupplier dbInterface;

    public SupplierConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOSupplier();
    }

    public void isUnique(String name, ResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            boolean x = dbInterface.findExact(name) == null;
            listener.onResult(x);
        });
    }
}
