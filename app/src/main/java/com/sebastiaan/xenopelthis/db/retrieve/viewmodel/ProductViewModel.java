package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductViewModel extends com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel<product> {
    private DAOProduct dbInterface;

    public ProductViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOProduct();
        liveList = dbInterface.getAllLive();
    }

    public LiveData<List<product>> getAll() {
        return dbInterface.getAllLive();
    }

    public void add(@NonNull ProductStruct p) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.add(p.toProduct()));
        Log.e("Edit", "placed new product with name: " + p.name);
    }

    public void add(@NonNull ProductStruct p, @NonNull ResultListener<Long> idCallback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallback.onResult(dbInterface.add(p.toProduct())));
        Log.e("Edit", "placed new product with name: " + p.name);
    }

    public void update(@NonNull ProductStruct p, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.update(p.toProduct(id)));
    }

    public void delete(@NonNull ProductStruct p, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.delete(p.toProduct(id)));
    }

    public void delete(@NonNull ProductStruct p, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            dbInterface.delete(p.toProduct(id));
            callback.onResult(null);
        });
    }

    public void deleteByID(@NonNull List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteByID(ids.toArray(new Long[]{})));
    }

    boolean nameExists(@NonNull ProductStruct p) { return (dbInterface.findExact(p.name) == null); }

    public void setHasBarcode(boolean hasBarcode, Long... ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.setHasBarcode(hasBarcode, ids));
    }

}

