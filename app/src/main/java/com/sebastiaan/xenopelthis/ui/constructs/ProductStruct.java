package com.sebastiaan.xenopelthis.ui.constructs;

import android.os.Parcel;
import android.os.Parcelable;

import com.sebastiaan.xenopelthis.db.entity.product;

@SuppressWarnings("WeakerAccess")
public class ProductStruct implements Parcelable {
    public String name, description;
    public boolean hasBarcode;

    public ProductStruct(String name, String description) {
        this(name, description, false);
    }

    public ProductStruct(String name, String description, boolean hasBarcode) {
        this.name = name;
        this.description = description;
        this.hasBarcode = hasBarcode;
    }

    public ProductStruct(product p) {
        this.name = p.getName();
        this.description = p.getProductDescription();
        this.hasBarcode = p.getHasBarcode();
    }

    /**
     * @return the product corresponding to the member variables
     */
    public product toProduct() {
        return new product(name, description, hasBarcode);
    }

    /**
     * @param id the id of the product to be returned
     * @return the product corresponding to the given id
     */
    public product toProduct(long id) {
        product p = toProduct();
        p.setId(id);
        return p;
    }

    protected ProductStruct(Parcel in) {
        name = in.readString();
        description = in.readString();
        hasBarcode = in.readByte() == 1;
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

    /**
     * @see Parcel#writeString(String)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (hasBarcode ? 1 : 0));
    }
}
