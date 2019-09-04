package com.sebastiaan.xenopelthis.db.retrieve.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sebastiaan.xenopelthis.db.dao.DAOEntity;

import java.util.List;

public abstract class ViewModel<T> extends AndroidViewModel {
    private DAOEntity<T> tl;
    public ViewModel(Application application, ) {
        super(application);
        
    }
    public void add(T t) {

    }
    abstract public void deleteByID(List<Long> ids);
}
