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

    /**
     * Add a relation with a given supplier and product
     * @param supplierID the id of the supplier
     * @param productID the id of the product
     */
    public void add(long supplierID, long productID) {
        relationInterface.add(new supplier_product(supplierID,productID));
    }

    /**
     * Add relations with given suppliers and a product
     * @param supplierIDs the ids of the suppliers
     * @param productID the id of the product
     */
    public void addAll(List<Long> supplierIDs, long productID) {
        List<supplier_product> relations = new ArrayList<>();
        for (Long supplierID : supplierIDs)
            relations.add(new supplier_product(supplierID, productID));
        relationInterface.add(relations.toArray(new supplier_product[]{}));
    }

    /**
     * Add relations with given products and a supplier
     * @param supplierID the id of the supplier
     * @param productIDs the ids of the products
     */
    public void addAll(long supplierID, List<Long> productIDs) {
        List<supplier_product> relations = new ArrayList<>();
        for (Long productID : productIDs)
            relations.add(new supplier_product(supplierID, productID));
        relationInterface.add(relations.toArray(new supplier_product[]{}));
    }

    /**
     * Update relations of a given product with a list of old relations and new relations
     * @param id the id of the product
     * @param oldRelations the list of the old relations
     * @param newRelations the list of the new relations
     */
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

    /**
     * Update relations of a given supplier with a list of old relations and new relations
     * @param id the id of the supplier
     * @param oldRelations the list of the old relations
     * @param newRelations the list of the new relations
     */
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
