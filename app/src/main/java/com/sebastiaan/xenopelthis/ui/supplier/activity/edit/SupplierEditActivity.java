package com.sebastiaan.xenopelthis.ui.supplier.activity.edit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.SupplierConstant;
import com.sebastiaan.xenopelthis.ui.BaseActivity;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;
import com.sebastiaan.xenopelthis.ui.supplier.activity.relation.SupplierEditRelationActivity;
import com.sebastiaan.xenopelthis.ui.supplier.view.dialog.OverrideDialog;
import com.sebastiaan.xenopelthis.ui.templates.dialog.OverrideListener;

public class SupplierEditActivity extends BaseActivity {
    private static final int REQ_RELATIONS = 0;

    private EditText name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress;

    private SupplierEditViewModel model;
    private boolean editMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_edit);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
        model = ViewModelProviders.of(this).get(SupplierEditViewModel.class);
    }

    private void findGlobalViews() {
        name = findViewById(R.id.supplier_edit_name);
        streetname = findViewById(R.id.supplier_edit_streetname);
        housenumber = findViewById(R.id.supplier_edit_housenumber);
        city = findViewById(R.id.supplier_edit_city);
        postalcode = findViewById(R.id.supplier_edit_postalcode);
        phonenumber = findViewById(R.id.supplier_edit_phonenumber);
        emailaddress = findViewById(R.id.supplier_edit_email);
        webaddress = findViewById(R.id.supplier_edit_webaddress);
    }

    private void setupGlobalViews() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("supplier-id") && intent.hasExtra("supplier")) {
            editMode = true;
            SupplierStruct clickedSupplier = intent.getParcelableExtra("supplier");
            name.setText(clickedSupplier.name);
            streetname.setText(clickedSupplier.streetname);
            housenumber.setText(clickedSupplier.housenumber);
            city.setText(clickedSupplier.city);
            postalcode.setText(clickedSupplier.postalcode);
            phonenumber.setText(clickedSupplier.phonenumber);
            emailaddress.setText(clickedSupplier.emailaddress);
            webaddress.setText(clickedSupplier.webaddress);
        }
    }


    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.supplier_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            Drawable icon = myToolbar.getNavigationIcon();
            if (icon != null) {
                icon.setColorFilter(getResources().getColor(R.color.colorWindowBackground, null), PorterDuff.Mode.SRC_IN);
                myToolbar.setNavigationIcon(icon);
            }
            if (editMode)
                actionbar.setTitle(R.string.supplier_edit_activity_actionbar_edit);
            else
                actionbar.setTitle(R.string.supplier_edit_activity_actionbar_add);
        }
    }

    private SupplierStruct getInput() {
        return new SupplierStruct(
                name.getText().toString(),
                streetname.getText().toString(),
                housenumber.getText().toString(),
                city.getText().toString(),
                postalcode.getText().toString(),
                phonenumber.getText().toString(),
                emailaddress.getText().toString(),
                webaddress.getText().toString());
    }
    private void checkInput(SupplierStruct s) {
        if (s.name.isEmpty() || s.city.isEmpty() || s.postalcode.isEmpty() || s.streetname.isEmpty() || s.housenumber.isEmpty()) {
            showEmptyErrors(s);
        } else {
            if (editMode)
                checkEdit(s);
            else
                checkNew(s);
        }
    }

    private void checkNew(SupplierStruct s) {
        SupplierConstant checker = new SupplierConstant(this);
        checker.isUnique(s.name, conflictSupplier -> {
            if (conflictSupplier != null) {
                Log.e("Checker", "Situation: new but taken. 'This name is already taken'.");
                showOverrideDialog(new SupplierStruct(conflictSupplier), conflictSupplier.getId(), () -> insertNew(s));
            } else {
                Log.e("Checker", "Situation: new and unique -> OK");
                insertNew(s);
            }
        });
    }

    private void insertNew(SupplierStruct s) {
        model.add(s, assignedID -> {
           Intent next = new Intent(this, SupplierEditRelationActivity.class);
           next.putExtra("supplier-id", assignedID);
           startActivity(next);
           finish();
        });
    }

    private  void checkEdit(SupplierStruct s) {
        Intent intent = getIntent();
        SupplierStruct clickedSupplier = intent.getParcelableExtra("supplier");
        long clickedID = intent.getLongExtra("supplier-id", -42);

        if (s.name.equals(clickedSupplier.name)) {
            Log.e("Checker", "Situation: edit and name did not change -> OK");
            updateExisting(s, clickedID);
        } else {
            SupplierConstant checker = new SupplierConstant(this);
            checker.isUnique(s.name, conflictSupplier -> {
                if (conflictSupplier != null) {
                    Log.e("Checker", "Situation: edit and name changed but taken. 'This name is already taken'.");
                    showOverrideDialog(new SupplierStruct(conflictSupplier), conflictSupplier.getId(), () -> updateExisting(s, clickedID));
                } else {
                    Log.e("Checker", "Situation: edit and name changed and unique -> OK");
                    updateExisting(s, clickedID);
                }
            });
        }
    }

    private void updateExisting(SupplierStruct s, long id) {
        model.update(s, id);
        Intent next = new Intent(this, SupplierEditRelationActivity.class);
        next.putExtra("supplier-id", id);
        startActivity(next);
        finish();
    }

    private void showOverrideDialog(SupplierStruct s, long conflictID, OverrideListener overrideListener) {
        runOnUiThread(() -> {
            OverrideDialog dialog = new OverrideDialog(this);
            dialog.showDialog(s, conflictID, () -> model.delete(s, conflictID, nothing ->overrideListener.onOverride()));
        });
    }

    private void showEmptyErrors(SupplierStruct s) {
        if (s.name.isEmpty())
            name.setError(getString(R.string.supplier_edit_activity_empty_error));

        if (s.city.isEmpty())
            city.setError(getString(R.string.supplier_edit_activity_empty_error));

        if (s.postalcode.isEmpty())
            postalcode.setError(getString(R.string.supplier_edit_activity_empty_error));

        if (s.streetname.isEmpty())
            streetname.setError(getString(R.string.supplier_edit_activity_empty_error));

        if (s.housenumber.isEmpty())
            housenumber.setError(getString(R.string.supplier_edit_activity_empty_error));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_RELATIONS && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                SupplierStruct s = getInput();
                checkInput(s);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_next, menu);
        return true;
    }
}
