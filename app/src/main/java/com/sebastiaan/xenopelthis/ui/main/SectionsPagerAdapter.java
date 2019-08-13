package com.sebastiaan.xenopelthis.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sebastiaan.xenopelthis.ui.inventory.InventoryFragment;
import com.sebastiaan.xenopelthis.ui.product.ProductFragment;
import com.sebastiaan.xenopelthis.ui.supplier.SupplierFragment;

class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Suppliers", "Products", "Inventory"};

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new SupplierFragment();
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