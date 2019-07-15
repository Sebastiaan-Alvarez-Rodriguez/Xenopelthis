package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductConstant {
    private DAOProduct dbInterface;

    public ProductConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOProduct();
    }

    public void isUnique(String name, ConstantResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            boolean x = dbInterface.findExact(name) == null;
            listener.onResult(x);
        });
    }
}
