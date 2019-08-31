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

    public BarcodeConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOBarcode();
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
            List<product> matches = dbInterface.getForBarcode(barcode);
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
            List<product> matches = dbInterface.getForBarcode(barcode);
            listener.onResult(matches.isEmpty());
        });
    }

    /**
     * Get current barcodes for a given product
     * @param id The product id
     * @param listener Result callback
     */
    public void getBarcodes(long id, ResultListener<List<barcode>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getAllForProduct(id)));
    }

    /**
     * Get current products for a given barcode
     * @param barcode The barcode
     * @param listener Result callback
     */
    public void getProducts(String barcode, ResultListener<List<product>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getForBarcode(barcode)));
    }

    public void getProductsCount(String barcode, ResultListener<Long> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> listener.onResult(dbInterface.getProductsCount(barcode)));
    }
//
//    public void deleteForProduct(List<Long> ids) {
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(() -> dbInterface.deleteForProduct(ids.toArray(new Long[]{})));
//    }
}
