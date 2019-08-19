package com.sebastiaan.xenopelthis.ui.mainBarcode;

import android.content.Intent;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.retrieve.constant.BarcodeConstant;
import com.sebastiaan.xenopelthis.db.retrieve.viewmodel.InventoryViewModel;
import com.sebastiaan.xenopelthis.recognition.Recognitron;
import com.sebastiaan.xenopelthis.ui.constructs.ProductStruct;
import com.sebastiaan.xenopelthis.ui.inventory.InventoryEditActivity;
import com.sebastiaan.xenopelthis.ui.product.view.adapter.AdapterAction;
import com.sebastiaan.xenopelthis.ui.templates.adapter.ActionListener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainBarcodeActivity extends AppCompatActivity implements ActionListener<product> {
    private final static int REQ_ADD = 0, REQ_BARCODE = 1;
    private ImageButton scanButton;
    private TextView assignmentText;
    private Button assignButton, unassignButton, addToInvButton;
    private RecyclerView list;


    private List<product> editOldProducts;
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

        Intent intent = new Intent(this, Recognitron.class);
        startActivityForResult(intent, REQ_BARCODE);
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

        unassignButton.setVisibility(View.GONE);
        unassignButton.setOnClickListener(v -> barcodeConstant.deleteForProduct(adapter.getSelected().stream().map(product::getId).collect(Collectors.toList())));

        addToInvButton.setVisibility(View.GONE);

        addToInvButton.setOnClickListener(v -> {
            if (adapter.getSelectedCount() > 1)
                return;

            InventoryViewModel model = new InventoryViewModel(getApplication());
            if (editOldProducts.size() == 1 && adapter.getSelectedCount() == 0) {
                product p = editOldProducts.get(0);
                if (p != null) {
                    model.get(p.getId(), product -> {
                       if (product == null)
                          model.add(new inventory_item(p.getId(), 0));
                        Intent intent = new Intent(this, InventoryEditActivity.class);
                        intent.putExtra("product", new ProductStruct(p));
                        intent.putExtra("product-id", p.getId());
                        intent.putExtra("amount", 0L);
                        startActivity(intent);
                    });
                }
            } else {
                Optional<product> p = adapter.getSelected().stream().findFirst();
                p.ifPresent(product1 -> model.get(product1.getId(), product -> {
                    if (product == null)
                        model.add(new inventory_item(product1.getId(), 0));
                    Intent intent = new Intent(this, InventoryEditActivity.class);
                    intent.putExtra("product", new ProductStruct(product1));
                    intent.putExtra("product-id", product1.getId());
                    intent.putExtra("amount", 0L);
                    startActivity(intent);
                }));
            }
        });
    }

    private void setupActionBar() {
        Toolbar myToolbar = findViewById(R.id.barcode_edit_toolbar);
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

        barcodeConstant.getProducts(barcodeString, productList -> {
            if (productList.isEmpty()) {
                assignmentText.setText("This barcode is not assigned yet");
            } else if (productList.size() == 1) {
                assignmentText.setText("Product for this barcode:");
                unassignButton.setVisibility(View.VISIBLE);
                addToInvButton.setVisibility(View.VISIBLE);
            } else {
                assignmentText.setText("Products for this barcode:");
            }

            adapter = new AdapterAction(this);
            adapter.onChanged(productList);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            list.setAdapter(adapter);
            editOldProducts = productList;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionModeChange(boolean actionMode) {
        if (actionMode) {
            unassignButton.setVisibility(View.VISIBLE);
            if (adapter.getSelectedCount() == 1)
                addToInvButton.setVisibility(View.VISIBLE);
            else
                addToInvButton.setVisibility(View.GONE);
        } else {
            unassignButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(product product) { }

    @Override
    public boolean onLongClick(product product) {
        if (adapter.getSelectedCount() == 1) {
            addToInvButton.setVisibility(View.VISIBLE);
            unassignButton.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_BARCODE && resultCode == RESULT_OK && data != null && data.hasExtra("barcode")) {
            barcodeString = data.getStringExtra("barcode");
            prepareList();
        } else if (requestCode == REQ_ADD && resultCode == RESULT_OK) {
            prepareList();
        }
    }
}
