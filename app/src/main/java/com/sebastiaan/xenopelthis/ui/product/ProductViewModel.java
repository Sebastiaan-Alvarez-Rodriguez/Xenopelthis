package com.sebastiaan.xenopelthis.ui.product;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductViewModel extends AndroidViewModel {

    private DAOProduct dbInterface;

    public ProductViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOProduct();
    }

    LiveData<List<product>> getAll() {
        return dbInterface.getAllLive();
    }

    void add(ProductStruct p) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.add(p.toProduct()));

        Log.e("Edit", "placed new product with name: " + p.name);
    }

    void update(ProductStruct p, long id) {
        dbInterface.update(p.toProduct(id));
    }

    public LiveData<List<supplier>> getSuppliersForProduct(long id) {
        return dbInterface.suppliersForProduct(id);
    }
}

