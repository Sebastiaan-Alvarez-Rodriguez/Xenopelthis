package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndID;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class InventoryViewModel extends AndroidViewModel {
    private DAOInventory inventoryInterface;

    private LiveData<List<ProductAndID>> liveList;

    public InventoryViewModel(Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        inventoryInterface = db.getDAOInventory();
        liveList = inventoryInterface.getAllLive();
    }

    public LiveData<List<ProductAndID>> getAll() { return liveList; }

    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> inventoryInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}
