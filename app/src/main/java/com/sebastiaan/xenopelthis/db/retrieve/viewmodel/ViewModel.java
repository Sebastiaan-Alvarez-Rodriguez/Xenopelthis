package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public abstract class ViewModel<T> extends AndroidViewModel {
    protected LiveData<List<T>> liveList;

    public ViewModel(Application application) {
        super(application);
    }

    public LiveData<List<T>> getAll() { return liveList; }

    abstract public void deleteByID(List<Long> ids);
}
