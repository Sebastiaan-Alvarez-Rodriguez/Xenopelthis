package com.sebastiaan.xenopelthis.db;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.sebastiaan.xenopelthis.db.dao.DAOBarcode;
import com.sebastiaan.xenopelthis.db.dao.DAOInventory;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplier;
import com.sebastiaan.xenopelthis.db.dao.DAOSupplierProduct;
import com.sebastiaan.xenopelthis.db.entity.barcode;
import com.sebastiaan.xenopelthis.db.entity.inventory_item;
import com.sebastiaan.xenopelthis.db.entity.product;
import com.sebastiaan.xenopelthis.db.entity.supplier;
import com.sebastiaan.xenopelthis.db.entity.supplier_product;

@androidx.room.Database(entities = {product.class, supplier.class, supplier_product.class, barcode.class, inventory_item.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static volatile Database INSTANCE;
    public static final String DB_NAME = "app.db";

    public abstract DAOProduct getDAOProduct();
    public abstract DAOSupplier getDAOSupplier();
    public abstract DAOSupplierProduct getDAOSupplierProduct();
    public abstract DAOBarcode getDAOBarcode();
    public abstract DAOInventory getDAOInventory();

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null || !INSTANCE.isOpen()) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
    public static void rebuildDatabase(final Context context) {
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME).build();
    }
}