package com.sebastiaan.xenopelthis.ui.constructs;

import android.os.Parcel;
import android.os.Parcelable;

import com.sebastiaan.xenopelthis.db.entity.barcode;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BarcodeStruct implements Parcelable {
    public String translation;

    public BarcodeStruct(String translation) {
        this.translation = translation;
    }

    public BarcodeStruct(barcode b) {this.translation = b.getTranslation();}

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

    /**
     * @return the barcode corresponding to the translation
     */
    public barcode toBarcode() {
        return new barcode(translation);
    }

    /**
     * @param id the id of the barcode to be returned
     * @return the barcode corresponding to the given id
     */
    public barcode toBarcode(long id) {
        barcode b = toBarcode();
        b.setId(id);
        return b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @see Parcel#writeString(String)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(translation);
    }
}
