package com.sebastiaan.xenopelthis.ui.product.activity.barcode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ProductEditBarcodeViewModel  extends AndroidViewModel {
    private BarcodeRepository repository;

    private LiveData<List<barcode>> cachedList = null;

    ProductEditBarcodeViewModel(@NonNull Application application) {
        super(application);
        repository = new BarcodeRepository(application);
    }

    public void add(@NonNull BarcodeStruct b, long id) {
        repository.add(b.toBarcode(id));
    }

    public void delete(@NonNull Collection<barcode> barcodes) {
        repository.delete(barcodes);
    }
    LiveData<List<barcode>> getForProductLive(long id) {
        if (cachedList == null)
            cachedList = repository.getForProductLive(id);
        return cachedList;
    }

    public void isUnique(String barcode, long id, ResultListener<Boolean> listener) {
        repository.isUnique(barcode, id, listener);
    }

    /**
     * @see #isUnique(String, long, ResultListener)
     * Essentially the same, but without exemptions
     */
    public void isUnique(String barcode, ResultListener<Boolean> listener) {
        repository.isUnique(barcode, listener);
    }
}