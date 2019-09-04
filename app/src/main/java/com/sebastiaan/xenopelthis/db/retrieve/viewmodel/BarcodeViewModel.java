package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

import java.util.Collection;
import java.util.List;

public class BarcodeViewModel extends AndroidViewModel {
    private BarcodeConstant barcodeConstant;
    private BarcodeRepository barcodeRepository;

    private LiveData<List<barcode>> barcodeCache = null;
    private LiveData<List<product>> productCache = null;

    public BarcodeViewModel(Application application) {
        super(application);
        barcodeRepository = new BarcodeRepository(application);

        barcodeConstant = new BarcodeConstant(application);
    }

    private static final int
            ALL = 0,
            ALLPRODUCTS = 1,
            BARCODESFORPRODUCT = 2,
            PRODUCTSFORBARCODE = 3,
            PRODUCTSNOTFORBARCODE = 4
            ;


    public BarcodeViewModel request(int type) {
        switch (type) {
            case 0:
                barcodeCache = getAllLive();
                productCache = null;
                return this;
            case 1:
                barcodeCache = null;
                productCache = getAllProductsLive();
                return this;
        }
        return this;
    }

    public LiveData<List<barcode>> get() {
        return barcodeCache == null ? productCache : barcodeCache;
    }
    /**
     * @return all barcodes
     */
    public LiveData<List<barcode>> getAllLive() {
        return barcodeRepository.getAllLive();
    }

    /**
     * @return all products having a barcode
     */
    public LiveData<List<product>> getAllProductsLive() {
        return barcodeRepository.getAllProductsLive();
    }

    /**
     * @param id The id of a product
     * @return all barcodes for a product with given id
     */
    public LiveData<List<barcode>> getForProductLive(long id) {
        return barcodeRepository.getForProductLive(id);
    }

    /**
     * @param barcodeString A barcode
     * @return all products for a given barcode
     */
    public LiveData<List<product>> getForBarcodeLive(String barcodeString) {
        return barcodeRepository.getForBarcodeLive(barcodeString);
    }

    /**
     * @param barcodeString The barcode for which all unassigned products must be returned
     * @return all unassigned products for a given barcode
     */
    public LiveData<List<product>> getUnassignedForBarcodeLive(String barcodeString) {
        return barcodeRepository.getUnassignedForBarcodeLive(barcodeString);
    }

    /**
     * Adds a given barcode-product relation to the database
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the product to be related to given barcode
     */
    public void add(@NonNull BarcodeStruct b, long id) {
        barcodeRepository.add(b.toBarcode(id));
    }

    /**
     * Deletes a given barcode-product relation
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the related product for given barcode
     */
    public void delete(@NonNull BarcodeStruct b, long id) {
        barcodeRepository.delete(b.toBarcode(id)));
    }

    /**
     * @see #delete(BarcodeStruct, long)
     * Same function, but for multiple deletes at once
     */
    public void delete(@NonNull Collection<barcode> barcodes) {
        barcodeRepository.delete(barcodes));
    }

    public BarcodeConstant constantQuery() {
        return barcodeConstant;
    }
}
