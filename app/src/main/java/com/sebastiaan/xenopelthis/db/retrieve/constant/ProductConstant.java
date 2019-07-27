package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductConstant {
    private DAOProduct dbInterface;

    public ProductConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOProduct();
    }

    public void isUnique(String name, ResultListener<product> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.findExact(name)));
    }

    public void getAll(ResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAll()));
    }
}
