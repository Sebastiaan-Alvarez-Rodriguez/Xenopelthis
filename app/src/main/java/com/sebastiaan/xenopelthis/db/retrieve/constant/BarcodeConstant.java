package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import androidx.annotation.Nullable;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarcodeConstant {

    private DAOBarcode dbInterface;

    public BarcodeConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOBarcode();
    }

    public void getAllForProduct(long id, ResultListener<List<barcode>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<barcode> x = dbInterface.getAllForProduct(id);
            listener.onResult(x);
        });
    }

    public void isUnique(List<barcode> barcodes, long id, ResultListener<List<barcode>> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
           List<barcode> conflicting = new ArrayList<>();
           for (barcode b : barcodes) {
               barcode found = dbInterface.findExact(b.getTranslation());
               if (found != null && found.getId() != id)
               conflicting.add(found);
           }
           listener.onResult(conflicting);
        });
    }

    public void getBarcode(long id, ResultListener<barcode> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
           listener.onResult(dbInterface.get(id));
        });
    }

    public void getProduct(String barcode, ResultListener<product> listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            listener.onResult(dbInterface.getForBarcode(barcode));
        });
    }
}
