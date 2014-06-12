package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 3/27/14.
 */
public class AddressData {
    private String houseId;
    private String moo;
    private String village;
    private String soi;
    private String road;
    private String subdistrict;
    private String district;
    private String province;
    private String postalcode;
    private String country;
    private String tel;
    private String telExt;
    private String floor;
    private String room;

    public AddressData(String _houseId, String _moo, String _village, String _soi,
                       String _road, String _subdistrict, String _district, String _province, String _postalcode,
                       String _country, String _tel, String _telExt){
        this.houseId = _houseId;
        this.moo = _moo;
        this.village = _village;
        this.soi = _soi;
        this.road = _road;
        this.subdistrict = _subdistrict;
        this.district = _district;
        this.province = _province;
        this.postalcode = _postalcode;
        this.country = _country;
        this.tel = _tel;
        this.telExt = _telExt;
    }
    public AddressData(String _houseId, String _moo, String _village, String _soi,
                       String _road, String _subdistrict, String _district, String _province, String _postalcode,
                       String _country, String _tel, String _telExt,String _floor,String _room){
        this.houseId = _houseId;
        this.moo = _moo;
        this.village = _village;
        this.soi = _soi;
        this.road = _road;
        this.subdistrict = _subdistrict;
        this.district = _district;
        this.province = _province;
        this.postalcode = _postalcode;
        this.country = _country;
        this.tel = _tel;
        this.telExt = _telExt;
        this.floor = _floor;
        this.road = _room;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public void setMoo(String moo) {
        this.moo = moo;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setSoi(String soi) {
        this.soi = soi;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setTelExt(String telExt) {
        this.telExt = telExt;
    }

    public String getHouseId() {
        return houseId;
    }

    public String getMoo() {
        return moo;
    }

    public String getVillage() {
        return village;
    }

    public String getSoi() {
        return soi;
    }

    public String getRoad() {
        return road;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getTel() {
        return tel;
    }

    public String getTelExt() {
        return telExt;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("houseId",this.houseId);
            obj.put("moo",this.moo);
            obj.put("village",this.village);
            obj.put("soi",this.soi);
            obj.put("road",this.road);
            obj.put("subdistrict",this.subdistrict);
            obj.put("district",this.district);
            obj.put("province",this.province);
            obj.put("postalcode",this.postalcode);
            obj.put("country",this.country);
            obj.put("tel",this.tel);
            obj.put("telExt",this.telExt);
            obj.put("floor",this.floor);
            obj.put("room",this.room);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj.toString();
    }
    public JSONObject toJsonObect(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("houseId",this.houseId);
            obj.put("moo",this.moo);
            obj.put("village",this.village);
            obj.put("soi",this.soi);
            obj.put("road",this.road);
            obj.put("subdistrict",this.subdistrict);
            obj.put("district",this.district);
            obj.put("province",this.province);
            obj.put("postalcode",this.postalcode);
            obj.put("country",this.country);
            obj.put("tel",this.tel);
            obj.put("telExt",this.telExt);
            obj.put("floor",this.floor);
            obj.put("room",this.room);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
