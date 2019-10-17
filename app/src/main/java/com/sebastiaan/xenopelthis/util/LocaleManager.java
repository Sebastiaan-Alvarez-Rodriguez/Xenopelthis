package com.sebastiaan.xenopelthis.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {
    //https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758

    public static final String LANGUAGE_ENGLISH = Locale.ENGLISH.getDisplayLanguage();
    public static final String LANGUAGE_DUTCH = "nl";
    private static final String LANGUAGE_KEY = "language";

    private final SharedPreferences prefs;

    public LocaleManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale(Context context) {
        return updateResources(context, getLocale());
    }

    public Context setNewLocale(Context context, Locale locale) {
        persistLanguage(locale);
        return updateResources(context, locale);
    }

    public Locale getLocale() {
        if (prefs.getBoolean(LANGUAGE_KEY, true))
            return new Locale(LANGUAGE_ENGLISH);
        else
            return new Locale((LANGUAGE_DUTCH));
    }

    public static Locale getLocale(Resources res) {
        return res.getConfiguration().getLocales().get(0);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(Locale locale) {
        if (prefs.edit().putBoolean(LANGUAGE_KEY, locale.getDisplayLanguage().equals(LANGUAGE_ENGLISH)).commit())
            Log.e("Debug", "commit succeeded");
        else
            Log.e("Debug", "commit failed");

    }

    private Context updateResources(Context context, Locale locale) {
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }
}
