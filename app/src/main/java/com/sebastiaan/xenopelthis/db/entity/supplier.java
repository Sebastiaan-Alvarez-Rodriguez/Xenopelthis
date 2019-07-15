package com.sebastiaan.xenopelthis.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;
import android.util.Log;

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class supplier {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name, streetname, housenumber, city, postalcode, phonenumber, emailaddress, webaddress;

    public supplier(String name, String streetname, String housenumber, String city, String postalcode, String phonenumber, String emailaddress, String webaddress) {
        this.name = name;
        this.streetname = streetname;
        this.housenumber = housenumber;
        this.city = city;
        this.postalcode = postalcode;
        this.phonenumber = phonenumber;
        this.emailaddress = emailaddress;
        this.webaddress = webaddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getWebaddress() {
        return webaddress;
    }

    public void setWebaddress(String webaddress) {
        this.webaddress = webaddress;
    }
}