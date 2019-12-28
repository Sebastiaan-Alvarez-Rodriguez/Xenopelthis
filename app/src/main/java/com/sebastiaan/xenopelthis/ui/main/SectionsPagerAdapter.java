package com.sebastiaan.xenopelthis.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.ui.inventory.InventoryFragment;
import com.sebastiaan.xenopelthis.ui.product.ProductFragment;
import com.sebastiaan.xenopelthis.ui.supplier.SupplierFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static String[] TAB_TITLES;

    public SectionsPagerAdapter(FragmentManager fm, Context c) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        TAB_TITLES = new String[] {
                c.getString(R.string.tab_text_1),
                c.getString(R.string.tab_text_2),
                c.getString(R.string.tab_text_3)
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1: return new ProductFragment();
            case 2: return new InventoryFragment();
            default: return new SupplierFragment();
        }
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}