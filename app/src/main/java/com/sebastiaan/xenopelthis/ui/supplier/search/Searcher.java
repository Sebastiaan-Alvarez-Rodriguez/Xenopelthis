package com.sebastiaan.xenopelthis.ui.supplier.search;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.supplier;

import java.util.ArrayList;
import java.util.List;

public class Searcher extends com.sebastiaan.xenopelthis.ui.templates.search.Searcher<supplier> {

    public Searcher(EventListener<supplier> listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected List<supplier> filter(List<supplier> list, @NonNull String query) {
        List<supplier> returnList = new ArrayList<>();
        for (supplier item : list)
            if (item.getName().toLowerCase().contains(query))
                returnList.add(item);
        return returnList;
    }
}
