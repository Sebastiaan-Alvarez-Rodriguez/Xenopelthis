package com.sebastiaan.xenopelthis.ui.barcode.activity.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sebastiaan.xenopelthis.db.retrieve.repository.BarcodeRepository;
import com.sebastiaan.xenopelthis.ui.constructs.BarcodeStruct;

class BarcodeMainViewModel extends AndroidViewModel {
    private BarcodeRepository repository;

    BarcodeMainViewModel(@NonNull Application application) {
        super(application);
        repository = new BarcodeRepository(application);
    }

    void delete(BarcodeStruct b, long id) {
        repository.delete(b.toBarcode(id));
    }
}
