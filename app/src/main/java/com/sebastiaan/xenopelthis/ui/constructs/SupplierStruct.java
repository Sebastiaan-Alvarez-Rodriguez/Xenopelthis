package com.sebastiaan.xenopelthis.ui.constructs;

import android.os.Parcel;
import android.os.Parcelable;

import com.sebastiaan.xenopelthis.db.entity.supplier;

@SuppressWarnings("WeakerAccess")
public class SupplierStruct implements Parcelable {

    public String name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress;

    public SupplierStruct(String name, String streetname, String housenumber, String city, String postalcode, String phonenumber, String emailaddress, String webaddress) {
        this.name = name;
        this.streetname = streetname;
        this.housenumber = housenumber;
        this.city = city;
        this.postalcode = postalcode;
        this.phonenumber = phonenumber;
        this.emailaddress = emailaddress;
        this.webaddress = webaddress;
    }

    public SupplierStruct(supplier s) {
        this.name = s.getName();
        this.streetname = s.getStreetname();
        this.housenumber = s.getHousenumber();
        this.city = s.getCity();
        this.postalcode = s.getPostalcode();
        this.phonenumber = s.getPhonenumber();
        this.emailaddress = s.getEmailaddress();
        this.webaddress = s.getWebaddress();
    }

    /**
     * @return the barcode corresponding to the member variables
     */
    public supplier toSupplier() {
        return new supplier(name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress);
    }

    /**
     * @param id the id of the barcode to be returned
     * @return the barcode corresponding to the given id
     */
    public supplier toSupplier(long id) {
        supplier s = toSupplier();
        s.setId(id);
        return s;
    }


    protected SupplierStruct(Parcel in) {
        name = in.readString();
        streetname = in.readString();
        housenumber = in.readString();
        city = in.readString();
        postalcode = in.readString();
        phonenumber = in.readString();
        emailaddress = in.readString();
        webaddress = in.readString();
    }

    public static final Creator<SupplierStruct> CREATOR = new Creator<SupplierStruct>() {
        @Override
        public SupplierStruct createFromParcel(Parcel in) {
            return new SupplierStruct(in);
        }

        @Override
        public SupplierStruct[] newArray(int size) {
            return new SupplierStruct[size];
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
        dest.writeString(streetname);
        dest.writeString(housenumber);
        dest.writeString(city);
        dest.writeString(postalcode);
        dest.writeString(phonenumber);
        dest.writeString(emailaddress);
        dest.writeString(webaddress);
    }
}
