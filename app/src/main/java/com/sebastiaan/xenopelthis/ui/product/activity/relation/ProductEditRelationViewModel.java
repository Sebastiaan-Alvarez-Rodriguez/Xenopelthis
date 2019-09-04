package com.sebastiaan.xenopelthis.ui.product.activity.relation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
import com.sebastiaan.xenopelthis.db.retrieve.repository.PSRepository;
import com.sebastiaan.xenopelthis.db.retrieve.repository.ProductRepository;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

import java.util.List;

class ProductEditRelationViewModel extends AndroidViewModel {
    private PSRepository repository;

     ProductEditRelationViewModel(@NonNull Application application) {
        super(application);
        repository = new PSRepository(application);
    }

    void updateProductWithSuppliers(long id, List<supplier> oldRelations, List<supplier> newRelations) {
         repository.updateProductWithSuppliers(id, oldRelations, newRelations);
    }
}
