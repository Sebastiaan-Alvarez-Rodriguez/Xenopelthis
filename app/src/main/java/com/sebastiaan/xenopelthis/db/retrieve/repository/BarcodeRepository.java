package com.sebastiaan.xenopelthis.db.retrieve.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BarcodeRepository{
    private DAOBarcode barcodeInterface;
    private DAOProduct productInterface;

    public BarcodeRepository(Context applicationContext) {
        barcodeInterface = Database.getDatabase(applicationContext).getDAOBarcode();
        productInterface = Database.getDatabase(applicationContext).getDAOProduct();
    }

    /**
     * @return all barcodes
     */
    public LiveData<List<barcode>> getAllLive() {
        return barcodeInterface.getAllLive();
    }

    /**
     * @return all products having a barcode
     */
    public LiveData<List<product>> getAllProductsLive() {
        return barcodeInterface.getAllProductsLive();
    }

    /**
     * @param id The id of a product
     * @return all barcodes for a product with given id
     */
    public LiveData<List<barcode>> getForProductLive(long id) {
        return barcodeInterface.getAllForProductLive(id);
    }

    /**
     * @param barcodeString A barcode
     * @return all products for a given barcode
     */
    public LiveData<List<product>> getForBarcodeLive(String barcodeString) {
        return barcodeInterface.getAllForBarcodeLive(barcodeString);
    }

    /**
     * @param barcodeString The barcode for which all unassigned products must be returned
     * @return all unassigned products for a given barcode
     */
    public LiveData<List<product>> getUnassignedForBarcodeLive(String barcodeString) {
        return barcodeInterface.getUnassignedForBarcodeLive(barcodeString);
    }



    /**
     * Adds a given barcode-product relation to the database
     * @param b The barcode to add
     */
    public void add(@NonNull barcode b) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            barcodeInterface.add(b);
            productInterface.setHasBarcode(true, b.getId());
        });
    }

    /**
     * Deletes a given barcode-product relation
     * @param b The barcode to delete
     */
    public void delete(@NonNull barcode b) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            long id = b.getId();
            barcodeInterface.delete(b);
            List<barcode> remaining = barcodeInterface.getForProduct(id);
            productInterface.setHasBarcode(remaining != null && !remaining.isEmpty(), id);
        });
    }

    /**
     * @see #delete(barcode)
     * Same function, but for multiple deletes at once
     */
    public void delete(@NonNull Collection<barcode> barcodes) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            barcodeInterface.delete(barcodes.toArray(new barcode[]{}));
            for (Long id : barcodes.stream().map(barcode::getId).collect(Collectors.toList())) {
                List<barcode> remaining = barcodeInterface.getForProduct(id);
                productInterface.setHasBarcode(remaining != null && !remaining.isEmpty(), id);
            }
        });
    }

    /**
     * Checks if a given barcode is unique for a given id
     * @param barcode The barcode to be checked
     * @param id The id to be exempted
     * @param listener Result callback
     */
    public void isUnique(String barcode, long id, ResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<product> matches = barcodeInterface.getForBarcode(barcode);
            listener.onResult(
                    matches.stream().noneMatch(product -> product.getId() != id));
        });
    }

    /**
     * @see #isUnique(String, long, ResultListener)
     * Essentially the same, but without exemptions
     */
    public void isUnique(String barcode, ResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<product> matches = barcodeInterface.getForBarcode(barcode);
            listener.onResult(matches.isEmpty());
        });
    }

    public void getProductsCount(String barcode, ResultListener<Long> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(barcodeInterface.getProductsCount(barcode)));
    }

    /**
     * Get current products for a given barcode
     * @param barcode The barcode
     * @param listener Result callback
     */
    public void getProducts(String barcode, ResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(barcodeInterface.getForBarcode(barcode)));
    }
}
