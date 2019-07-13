package com.sebastiaan.xenopelthis.ui.constructs;

import android.os.Parcel;
import android.os.Parcelable;

import com.sebastiaan.xenopelthis.db.entity.product;

public class ProductStruct implements Parcelable {
    public String name, description;

    public ProductStruct(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public product toProduct() {
        return new product(name, description);
    }

    public product toProduct(long id) {
        product p = toProduct();
        p.setId(id);
        return p;
    }

    protected ProductStruct(Parcel in) {
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<ProductStruct> CREATOR = new Creator<ProductStruct>() {
        @Override
        public ProductStruct createFromParcel(Parcel in) {
            return new ProductStruct(in);
        }

        @Override
        public ProductStruct[] newArray(int size) {
            return new ProductStruct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }
}
