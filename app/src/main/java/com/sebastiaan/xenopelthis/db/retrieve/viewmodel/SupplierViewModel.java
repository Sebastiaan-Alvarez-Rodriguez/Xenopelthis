package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.util.Log;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.retrieve.ResultListener;
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

    public void add(@NonNull SupplierStruct s, @NonNull ResultListener<Long> idCallBack) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> idCallBack.onResult(dbInterface.add(s.toSupplier())));
        Log.e("Edit", "placed new supplier with name: " + s.name);
    }

    public void update(SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.update(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.delete(s.toSupplier(id)));
    }

    public void delete(@NonNull SupplierStruct s, long id, ResultListener<Void> callback) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            dbInterface.delete(s.toSupplier(id));
            callback.onResult(null);
        });
    }

    public void deleteByID(List<Long> ids) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> dbInterface.deleteByID(ids.toArray(new Long[]{})));
    }

}