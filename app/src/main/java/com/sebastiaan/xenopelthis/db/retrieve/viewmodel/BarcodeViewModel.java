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

    public void update(@NonNull BarcodeStruct b, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.update(b.toBarcode(id)));
    }

    /**
     * Calculates the difference between two lists of barcodes for one product,
     * removes the removed items in the database and adds the added items in the database.
     * You should only call this function when you have a list which might have changed in UI,
     * and you want to update this in DB.
     * @param barcodesOld the old list, before the changes
     * @param barcodesNew the new list, after the changes
     * @param productID the productID for the list of barcodes
     */
    public void updateList(List<barcode> barcodesOld, List<barcode> barcodesNew, long productID) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<barcode> removeList = ListUtil.getRemoved(barcodesOld, barcodesNew);
            List<barcode> addedList = ListUtil.getAdded(barcodesOld, barcodesNew);

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
}
