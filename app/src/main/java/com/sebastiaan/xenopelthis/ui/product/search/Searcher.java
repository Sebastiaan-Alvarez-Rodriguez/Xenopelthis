package com.sebastiaan.xenopelthis.ui.product.search;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.entity.product;

import java.util.ArrayList;
import java.util.List;

public class Searcher extends com.sebastiaan.xenopelthis.ui.templates.search.Searcher<product> {


    public Searcher(EventListener<product> listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected List<product> filter(List<product> list, @NonNull String query) {
        List<product> returnlist = new ArrayList<>();
        for (product item : list)
            if (item.getName().toLowerCase().contains(query))
                returnlist.add(item);
        return returnlist;
    }
}
