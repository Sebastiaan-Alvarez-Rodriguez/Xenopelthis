package com.sebastiaan.xenopelthis.db.retrieve.repository;

import android.content.Context;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;
import com.sebastiaan.xenopelthis.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PSRepository {
    private DAOSupplierProduct relationInterface;


    public PSRepository(Context applicationContext) {
        relationInterface = Database.getDatabase(applicationContext).getDAOSupplierProduct();
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

    public void updateProductWithSuppliers(long id, List<supplier> oldRelations, List<supplier> newRelations) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<supplier_product> removeList = ListUtil.getRemoved(oldRelations, newRelations).stream().map(supplier -> new supplier_product(supplier.getId(), id)).collect(Collectors.toList());
            List<supplier_product> addList = ListUtil.getAdded(oldRelations, newRelations).stream().map(supplier -> new supplier_product(supplier.getId(), id)).collect(Collectors.toList());
            if (!removeList.isEmpty())
                relationInterface.delete(removeList.toArray(new supplier_product[]{}));
            if (!addList.isEmpty())
                relationInterface.add(addList.toArray(new supplier_product[]{}));
        });
    }

    public void updateSupplierWithProducts(long id, List<product> oldRelations, List<product> newRelations) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            List<supplier_product> removeList = ListUtil.getRemoved(oldRelations, newRelations).stream().map(product -> new supplier_product(id, product.getId())).collect(Collectors.toList());
            List<supplier_product> addList = ListUtil.getAdded(oldRelations, newRelations).stream().map(product -> new supplier_product(id, product.getId())).collect(Collectors.toList());
            if (!removeList.isEmpty())
                relationInterface.delete(removeList.toArray(new supplier_product[]{}));
            if (!addList.isEmpty())
                relationInterface.add(addList.toArray(new supplier_product[]{}));
        });
    }
}
