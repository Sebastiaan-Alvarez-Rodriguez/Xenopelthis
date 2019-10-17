package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.ui.main.activity.main.MainActivity;
import com.sebastiaan.xenopelthis.util.App;

import java.util.Locale;

import static com.sebastiaan.xenopelthis.util.LocaleManager.LANGUAGE_DUTCH;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Context context = getActivity();
        SwitchPreferenceCompat language = getPreferenceManager().findPreference("language");
        if (language != null) {
            //App.localeManager.setNewLocale(context, Resources.getSystem().getConfiguration().getLocales().get(0));
            language.setChecked(App.localeManager.getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage()));
            Log.e("Debug", Locale.getDefault().getDisplayLanguage());
            Log.e("Debug", "system: " + Resources.getSystem().getConfiguration().locale.getLanguage());
            Log.e("Debug", "App: " + App.localeManager.getLocale().getLanguage());

            //language.setChecked(Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage().equals("en"));
            //language.setChecked(Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage()));

            language.setOnPreferenceChangeListener((preference, newValue) -> {
                if (language.isChecked()) {
                    App.localeManager.setNewLocale(context, Locale.ENGLISH);
                    Log.e("Debug", "language checked");
                } else {
                    App.localeManager.setNewLocale(context, new Locale(LANGUAGE_DUTCH));
                    Log.e("Debug", "language not checked");
                }

                language.setChecked(!language.isChecked());

                //TODO: change to home activity / parent = parent
                Intent i = new Intent(context, SettingsActivity.class);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                return true;
            });
        }
    }

}