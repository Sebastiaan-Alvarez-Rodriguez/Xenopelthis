package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.sebastiaan.xenopelthis.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Context context = getPreferenceManager().getContext();
        Preference language = getPreferenceManager().findPreference("language");
        if (language != null) {
            String currentLanguage = Locale.getDefault().getDisplayLanguage();
            language.setSummary(context.getString(R.string.settings_fragment_current_language, currentLanguage));

            language.setOnPreferenceClickListener(preference -> {
                if (preference.getKey().equals("language")) {
                    //TODO: make this preference a switch and switch between dutch and english
                }
                return true;
            });
        }
    }
}