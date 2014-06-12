package com.cloud9worldwide.questionnaire.core;

/**
 * Created by cloud9 on 4/8/14.
 */
public class Globals {
    public static final String PREFS_NAME = "QPrefsFile";
    public static final String TOKEN_ACCESS = "tokenAccess";
    public static final String USER_NAME = "username";
    public static final String IS_LOGIN = "isLogin";
    public static final String STAFF_ID = "staffId";

    private Boolean isLogin = false;
    private String loginTokenAccess = null;
    private String UDID = null;
    private String username = null;
    private String staffId;
    private String contactId = "-1";
    private Boolean isCustomerLocal = false;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Boolean getIsCustomerLocal() {
        return isCustomerLocal;
    }

    public void setIsCustomerLocal(Boolean isCustomerLocal) {
        this.isCustomerLocal = isCustomerLocal;
    }

    private static Globals instance;

    // Global variable
    private int data;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(int d){
        this.data=d;
    }
    public int getData(){
        return this.data;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setLoginTokenAccess(String loginTokenAccess) {
        this.loginTokenAccess = loginTokenAccess;
    }

    public String getLoginTokenAccess() {
        return loginTokenAccess;
    }

    public void setUDID(String UDID) {
        this.UDID = UDID;
    }

    public String getUDID() {
        return UDID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
