package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RelationViewModel extends AndroidViewModel {

    private DAOSupplierProduct relationInterface;
    private DAOProduct productInterface;
    private DAOSupplier supplierInterface;

    public RelationViewModel(@NonNull Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        relationInterface = db.getDAOSupplierProduct();
        productInterface = db.getDAOProduct();
        supplierInterface = db.getDAOSupplier();
    }

    public LiveData<List<product>> getAllForProduct(long id) {
        return relationInterface.productsForSupplier(id);
    }

    public LiveData<List<supplier>> getAllForSupplier(long id) {
        return relationInterface.suppliersForProduct(id);
    }

    public void addProductWithSuppliers(ProductStruct p, List<Long> supplierIDs) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            long productID = productInterface.add(p.toProduct());
            addAll(supplierIDs, productID);
        });
    }

    public void addSupplierWithProducts(SupplierStruct s, List<Long> productIDs) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            long supplierID = supplierInterface.add(s.toSupplier());
            addAll(supplierID, productIDs);
        });
    }

    public void add(long supplierID, long productID) {
        relationInterface.add(new supplier_product(supplierID,productID));
    }

    public void addAll(List<Long> supplierIDs, long productID) {
        List<supplier_product> relations = new ArrayList<>();
        for (Long supplierID : supplierIDs)
            relations.add(new supplier_product(supplierID, productID));
        relationInterface.add(relations.toArray(new supplier_product[]{}));
    }

    public void addAll(long supplierID, List<Long> productIDs) {
        List<supplier_product> relations = new ArrayList<>();
        for (Long productID : productIDs)
            relations.add(new supplier_product(supplierID, productID));
        relationInterface.add(relations.toArray(new supplier_product[]{}));
    }
}
