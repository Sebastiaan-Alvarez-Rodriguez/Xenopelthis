package com.sebastiaan.xenopelthis.ui.constructs;

import android.os.Parcel;
import android.os.Parcelable;

import com.sebastiaan.xenopelthis.db.entity.barcode;

public class BarcodeStruct implements Parcelable {
    public String translation;

    public BarcodeStruct(String translation) {
        this.translation = translation;
    }

    protected BarcodeStruct(Parcel in) {
        translation = in.readString();
    }

    public static final Creator<BarcodeStruct> CREATOR = new Creator<BarcodeStruct>() {
        @Override
        public BarcodeStruct createFromParcel(Parcel in) {
            return new BarcodeStruct(in);
        }

        @Override
        public BarcodeStruct[] newArray(int size) {
            return new BarcodeStruct[size];
        }
    };

    public barcode toBarcode() {
        return new barcode(translation);
    }

    public barcode toBarcode(long id) {
        barcode b = toBarcode();
        b.setId(id);
        return b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(translation);
    }
}
