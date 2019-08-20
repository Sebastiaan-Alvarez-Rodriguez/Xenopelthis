package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.datatypes.ProductAndAmount;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.BarcodeViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.ProductViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.InventoryEditActivity;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: change to 3 activities, one for 0 products, one for 1 product and one for >1 products

public class MainBarcodeActivity extends AppCompatActivity implements ActionListener<product> {
    private final static int REQ_ADD = 0, REQ_BARCODE = 1;
    private ImageButton scanButton;
    private TextView assignmentText;
    private Button assignButton, unassignButton, addToInvButton;
    private RecyclerView list;

    private AdapterAction adapter;
    private BarcodeConstant barcodeConstant;

    private String barcodeString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeConstant = new BarcodeConstant(this);
        setContentView(R.layout.activity_barcode);
        findGlobalViews();
        setupButtons();
        setupActionBar();
        Intent intent = getIntent();
        if (intent.hasExtra("barcode"))
            barcodeString = intent.getStringExtra("barcode");
        prepareList();
        setButtonVisibilities();
    }

    private void setButtonVisibilities() {
        if (adapter.getItemCount() == 0) {
            unassignButton.setVisibility(View.GONE);
            addToInvButton.setVisibility(View.GONE);
        } else if (adapter.getItemCount() == 1 || adapter.getSelectedCount() == 1) {
            unassignButton.setVisibility(View.VISIBLE);
            addToInvButton.setVisibility(View.VISIBLE);
        } else {
            unassignButton.setVisibility(View.VISIBLE);
            addToInvButton.setVisibility(View.GONE);
        }
    }

    private void findGlobalViews() {
        scanButton = findViewById(R.id.barcode_edit_button);
        assignmentText = findViewById(R.id.barcode_txt_assignment);
        assignButton = findViewById(R.id.barcode_btn_assign);
        list = findViewById(R.id.list);
        unassignButton = findViewById(R.id.barcode_btn_unassign);
        addToInvButton = findViewById(R.id.barcode_btn_invAdd);
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognitron.class);
            startActivityForResult(intent, REQ_BARCODE);
        });

        assignButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainBarcodeAddActivity.class);
            intent.putExtra("barcode", barcodeString);
            startActivityForResult(intent, REQ_ADD);
        });

        unassignButton.setOnClickListener(v -> {
            List<Long> productIds = adapter.getSelected().stream().map(product::getId).collect(Collectors.toList());
            barcodeConstant.deleteForProduct(productIds);
            ProductViewModel productModel = ViewModelProviders.of(this).get(ProductViewModel.class);
            for (Long id : productIds) {
                barcodeConstant.getAllForProduct(id, barcodes -> {
                    if (barcodes.isEmpty())
                        productModel.setHasBarcode(false, id);
                });
            }
        });

        addToInvButton.setOnClickListener(v -> {
            if (adapter.getSelectedCount() > 1)
                return;

            InventoryViewModel model = ViewModelProviders.of(this).get(InventoryViewModel.class);
            if (adapter.getItemCount() == 1 && adapter.getSelectedCount() == 0) {
                product p = adapter.getItems().get(0);
                if (p != null)
                    model.get(p.getId(), product -> startInventoryAdd(model, product, p));
            } else {
                Optional<product> p = adapter.getSelected().stream().findFirst();
                p.ifPresent(product1 -> model.get(product1.getId(), product -> startInventoryAdd(model, product, product1)));
            }
        });
    }

    private void startInventoryAdd(InventoryViewModel model, ProductAndAmount product, product p) {
        Intent intent = new Intent(this, InventoryEditActivity.class);

        if (product == null) {
            model.add(new inventory_item(p.getId(), 0L));
            intent.putExtra("amount", 0L);
        } else {
            intent.putExtra("amount", product.getAmount());
        }

        intent.putExtra("product", new ProductStruct(p));
        intent.putExtra("product-id", p.getId());

        startActivity(intent);
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Barcode");
        }
    }

    private void prepareList() {
        if (barcodeString == null || barcodeString.isEmpty())
            return;

        adapter = new AdapterAction(this) {
            @Override
            public void onClick(View view, int pos) { onLongClick(view, pos); }
        };

        BarcodeViewModel model = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        model.getForBarcodeLive(barcodeString).observe(this, adapter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            assignmentText.setText("This barcode is not assigned yet");
        } else if (adapter.getItemCount() == 1) {
            assignmentText.setText("Product for this barcode:");
        } else {
            assignmentText.setText("Products for this barcode:");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionModeChange(boolean actionMode) { }

    @Override
    public void onClick(product product) { }

    @Override
    public boolean onLongClick(product product) {
        setButtonVisibilities();
        return true;
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            barcodeString = data.getStringExtra("barcode");
            prepareList();
        }
        setButtonVisibilities();
    }
}
