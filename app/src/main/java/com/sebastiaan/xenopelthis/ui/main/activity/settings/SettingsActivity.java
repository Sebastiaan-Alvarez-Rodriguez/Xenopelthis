package com.sebastiaan.xenopelthis.ui.main.activity.settings;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_assign);
        findGlobalViews();
        setupActionBar();
        prepareList();

    }

    private void findGlobalViews() {
        list = findViewById(R.id.list);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            actionbar.setTitle(R.string.settings_actionbar);
        }
    }

    private void prepareList() {

    }

}
