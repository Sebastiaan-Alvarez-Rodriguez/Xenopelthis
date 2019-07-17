package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.entity.product;


public class InventoryViewModel extends AndroidViewModel {
    private DAOInventory inventoryInterface;
    private DAOProduct productInterface;

    public InventoryViewModel(Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        inventoryInterface = db.getDAOInventory();
        productInterface = db.getDAOProduct();
    }
}
