package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;

import java.util.List;


public class InventoryViewModel extends AndroidViewModel {
    private DAOInventory inventoryInterface;

    private LiveData<List<inventory_item>> liveList;

    public InventoryViewModel(Application application) {
        super(application);
        Database db = Database.getDatabase(application);
        inventoryInterface = db.getDAOInventory();
        liveList = inventoryInterface.getAllLive();
    }

    public LiveData<List<inventory_item>> getAll() { return liveList; }

}
