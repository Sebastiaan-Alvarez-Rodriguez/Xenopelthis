package com.sebastiaan.xenopelthis.ui.product.activity.relation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.repository.PSRepository;

import java.util.List;

public class ProductEditRelationViewModel extends AndroidViewModel {
    private PSRepository repository;

    public ProductEditRelationViewModel(@NonNull Application application) {
        super(application);
        repository = new PSRepository(application);
    }

    void updateProductWithSuppliers(long id, List<supplier> oldRelations, List<supplier> newRelations) {
         repository.updateProductWithSuppliers(id, oldRelations, newRelations);
    }
}
