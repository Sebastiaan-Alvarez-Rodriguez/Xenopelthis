package com.sebastiaan.xenopelthis.ui.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        //TODO: put in function
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

    //TODO: remove or transform to proper function (see onCreate)
    private void setupGlobalViews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SupplierStruct supplier = extras.getParcelable("supplier");
            if (supplier != null) {
                name.setText(supplier.name);
                streetname.setText(supplier.streetname);
                housenumber.setText(supplier.housenumber);
                city.setText(supplier.city);
                postalcode.setText(supplier.postalcode);
                phonenumber.setText(supplier.phonenumber);
                emailaddress.setText(supplier.emailaddress);
                webaddress.setText(supplier.webaddress);
            }
        }
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.supplier_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        //TODO: add title dependant on editmode
        if (actionbar != null)
            actionbar.setDisplayHomeAsUpEnabled(true);
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
            } else {
                Log.e("Checker", "Situation: new and unique -> OK");
                //TODO: new supplier must be added
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
            //TODO: old item must be updated
            startActivityForResult(next, REQ_RELATIONS);
        } else {
            SupplierConstant checker = new SupplierConstant(this);
            checker.isUnique(s.name, unique -> {
                if (!unique) {
                    Log.e("Checker", "Situation: edit and name changed but taken. 'This name is already taken'.");
                } else {
                    Log.e("Checker", "Situation: edit and name changed and unique -> OK");
                    //TODO: old item must be replaced
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
