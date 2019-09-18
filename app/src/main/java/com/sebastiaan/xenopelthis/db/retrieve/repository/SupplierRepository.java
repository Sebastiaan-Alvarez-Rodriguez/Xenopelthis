package com.sebastiaan.xenopelthis.db.retrieve.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SupplierRepository {
    private DAOSupplier supplierInterface;


    public LiveData<List<supplier>> getAll() {
        return supplierInterface.getAllLive();
    }

    public SupplierRepository(Application application) {
        supplierInterface = Database.getDatabase(application).getDAOSupplier();
    }

    public void add(SupplierStruct s) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.add(s.toSupplier()));
    }

    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallBack) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallBack.onResult(supplierInterface.add(s.toSupplier())));
    }

    public void update(SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.update(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.delete(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            supplierInterface.delete(s.toSupplier(id));
            callback.onResult(null);
        });
    }

    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}
