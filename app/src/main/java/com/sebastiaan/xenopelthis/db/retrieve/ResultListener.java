package com.sebastiaan.xenopelthis.db.retrieve;

/**
 * Simple interface to return query results of any type to UI
 * @param <T> Type of returned result
 */
public interface ResultListener<T> {
    void onResult(T result);
}
