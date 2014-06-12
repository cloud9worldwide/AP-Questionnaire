package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 3/26/14.
 */
public class ContactSearchData {
    private String contactId;
    private String fname;
    private String lname;
    private String unitNumber;
    private String lastVisit;
    private Boolean isOpporpunity;

    public ContactSearchData(String _contactId, String _fname, String _lname, String _unitNumber, String _lastVisit, Boolean _isOpporpunity){
        this.contactId = _contactId;
        this.fname = _fname;
        this.lname = _lname;
        this.unitNumber = _unitNumber;
        this.lastVisit = _lastVisit;
        this.isOpporpunity = _isOpporpunity;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setIsOpporpunity(Boolean isOpporpunity) {
        this.isOpporpunity = isOpporpunity;
    }

    public Boolean getIsOpporpunity() {
        return isOpporpunity;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("contactId",this.contactId);
            obj.put("fname",this.fname);
            obj.put("lname",this.lname);
            obj.put("unitNumber",this.unitNumber);
            obj.put("lastVisit",this.lastVisit);
            obj.put("isOpporpunity",this.isOpporpunity);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj.toString();
    }
    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("contactId",this.contactId);
            obj.put("fname",this.fname);
            obj.put("lname",this.lname);
            obj.put("unitNumber",this.unitNumber);
            obj.put("lastVisit",this.lastVisit);
            obj.put("isOpporpunity",this.isOpporpunity);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
