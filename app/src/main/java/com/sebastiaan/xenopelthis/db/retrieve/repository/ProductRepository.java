package com.sebastiaan.xenopelthis.db.retrieve.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepository {
    private DAOProduct productInterface;

    public ProductRepository(Context applicationContext) {
        productInterface = Database.getDatabase(applicationContext).getDAOProduct();
    }

    /**
     * @return all products
     */
    public LiveData<List<product>> getAll() {
        return productInterface.getAllLive();
    }

    /**
     * Adds a given product to the database
     * @param p the product to add
     */
    public void add(@NonNull ProductStruct p) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> productInterface.add(p.toProduct()));
    }

    /**
     * @see #add(ProductStruct)
     * The same but with a result callback
     * @param idCallback result callback
     */
    public void add(@NonNull ProductStruct p, @NonNull ResultListener<Long> idCallback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallback.onResult(productInterface.add(p.toProduct())));
    }

    /**
     * Update a given product
     * @param p the product to be updated
     * @param id the id of the product to be updated
     */
    public void update(@NonNull ProductStruct p, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> productInterface.update(p.toProduct(id)));
    }

    /**
     * Delete a given product
     * @param p the product to be deleted
     * @param id the id of the product to be deleted
     */
    public void delete(@NonNull ProductStruct p, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> productInterface.delete(p.toProduct(id)));
    }

    /**
     * @see #delete(ProductStruct, long)
     * The same but with a result callback
     * @param callback the result callback
     */
    public void delete(@NonNull ProductStruct p, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            productInterface.delete(p.toProduct(id));
            callback.onResult(null);
        });
    }

    /**
     * Delete given products
     * @param ids the ids of the products to be deleted
     */
    public void deleteByID(@NonNull List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> productInterface.deleteByID(ids.toArray(new Long[]{})));
    }
}
