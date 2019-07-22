package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarcodeViewModel extends AndroidViewModel {
    private DAOBarcode dbInterface;

    public BarcodeViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOBarcode();
    }

    /**
     * @return all products having a barcode
     */
    public LiveData<List<product>> getAllProductsLive() {
        return dbInterface.getAllProductsLive();
    }

    /**
     * @return all barcodes
     */
    public LiveData<List<barcode>> getAllLive() {
        return dbInterface.getAllLive();
    }

    /**
     * @return all barcodes for a specific product
     */
    public LiveData<List<barcode>> getForProductLive(long id) {
        return dbInterface.getAllForProductLive(id);
    }

    public void deleteForProduct(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteForProduct(ids.toArray(new Long[]{})));
    }
}
