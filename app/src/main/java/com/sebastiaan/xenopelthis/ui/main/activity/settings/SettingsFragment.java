package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.sebastiaan.xenopelthis.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
