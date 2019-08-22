package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import androidx.annotation.Nullable;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BarcodeConstant {

    private DAOBarcode dbInterface;
    private DAOProduct productInterface;

    public BarcodeConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOBarcode();
        productInterface = Database.getDatabase(context).getDAOProduct();
    }

    public void isUnique(String barcode, long id, ResultListener<Boolean> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<product> matches = dbInterface.getForBarcode(barcode);
            listener.onResult(
                    matches.stream()
                    .filter(product -> product.getId() != id)
                    .collect(Collectors.toList())
                    .isEmpty());
        });
    }
    public void getAllForProduct(long id, ResultListener<List<barcode>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<barcode> x = dbInterface.getAllForProduct(id);
            listener.onResult(x);
        });
    }

    public void getBarcodes(long id, ResultListener<List<barcode>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAllForProduct(id)));
    }

    public void getProducts(String barcode, ResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getForBarcode(barcode)));
    }

    public void getForBarcodeCount(String barcodeString, ResultListener<Integer> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAllForBarcodeCount(barcodeString)));
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

    public void deleteBarcodeForProducts(List<Long> ids, String barcode) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteBarcodeForProducts(barcode, ids.toArray(new Long[] {})));
    }
}
