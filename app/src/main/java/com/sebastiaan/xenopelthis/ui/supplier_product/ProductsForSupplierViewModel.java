package com.sebastiaan.xenopelthis.ui.supplier_product;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;

public class ProductsForSupplierViewModel extends AndroidViewModel {

    private DAOSupplierProduct dbInterface;

    public ProductsForSupplierViewModel(@NonNull Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOSupplierProduct();
    }

    public LiveData<List<product>> getAll(long id) {
        return dbInterface.productsForSupplier(id);
    }
}
