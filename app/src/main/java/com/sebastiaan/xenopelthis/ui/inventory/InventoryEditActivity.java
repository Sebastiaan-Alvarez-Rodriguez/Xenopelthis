package com.sebastiaan.xenopelthis.ui.inventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;

public class InventoryEditActivity extends AppCompatActivity {
    private static final int REQ_RELATIONS = 0;

    private TextView productName, productDescription;
    private EditText amount;

    private InventoryViewModel model;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(InventoryViewModel.class);
        setContentView(R.layout.activity_inventory_edit);
        findGlobalViews();
        setupGlobalViews();
        setupIncr();
        setupDecr();
        setupActionBar();
    }

    private void findGlobalViews() {
        productName = findViewById(R.id.inventory_edit_productName);
        productDescription = findViewById(R.id.inventory_edit_productDescription);
        amount = findViewById(R.id.inventory_edit_amount);
    }

    private void setupGlobalViews() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product") && intent.hasExtra("product-id") && intent.hasExtra("amount")) {
            ProductStruct clickedProduct = intent.getParcelableExtra("product");
            productName.setText(clickedProduct.name);
            productDescription.setText(clickedProduct.description);
            amount.setText(String.valueOf(intent.getLongExtra("amount", -42)));
        }
    }

    private void setupActionBar() {
        Toolbar myToolbar;
        myToolbar = findViewById(R.id.inventory_edit_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Edit");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupIncr() {
        ImageButton add = findViewById(R.id.inventory_edit_add);
        add.setOnClickListener(v -> {
            int amount_nr = Integer.parseInt(amount.getText().toString());
            amount.setText(String.valueOf(++amount_nr));
        });

        add.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            long increaser = 700;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (handler != null)
                            return true;
                        handler = new Handler();
                        handler.postDelayed(adding, increaser);
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                        v.setPressed(false);
                    case MotionEvent.ACTION_UP:
                        if (handler == null)
                            return true;
                        handler.removeCallbacks(adding);
                        handler = null;
                        increaser = 500;
                        break;
                }
                return false;
            }

            Runnable adding = new Runnable() {
                @Override
                public void run() {
                    int amount_nr = Integer.parseInt(amount.getText().toString());
                    amount.setText(String.valueOf(++amount_nr));
                    if (increaser > 50)
                        increaser -= 35;
                    handler.postDelayed(this, increaser);
                }
            };
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDecr() {
        ImageButton sub = findViewById(R.id.inventory_edit_sub);
        sub.setOnClickListener(v -> {
            int amount_nr = Integer.parseInt(amount.getText().toString());
            if (amount_nr > 0)
                amount.setText(String.valueOf(--amount_nr));
        });

        sub.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            long decreaser = 700;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (handler != null)
                            return true;
                        handler = new Handler();
                        handler.postDelayed(subtracting,  decreaser);
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                        v.setPressed(false);
                    case MotionEvent.ACTION_UP:
                        if (handler == null)
                            return true;
                        handler.removeCallbacks(subtracting);
                        handler = null;
                        decreaser = 500;
                        break;
                }
                return false;
            }

            Runnable subtracting = new Runnable() {
                @Override
                public void run() {
                    int amount_nr = Integer.parseInt(amount.getText().toString());
                    if (amount_nr > 0)
                        amount.setText(String.valueOf(--amount_nr));
                    if (decreaser > 50)
                        decreaser -= 35;
                    handler.postDelayed(this, decreaser);
                }
            };
        });
    }

    private void checkInput() {
        if (amount.getText().toString().isEmpty()) {
            amount.setError("This field must be filled");
        } else {
            long amount_nr = Long.valueOf(amount.getText().toString());
            updateExisting(new inventory_item(getIntent().getLongExtra("product-id", -42), amount_nr));
        }
    }

    private void updateExisting(inventory_item item) {
        model.update(item);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu_continue:
                checkInput();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu_next, menu);
        return true;
    }
}
