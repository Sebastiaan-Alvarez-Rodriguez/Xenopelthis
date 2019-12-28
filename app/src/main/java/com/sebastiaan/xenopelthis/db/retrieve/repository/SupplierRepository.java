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
/**
 * Repository class containing queries related to {@link supplier}, to decouple database from database function callers
 */
public class SupplierRepository {
    private DAOSupplier supplierInterface;

    /**
     * @return all suppliers from database
     */
    public LiveData<List<supplier>> getAll() {
        return supplierInterface.getAllLive();
    }

    public SupplierRepository(Application application) {
        supplierInterface = Database.getDatabase(application).getDAOSupplier();
    }

    /**
     * Add a supplier to database
     * @param s supplier to add
     */
    public void add(SupplierStruct s) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.add(s.toSupplier()));
    }

    /**
     * @see #add(SupplierStruct)
     * same but with a result callback
     * @param idCallBack result callback
     */
    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallBack) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallBack.onResult(supplierInterface.add(s.toSupplier())));
    }

    /**
     * Update a given supplier
     * @param s supplier to update
     * @param id id of supplier to update
     */
    public void update(SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.update(s.toSupplier(id)));
    }

    /**
     * Delete a given supplier
     * @param s supplier to delete
     * @param id id of supplier to delete
     */
    public void delete(@NonNull SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.delete(s.toSupplier(id)));
    }

    /**
     * @see #delete(SupplierStruct, long)
     * same but with a result callback
     * @param callback result callback
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
     * @param ids ids of suppliers to delete
     */
    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> supplierInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}
