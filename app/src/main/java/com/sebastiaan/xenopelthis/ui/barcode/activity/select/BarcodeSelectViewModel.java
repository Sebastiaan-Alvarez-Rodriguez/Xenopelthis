package com.sebastiaan.xenopelthis.ui.barcode.activity.select;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

import java.util.List;

class BarcodeSelectViewModel extends AndroidViewModel {
    private BarcodeRepository repository;

    private LiveData<List<product>> cachedList;

    public BarcodeSelectViewModel(@NonNull Application application) {
        super(application);
        repository = new BarcodeRepository(application);
        cachedList = null;
    }

    /**
     * @param barcodeString A barcode
     * @return all products for a given barcode
     */
    public LiveData<List<product>> getForBarcodeLive(String barcodeString) {
        if (cachedList == null)
            cachedList = repository.getForBarcodeLive(barcodeString);
        return cachedList;
    }

    /**
     * Deletes a given barcode-product relation
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the related product for given barcode
     */
    public void delete(@NonNull BarcodeStruct b, long id) {
        repository.delete(b.toBarcode(id));
    }
}