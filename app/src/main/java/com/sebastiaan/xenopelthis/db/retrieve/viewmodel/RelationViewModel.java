package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.util.ListUtil;

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


}
