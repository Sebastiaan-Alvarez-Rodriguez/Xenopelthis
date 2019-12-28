package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.ui.main.activity.main.MainActivity;
import com.sebastiaan.xenopelthis.util.FileUtil;

//https://github.com/android/architecture-components-samples/issues/340

public class SettingsActivity extends AppCompatActivity {
    private Button importButton, exportButton;

    private static final int REQUEST_CODE_EXPORT = 0;
    private static final int REQUEST_CODE_IMPORT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, new SettingsFragment()).commit();
        setContentView(R.layout.activity_settings);
        findGlobalViews();
        setupButtons();
        setupActionBar();
    }

    private void findGlobalViews() {
        importButton = findViewById(R.id.settings_import);
        exportButton = findViewById(R.id.settings_export);
    }

    private void setupButtons() {
        importButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            Intent finalIntent = Intent.createChooser(intent, "Select file to import from");
            startActivityForResult(finalIntent, REQUEST_CODE_IMPORT);
        });
        exportButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT).setType("*/*");
            Intent finalIntent = Intent.createChooser(intent, "Select location to export to");
            startActivityForResult(finalIntent, REQUEST_CODE_EXPORT);
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.settings_actionbar);
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMPORT) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri userChosenUri = data.getData();
                assert userChosenUri != null;
                Database database = Database.getDatabase(this);
                database.close();
                if (FileUtil.importDatabase(this, userChosenUri)) {
                    Database.rebuildDatabase(this);
                    Log.e("IO", "Import OK");
                    Intent reboot = new Intent(this, MainActivity.class);
                    reboot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(reboot);
                    finish();
                } else {
                    Log.e("IO", "Import FAILED");
                }
            } else {
                Log.e("IO","Import request returned not OK");
            }
        } else if (requestCode == REQUEST_CODE_EXPORT) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri userChosenUri = data.getData();
                assert userChosenUri != null;
                Database database = Database.getDatabase(this);
                database.close();
                if (FileUtil.exportDatabase(this, userChosenUri)) {
                    Database.rebuildDatabase(this);
                    Log.e("IO", "Export OK");
                    Intent reboot = new Intent(this, MainActivity.class);
                    reboot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(reboot);
                    finish();
                } else {
                    Log.e("IO", "Export FAILED");
                }
            } else {
                Log.e("IO","Export request returned not OK");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
