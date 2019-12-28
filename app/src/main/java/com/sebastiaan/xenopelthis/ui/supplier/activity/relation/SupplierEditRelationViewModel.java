package com.sebastiaan.xenopelthis.ui.supplier.activity.relation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.repository.PSRepository;

import java.util.List;

public class SupplierEditRelationViewModel extends AndroidViewModel {
    private PSRepository repository;

    public SupplierEditRelationViewModel(@NonNull Application application) {
        super(application);
        repository = new PSRepository(application);
    }

    void updateSupplierWithProducts(long id, List<product> oldRelations, List<product> newRelations) {
        repository.updateSupplierWithProducts(id, oldRelations, newRelations);
    }
}