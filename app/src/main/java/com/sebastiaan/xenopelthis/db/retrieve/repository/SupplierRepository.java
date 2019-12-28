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

    /**
     * @return all suppliers from the database
     */
    public LiveData<List<supplier>> getAll() {
        return supplierInterface.getAllLive();
    }

    public SupplierRepository(Application application) {
        supplierInterface = Database.getDatabase(application).getDAOSupplier();
    }

    /**
     * Add a supplier to the database
     * @param s the supplier to be added
     */
    public void add(SupplierStruct s) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.add(s.toSupplier()));
    }

    /**
     * @see #add(SupplierStruct)
     * The same but with a result callback
     * @param idCallBack the result callback
     */
    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallBack) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallBack.onResult(supplierInterface.add(s.toSupplier())));
    }

    /**
     * Update a given supplier
     * @param s the supplier to be updated
     * @param id the id of the supplier to be updated
     */
    public void update(SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.update(s.toSupplier(id)));
    }

    /**
     * Delete a given supplier
     * @param s the supplier to be deleted
     * @param id the id of the supplier to be updated
     */
    public void delete(@NonNull SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.delete(s.toSupplier(id)));
    }

    /**
     * @see #delete(SupplierStruct, long)
     * The same but with a result callback
     * @param callback the result callback
     */
    public void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            supplierInterface.delete(s.toSupplier(id));
            callback.onResult(null);
        });
    }

    /**
     * Delete all given suppliers
     * @param ids the ids of the suppliers to be deleted
     */
    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}
