package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.sebastiaan.xenopelthis.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //Context context = getPreferenceManager().getContext();
        SwitchPreferenceCompat language = getPreferenceManager().findPreference("language");
        if (language != null) {
            language.setDefaultValue(Locale.getDefault().getDisplayLanguage().equals("en"));

//            language.setOnPreferenceChangeListener((preference, newValue) -> {
//                //TODO: set language
//                return true;
//            });

        }
    }
}