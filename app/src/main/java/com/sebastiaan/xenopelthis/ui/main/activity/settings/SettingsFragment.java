package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.sebastiaan.xenopelthis.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SettingsFragment extends PreferenceFragmentCompat {
    static final int REQUEST_CODE = 0;
    //TODO: perhaps retrieve from database?
    static final String DATABASE_NAME = "app.db";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

//        Context context = getContext();
//        context.getDatabasePath()

        Preference export = getPreferenceManager().findPreference("export");
        if (export != null) {
            export.setOnPreferenceClickListener(listener -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                //intent.type = "*/*
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Context context = getContext();
        if (data != null && context != null) {
            Uri userChosenUri = data.getData();
            if (userChosenUri != null) {
                try {
                    FileInputStream inStream = new FileInputStream(context.getDatabasePath(DATABASE_NAME));
                    ContentResolver contentResolver = context.getContentResolver();
                    OutputStream outStream = contentResolver.openOutputStream(userChosenUri);

                    //TODO: read and close the inStream and then outStream
                    //then input.copyTo(output)
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
//            val inStream = getDatabasePath(DATABASE_NAME).inputStream()
//            val outStream = contentResolver.openOutputStream(userChosenUri)
//
//            inStream.use { input ->
//                    outStream.use { output ->
//                    input.copyTo(output)
//                }
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
