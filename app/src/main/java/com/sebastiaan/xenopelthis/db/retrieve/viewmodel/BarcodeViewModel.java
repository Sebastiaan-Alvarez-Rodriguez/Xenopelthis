package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarcodeViewModel extends AndroidViewModel {
    private DAOBarcode dbInterface;
    private DAOProduct productInterface;

    public BarcodeViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOBarcode();
        productInterface = Database.getDatabase(application).getDAOProduct();
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

    public void update(List<barcode> barcodesOld, List<barcode> barcodesNew, long productID) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<barcode> removeList = getRemoved(barcodesOld, barcodesNew);
            List<barcode> addedList = getAdded(barcodesOld, barcodesNew);

            if (!removeList.isEmpty())
                dbInterface.delete(removeList.toArray(new barcode[]{}));
            if (!addedList.isEmpty())
                dbInterface.add(addedList.toArray(new barcode[]{}));

            productInterface.setHasBarcode(!barcodesNew.isEmpty(), productID);
        });
    }

    public void deleteForProduct(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteForProduct(ids.toArray(new Long[]{})));
    }

    private <T> List<T> getRemoved(List<T> old, List<T> cur) {
        List<T> retList = new ArrayList<>();
        for (T t : old)
            if (!cur.contains(t))
                retList.add(t);
        return retList;
    }

    private <T> List<T> getAdded(List<T> old, List<T> cur) {
        List<T> retList = new ArrayList<>();
        for (T t : cur)
            if (!old.contains(t))
                retList.add(t);
        return retList;
    }
}
