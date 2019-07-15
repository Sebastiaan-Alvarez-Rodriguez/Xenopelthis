package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SupplierViewModel extends AndroidViewModel {
    private LiveData<List<supplier>> liveList;
    private DAOSupplier dbInterface;

    public SupplierViewModel(Application application) {
        super(application);
        dbInterface = Database.getDatabase(application).getDAOSupplier();
        liveList = dbInterface.getAllLive();
    }

    public LiveData<List<supplier>> getAll() {
        return liveList;
    }

    public void add(SupplierStruct s) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.add(s.toSupplier()));

        Log.e("Edit", "placed new supplier with name: " + s.name);
    }

    public void update(SupplierStruct s, long id) {
        dbInterface.update(s.toSupplier(id));
    }
}