package com.sebastiaan.xenopelthis.db.retrieve;

public interface ResultListener<T> {
    void onResult(T result);
}
