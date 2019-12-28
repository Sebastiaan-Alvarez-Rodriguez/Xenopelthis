package com.sebastiaan.xenopelthis.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@SuppressWarnings("unchecked")
@Dao
public interface DAOEntity<T> {
    @Insert
    long add(T t);

    @Insert
    List<Long> add(T... t);

    @Update
    int update(T... t);

    @Delete
    void delete(T... t);
}
