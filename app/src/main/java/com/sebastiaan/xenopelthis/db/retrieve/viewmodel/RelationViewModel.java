package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

//    public LiveData<List<product>> getAllForProduct(long id) {
//        return relationInterface.productsForSupplier(id);
//    }
//
//    public LiveData<List<supplier>> getAllForSupplier(long id) {
//        return relationInterface.suppliersForProduct(id);
//    }

    public void addProductWithSuppliers(ProductStruct p, List<supplier> suppliers) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<Long> supplierIDs = new ArrayList<>();
            for (supplier s : suppliers)
                supplierIDs.add(s.getId());

            long productID = productInterface.add(p.toProduct());
            addAll(supplierIDs, productID);
        });
    }

    public void addSupplierWithProducts(SupplierStruct s, List<product> products) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<Long> productIDs = new ArrayList<>();
            for (product p : products)
                productIDs.add(p.getId());

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

    public void updateProductWithSuppliers(ProductStruct p, long id, List<supplier> oldRelations, List<supplier> newRelations) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            productInterface.update(p.toProduct(id));

            List<supplier_product> removeList = getRemoved(oldRelations, newRelations).stream().map(supplier -> new supplier_product(supplier.getId(), id)).collect(Collectors.toList());
            List<supplier_product> addList = getAdded(oldRelations, newRelations).stream().map(supplier -> new supplier_product(supplier.getId(), id)).collect(Collectors.toList());
            if (!removeList.isEmpty())
                relationInterface.remove(removeList.toArray(new supplier_product[]{}));
            if (!addList.isEmpty())
                relationInterface.add(addList.toArray(new supplier_product[]{}));
        });
    }

    public void updateSupplierWithProducts(SupplierStruct s, long id, List<product> oldRelations, List<product> newRelations) {
         Executor myExecutor = Executors.newSingleThreadExecutor();
         myExecutor.execute(() -> {
             supplierInterface.update(s.toSupplier(id));
             List<supplier_product> removeList = getRemoved(oldRelations, newRelations).stream().map(product -> new supplier_product(id, product.getId())).collect(Collectors.toList());
             List<supplier_product> addList = getAdded(oldRelations, newRelations).stream().map(product -> new supplier_product(id, product.getId())).collect(Collectors.toList());
             if (!removeList.isEmpty())
                relationInterface.remove(removeList.toArray(new supplier_product[]{}));
             if (!addList.isEmpty())
                relationInterface.add(addList.toArray(new supplier_product[]{}));
         });
    }
}