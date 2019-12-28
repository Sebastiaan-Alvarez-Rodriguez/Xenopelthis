package com.sebastiaan.xenopelthis.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.Nullable;

/**
 * Class representing supplier entries
 */

@SuppressWarnings("unused")
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

    /**
     * Function to get the associated id
     * @return the object's id
     */
    public long getId() {
        return id;
    }

    /**
     * Function to set the associated id
     * @param id the long to which the object's id should be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Function to get the associated name
     * @return the object's name
     */
    public String getName() {
        return name;
    }

    /**
     * Function to set the associated name
     * @param name the String to which the object's name should be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function to get the associated streetname
     * @return the object's streetname
     */
    public String getStreetname() {
        return streetname;
    }

    /**
     * Function to set the associated streetname
     * @param streetname the String to which the object's streetname should be set
     */
    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    /**
     * Function to get the associated housenumber
     * @return the object's housenumber
     */
    public String getHousenumber() {
        return housenumber;
    }

    /**
     * Function to set the associated housenumber
     * @param housenumber the String to which the object's housenumber should be set
     */
    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    /**
     * Function to get the associated city
     * @return the object's city
     */
    public String getCity() {
        return city;
    }

    /**
     * Function to set the associated city
     * @param city the String to which the object's city should be set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Function to get the associated postalcode
     * @return the object's postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * Function to set the associated postalcode
     * @param postalcode the String to which the object's postalcode should be set
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    /**
     * Function to get the associated phonenumber
     * @return the object's phonenumber
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * Function to set the associated phonenumber
     * @param phonenumber the String to which the object's phonenumber should be set
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * Function to get the associated emailaddres
     * @return the object's emailaddress
     */
    public String getEmailaddress() {
        return emailaddress;
    }

    /**
     * Function to set the associated emailaddress
     * @param emailaddress the String to which the object's emailaddress should be set
     */
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    /**
     * Function to get the associated webaddress
     * @return the object's webaddress
     */
    public String getWebaddress() {
        return webaddress;
    }

    /**
     * Function to set the associated webaddress
     * @param webaddress the String to which the object's webaddress should be set
     */
    public void setWebaddress(String webaddress) {
        this.webaddress = webaddress;
    }

    /**
     * Function to compare an object to this supplier on equality
     * @param obj the object to be compared to this supplier
     * @return true if there is equality, false on inequality or if the object is not a supplier
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof supplier))
            return false;

        supplier other = (supplier) obj;
        return this.id == other.getId();
    }

    /**
     * @see Long#hashCode()
     * @return the hashCode of the object's id
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
}