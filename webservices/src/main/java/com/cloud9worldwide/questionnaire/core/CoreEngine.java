package com.cloud9worldwide.questionnaire.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AddressData;
import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.ContactData;
import com.cloud9worldwide.questionnaire.data.ContactSearchData;
import com.cloud9worldwide.questionnaire.data.ProjectData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.QuestionnaireAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionnaireData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;
import com.cloud9worldwide.questionnaire.data.ValTextData;
import com.cloud9worldwide.questionnaire.database.MySQLiteHelper;
import com.cloud9worldwide.questionnaire.webservices.AnswerHistoryMethod;
import com.cloud9worldwide.questionnaire.webservices.CustomerInfoMethod;
import com.cloud9worldwide.questionnaire.webservices.CustomerSearchMethod;
import com.cloud9worldwide.questionnaire.webservices.DownloadImages;
import com.cloud9worldwide.questionnaire.webservices.DownloadQuestionnaire;
import com.cloud9worldwide.questionnaire.webservices.ForgotpasswordMethod;
import com.cloud9worldwide.questionnaire.webservices.LoginMethod;
import com.cloud9worldwide.questionnaire.webservices.SaveCustomerMethod;
import com.cloud9worldwide.questionnaire.webservices.SavequestionnaireMethod;
import com.cloud9worldwide.questionnaire.webservices.UpdateCustomerMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cloud9 on 3/31/14.
 */
public class CoreEngine {
    private static final String debugTag = "CoreEngine";
    //private String webserviceUrl = "http://www.cloud9worldwide.com/webservices/ws_questionnaire.php";
    private String webserviceUrl = "http://www.siwapon.me/apquestionnaire/service.aspx";

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_UDID = "udid";
    private static final String PARAM_FNAME = "fname";
    private static final String PARAM_LNAME = "lname";
    private static final String PARAM_MOBILE = "mobile";
    private static final String PARAM_CONTACTID = "contactid";
    private static final String PARAM_QUESTIONID = "questionid";
    private static final String PARAM_CUSTOMERID = "customerid";


    private final Context mCtx;

    //Login variables
    private String staffId;
    private String tokenAccess;
    private String loginMessage;
    private Boolean loginStatus;
    private ArrayList<ProjectData> projects;
    private ArrayList<QuestionnaireData> questionnaireUpdate;

    //Logout variables
    private String logoutMessage;

    //Forgot password variables
    private String forgotMessage;

    //image url for download
    private ArrayList<String> downloadImgUrl;

    private final String enviromentState;
    private QuestionnaireFS _qnFS;


    private String responseMessage;

    //SharedPreferences Setting
    private SharedPreferences settings;
    public Globals globals;
    private String device_id;

    private Boolean isModeOnline = true;

    public void setIsModeOnline(Boolean isModeOnline) {
        this.isModeOnline = isModeOnline;
    }

    public  CoreEngine(Context _context){
        globals = Globals.getInstance();
        this.mCtx = _context;
        this.downloadImgUrl = new ArrayList<String>();
        this._qnFS = new QuestionnaireFS(this.mCtx);
        enviromentState = Environment.getExternalStorageState();
        questionnaireUpdate = new ArrayList<QuestionnaireData>();

        settings = this.mCtx.getSharedPreferences(Globals.PREFS_NAME, 0);
        device_id = Secure.getString(this.mCtx.getContentResolver(),
                Secure.ANDROID_ID);
        globals.setUDID(device_id);

        //Check Session Login
        boolean isLogin = settings.getBoolean(Globals.IS_LOGIN, false);
        if(isLogin){
            globals.setIsLogin(isLogin);
            tokenAccess = settings.getString(Globals.TOKEN_ACCESS, null);
            globals.setUsername(settings.getString(Globals.USER_NAME,null));
            globals.setLoginTokenAccess(tokenAccess);
            this.loginStatus = isLogin;
            this.staffId = settings.getString(Globals.STAFF_ID,null);
            globals.setStaffId(this.staffId);
        }else {
            globals.setIsLogin(false);
            globals.setLoginTokenAccess(null);
            globals.setUsername(null);
            this.loginStatus = false;
            this.staffId = null;
            globals.setStaffId(this.staffId);
        }
    }
    public void setWebserviceUrl(String _url){
        this.webserviceUrl = _url;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if(isModeOnline)
                return true;
            else
                return false;
        }
        return false;
    }
    /**
     * Login Method
     * @param params [0 - username, 1 - password, 2 - udid]
     * @return true / false
     */
    public synchronized boolean Login(String... params){

        downloadImgUrl = new ArrayList<String>();
        questionnaireUpdate = new ArrayList<QuestionnaireData>();

        if(!isOnline()){
            this.loginMessage = "Not have internet connection";
            this.tokenAccess = "";
            this.loginStatus = false;
            return false;
        }
        try {
            //set default vars
            this.tokenAccess = "";
            this.loginMessage = "";
            this.loginStatus = false;


            JSONObject jsonObj = new JSONObject();
            jsonObj.put(PARAM_USERNAME,params[0]);
            jsonObj.put(PARAM_PASSWORD,params[1]);
            jsonObj.put(PARAM_UDID,this.device_id);
            Log.d(debugTag,this.device_id);
            String r = LoginMethod.execute(this.mCtx, webserviceUrl, jsonObj.toString());

            if(r == null){
                this.loginMessage = "Server error";
                this.loginStatus = false;
                return false;
            }

            try{
                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")){
                    //Log.d(debugTag, respObj.getJSONObject("result").getString("message"));
                    this.loginMessage = respObj.getJSONObject("result").getString("message");
                    this.tokenAccess = respObj.getJSONObject("result").getString("tokenaccess");
                    this.staffId = respObj.getJSONObject("result").getString("staffid");
                    this.loginStatus = true;
                    Log.d(debugTag, this.tokenAccess);

                    //set global & pref
                    globals.setIsLogin(true);
                    globals.setLoginTokenAccess(this.tokenAccess);
                    globals.setUsername(params[0]);
                    globals.setStaffId(this.staffId);

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(Globals.IS_LOGIN, true);
                    editor.putString(Globals.TOKEN_ACCESS,this.tokenAccess);
                    editor.putString(Globals.USER_NAME,params[0]);
                    editor.putString(Globals.STAFF_ID,this.staffId);

                    // Commit the edits!
                    editor.commit();

                    MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
                    _dbHelper.open();
                    _dbHelper.deleteAll(_dbHelper.DATABASE_TABLE_PROJECT);
                    //Log.d(debugTag, this.tokenAccess);


                    JSONArray projectlist = respObj.getJSONObject("result").getJSONArray("projectlist");
                    for (int i = 0; i < projectlist.length(); i++) {
                        JSONObject project = projectlist.getJSONObject(i);

                        ProjectData _projectData = new ProjectData(
                                project.getString("projectId"),
                                project.getString("projectName"),
                                project.getString("logo"),
                                project.getString("background"));


                        long rewId = _dbHelper.createProject(
                                _projectData.getId(),
                                _projectData.getName(),
                                _projectData.getLogoUrl(),
                                _projectData.getBackgroundUrl());

                        this.downloadImgUrl.add(_projectData.getLogoUrl());
                        this.downloadImgUrl.add(_projectData.getBackgroundUrl());


                        //Log.d(debugTag, rewId+" :: project row id");
                        /**
                         * GET Questionnaire list
                         */
                        JSONArray questionnairelist = project.getJSONArray("questionnairelist");

                        ArrayList<String> tmp_questionids = new ArrayList<String>();

                        for (int j = 0; j < questionnairelist.length(); j++) {
                            JSONObject _questionnaire = questionnairelist.getJSONObject(j);
                            QuestionnaireData _questionnareData = new QuestionnaireData(
                                    _questionnaire.getString("questionnaireId"),
                                    _questionnaire.getString("questionnaireType"),
                                    _questionnaire.getString("logo"),
                                    _questionnaire.getString("timestamp")
                            );
                            tmp_questionids.add(_questionnareData.getId());

                            this.downloadImgUrl.add(_questionnareData.getLogoUrl());

                            try{
                                Cursor _cursor = _dbHelper.getQuestionnaireById(_questionnareData.getId());
                                if (_cursor.getCount() > 0) {
                                    //Log.d(debugTag, "have questionnaire ");
                                    _cursor.moveToFirst();
                                    String jsonFile = _cursor.getString(_cursor.getColumnIndex("questionnaireid"))
                                            +"_"
                                            +_cursor.getString(_cursor.getColumnIndex("timestamp"))
                                            +".json";

                                    Log.d(debugTag, jsonFile);

                                    if(!this._qnFS.checkFileIsExists(jsonFile)){
                                        questionnaireUpdate.add(_questionnareData);
                                        Log.d(debugTag,jsonFile+" not found !");
                                        //updateQuestionnaire
                                        _dbHelper.updateQuestionnaire(_questionnareData.getId(),_projectData.getId(),
                                                _questionnareData.getType(),_questionnareData.getLogoUrl(),_questionnareData.getTimeStamp());
                                    }else{
                                        Log.d(debugTag,jsonFile+" found in SD storage !");
                                    }

                                    // check questionnaire update
                                }else {
                                    Log.d(debugTag, "not have questionnaire ");
                                    questionnaireUpdate.add(_questionnareData);
                                    long _qrow_id = _dbHelper.createQuestionnaire(
                                            _questionnareData.getId(),
                                            _projectData.getId(),
                                            _questionnareData.getType(),
                                            _questionnareData.getLogoUrl(),
                                            _questionnareData.getTimeStamp());
                                    Log.d(debugTag, _qrow_id+" :: questionnaire row id");
                                }



                            }catch (SQLException ee) {
                                ee.printStackTrace();
                            }



                        }

                        _dbHelper.deleteQuestionnairExceptId(tmp_questionids,_projectData.getId());
                    }
                    _dbHelper.close();
                    return true;
                }else{
                    //Log.d(debugTag,respObj.getJSONObject("result").getString("message")+" fail");
                    this.loginMessage = respObj.getJSONObject("result").getString("message");
                    this.loginStatus = false;
                    return false;
                }
            }catch (JSONException er){
                //er.printStackTrace();
                this.loginMessage = er.getMessage();
                this.loginStatus = false;
                return false;
            }
        }catch (Exception e){
            //e.printStackTrace();
            this.loginMessage = e.getMessage();
            this.loginStatus = false;
            return false;
        }

    }
    public boolean Logout(){
        if(this.isOnline()){
            //clear all data
            this.loginStatus = false;
            this.loginMessage = null;
            this.questionnaireUpdate = new ArrayList<QuestionnaireData>();
            this.projects = new ArrayList<ProjectData>();
            this.downloadImgUrl = new ArrayList<String>();

            //clear all data in globals & prefs
            globals.setIsLogin(false);
            globals.setUsername(null);
            globals.setLoginTokenAccess(null);

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(Globals.IS_LOGIN, false);
            editor.putString(Globals.TOKEN_ACCESS,null);
            editor.putString(Globals.USER_NAME,null);
            // Commit the edits!
            editor.commit();

            return true;
        }else{
            this.logoutMessage = "Not have internet connection";
            return false;
        }
    }
    public String getTokenAccess() {
        return tokenAccess;
    }
    public Boolean getLoginStatus() {
        return loginStatus;
    }
    public String getLoginMessage() {
        return loginMessage;
    }
    public ArrayList<ProjectData> getProjects() {
        projects = new ArrayList<ProjectData>();

        MySQLiteHelper _sqlHelper = new MySQLiteHelper(this.mCtx);
        _sqlHelper.open();
        Cursor _cursor = _sqlHelper.getAllProjects();
        if(_cursor != null)
            _cursor.moveToFirst();
        for (int i = 0; i < _cursor.getCount(); i++) {
            //_cursor
            ProjectData _pData = new ProjectData(
                    _cursor.getString(1),
                    _cursor.getString(2),
                    _cursor.getString(3),
                    _cursor.getString(4));
            //Log.d(debugTag,_pData.getName()+" ,"+_cursor.getString(1)+" ,"+_cursor.getString(2)+" ,"+_cursor.getString(3));

            Cursor _qcursor = _sqlHelper.getAllQuestionnaireByProject(_pData.getId());
            if(_qcursor != null) {
                _qcursor.moveToFirst();
                //Log.d(debugTag, "_qcursor ::" + _qcursor.getString(_qcursor.getColumnIndex("timestamp")));

                for (int j = 0; j < _qcursor.getCount(); j++) {
                    QuestionnaireData _qData = new QuestionnaireData(
                            _qcursor.getString(_qcursor.getColumnIndex("questionnaireid")),
                            _qcursor.getString(_qcursor.getColumnIndex("questionnairetype")),
                            _qcursor.getString(_qcursor.getColumnIndex("logo")),
                            _qcursor.getString(_qcursor.getColumnIndex("timestamp")));
                    _pData.addQuestionnaire(_qData);
                    _qcursor.moveToNext();
                }

            }
            _cursor.moveToNext();
            projects.add(_pData);
        }
        _sqlHelper.close();

        return projects;
    }
    public synchronized boolean checkUpdateQuestionnaire(){
        return (this.questionnaireUpdate.size() > 0);
    }
    public synchronized boolean updateQuestionnaire(ProgressDialog _pDialog){
        try {
            ArrayList<String> _jsonData = new ArrayList<String>();
            _jsonData =  DownloadQuestionnaire.download(_pDialog,webserviceUrl,this.tokenAccess,this.questionnaireUpdate);
            for (int i = 0; i < this.questionnaireUpdate.size(); i++) {
                QuestionnaireData _question_data = this.questionnaireUpdate.get(i);
                String _json_str = _jsonData.get(i);
                String _fileName = _question_data.getId()+"_"+_question_data.getTimeStamp()+".json";

                //Extract question data & find url image for download to local
                try {
                    JSONObject _json_obj = new JSONObject(_json_str);
                    JSONArray _questionlist = _json_obj.getJSONObject("result").getJSONArray("questionlist");
                    JSONArray _staffquestionlist = _json_obj.getJSONObject("result").getJSONArray("staffquestionlist");

                    ArrayList<QuestionTypeData> _arr_qTypeData = QuestionlistJSON2DATA.parseData(_questionlist);
                    for (int j = 0; j < _arr_qTypeData.size(); j++) {
                        QuestionTypeData _qTypeData = _arr_qTypeData.get(j);
                        if(_qTypeData.getQuestion().getImageUrl().trim().length() > 0){
                            this.downloadImgUrl.add(_qTypeData.getQuestion().getImageUrl().trim());
                        }
                        ArrayList<AnswerData> _arr_answer = _qTypeData.getAnswers();
                        for (int k = 0; k < _arr_answer.size(); k++) {
                            AnswerData _answer = _arr_answer.get(k);
                            if(_answer.getImageUrl().trim().length() > 0)
                                this.downloadImgUrl.add(_answer.getImageUrl().trim());
                            if(_answer.getIconActiveUrl().trim().length() > 0)
                                this.downloadImgUrl.add(_answer.getIconActiveUrl().trim());
                            if(_answer.getIconInActiveUrl().trim().length() >0 )
                                this.downloadImgUrl.add(_answer.getIconInActiveUrl().trim());
                        }
                    }

                    ArrayList<QuestionTypeData> _arr_stqTypeData = QuestionlistJSON2DATA.parseData(_staffquestionlist);
                    for (int j = 0; j < _arr_stqTypeData.size(); j++) {
                        QuestionTypeData _qTypeData = _arr_stqTypeData.get(j);
                        if(_qTypeData.getQuestion().getImageUrl().trim().length() > 0){
                            this.downloadImgUrl.add(_qTypeData.getQuestion().getImageUrl().trim());
                        }
                        ArrayList<AnswerData> _arr_answer = _qTypeData.getAnswers();
                        for (int k = 0; k < _arr_answer.size(); k++) {
                            AnswerData _answer = _arr_answer.get(k);
                            if(_answer.getImageUrl().trim().length() > 0)
                                this.downloadImgUrl.add(_answer.getImageUrl().trim());
                            if(_answer.getIconActiveUrl().trim().length() > 0)
                                this.downloadImgUrl.add(_answer.getIconActiveUrl().trim());
                            if(_answer.getIconInActiveUrl().trim().length() >0 )
                                this.downloadImgUrl.add(_answer.getIconInActiveUrl().trim());
                        }
                    }
                    //Save json file to SD
                    if (enviromentState.equals(Environment.MEDIA_MOUNTED)) {
                        this._qnFS.generateFileOnSD(_fileName,_json_str);
                    } else if (enviromentState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                        // read but cant write
                        Toast.makeText(this.mCtx, "Media mounted read only.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this.mCtx, "Media not mounted", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            //Log.d("Core",this.downloadImgUrl.size()+"xxxx ");
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  false;
    }
    public synchronized boolean checkDownloadImages(){
        return (this.downloadImgUrl.size() > 0);
    }
    public synchronized void startDownloadImages(ProgressDialog _pDialog){
       try {
           Log.e("download url",this.downloadImgUrl.toString());
           DownloadImages.download(_pDialog,this.downloadImgUrl);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
    /**
     *
     * @param params (0 - fname, 1 - lname , 2 - mobile)
     * @return ArrayList<ContactSearchData>, null if not found
     */
    public synchronized ArrayList<ContactSearchData> searchContact(String... params){
        ArrayList<ContactSearchData> _data = new ArrayList<ContactSearchData>();
        if(isOnline()){
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(PARAM_FNAME,params[0]);
                jsonObj.put(PARAM_LNAME,params[1]);
                jsonObj.put(PARAM_MOBILE,params[2]);
                try {
                    String r = CustomerSearchMethod.execute(this.mCtx, webserviceUrl, jsonObj.toString());
                    //Log.e(debugTag,r);
                    if(r == null) {
                        this.responseMessage = "Server error";
                        return null;
                    }
                    JSONObject respObj = new JSONObject(r);
                    if(respObj.getBoolean("status")) {
                        JSONArray _arr_json = respObj.getJSONArray("result");
                        for (int i = 0; i < _arr_json.length(); i++) {
                            JSONObject _json = _arr_json.getJSONObject(i);
                            ContactSearchData _sData = new ContactSearchData(
                                    _json.getString("contactid"),
                                    _json.getString("fname"),
                                    _json.getString("lname"),
                                    _json.getString("unitnumber"),
                                    _json.getString("lastvisit"),
                                    _json.getBoolean("isOpporpunity")
                            );
                            _data.add(_sData);
                        }
                        return _data;
                    }else{
                        this.responseMessage = "Contact not found";
                    }
                }
                catch (Exception er)
                {
                    er.printStackTrace();
                    this.responseMessage = er.getMessage();
                }

            }catch (JSONException e)
            {
                e.printStackTrace();
                this.responseMessage = e.getMessage();
            }
            return null;
        }else{
            this.responseMessage = "Not have internet connection";
            return null;
        }

    }
    public synchronized ContactData getContactInfo(String _contact_id){
        ContactData _data;
        //globals.getIsCustomerLocal()
        if(isOnline()){
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(PARAM_CONTACTID,_contact_id);
                try
                {
                    String r = CustomerInfoMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
                    if(r == null) {
                        this.responseMessage = "Server error";
                        return null;
                    }
                    JSONObject respObj = new JSONObject(r);
                    if(respObj.getBoolean("status")) {
                        JSONObject contactObj = respObj.getJSONObject("result");
                        AddressData _address_work;
                        AddressData _address;

                        JSONArray _mobile_arr = contactObj.getJSONArray("mobiles");
                        JSONObject _address_work_json = contactObj.getJSONObject("address_work");
                        JSONObject _address_json = contactObj.getJSONObject("address");

                        //Mobiles
                        ArrayList<String> mobiles = new ArrayList<String>();
                        if(_mobile_arr.length() > 0){
                            for (int i = 0; i < _mobile_arr.length(); i++) {
                                JSONObject _mobileObj = _mobile_arr.getJSONObject(i);
                                mobiles.add(_mobileObj.getString("mobile"));
                            }
                        }

                        //Tels
                        ArrayList<String> tels = new ArrayList<String>();
                        if(contactObj.has("tels")) {
                            JSONArray _tels_arr = contactObj.getJSONArray("tels");
                            if (_tels_arr.length() > 0) {
                                for (int i = 0; i < _tels_arr.length(); i++) {
                                    JSONObject _telObj = _tels_arr.getJSONObject(i);
                                    tels.add(_telObj.getString("tel"));
                                }
                            }
                        }

                        //Address work
                        String floor_w = "";
                        if(_address_work_json.has("floor"))
                            floor_w = _address_work_json.getString("floor");
                        String room_w = "";
                        if(_address_work_json.has("room"))
                            room_w = _address_work_json.getString("room");

                        _address_work = new AddressData(
                                _address_work_json.getString("house_id"),
                                _address_work_json.getString("moo"),
                                _address_work_json.getString("village"),
                                _address_work_json.getString("soi"),
                                _address_work_json.getString("road"),
                                _address_work_json.getString("subdistrict"),
                                _address_work_json.getString("district"),
                                _address_work_json.getString("province"),
                                _address_work_json.getString("postalcode"),
                                _address_work_json.getString("country"),
                                _address_work_json.getString("tel"),
                                _address_work_json.getString("tel_ext"),
                                floor_w,
                                room_w
                        );

                        //Address
                        String floor = "";
                        if(_address_json.has("floor"))
                            floor = _address_json.getString("floor");
                        String room = "";
                        if(_address_json.has("room"))
                            room = _address_json.getString("room");
                        _address = new AddressData(
                                _address_json.getString("house_id"),
                                _address_json.getString("moo"),
                                _address_json.getString("village"),
                                _address_json.getString("soi"),
                                _address_json.getString("road"),
                                _address_json.getString("subdistrict"),
                                _address_json.getString("district"),
                                _address_json.getString("province"),
                                _address_json.getString("postalcode"),
                                _address_json.getString("country"),
                                _address_json.getString("tel"),
                                _address_json.getString("tel_ext"),
                                floor,
                                room
                        );
                        String prifix_vip = "";
                        if(contactObj.has("prifix_vip"))
                            prifix_vip = contactObj.getString("prifix_vip");

                        _data = new ContactData(
                                contactObj.getString("prefix"),
                                contactObj.getString("fname"),
                                contactObj.getString("lname"),
                                contactObj.getString("nickname"),
                                contactObj.getString("birthdate"),
                                contactObj.getString("email"),
                                mobiles,
                                _address_work,
                                _address,
                                tels,
                                prifix_vip
                        );
                        return _data;
                    }else{
                        this.responseMessage = "Contact not found";
                    }
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }else {
            //return null;
            // get contact info for local database
            MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);


            if(globals.getIsCustomerLocal()){
                //move customer local to server
                ContactData _contact = null;
                //long _contact_id = Long.getLong(_contact_id);
                long contact_id = Long.parseLong(_contact_id);
                try {
                    _dbHelper.open();

                    Cursor _mobiles_cursor = _dbHelper.getMobiles(contact_id);
                    Cursor _address_work_cursor = _dbHelper.getAddressWork(contact_id);
                    Cursor _address_cursor = _dbHelper.getAddress(contact_id);
                    Cursor _contact_cursor = _dbHelper.getContact(contact_id);
                    Cursor _tels_cursor = _dbHelper.getTels(contact_id);


                    //prepare mobiles data
                    ArrayList<String> _mobiles = new ArrayList<String>();
                    if(_mobiles_cursor != null){
                        //Log.e("test",String.valueOf(_mobiles_cursor.getCount()));
                        _mobiles_cursor.moveToFirst();
                        for (int i = 0; i < _mobiles_cursor.getCount(); i++) {
                            _mobiles.add(_mobiles_cursor.getString(0));
                            _mobiles_cursor.moveToNext();
                        }
                    }

                    //prepare tels data
                    ArrayList<String> _tels = new ArrayList<String>();
                    if(_tels_cursor != null){
                        //Log.e("test",String.valueOf(_mobiles_cursor.getCount()));
                        _tels_cursor.moveToFirst();
                        for (int i = 0; i < _tels_cursor.getCount(); i++) {
                            _tels.add(_tels_cursor.getString(0));
                            _tels_cursor.moveToNext();
                        }
                    }

                    //prepare address work data
                    AddressData _address_work;
                    if(_address_work_cursor != null && _address_work_cursor.getCount() > 0) {
                        _address_work_cursor.moveToFirst();
                        String _house_id = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                        String _moo = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.MOO));
                        String _village = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.VILLAGE));
                        String _soi = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SOI));
                        String _road = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROAD));
                        String _subdistrict = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                        String _district = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.DISTRICT));
                        String _province = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.PROVINCE));
                        String _postalcode = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                        String _country = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.COUNTRY));
                        String _tel = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL));
                        String _tel_ext = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                        String _floor = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.FLOOR));
                        String _room = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROOM));
                        _address_work = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                                _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
                    }else{
                        _address_work = new AddressData("","","","","","","","","","","","","","");
                    }

                    //prepare address data
                    AddressData _address;
                    if(_address_cursor != null && _address_cursor.getCount() > 0) {
                        _address_cursor.moveToFirst();
                        String _house_id = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                        String _moo = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.MOO));
                        String _village = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.VILLAGE));
                        String _soi = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SOI));
                        String _road = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROAD));
                        String _subdistrict = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                        String _district = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.DISTRICT));
                        String _province = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.PROVINCE));
                        String _postalcode = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                        String _country = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.COUNTRY));
                        String _tel = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL));
                        String _tel_ext = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                        String _floor = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.FLOOR));
                        String _room = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROOM));
                        _address = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                                _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
                    }else{
                        _address = new AddressData("","","","","","","","","","","","","","");
                    }

                    if(_contact_cursor != null){
                        _contact_cursor.moveToFirst();
                        String _prefix = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX));
                        String _fname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.FNAME));
                        String _lname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.LNAME));
                        String _nickname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.NICKNAME));
                        String _birthdate = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.BIRTHDATE));
                        String _email = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.EMAIL));
                        String _prefix_vip = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX_VIP));
                        _contact = new ContactData(_prefix,_fname,_lname,_nickname,_birthdate,_email,_mobiles,_address_work,_address,_tels,_prefix_vip);
                        _contact.setOffline("1");
                    }
                    _dbHelper.close();
                }
                catch (SQLException e)
                {
                    this.responseMessage = e.getMessage();
                    Log.e(debugTag,this.responseMessage);
                    _dbHelper.close();
                    return  null;
                }

               return _contact;
            }else{
                return null;
            }
        }
    }
    public synchronized boolean saveContact(ContactData _data){
        if(isOnline()){
            //online
            return save_contact_online(_data);
        }else{
            //offline
            return save_contact_offline(_data);
        }
    }
    /**
     *
     * @param _data
     */
    private synchronized boolean save_contact_online(ContactData _data){
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("tokenaccess",globals.getLoginTokenAccess());
            jsonObj.put("prefix", _data.getPrefix());
            jsonObj.put("prefix_vip", _data.getPrefix_vip());
            jsonObj.put("fname",_data.getFname());
            jsonObj.put("lname",_data.getLname());
            jsonObj.put("nickname",_data.getNickname());
            jsonObj.put("birthdate",_data.getBirthdate());
            jsonObj.put("email",_data.getEmail());
            jsonObj.put("offine",_data.getOffline());

            JSONArray mobile_js_arr = new JSONArray();
            ArrayList<String> _mobiles = _data.getMobiles();
            for (int i = 0; i < _mobiles.size(); i++) {
                JSONObject mobile = new JSONObject();
                mobile.put("mobile",_mobiles.get(i));
                mobile_js_arr.put(i,mobile);
            }
            jsonObj.put("mobiles",mobile_js_arr);

            JSONArray tels_js_arr = new JSONArray();
            ArrayList<String> _tels = _data.getTels();
            for (int i = 0; i < _tels.size(); i++) {
                JSONObject tel = new JSONObject();
                tel.put("mobile",_tels.get(i));
                tels_js_arr.put(i,tel);
            }
            jsonObj.put("tels",tels_js_arr);


            AddressData _address_work = _data.getAddressWork();
            AddressData _address = _data.getAddress();
            JSONObject _address_work_js_obj = new JSONObject();
            JSONObject _address_js_obj = new JSONObject();

            _address_work_js_obj.put("house_id",_address_work.getHouseId());
            _address_work_js_obj.put("moo",_address_work.getMoo());
            _address_work_js_obj.put("village",_address_work.getVillage());
            _address_work_js_obj.put("soi",_address_work.getSoi());
            _address_work_js_obj.put("road",_address_work.getRoad());
            _address_work_js_obj.put("subdistrict",_address_work.getSubdistrict());
            _address_work_js_obj.put("district",_address_work.getDistrict());
            _address_work_js_obj.put("province",_address_work.getProvince());
            _address_work_js_obj.put("postalcode",_address_work.getPostalcode());
            _address_work_js_obj.put("country",_address_work.getCountry());
            _address_work_js_obj.put("tel",_address_work.getTel());
            _address_work_js_obj.put("tel_ext",_address_work.getTelExt());
            _address_work_js_obj.put("floor",_address_work.getFloor());
            _address_work_js_obj.put("room",_address_work.getRoom());

            _address_js_obj.put("house_id",_address.getHouseId());
            _address_js_obj.put("moo",_address.getMoo());
            _address_js_obj.put("village",_address.getVillage());
            _address_js_obj.put("soi",_address.getSoi());
            _address_js_obj.put("road",_address.getRoad());
            _address_js_obj.put("subdistrict",_address.getSubdistrict());
            _address_js_obj.put("district",_address.getDistrict());
            _address_js_obj.put("province",_address.getProvince());
            _address_js_obj.put("postalcode",_address.getPostalcode());
            _address_js_obj.put("country",_address.getCountry());
            _address_js_obj.put("tel",_address.getTel());
            _address_js_obj.put("tel_ext",_address.getTelExt());
            _address_js_obj.put("floor",_address.getFloor());
            _address_js_obj.put("room",_address.getRoom());

            jsonObj.put("address_work",_address_work_js_obj);
            jsonObj.put("address",_address_js_obj);

            Log.e(debugTag,jsonObj.toString());

            try {
                String r = SaveCustomerMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
                if(r == null) {
                    this.responseMessage = "Server error";
                    return false;
                }
                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")) {
                    this.responseMessage = respObj.getJSONObject("result").getString("message");
                    String _contactId_str = respObj.getJSONObject("result").getString("contactid");

                    //setup current contact
                    globals.setContactId(_contactId_str);
                    globals.setIsCustomerLocal(false);
                    return true;
                }else{
                    this.responseMessage = respObj.getJSONObject("result").getString("message");
                    return false;
                }
            }
            catch (SaveCustomerMethod.ApiException ae)
            {
                ae.printStackTrace();
                this.responseMessage = ae.getMessage();
            }

            return false;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            this.responseMessage = e.getMessage();
        }
        return false;
    }
    /**
     * Save contact data in local storage
     * @param _data
     */
    private boolean save_contact_offline(ContactData _data){
        //this.responseMessage = "offline mode not working";
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();

        long _contactId = _dbHelper.createContact(
                _data.getPrefix(),_data.getFname(),
                _data.getLname(),_data.getNickname(),
                _data.getBirthdate(),_data.getEmail(),_data.getPrefix_vip());

        if(_contactId > 0){
            ArrayList<String> _mobile = _data.getMobiles();
            for (int i = 0; i < _mobile.size(); i++) {
                _dbHelper.createMobile(_contactId,_mobile.get(i));
            }

            ArrayList<String> _tels = _data.getTels();
            for (int i = 0; i < _tels.size(); i++) {
                _dbHelper.createTel(_contactId,_tels.get(i));
            }

            AddressData _address_work = _data.getAddressWork();
            _dbHelper.createAddressWork(_contactId,_address_work.getHouseId(),_address_work.getMoo(),_address_work.getVillage(),
                    _address_work.getSoi(),_address_work.getRoad(),_address_work.getSubdistrict(),_address_work.getDistrict(),
                    _address_work.getProvince(),_address_work.getPostalcode(),_address_work.getCountry(),_address_work.getTel(),
                    _address_work.getTelExt(),_address_work.getFloor(),_address_work.getRoom());
            AddressData _address = _data.getAddress();
            _dbHelper.createAddress(_contactId, _address.getHouseId(), _address.getMoo(), _address.getVillage(),
                    _address.getSoi(), _address.getRoad(), _address.getSubdistrict(), _address.getDistrict(),
                    _address.getProvince(), _address.getPostalcode(), _address.getCountry(), _address.getTel(),
                    _address.getTelExt(),_address.getFloor(),_address.getRoom());
        }
        globals.setContactId(String.valueOf(_contactId));
        globals.setIsCustomerLocal(true);
        _dbHelper.close();
        return true;
    }
    public synchronized boolean updateContact(ContactData _data){
        if(isOnline()){
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tokenaccess",globals.getLoginTokenAccess());
                jsonObj.put("contactid",_data.getContactId());
                jsonObj.put("prefix", _data.getPrefix());
                jsonObj.put("prefix_vip", _data.getPrefix_vip());
                jsonObj.put("fname",_data.getFname());
                jsonObj.put("lname",_data.getLname());
                jsonObj.put("nickname",_data.getNickname());
                jsonObj.put("birthdate",_data.getBirthdate());
                jsonObj.put("email",_data.getEmail());

                JSONArray mobile_js_arr = new JSONArray();
                ArrayList<String> _mobiles = _data.getMobiles();
                for (int i = 0; i < _mobiles.size(); i++) {
                    JSONObject mobile = new JSONObject();
                    mobile.put("mobile",_mobiles.get(i));
                    mobile_js_arr.put(i,mobile);
                }
                jsonObj.put("mobiles",mobile_js_arr);

                JSONArray tel_js_arr = new JSONArray();
                ArrayList<String> _tels = _data.getTels();
                for (int i = 0; i < _tels.size(); i++) {
                    JSONObject tel = new JSONObject();
                    tel.put("tel",_tels.get(i));
                    tel_js_arr.put(i,tel);
                }
                jsonObj.put("tels",tel_js_arr);


                AddressData _address_work = _data.getAddressWork();
                AddressData _address = _data.getAddress();
                JSONObject _address_work_js_obj = new JSONObject();
                JSONObject _address_js_obj = new JSONObject();

                _address_work_js_obj.put("house_id",_address_work.getHouseId());
                _address_work_js_obj.put("moo",_address_work.getMoo());
                _address_work_js_obj.put("village",_address_work.getVillage());
                _address_work_js_obj.put("soi",_address_work.getSoi());
                _address_work_js_obj.put("road",_address_work.getRoad());
                _address_work_js_obj.put("subdistrict",_address_work.getSubdistrict());
                _address_work_js_obj.put("district",_address_work.getDistrict());
                _address_work_js_obj.put("province",_address_work.getProvince());
                _address_work_js_obj.put("postalcode",_address_work.getPostalcode());
                _address_work_js_obj.put("country",_address_work.getCountry());
                _address_work_js_obj.put("tel",_address_work.getTel());
                _address_work_js_obj.put("tel_ext",_address_work.getTelExt());
                _address_work_js_obj.put("floor",_address_work.getFloor());
                _address_work_js_obj.put("room",_address_work.getRoom());

                _address_js_obj.put("house_id",_address.getHouseId());
                _address_js_obj.put("moo",_address.getMoo());
                _address_js_obj.put("village",_address.getVillage());
                _address_js_obj.put("soi",_address.getSoi());
                _address_js_obj.put("road",_address.getRoad());
                _address_js_obj.put("subdistrict",_address.getSubdistrict());
                _address_js_obj.put("district",_address.getDistrict());
                _address_js_obj.put("province",_address.getProvince());
                _address_js_obj.put("postalcode",_address.getPostalcode());
                _address_js_obj.put("country",_address.getCountry());
                _address_js_obj.put("tel",_address.getTel());
                _address_js_obj.put("tel_ext",_address.getTelExt());
                _address_js_obj.put("floor",_address.getFloor());
                _address_js_obj.put("room",_address.getRoom());

                jsonObj.put("address_work",_address_work_js_obj);
                jsonObj.put("address",_address_js_obj);

                Log.e(debugTag,jsonObj.toString());

                try {
                    String r = UpdateCustomerMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
                    if(r == null) {
                        this.responseMessage = "Server error";
                        return false;
                    }
                    JSONObject respObj = new JSONObject(r);
                    if(respObj.getBoolean("status")) {
                        this.responseMessage = respObj.getJSONObject("result").getString("message");
                        return true;
                    }else{
                        this.responseMessage = respObj.getJSONObject("result").getString("message");
                        return false;
                    }
                }
                catch (UpdateCustomerMethod.ApiException ae)
                {
                    ae.printStackTrace();
                    this.responseMessage = ae.getMessage();
                }

                return false;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                this.responseMessage = e.getMessage();
            }
            return false;
        }else
        {
            this.responseMessage = "offline mode not working";
        }
        return false;
    }
    public synchronized boolean saveQuestionnaireData(QuestionnaireAnswerData _data){
        if(isOnline()){
            MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);


            if(_data.getIscustomerLocal()){
                //move customer local to server
                ContactData _contact = null;
                long _contact_id = Long.parseLong(_data.getCustomerId());
                try {
                    _dbHelper.open();

                    Cursor _mobiles_cursor = _dbHelper.getMobiles(_contact_id);
                    Cursor _address_work_cursor = _dbHelper.getAddressWork(_contact_id);
                    Cursor _address_cursor = _dbHelper.getAddress(_contact_id);
                    Cursor _contact_cursor = _dbHelper.getContact(_contact_id);
                    Cursor _tels_cursor = _dbHelper.getTels(_contact_id);

                    //prepare mobiles data
                    ArrayList<String> _mobiles = new ArrayList<String>();
                    if(_mobiles_cursor != null){
                        _mobiles_cursor.moveToFirst();
                        for (int i = 0; i < _mobiles_cursor.getCount(); i++) {
                            _mobiles.add(_mobiles_cursor.getString(0));
                            _mobiles_cursor.moveToNext();
                        }
                    }

                    //prepare tels data
                    ArrayList<String> _tels = new ArrayList<String>();
                    if(_tels_cursor != null){
                        _tels_cursor.moveToFirst();
                        for (int i = 0; i < _tels_cursor.getCount(); i++) {
                            _tels.add(_tels_cursor.getString(0));
                            _tels_cursor.moveToNext();
                        }
                    }

                    //prepare address work data
                    AddressData _address_work;
                    if(_address_work_cursor != null && _address_work_cursor.getCount() > 0) {
                        _address_work_cursor.moveToFirst();
                        String _house_id = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                        String _moo = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.MOO));
                        String _village = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.VILLAGE));
                        String _soi = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SOI));
                        String _road = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROAD));
                        String _subdistrict = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                        String _district = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.DISTRICT));
                        String _province = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.PROVINCE));
                        String _postalcode = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                        String _country = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.COUNTRY));
                        String _tel = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL));
                        String _tel_ext = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                        String _floor = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.FLOOR));
                        String _room = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROOM));
                        _address_work = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                                _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
                    }else{
                        _address_work = new AddressData("","","","","","","","","","","","","","");
                    }

                    //prepare address data
                    AddressData _address;
                    if(_address_cursor != null && _address_cursor.getCount() > 0) {
                        _address_cursor.moveToFirst();
                        String _house_id = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                        String _moo = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.MOO));
                        String _village = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.VILLAGE));
                        String _soi = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SOI));
                        String _road = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROAD));
                        String _subdistrict = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                        String _district = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.DISTRICT));
                        String _province = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.PROVINCE));
                        String _postalcode = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                        String _country = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.COUNTRY));
                        String _tel = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL));
                        String _tel_ext = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                        String _floor = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.FLOOR));
                        String _room = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROOM));
                        _address = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                                _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
                    }else{
                        _address = new AddressData("","","","","","","","","","","","","","");
                    }

                    if(_contact_cursor != null){
                        _contact_cursor.moveToFirst();
                        String _prefix = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX));
                        String _prefix_vip = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX_VIP));
                        String _fname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.FNAME));
                        String _lname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.LNAME));
                        String _nickname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.NICKNAME));
                        String _birthdate = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.BIRTHDATE));
                        String _email = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.EMAIL));
                        _contact = new ContactData(_prefix,_fname,_lname,_nickname,_birthdate,_email,_mobiles,_address_work,_address,_tels,_prefix_vip);
                        _contact.setOffline("1");
                    }
                    _dbHelper.close();
                }
                catch (SQLException e)
                {
                    this.responseMessage = e.getMessage();
                    Log.e(debugTag,this.responseMessage);
                    _dbHelper.close();
                    return  false;
                }

                if(_contact != null){
                    //save local data to server
                    if(this.save_contact_online(_contact)){
                        String local_customer_id = _data.getCustomerId();
                        _data.setCustomerId(String.valueOf(globals.getContactId()));
                        _data.setIscustomerLocal(false);
                        if(this.saveQuestionnaireData_online(_data)){
                            this.responseMessage = "save questionnaire data successful.";
                            Log.i(debugTag,this.responseMessage);
                            // if save to server successful , need remove local data
                            this.remove_contact_local(local_customer_id);
                            return true;
                        }else{
                            Log.e(debugTag,this.responseMessage);
                            return false;
                        }
                    }
                }else{
                    this.responseMessage  = "Contact data not found in local storage";
                    Log.e(debugTag,this.responseMessage);
                    return  false;
                }
            } else {
                if(this.saveQuestionnaireData_online(_data)){
                    this.responseMessage = "save questionnaire data successful.";
                    Log.i(debugTag,this.responseMessage);
                    return true;
                }else {
                    Log.e(debugTag,this.responseMessage);
                    return false;
                }
            }

            return true;
        }else{
            MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
            _dbHelper.open();

            //save questionnaire answer data
            int isCustomerLocal = 0;
            if(_data.getIscustomerLocal() == Boolean.TRUE){
                isCustomerLocal = 1;
            }else{

            }
            //int isCustomerLocal = (_data.getIscustomerLocal())? 1 : 0;
            //long contactid = Long.getLong(_data.getCustomerId());


            String contactid = String.valueOf(_data.getCustomerId());
            long save_questionnaire_Id = _dbHelper.createQuestionnaireAnswer(contactid,globals.getStaffId(),_data.getProjectId(),
                    _data.getQuestionnaireId(),_data.getTimedevice(),isCustomerLocal);

            //save question answer data
            ArrayList<QuestionAnswerData> _question_ans_all = _data.getAnswers();
            for (int i = 0; i < _question_ans_all.size(); i++) {
                QuestionAnswerData _question = _question_ans_all.get(i);
                ArrayList<SaveAnswerData> _ans_all = _question.getAnswer();

                for (int j = 0; j < _ans_all.size(); j++) {
                    SaveAnswerData _ans = _ans_all.get(j);
                    _dbHelper.createAnswer(save_questionnaire_Id,_question.getQuestionId(),_ans.getValue(),_ans.getFreetxt());
                }

            }

            //save staff question answer data
            ArrayList<QuestionAnswerData> _staff_question_ans_all = _data.getStaffanswers();
            for (int i = 0; i < _staff_question_ans_all.size(); i++) {
                QuestionAnswerData _question = _staff_question_ans_all.get(i);
                ArrayList<SaveAnswerData> _ans_all = _question.getAnswer();

                for (int j = 0; j < _ans_all.size(); j++) {
                    SaveAnswerData _ans = _ans_all.get(j);
                    _dbHelper.createStaffAnswer(save_questionnaire_Id, _question.getQuestionId(), _ans.getValue(), _ans.getFreetxt());
                }

            }

            _dbHelper.close();
            return true;
        }
    }
    private synchronized boolean saveQuestionnaireData_online(QuestionnaireAnswerData _data){
        //try {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("customerid",_data.getCustomerId());
                jsonObj.put("staffid",globals.getStaffId());
                jsonObj.put("projectid",_data.getProjectId());
                jsonObj.put("questionnaireid",_data.getQuestionnaireId());
                jsonObj.put("tokenaccess",globals.getLoginTokenAccess());
                jsonObj.put("timedevice",_data.getTimedevice());

                ArrayList<QuestionAnswerData> _answers_all = _data.getAnswers();

                JSONArray _answers_all_json = new JSONArray();
                for (int i = 0; i < _answers_all.size(); i++) {
                    QuestionAnswerData _answer = _answers_all.get(i);
                    JSONObject _question_answer_json = new JSONObject();

                    JSONArray _ans_all_json = new JSONArray();
                    ArrayList<SaveAnswerData> _ans_all = _answer.getAnswer();
                    for (int j = 0; j < _ans_all.size(); j++) {
                        SaveAnswerData _ans = _ans_all.get(j);
                        JSONObject _ans_json = new JSONObject();
                        _ans_json.put("value",_ans.getValue());
                        _ans_json.put("freetxt",_ans.getFreetxt());
                        _ans_all_json.put(j,_ans_json);
                    }

                    _question_answer_json.put("questionid",_answer.getQuestionId());
                    _question_answer_json.put("answer",_ans_all_json);

                    _answers_all_json.put(i,_question_answer_json);
                }
                //answer data all
                jsonObj.put("answers",_answers_all_json);

                ArrayList<QuestionAnswerData> _staff_answers_all = _data.getStaffanswers();
                JSONArray _staff_answers_all_json = new JSONArray();
                for (int i = 0; i < _staff_answers_all.size(); i++) {
                    QuestionAnswerData _answer = _staff_answers_all.get(i);

                    JSONObject _question_answer_json = new JSONObject();

                    JSONArray _ans_all_json = new JSONArray();
                    ArrayList<SaveAnswerData> _ans_all = _answer.getAnswer();
                    for (int j = 0; j < _ans_all.size(); j++) {
                        SaveAnswerData _ans = _ans_all.get(j);
                        JSONObject _ans_json = new JSONObject();
                        _ans_json.put("value",_ans.getValue());
                        _ans_json.put("freetxt",_ans.getFreetxt());
                        _ans_all_json.put(j,_ans_json);
                    }

                    _question_answer_json.put("questionid",_answer.getQuestionId());
                    _question_answer_json.put("answer",_ans_all_json);

                    _staff_answers_all_json.put(i,_question_answer_json);
                }
                //staff answer data all
                jsonObj.put("staffanswers",_staff_answers_all_json);

                Log.d(debugTag,jsonObj.toString());
                String r = "";
                try {
                    r = SavequestionnaireMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
                }catch (SavequestionnaireMethod.ApiException je){
                    je.printStackTrace();

                }


                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")){
                    Log.d(debugTag, respObj.getJSONObject("result").getString("message") + " - OK");
                    return true;
                }else{
                    Log.d(debugTag,respObj.getJSONObject("result").getString("message"));
                    this.responseMessage = respObj.getJSONObject("result").getString("message");
                    return false;
                }

            }
            catch (JSONException e){
                e.printStackTrace();
                this.responseMessage = e.getMessage();
            }
        //}
        //catch (SavequestionnaireMethod.ApiException je)
       // {
       //     je.printStackTrace();
       //     this.responseMessage = je.getMessage();
        //}

        return false;
    }
    private synchronized String save_contact_online(String _local_contact_id){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        ContactData _contact = null;
        long _contact_id = Long.parseLong(_local_contact_id);
        try {
            _dbHelper.open();

            Cursor _mobiles_cursor = _dbHelper.getMobiles(_contact_id);
            Cursor _address_work_cursor = _dbHelper.getAddressWork(_contact_id);
            Cursor _address_cursor = _dbHelper.getAddress(_contact_id);
            Cursor _contact_cursor = _dbHelper.getContact(_contact_id);
            Cursor _tels_cursor = _dbHelper.getTels(_contact_id);


            //prepare mobiles data
            ArrayList<String> _mobiles = new ArrayList<String>();
            if(_mobiles_cursor != null){
                _mobiles_cursor.moveToFirst();
                for (int i = 0; i < _mobiles_cursor.getCount(); i++) {
                    _mobiles.add(_mobiles_cursor.getString(0));
                    _mobiles_cursor.moveToNext();
                }
            }

            //prepare tels data
            ArrayList<String> _tels = new ArrayList<String>();
            if(_tels_cursor != null){
                _tels_cursor.moveToFirst();
                for (int i = 0; i < _tels_cursor.getCount(); i++) {
                    _tels.add(_tels_cursor.getString(0));
                    _tels_cursor.moveToNext();
                }
            }

            //prepare address work data
            AddressData _address_work;
            if(_address_work_cursor != null && _address_work_cursor.getCount() > 0) {
                _address_work_cursor.moveToFirst();
                String _house_id = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                String _moo = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.MOO));
                String _village = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.VILLAGE));
                String _soi = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SOI));
                String _road = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROAD));
                String _subdistrict = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                String _district = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.DISTRICT));
                String _province = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.PROVINCE));
                String _postalcode = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                String _country = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.COUNTRY));
                String _tel = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL));
                String _tel_ext = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                String _floor = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.FLOOR));
                String _room = _address_work_cursor.getString(_address_work_cursor.getColumnIndex(_dbHelper.ROOM));
                _address_work = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                        _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
            }else{
                _address_work = new AddressData("","","","","","","","","","","","","","");
            }

            //prepare address data
            AddressData _address;
            if(_address_cursor != null && _address_cursor.getCount() > 0) {
                _address_cursor.moveToFirst();
                String _house_id = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.HOUSE_ID));
                String _moo = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.MOO));
                String _village = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.VILLAGE));
                String _soi = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SOI));
                String _road = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROAD));
                String _subdistrict = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.SUBDISTRICT));
                String _district = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.DISTRICT));
                String _province = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.PROVINCE));
                String _postalcode = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.POSTALCODE));
                String _country = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.COUNTRY));
                String _tel = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL));
                String _tel_ext = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.TEL_EXT));
                String _floor = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.FLOOR));
                String _room = _address_cursor.getString(_address_cursor.getColumnIndex(_dbHelper.ROOM));
                _address = new AddressData(_house_id,_moo,_village,_soi,_road,_subdistrict,
                        _district,_province,_postalcode,_country,_tel,_tel_ext,_floor,_room);
            }else{
                _address = new AddressData("","","","","","","","","","","","","","");
            }

            if(_contact_cursor != null){
                _contact_cursor.moveToFirst();
                String _prefix = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX));
                String _prefix_vip = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.PREFIX_VIP));
                String _fname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.FNAME));
                String _lname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.LNAME));
                String _nickname = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.NICKNAME));
                String _birthdate = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.BIRTHDATE));
                String _email = _contact_cursor.getString(_contact_cursor.getColumnIndex(_dbHelper.EMAIL));
                _contact = new ContactData(_prefix,_fname,_lname,_nickname,_birthdate,_email,_mobiles,_address_work,_address,_tels,_prefix_vip);
                _contact.setOffline("1");
            }
            _dbHelper.close();
        }
        catch (SQLException e)
        {
            this.responseMessage = e.getMessage();
            Log.e(debugTag,this.responseMessage);
            _dbHelper.close();
            return  "-1";
        }

        if(_contact != null){
            //save local data to server
            if(this.save_contact_online(_contact)){
                return String.valueOf(globals.getContactId());
            }else{
                return "-1";
            }
        }else{
            return "-1";
        }
    }
    public synchronized boolean sync_save_questionnaire(ProgressDialog _pDialog){
        if(isOnline()){
            MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
            _dbHelper.open();

            ArrayList<QuestionnaireAnswerData> _questionnaire_answer_all = new ArrayList<QuestionnaireAnswerData>();


            //Get all questionnaire in local
            Cursor _questionnaire_answer_cursor_all = _dbHelper.getAllQuestionnaireAnswers();
            if(_questionnaire_answer_cursor_all != null){
                _questionnaire_answer_cursor_all.moveToFirst();
                for (int i = 0; i < _questionnaire_answer_cursor_all.getCount(); i++) {
                    QuestionnaireAnswerData _data = new QuestionnaireAnswerData();
                    _data.setCustomerId(_questionnaire_answer_cursor_all.getString(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.CUSTOMER_ID)));
                    int isCustomerLocal = _questionnaire_answer_cursor_all.getInt(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.OFFLINE_CUSTOMER));


                    if(isCustomerLocal == 1){
                        //save local customer to online customer
                        String new_contact_id = save_contact_online(_data.getCustomerId());
                        _data.setCustomerId(new_contact_id);
                    }


                    boolean isLocal = (isCustomerLocal == 1);
                    _data.setIscustomerLocal(isLocal);
                    _data.setProjectId(_questionnaire_answer_cursor_all.getString(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.PROJECT_ID)));
                    _data.setTimedevice(_questionnaire_answer_cursor_all.getString(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.TIMEDEVICE)));

                    _data.setQuestionnaireId(_questionnaire_answer_cursor_all.getString(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.QUESTIONNAIRE_ID)));

                    long _save_questionnaire_id = _questionnaire_answer_cursor_all.getLong(
                            _questionnaire_answer_cursor_all.getColumnIndex(_dbHelper.ROW_ID));

                    //for delete local data after save to server successful
                    _data.setSaveQuestionnaireId(_save_questionnaire_id);

                    //Get all question answer data in local
                    ArrayList<QuestionAnswerData> _question_answer_all = new ArrayList<QuestionAnswerData>();
                    Cursor _questions_cursor_all = _dbHelper.getQuestions(_save_questionnaire_id);
                    if(_questions_cursor_all != null){
                        _questions_cursor_all.moveToFirst();
                        for (int j = 0; j < _questions_cursor_all.getCount(); j++) {
                            String _question_id = _questions_cursor_all.getString(0);
                            Cursor _answers_cursor_all = _dbHelper.getAnswers(_save_questionnaire_id,_question_id);

                            if(_answers_cursor_all != null){
                                _answers_cursor_all.moveToFirst();

                                ArrayList<SaveAnswerData> _save_ans_all = new ArrayList<SaveAnswerData>();
                                for (int k = 0; k < _answers_cursor_all.getCount(); k++) {

                                    _save_ans_all.add(new SaveAnswerData(
                                            _answers_cursor_all.getString(_answers_cursor_all.getColumnIndex(_dbHelper.ANS_VALUE)),
                                            _answers_cursor_all.getString(_answers_cursor_all.getColumnIndex(_dbHelper.ANS_FREETXT))
                                            )
                                    );
                                    _answers_cursor_all.moveToNext();
                                }
                                QuestionAnswerData _question_answer = new QuestionAnswerData(_question_id,_save_ans_all);
                                _question_answer_all.add(_question_answer);
                            }
                            _questions_cursor_all.moveToNext();
                        }
                    }
                    _data.setAnswers(_question_answer_all);

                    //Get all staff question answer data in local
                    ArrayList<QuestionAnswerData> _staff_question_answer_all = new ArrayList<QuestionAnswerData>();
                    Cursor _staff_questions_cursor_all = _dbHelper.getStaffQuestions(_save_questionnaire_id);
                    if(_staff_questions_cursor_all != null){
                        _staff_questions_cursor_all.moveToFirst();
                        for (int j = 0; j < _staff_questions_cursor_all.getCount(); j++) {
                            String _question_id = _staff_questions_cursor_all.getString(0);
                            Cursor _answers_cursor_all = _dbHelper.getStaffAnswers(_save_questionnaire_id,_question_id);

                            if(_answers_cursor_all != null){
                                _answers_cursor_all.moveToFirst();

                                ArrayList<SaveAnswerData> _save_ans_all = new ArrayList<SaveAnswerData>();
                                for (int k = 0; k < _answers_cursor_all.getCount(); k++) {

                                    _save_ans_all.add(new SaveAnswerData(
                                                    _answers_cursor_all.getString(_answers_cursor_all.getColumnIndex(_dbHelper.ANS_VALUE)),
                                                    _answers_cursor_all.getString(_answers_cursor_all.getColumnIndex(_dbHelper.ANS_FREETXT))
                                            )
                                    );
                                    _answers_cursor_all.moveToNext();
                                }
                                QuestionAnswerData _question_answer = new QuestionAnswerData(_question_id,_save_ans_all);
                                _staff_question_answer_all.add(_question_answer);
                            }
                            _questions_cursor_all.moveToNext();
                        }
                    }
                    _data.setStaffanswers(_staff_question_answer_all);
                    _questionnaire_answer_all.add(_data);
                    _questionnaire_answer_cursor_all.moveToNext();
                }
            }


            int all_que = _questionnaire_answer_all.size();
            int cur_que = 0;
            for (int i = 0; i < _questionnaire_answer_all.size(); i++) {
                cur_que = i+1;
                //_pDialog.setMessage("Save data to server... ["+cur_que+"/"+all_que+"]");
                if(this.saveQuestionnaireData(_questionnaire_answer_all.get(i))){
                    //remove local data
                    _dbHelper.deleteQuestionnaireAnswer(_questionnaire_answer_all.get(i).getSaveQuestionnaireId());
                    _dbHelper.deleteAnswers(_questionnaire_answer_all.get(i).getSaveQuestionnaireId());
                    _dbHelper.deleteStaffAnswers(_questionnaire_answer_all.get(i).getSaveQuestionnaireId());
                }
            }

            _dbHelper.close();
            return true;
        }else{
            this.responseMessage = "Not have internet connection";
            return false;
        }

    }
    private void remove_contact_local(String _contact_id){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        //long contactId = Long.getLong(_contact_id);
        long contactId = Long.parseLong(_contact_id);
        _dbHelper.deleteMobiles(contactId);
        _dbHelper.deleteAddressWork(contactId);
        _dbHelper.deleteAddress(contactId);
        _dbHelper.deleteContact(contactId);
        _dbHelper.deleteTels(contactId);
        _dbHelper.close();
    }
    public ArrayList<QuestionTypeData> getQuestionnaireData(String _questionnaire_id, String _timestamp){
        ArrayList<QuestionTypeData> _data = new ArrayList<QuestionTypeData>();
        String _fileName = _questionnaire_id+"_"+_timestamp+".json";
        String _json_str = this._qnFS.readFileOnSD(_fileName);
        try {
            JSONObject _json_obj = new JSONObject(_json_str);
            JSONArray _questionlist = _json_obj.getJSONObject("result").getJSONArray("questionlist");
            _data = QuestionlistJSON2DATA.parseData(_questionlist);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return _data;
    }
    public ArrayList<QuestionTypeData> getStaffQuestionnaireData(String _questionnaire_id, String _timestamp){
        ArrayList<QuestionTypeData> _data = new ArrayList<QuestionTypeData>();
        String _fileName = _questionnaire_id+"_"+_timestamp+".json";
        String _json_str = this._qnFS.readFileOnSD(_fileName);
        try {
            JSONObject _json_obj = new JSONObject(_json_str);
            JSONArray _staffquestionlist = _json_obj.getJSONObject("result").getJSONArray("staffquestionlist");
            _data = QuestionlistJSON2DATA.parseData(_staffquestionlist);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return _data;
    }

    public synchronized void initProvincesData(Context ctx){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        _dbHelper.deleteAll(_dbHelper.TB_PROVINCE);
        try {
            InputStreamReader is = new InputStreamReader(ctx.getAssets()
                    .open("Province.csv"));

            BufferedReader buffer = new BufferedReader(is);

            String line = "";
            String tableName = _dbHelper.TB_PROVINCE;
            String columns = _dbHelper.ProvinceID+", "+_dbHelper.ProvinceName+", "+_dbHelper.ProvinceNameEng+", "+_dbHelper.ISDelete;
            String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
            String str2 = ");";

            _dbHelper.db.beginTransaction();
            int i =0;
            while ((line = buffer.readLine()) != null) {
                if(i > 0) {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    sb.append("'" + str[0] + "',");
                    sb.append("'" + str[1] + "',");
                    sb.append("'" + str[2] + "',");
                    sb.append("'" + str[3] + "'");
                    sb.append(str2);
                    _dbHelper.db.execSQL(sb.toString());
                }
                i++;
            }

            _dbHelper.db.setTransactionSuccessful();
            _dbHelper.db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        _dbHelper.close();
    }
    public synchronized void initDistrictData(Context ctx){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        _dbHelper.deleteAll(_dbHelper.TB_DISTRICT);
        try {
            InputStreamReader is = new InputStreamReader(ctx.getAssets()
                    .open("District.csv"));

            BufferedReader buffer = new BufferedReader(is);

            String line = "";
            String tableName = _dbHelper.TB_DISTRICT;
            ////ProvinceID,DistrictID,DistrictName,DistrictNameEng,PostCode,AutoID
            String columns = _dbHelper.ProvinceID+", "+_dbHelper.DistrictID+","+_dbHelper.DistrictName+", "+_dbHelper.DistrictNameEng+", "+_dbHelper.PostCode+", "+_dbHelper.AutoID;
            String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
            String str2 = ");";

            _dbHelper.db.beginTransaction();
            int i = 0;
            while ((line = buffer.readLine()) != null) {
                if(i > 0) {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    sb.append("'" + str[0] + "',");
                    sb.append("'" + str[1] + "',");
                    sb.append("'" + str[2] + "',");
                    sb.append("'" + str[3] + "',");
                    sb.append("'" + str[4] + "',");
                    sb.append("'" + str[5] + "'");
                    sb.append(str2);
                    _dbHelper.db.execSQL(sb.toString());
                }
                i++;
            }

            _dbHelper.db.setTransactionSuccessful();
            _dbHelper.db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        _dbHelper.close();
    }
    public synchronized void initSubDistrictData(Context ctx){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        _dbHelper.deleteAll(_dbHelper.TB_SUBDISTRICT);
        try {
            InputStreamReader is = new InputStreamReader(ctx.getAssets()
                    .open("SubDistrict.csv"));

            BufferedReader buffer = new BufferedReader(is);

            String line = "";
            String tableName = _dbHelper.TB_SUBDISTRICT;
            //ProvinceID,DistrictID,SubDistrictID,LandOfficeID,SubDistrictName,SubDistrictNameEng,PostCode
            String columns =    _dbHelper.ProvinceID+", "+
                                _dbHelper.DistrictID+", "+
                                _dbHelper.SubDistrictID+","+
                                _dbHelper.LandOfficeID+","+
                                _dbHelper.SubDistrictName+", "+
                                _dbHelper.SubDistrictNameEng+", "+
                                _dbHelper.PostCode;
            String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
            String str2 = ");";

            _dbHelper.db.beginTransaction();
            int i = 0;
            while ((line = buffer.readLine()) != null) {
                if(i > 0) {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    sb.append("'" + str[0] + "',");
                    sb.append("'" + str[1] + "',");
                    sb.append("'" + str[2] + "',");
                    sb.append("'" + str[3] + "',");
                    sb.append("'" + str[4] + "',");
                    sb.append("'" + str[5] + "',");
                    sb.append("'" + str[6] + "'");
                    sb.append(str2);
                    _dbHelper.db.execSQL(sb.toString());
                }
                i++;
            }

            _dbHelper.db.setTransactionSuccessful();
            _dbHelper.db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        _dbHelper.close();
    }
    public ArrayList<ValTextData> getProvinces(){
        ArrayList<ValTextData> data = new ArrayList<ValTextData>();
        data.add(0,new ValTextData("0","กรุณาเลือก"));
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        Cursor _cursor = _dbHelper.getAllProvince();
        if(_cursor != null)
            _cursor.moveToFirst();
        for (int i = 0; i < _cursor.getCount(); i++) {
            ValTextData val = new ValTextData(_cursor.getString(0),_cursor.getString(1));
            data.add(val);
            _cursor.moveToNext();
        }
        _dbHelper.close();
        return data;
    }
    public ArrayList<ValTextData> getDistrictByProvince(String province_id){
        ArrayList<ValTextData> data = new ArrayList<ValTextData>();
        data.add(0,new ValTextData("0","กรุณาเลือก"));
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();

        try {
            Cursor _cursor = _dbHelper.getAllDistrictByProvince(province_id);
            if(_cursor != null)
                _cursor.moveToFirst();
            for (int i = 0; i < _cursor.getCount(); i++) {
                ValTextData val = new ValTextData(_cursor.getString(0),_cursor.getString(1));
                val.setText2(_cursor.getString(2));
                data.add(val);
                _cursor.moveToNext();
            }
            _dbHelper.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        return  data;
    }
    public ArrayList<ValTextData> getSubDistrictByDistrict(String _district_id){
        ArrayList<ValTextData> data = new ArrayList<ValTextData>();
        data.add(0,new ValTextData("0","กรุณาเลือก"));
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        Cursor _cursor = _dbHelper.getSubDistrictByDistrict(_district_id);
        if(_cursor != null)
            _cursor.moveToFirst();
        for (int i = 0; i < _cursor.getCount(); i++) {
            ValTextData val = new ValTextData(_cursor.getString(0),_cursor.getString(1));
            val.setText2(_cursor.getString(2));
            data.add(val);
            _cursor.moveToNext();
        }
        _dbHelper.close();
        return  data;
    }


    public synchronized void initCountryData(Context ctx){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        _dbHelper.deleteAll(_dbHelper.DATABASE_TABLE_COUNTRY);
        try {
            InputStreamReader is = new InputStreamReader(ctx.getAssets()
                    .open("tbl_CountryInfo.csv"));

            BufferedReader buffer = new BufferedReader(is);

            String line = "";
            String tableName = _dbHelper.DATABASE_TABLE_COUNTRY;
            String columns = " id, title";
            String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
            String str2 = ");";

            _dbHelper.db.beginTransaction();
            int i =0;
            while ((line = buffer.readLine()) != null) {
                if(i > 0) {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    sb.append("'" + str[0] + "',");
                    sb.append("'" + str[1] + "'");
                    sb.append(str2);
                    _dbHelper.db.execSQL(sb.toString());
                }
                i++;
            }

            _dbHelper.db.setTransactionSuccessful();
            _dbHelper.db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        _dbHelper.close();
    }
    public synchronized void initNationalityData(Context ctx){
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        _dbHelper.deleteAll(_dbHelper.DATABASE_TABLE_NATIONALITY);
        try {
            InputStreamReader is = new InputStreamReader(ctx.getAssets()
                    .open("tbl_Nationality.csv"));

            BufferedReader buffer = new BufferedReader(is);

            String line = "";
            String tableName = _dbHelper.DATABASE_TABLE_NATIONALITY;
            String columns = " id, title";
            String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
            String str2 = ");";

            _dbHelper.db.beginTransaction();
            int i =0;
            while ((line = buffer.readLine()) != null) {
                if(i > 0) {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    sb.append("'" + str[0] + "',");
                    sb.append("'" + str[1] + "'");
                    sb.append(str2);
                    _dbHelper.db.execSQL(sb.toString());
                }
                i++;
            }

            _dbHelper.db.setTransactionSuccessful();
            _dbHelper.db.endTransaction();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(debugTag,e.getMessage());
        }

        _dbHelper.close();
    }

    public ArrayList<ValTextData> getCountry(){
        ArrayList<ValTextData> data = new ArrayList<ValTextData>();
        data.add(0,new ValTextData("0","กรุณาเลือก"));
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        Cursor _cursor = _dbHelper.getAllCountry();
        if(_cursor != null)
            _cursor.moveToFirst();
        for (int i = 0; i < _cursor.getCount(); i++) {
            ValTextData val = new ValTextData(_cursor.getString(0),_cursor.getString(1));
            data.add(val);
            _cursor.moveToNext();
        }
        _dbHelper.close();
        return data;
    }
    public ArrayList<ValTextData> getNationality(){
        ArrayList<ValTextData> data = new ArrayList<ValTextData>();
        data.add(0,new ValTextData("0","กรุณาเลือก"));
        MySQLiteHelper _dbHelper = new MySQLiteHelper(this.mCtx);
        _dbHelper.open();
        Cursor _cursor = _dbHelper.getAllNationality();
        if(_cursor != null)
            _cursor.moveToFirst();
        for (int i = 0; i < _cursor.getCount(); i++) {
            ValTextData val = new ValTextData(_cursor.getString(0),_cursor.getString(1));
            data.add(val);
            _cursor.moveToNext();
        }
        _dbHelper.close();
        return data;
    }
    /**
     * Forgor password Method
     * @param params [ 0 - email ]
     * @return true / false
     */
    public boolean ForgotPassword(String... params){
        this.forgotMessage = "";
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email",params[0]);
            String r = ForgotpasswordMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
            try{
                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")){
                    //Log.d(debugTag, respObj.getJSONObject("result").getString("message") + " - OK");
                    this.forgotMessage = respObj.getJSONObject("result").getString("message");
                    return true;
                }else{
                    //Log.d(debugTag,respObj.getJSONObject("result").getString("message"));
                    this.forgotMessage = respObj.getJSONObject("result").getString("message");
                    return false;
                }
            }catch (JSONException er){
                er.printStackTrace();
                this.forgotMessage = er.getMessage();
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.forgotMessage = e.getMessage();
            return  false;
        }
    }
    public String getForgotMessage() {
        return forgotMessage;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void TestLogin(){
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("username","usertest");
            jsonObj.put("password","11111");

            String r = LoginMethod.execute(this.mCtx, webserviceUrl, jsonObj.toString());
            try{
                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")){
                    Log.d(debugTag, respObj.getJSONObject("result").getString("message") + " - OK");
                }else{
                    Log.d(debugTag,respObj.getJSONObject("result").getString("message"));
                }
            }catch (JSONException er){
                er.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void TestForgotPassword(){
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email","test@example.com");
            String r = ForgotpasswordMethod.execute(this.mCtx,webserviceUrl,jsonObj.toString());
            try{
                JSONObject respObj = new JSONObject(r);
                if(respObj.getBoolean("status")){
                    Log.d(debugTag, respObj.getJSONObject("result").getString("message") + " - OK");
                }else{
                    Log.d(debugTag,respObj.getJSONObject("result").getString("message"));
                }
            }catch (JSONException er){
                er.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public synchronized ArrayList<QuestionAnswerData> getQuestionnaireAnswerHistory(String _questionnaire_id){
        ArrayList<QuestionAnswerData> _data = new ArrayList<QuestionAnswerData>();
        if(isOnline() && !globals.getIsCustomerLocal()) {
            try{
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tokenaccess",globals.getLoginTokenAccess());
                jsonObj.put("questionnaireid",_questionnaire_id);
                jsonObj.put(PARAM_CUSTOMERID,String.valueOf(globals.getContactId()));

                try{
                    String r = AnswerHistoryMethod.execute(this.mCtx, webserviceUrl, jsonObj.toString());
                    JSONObject respObj = new JSONObject(r);
                    if(respObj.getBoolean("status")) {

                        JSONArray _question_ans_json = respObj.getJSONArray("result");
                        for (int i = 0; i < _question_ans_json.length(); i++) {
                            JSONObject _question = _question_ans_json.getJSONObject(i);
                            String _question_id = _question.getString("questionId");
                            JSONArray _ans_json = _question.getJSONArray("answer");
                            ArrayList<SaveAnswerData> _ans = new ArrayList<SaveAnswerData>();
                            for (int j = 0; j < _ans_json.length(); j++) {
                                JSONObject _ans_h = _ans_json.getJSONObject(j);
                                _ans.add(new SaveAnswerData(_ans_h.getString("value"),_ans_h.getString("freetxt")));
                            }

                            QuestionAnswerData _question_ans = new QuestionAnswerData(_question_id,_ans);
                            _data.add(_question_ans);
                        }
                    }else{
                        this.responseMessage = respObj.getJSONObject("result").getString("message");
                    }
                }
                catch (AnswerHistoryMethod.ApiException ae)
                {
                    ae.printStackTrace();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return _data;

    }
    public synchronized ArrayList<SaveAnswerData> getAnswerHistory(String _question_id){
        ArrayList<SaveAnswerData> _ans = new ArrayList<SaveAnswerData>();
        if(isOnline() && !globals.getIsCustomerLocal()){
            try{
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tokenaccess",globals.getLoginTokenAccess());
                jsonObj.put(PARAM_QUESTIONID,_question_id);
                jsonObj.put(PARAM_CUSTOMERID,String.valueOf(globals.getContactId()));

                try{
                    String r = AnswerHistoryMethod.execute(this.mCtx, webserviceUrl, jsonObj.toString());
                    JSONObject respObj = new JSONObject(r);
                    if(respObj.getBoolean("status")) {
                        JSONArray _ans_json = respObj.getJSONObject("result").getJSONArray("answer");
                        for (int i = 0; i < _ans_json.length(); i++) {
                            JSONObject _ans_h = _ans_json.getJSONObject(i);
                            _ans.add(new SaveAnswerData(_ans_h.getString("value"),_ans_h.getString("freetxt")));
                        }
                    }else{
                        this.responseMessage = respObj.getJSONObject("result").getString("message");
                        _ans.add(new SaveAnswerData("-1", ""));
                    }
                }
                catch (AnswerHistoryMethod.ApiException ae)
                {
                    ae.printStackTrace();
                    _ans.add(new SaveAnswerData("-1",""));
                }

            }
            catch (JSONException e){
                e.printStackTrace();
                _ans.add(new SaveAnswerData("-1",""));
            }
        }else{
            _ans.add(new SaveAnswerData("-1",""));
        }
        return _ans;
    }
}
