package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;
import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarcodeViewModel extends AndroidViewModel {
    private DAOBarcode dbInterface;
    private BarcodeConstant barcodeConstant;

    public BarcodeViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOBarcode();
        barcodeConstant = new BarcodeConstant(application);
    }

    /**
     * @return all barcodes
     */
    public LiveData<List<barcode>> getAllLive() {
        return dbInterface.getAllLive();
    }

    /**
     * @return all products having a barcode
     */
    public LiveData<List<product>> getAllProductsLive() {
        return dbInterface.getAllProductsLive();
    }

    /**
     * @param id The id of a product
     * @return all barcodes for a product with given id
     */
    public LiveData<List<barcode>> getForProductLive(long id) {
        return dbInterface.getAllForProductLive(id);
    }

    /**
     * @param barcodeString A barcode
     * @return all products for a given barcode
     */
    public LiveData<List<product>> getForBarcodeLive(String barcodeString) {
        return dbInterface.getAllForBarcodeLive(barcodeString);
    }

    /**
     * Adds a given barcode struct to the database
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the product to be related to given barcode
     */
    public void add(@NonNull BarcodeStruct b, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.add(b.toBarcode(id)));
    }
//
//    public void update(@NonNull BarcodeStruct b, long id) {
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(() -> dbInterface.update(b.toBarcode(id)));
//    }

    /**
     * Deletes a given barcode-product relation
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the related product for given barcode
     */
    public void delete(@NonNull BarcodeStruct b, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.delete(b.toBarcode(id)));
    }

    /**
     * @see #delete(BarcodeStruct, long)
     * Same function, but for multiple deletes at once
     */
    public void delete(@NonNull Collection<barcode> barcodes) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.delete(barcodes.toArray(new barcode[]{})));
    }

    /**
     * @param barcodeString The barcode for which all unassigned products must be returned
     * @return all unassigned products for a given barcode
     */
    public LiveData<List<product>> getUnassignedForBarcodeLive(String barcodeString) {
        return dbInterface.getUnassignedForBarcodeLive(barcodeString);
    }

    public BarcodeConstant constantQuery() {
        return barcodeConstant;
    }
}
