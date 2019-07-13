package com.sebastiaan.xenopelthis.ui.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.ui.constructs.SupplierStruct;

public class SupplierEditActivity extends AppCompatActivity {

    private EditText name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_edit);
        findGlobalViews();
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

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.supplier_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
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
    private boolean checkInput(SupplierStruct s) {
        if (s.name.isEmpty() || s.city.isEmpty() || s.postalcode.isEmpty() || s.streetname.isEmpty() || s.housenumber.isEmpty()) {
            showEmptyErrors(s);
            return false;
        }
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_done:
                SupplierStruct s = getInput();
                if (checkInput(s)) {
                    Intent result = new Intent();
                    result.putExtra("result", s);
                    setResult(RESULT_OK, result);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_ok, menu);
        return true;
    }
}
