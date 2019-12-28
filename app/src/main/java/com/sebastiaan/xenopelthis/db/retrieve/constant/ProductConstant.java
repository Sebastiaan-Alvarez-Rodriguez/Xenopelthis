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

    /**
     * Checks if a given product name is unique
     * @param name name to check
     * @param listener result callback
     */
    public void isUnique(String name, ResultListener<product> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.findExact(name)));
    }

    /**
     * Get current product for a given id
     * @param id product id
     * @param listener result callback
     */
    public void get(long id, ResultListener<product> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.get(id)));
    }

    /**
     * Get all products from database
     * @param listener result callback
     */
    public void getAll(ResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAll()));
    }
}
