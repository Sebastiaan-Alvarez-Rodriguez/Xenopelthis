package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.sebastiaan.xenopelthis.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final int BUFFER_SIZE = 2056;
    static final int REQUEST_CODE_EXPORT = 0;
    //TODO: perhaps retrieve from database?
    static final String DATABASE_NAME = "app.db";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference export = getPreferenceManager().findPreference("export");
        if (export != null) {
            export.setOnPreferenceClickListener(listener -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT).setType("*/*");
                startActivityForResult(intent, REQUEST_CODE_EXPORT);
                return true;
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Context context = getContext();
        if (requestCode == REQUEST_CODE_EXPORT && data != null && context != null) {
            Uri userChosenUri = data.getData();
            if (userChosenUri != null) {
                try {
                    FileInputStream inStream = new FileInputStream(context.getDatabasePath(DATABASE_NAME));
                    ContentResolver contentResolver = context.getContentResolver();
                    OutputStream outStream = contentResolver.openOutputStream(userChosenUri);

                    copy(inStream, outStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void copy(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in_buf = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out_buf = new BufferedOutputStream(output, BUFFER_SIZE);

        int n = 0;
        try {
            while ((n = in_buf.read(buffer, 0, BUFFER_SIZE)) != -1)
                out_buf.write(buffer, 0, n);
        } finally {
            try {
                out_buf.close();
            } catch (IOException e) {
                Log.e("exception: ", e.getMessage(), e);
            }

            try {
                in_buf.close();
            } catch (IOException e) {
                Log.e("exception: ", e.getMessage(), e);
            }

        }
    }
}
