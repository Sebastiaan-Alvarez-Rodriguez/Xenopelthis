package com.sebastiaan.xenopelthis.ui.supplier;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.retrieve.constant.SupplierConstant;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

public class SupplierEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private EditText name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress;

    private boolean editMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_edit);
        findGlobalViews();
        setupGlobalViews();
        setupActionBar();
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
            if (editMode)
                actionbar.setTitle("Edit");
            else
                actionbar.setTitle("Add");
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
        checker.isUnique(s.name, unique -> {
            if (!unique) {
                Log.e("Checker", "Situation: new but taken. 'This name is already taken'.");
                //TODO: Could ask user whether he wants to override the conflicting item... Is that user-friendly?
                // In code we just need to give a "product-id" of conflicting item to next activity for override
                Snackbar.make(findViewById(R.id.supplier_edit_layout), "'"+s.name+"' is already in use", Snackbar.LENGTH_LONG).show();
            } else {
                Log.e("Checker", "Situation: new and unique -> OK");
                Intent next = new Intent(this, SupplierEditRelationActivity.class);
                next.putExtra("result-supplier", s);
                startActivityForResult(next, REQ_RELATIONS);
            }
        });
    }

    private  void checkEdit(SupplierStruct s) {
        Intent intent = getIntent();
        SupplierStruct clickedSupplier = intent.getParcelableExtra("supplier");
        long clickedID = intent.getLongExtra("supplier-id", -42);

        Intent next = new Intent(this, SupplierEditRelationActivity.class);
        next.putExtra("result-supplier", s);
        next.putExtra("supplier-id", clickedID);

        if (s.name.equals(clickedSupplier.name)) {
            Log.e("Checker", "Situation: edit and name did not change -> OK");
            startActivityForResult(next, REQ_RELATIONS);
        } else {
            SupplierConstant checker = new SupplierConstant(this);
            checker.isUnique(s.name, unique -> {
                if (!unique) {
                    Log.e("Checker", "Situation: edit and name changed but taken. 'This name is already taken'.");
                    //TODO: Could ask user whether he wants to override the conflicting item... Is that user-friendly?
                    // In code we need to give a "product-id" of conflicting item to next activity for override
                    // AND we must somehow delete the existing item being edited in the database
                    Snackbar.make(findViewById(R.id.supplier_edit_layout), "'"+s.name+"' is already in use", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e("Checker", "Situation: edit and name changed and unique -> OK");
                    startActivityForResult(next, REQ_RELATIONS);
                }
            });
        }
    }

    private void showEmptyErrors(SupplierStruct s) {
        if (s.name.isEmpty())
            name.setError("This field must be filled");

        if (s.city.isEmpty())
            city.setError("This field must be filled");

        if (s.postalcode.isEmpty())
            postalcode.setError("This field must be filled");

        if (s.streetname.isEmpty())
            streetname.setError("This field must be filled");

        if (s.housenumber.isEmpty())
            housenumber.setError("This field must be filled");
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
