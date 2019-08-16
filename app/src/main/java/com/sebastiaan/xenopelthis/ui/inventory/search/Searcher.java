package com.sebastiaan.xenopelthis.ui.inventory.search;

import androidx.annotation.NonNull;

import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;

import java.util.ArrayList;
import java.util.List;

public class Searcher extends com.sebastiaan.xenopelthis.ui.templates.search.Searcher<ProductAndAmount> {
    public Searcher(EventListener<ProductAndAmount> listener) { super(listener); }

    @NonNull
    @Override
    protected List<ProductAndAmount> filter(List<ProductAndAmount> list, @NonNull String query) {
        List<ProductAndAmount> returnList = new ArrayList<>();
        for (ProductAndAmount item : list)
            if (item.getP().getName().toLowerCase().contains(query))
                returnList.add(item);
        return returnList;
    }
}
