package com.sebastiaan.xenopelthis.ui.supplier_product;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.List;

public class SuppliersForProductViewModel extends AndroidViewModel {

    private DAOSupplierProduct dbInterface;

    public SuppliersForProductViewModel(@NonNull Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOSupplierProduct();
    }

    public LiveData<List<supplier>> getAll(long id) {
        return dbInterface.suppliersForProduct(id);
    }
}
