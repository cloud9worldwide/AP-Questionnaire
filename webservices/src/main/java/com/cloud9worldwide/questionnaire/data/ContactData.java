package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 3/27/14.
 */
public class ContactData {
    private String contactId;
    private String prefix;
    private String prefix_vip;
    private String fname;
    private String lname;
    private String nickname;
    private String birthdate;
    private String email;
    private ArrayList<String> tels;
    private ArrayList<String> mobiles;
    private AddressData addressWork;
    private AddressData address;
    private String offline;

    public ContactData(String _prefix, String _fname, String _lname, String _nickname,
                       String _birthdate, String _email, ArrayList<String> _mobiles,
                       AddressData _addressWork, AddressData _address){
        this.prefix = _prefix;
        this.fname = _fname;
        this.lname = _lname;
        this.nickname = _nickname;
        this.birthdate = _birthdate;
        this.email = _email;
        this.mobiles = _mobiles;
        this.addressWork = _addressWork;
        this.address = _address;

        this.contactId = "-1";
        this.offline = "0";

        this.tels = new ArrayList<String>();
        this.prefix_vip = "";
    }
    public ContactData(String _prefix, String _fname, String _lname, String _nickname,
                       String _birthdate, String _email, ArrayList<String> _mobiles,
                       AddressData _addressWork, AddressData _address,ArrayList<String> tels,String _prefix_vip){
        this.prefix = _prefix;
        this.fname = _fname;
        this.lname = _lname;
        this.nickname = _nickname;
        this.birthdate = _birthdate;
        this.email = _email;
        this.mobiles = _mobiles;
        this.addressWork = _addressWork;
        this.address = _address;

        this.contactId = "-1";
        this.offline = "0";

        this.tels = tels;
        this.prefix_vip = _prefix_vip;
    }

    public String getOffline() {
        return offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobiles(ArrayList<String> mobiles) {
        this.mobiles = mobiles;
    }

    public void setAddressWork(AddressData addressWork) {
        this.addressWork = addressWork;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getMobiles() {
        return mobiles;
    }

    public AddressData getAddressWork() {
        return addressWork;
    }

    public AddressData getAddress() {
        return address;
    }

    public ArrayList<String> getTels() {
        return tels;
    }

    public void setTels(ArrayList<String> tels) {
        this.tels = tels;
    }

    public String getPrefix_vip() {
        return prefix_vip;
    }

    public void setPrefix_vip(String prefix_vip) {
        this.prefix_vip = prefix_vip;
    }

    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("prefix",this.prefix);
            obj.put("prefix_vip",this.prefix_vip);
            obj.put("fname",this.fname);
            obj.put("lname",this.lname);
            obj.put("nickname",this.nickname);
            obj.put("birthdate",this.birthdate);
            obj.put("email",this.email);
            obj.put("tels",this.tels.toString());
            obj.put("mobiles",this.mobiles.toString());
            obj.put("addressWork",this.addressWork.toString());
            obj.put("address",this.address.toString());
            obj.put("contactId",this.contactId);
            obj.put("offline",this.offline);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj.toString();
    }
    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("prefix",this.prefix);
            obj.put("prefix_vip",this.prefix_vip);
            obj.put("fname",this.fname);
            obj.put("lname",this.lname);
            obj.put("nickname",this.nickname);
            obj.put("birthdate",this.birthdate);
            obj.put("email",this.email);
            obj.put("tels",this.tels.toString());
            obj.put("mobiles",this.mobiles.toString());
            obj.put("addressWork",this.addressWork.toString());
            obj.put("address",this.address.toString());
            obj.put("contactId",this.contactId);
            obj.put("offline",this.offline);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
