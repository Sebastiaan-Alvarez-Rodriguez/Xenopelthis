package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SupplierViewModel extends com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel<supplier> {
    private DAOSupplier dbInterface;

    public SupplierViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOSupplier();
        cacheList = dbInterface.getAllLive();
    }

    public void add(SupplierStruct s) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.add(s.toSupplier()));

        Log.e("Edit", "placed new supplier with name: " + s.name);
    }

    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallBack) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallBack.onResult(dbInterface.add(s.toSupplier())));
        Log.e("Edit", "placed new supplier with name: " + s.name);
    }

    public void update(SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.update(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.delete(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            dbInterface.delete(s.toSupplier(id));
            callback.onResult(null);
        });
    }

    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}