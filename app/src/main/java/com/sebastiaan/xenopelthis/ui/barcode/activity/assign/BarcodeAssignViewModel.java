package com.sebastiaan.xenopelthis.ui.barcode.activity.assign;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

import java.util.List;

public class BarcodeAssignViewModel extends AndroidViewModel {
    private BarcodeRepository repository;

    private LiveData<List<product>> cachedList;

    public BarcodeAssignViewModel(@NonNull Application application) {
        super(application);
        repository = new BarcodeRepository(application);
        cachedList = null;
    }

    /**
     * @param barcodeString The barcode for which all unassigned products must be returned
     * @return all unassigned products for a given barcode
     */
    public LiveData<List<product>> getUnassignedForBarcodeLive(String barcodeString) {
        if (cachedList == null)
            cachedList = repository.getUnassignedForBarcodeLive(barcodeString);
        return cachedList;
    }

    /**
     * Adds a given barcode-product relation to the database
     * @param b The barcodestruct to extract barcode from
     * @param id The id of the product to be related to given barcode
     */
    public void add(@NonNull BarcodeStruct b, long id) {
        repository.add(b.toBarcode(id));
    }
}
