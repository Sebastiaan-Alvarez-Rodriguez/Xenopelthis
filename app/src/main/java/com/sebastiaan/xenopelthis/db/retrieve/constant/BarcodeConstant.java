package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;

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
}
