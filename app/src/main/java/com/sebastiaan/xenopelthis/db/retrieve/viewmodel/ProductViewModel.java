package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductViewModel extends com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ViewModel<product> {
    private DAOProduct dbInterface;

    public ProductViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOProduct();
        cacheList = dbInterface.getAllLive();
    }


}

