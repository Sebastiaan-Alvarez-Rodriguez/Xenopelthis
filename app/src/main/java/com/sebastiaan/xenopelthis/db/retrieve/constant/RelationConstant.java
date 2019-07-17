package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RelationConstant {
    private DAOSupplierProduct dbInterface;

    public RelationConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOSupplierProduct();
    }

    public void getSuppliersForProduct(long id, ConstantResultListener<List<supplier>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<supplier> x = dbInterface.suppliersForProduct(id);
            listener.onResult(x);
        });
    }

    public void getProductsForSupplier(long id, ConstantResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<product> x = dbInterface.productsForSupplier(id);
            listener.onResult(x);
        });
    }
}
