package com.cloud9worldwide.questionnaire.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by cloud9 on 3/27/14.
 */
public class MySQLiteHelper {

    public static final String DATABASE_NAME = "questionnaire_database.db";
    public static final int DATABASE_VERSION = 1;


    public static final String ROW_ID = "_id";
    public static final String PROJECT_ID = "projectid";
    public static final String NAME = "name";
    public static final String LOGO = "logo";
    public static final String BACKGROUND = "background";
    public static final String QUESTIONNAIRE_ID = "questionnaireid";
    public static final String TYPE = "questionnairetype";
    public static final String TIMESTAMP = "timestamp";

    //contact fields
    public static final String PREFIX = "prefix";
    public static final String PREFIX_VIP = "prefix_vip";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String NICKNAME = "nickname";
    public static final String BIRTHDATE = "birthdate";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";

    //Address fields
    public static final String HOUSE_ID = "house_id";
    public static final String MOO = "moo";
    public static final String VILLAGE = "village";
    public static final String SOI = "soi";
    public static final String ROAD = "road";
    public static final String SUBDISTRICT = "subdistrict";
    public static final String DISTRICT = "district";
    public static final String PROVINCE = "province";
    public static final String POSTALCODE = "postalcode";
    public static final String COUNTRY = "country";
    public static final String TEL = "tel";
    public static final String TEL_EXT = "tel_ext";
    public static final String FLOOR = "floor";
    public static final String ROOM = "room";

    //Save Questionnaire
    public static final String CUSTOMER_ID = "customerid";
    public static final String STAFF_ID = "staffid";
    public static final String TIMEDEVICE = "timedevice";
    public static final String QUESTION_ID = "questionid";
    public static final String ANS_VALUE = "ans_value";
    public static final String ANS_FREETXT = "ans_freetxt";

    public static final String CONTACT_ID = "contactid";
    public static final String OFFLINE_CUSTOMER = "offline";

    public static final String SAVE_QUESTIONNAIREID = "savequestionnarieid";

    public static final String DATABASE_TABLE_QUESTIONNAIRE = "questionnaires";
    public static final String DATABASE_TABLE_PROJECT = "projects";
    public static final String DATABASE_TABLE_CONTACT = "contact";
    public static final String DATABASE_TABLE_ADDRESS = "address";
    public static final String DATABASE_TABLE_ADDRESS_WORK = "address_work";
    public static final String DATABASE_TABLE_MOBILES = "mobiles";
    public static final String DATABASE_TABLE_TELS = "tels";
    public static final String DATABASE_TABLE_SAVEQUESTIONNAIRE = "savequestionnaire";
    public static final String DATABASE_TABLE_SAVEANSWERS = "saveanswers";
    public static final String DATABASE_TABLE_SAVESTAFFANSWERS = "savestaffanswers";

    public static final String DATABASE_TABLE_NATIONALITY = "nationality";
    public static final String DATABASE_TABLE_COUNTRY = "country";


    public static final String CREATE_TABLE_PROJECT =
            "create table "+DATABASE_TABLE_PROJECT+" (_id integer primary key autoincrement, "
                    + PROJECT_ID+ " TEXT,"
                    + NAME+ " TEXT,"
                    + LOGO+ " TEXT,"
                    + BACKGROUND+ " TEXT" + ");";

    public static final String CREATE_TABLE_QUESTIONNAIRE = "create table "+DATABASE_TABLE_QUESTIONNAIRE+" (_id integer primary key autoincrement, "
            +QUESTIONNAIRE_ID+" TEXT,"
            +PROJECT_ID+" TEXT,"
            +TYPE+" TEXT,"
            +LOGO+" TEXT,"
            +TIMESTAMP+" TEXT"+ ");";

    public static final String CREATE_TABLE_CONTACT = "create table "+DATABASE_TABLE_CONTACT
            +" (_id integer primary key autoincrement, "
            +PREFIX + " TEXT, "
            +PREFIX_VIP + " TEXT, "
            +FNAME + " TEXT, "
            +LNAME + " TEXT, "
            +NICKNAME + " TEXT, "
            +BIRTHDATE + " TEXT, "
            +EMAIL + " TEXT "
            +" );";

    public static final String CRATE_TABLE_MOBILES = "create table "+DATABASE_TABLE_MOBILES
            +" (_id integer primary key autoincrement, "
            +"contactid INTEGER, "
            +MOBILE + " TEXT "
            +" );";
    public static final String CREATE_TABLE_TELS = "create table "+ DATABASE_TABLE_TELS
            +" (_id integer primary key autoincrement, "
            +"contactid INTEGER, "
            +TEL + " TEXT "
            +" );";

    private static final String CREATE_TABLE_ADDRESS_WORK = "create table "+DATABASE_TABLE_ADDRESS_WORK
            +" (_id integer primary key autoincrement, "
            +"contactid INTEGER, "
            +HOUSE_ID + " TEXT, "
            +MOO + " TEXT, "
            +VILLAGE + " TEXT, "
            +SOI + " TEXT, "
            +ROAD + " TEXT, "
            +SUBDISTRICT + " TEXT, "
            +DISTRICT + " TEXT, "
            +PROVINCE + " TEXT, "
            +POSTALCODE + " TEXT, "
            +COUNTRY + " TEXT, "
            +TEL + " TEXT, "
            +TEL_EXT + " TEXT, "
            +FLOOR + " TEXT, "
            +ROOM + " TEXT "
            +" );";

    private static final String CREATE_TABLE_ADDRESS = "create table "+DATABASE_TABLE_ADDRESS
            +" (_id integer primary key autoincrement, "
            +"contactid INTEGER, "
            +HOUSE_ID + " TEXT, "
            +MOO + " TEXT, "
            +VILLAGE + " TEXT, "
            +SOI + " TEXT, "
            +ROAD + " TEXT, "
            +SUBDISTRICT + " TEXT, "
            +DISTRICT + " TEXT, "
            +PROVINCE + " TEXT, "
            +POSTALCODE + " TEXT, "
            +COUNTRY + " TEXT, "
            +TEL + " TEXT, "
            +TEL_EXT + " TEXT, "
            +FLOOR + " TEXT, "
            +ROOM + " TEXT "
            +" );";

    private static final String CREATE_TABLE_SAVEQUESTIONNAIRE = "create table "+DATABASE_TABLE_SAVEQUESTIONNAIRE
            +" (_id integer primary key autoincrement, "
            +CUSTOMER_ID + " TEXT, "
            +STAFF_ID + " TEXT, "
            +PROJECT_ID + " TEXT, "
            +QUESTIONNAIRE_ID + " TEXT, "
            +TIMEDEVICE + " TEXT, "
            +OFFLINE_CUSTOMER + " INTEGER "
            +" );";

    private static final String CREATE_TABLE_SAVEANSWERS = "create table "+DATABASE_TABLE_SAVEANSWERS
            +" (_id integer primary key autoincrement, "
            +SAVE_QUESTIONNAIREID +" INTEGER, "
            +QUESTION_ID + " TEXT, "
            +ANS_VALUE + " TEXT, "
            +ANS_FREETXT + " TEXT"
            +" );";

    private static final String CREATE_TABLE_SAVESTAFFANSWER = "create table "+DATABASE_TABLE_SAVESTAFFANSWERS
            +" (_id integer primary key autoincrement, "
            +SAVE_QUESTIONNAIREID +" INTEGER, "
            +QUESTION_ID + " TEXT, "
            +ANS_VALUE + " TEXT, "
            +ANS_FREETXT + " TEXT"
            +" );";

    public static final String ProvinceID = "ProvinceID";
    public static final String ProvinceName = "ProvinceName";
    public static final String ProvinceNameEng = "ProvinceNameEng";
    public static final String ISDelete = "ISDelete";
    public static final String TB_PROVINCE = "province";
    private static final String CREATE_TABLE_PROVINCE = "create table "+TB_PROVINCE +" ( "
            + ProvinceID + " TEXT, "
            + ProvinceName + " TEXT, "
            + ProvinceNameEng + " TEXT, "
            + ISDelete + " TEXT "
            + " ); ";

    //ProvinceID,DistrictID,DistrictName,DistrictNameEng,PostCode,AutoID
    public static final String DistrictID = "DistrictID";
    public static final String DistrictName = "DistrictName";
    public static final String DistrictNameEng = "DistrictNameEng";
    public static final String PostCode = "PostCode";
    public static final String AutoID = "AutoID";
    public static final String TB_DISTRICT = "district";
    private static final String CREATE_TABLE_DISTRICT = "create table "+TB_DISTRICT +" ( "
            + ProvinceID + " TEXT, "
            + DistrictID + " TEXT, "
            + DistrictName + " TEXT, "
            + DistrictNameEng + " TEXT, "
            + PostCode + " TEXT, "
            + AutoID + " TEXT "
            + " ); ";

    //ProvinceID,DistrictID,SubDistrictID,LandOfficeID,SubDistrictName,SubDistrictNameEng,PostCode
    public static final String SubDistrictID = "SubDistrictID";
    public static final String LandOfficeID = "LandOfficeID";
    public static final String SubDistrictName = "SubDistrictName";
    public static final String SubDistrictNameEng = "SubDistrictNameEng";
    public static final String TB_SUBDISTRICT = "subdistrict";
    private static final String CREATE_TABLE_SUBDISTRICT = "create table "+TB_SUBDISTRICT +" ( "
            + ProvinceID + " TEXT, "
            + DistrictID + " TEXT, "
            + SubDistrictID + " TEXT, "
            + LandOfficeID + " TEXT, "
            + SubDistrictName + " TEXT, "
            + SubDistrictNameEng + " TEXT, "
            + PostCode + " TEXT "
            + " ); ";


    public static final String CREATE_TABLE_NATIONALITY = "create table "+ DATABASE_TABLE_NATIONALITY +" ( "
            +" id TEXT, "
            +" title TEXT "
            +" );";

    public static final String CREATE_TABLE_COUNTRY = "create table "+ DATABASE_TABLE_COUNTRY+" ( "
            +" id TEXT, "
            +" title TEXT "
            +" );";


    private final Context context;
    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    public  MySQLiteHelper(Context ctx){
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_PROJECT);
            db.execSQL(CREATE_TABLE_QUESTIONNAIRE);
            db.execSQL(CREATE_TABLE_CONTACT);
            db.execSQL(CRATE_TABLE_MOBILES);
            db.execSQL(CREATE_TABLE_ADDRESS_WORK);
            //Log.e("DB",CREATE_TABLE_ADDRESS_WORK);
            db.execSQL(CREATE_TABLE_ADDRESS);
            //Log.e("DB",CREATE_TABLE_ADDRESS);
            db.execSQL(CREATE_TABLE_SAVEQUESTIONNAIRE);
            db.execSQL(CREATE_TABLE_SAVEANSWERS);
            db.execSQL(CREATE_TABLE_SAVESTAFFANSWER);
            db.execSQL(CREATE_TABLE_TELS);

            db.execSQL(CREATE_TABLE_PROVINCE);
            db.execSQL(CREATE_TABLE_DISTRICT);
            db.execSQL(CREATE_TABLE_SUBDISTRICT);
            db.execSQL(CREATE_TABLE_NATIONALITY);
            db.execSQL(CREATE_TABLE_COUNTRY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            // Adding any table mods to this guy here
        }

        @Override
        public SQLiteDatabase getWritableDatabase() {
            return super.getWritableDatabase();
        }
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DBAdapter
     */
    public MySQLiteHelper open() throws SQLException
    {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }

    public SQLiteDatabase getWritableDatabase() {
        return this.DBHelper.getWritableDatabase();
    }

    public void deleteAll(String _tbname){
        this.db.delete(_tbname, null, null);
    }

    /**
     * Create a new project. If the project is successfully created return the new
     * rowId for that project, otherwise return a -1 to indicate failure.
     * @param projectid
     * @param name
     * @param logo
     * @param background
     * @return rowId or -1 if failed
     */
    public long createProject(String projectid,String name, String logo, String background){
        ContentValues initialValues = new ContentValues();
        initialValues.put(PROJECT_ID, projectid);
        initialValues.put(NAME, name);
        initialValues.put(LOGO, logo);
        initialValues.put(BACKGROUND, background);
        return this.db.insert(DATABASE_TABLE_PROJECT, null, initialValues);
    }

    /**
     * Delete the project with the given rowId
     *
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteProject(long rowId) {

        return this.db.delete(DATABASE_TABLE_PROJECT, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all projects in the database
     *
     * @return Cursor over all projects
     */
    public Cursor getAllProjects() {

        return this.db.query(DATABASE_TABLE_PROJECT, new String[] { ROW_ID,PROJECT_ID,
                NAME, LOGO, BACKGROUND }, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the project that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching project, if found
     * @throws SQLException if project could not be found/retrieved
     */
    public Cursor getProject(long rowId) throws SQLException {

        Cursor mCursor =

                this.db.query(true, DATABASE_TABLE_PROJECT, new String[] { ROW_ID,PROJECT_ID, NAME,
                        LOGO, BACKGROUND}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the project.
     *
     * @param rowId
     * @param projectid
     * @param name
     * @param logo
     * @param background
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateProject(long rowId,String projectid, String name, String logo,
                                 String background){
        ContentValues args = new ContentValues();
        args.put(PROJECT_ID, projectid);
        args.put(NAME, name);
        args.put(LOGO, logo);
        args.put(BACKGROUND, background);

        return this.db.update(DATABASE_TABLE_PROJECT, args, ROW_ID + "=" + rowId, null) >0;
    }


    /**
     * Create a new questionnaire. If the questionnaire is successfully created return the new
     * rowId for that questionnaire, otherwise return a -1 to indicate failure.
     * @param questionnaireid
     * @param projectid
     * @param questionnairetype
     * @param logo
     * @param timestamp
     * @return rowId or -1 if failed
     */
    public long createQuestionnaire(String questionnaireid,String projectid,String questionnairetype, String logo, String timestamp){
        ContentValues initialValues = new ContentValues();
        initialValues.put(QUESTIONNAIRE_ID, questionnaireid);
        initialValues.put(PROJECT_ID, projectid);
        initialValues.put(TYPE, questionnairetype);
        initialValues.put(LOGO, logo);
        initialValues.put(TIMESTAMP, timestamp);
        return this.db.insert(DATABASE_TABLE_QUESTIONNAIRE, null, initialValues);
    }

    public boolean updateQuestionnaire(String questionnaire_id,String projectid,String questionnairetype, String logo, String timestamp){
        ContentValues initialValues = new ContentValues();
        //nitialValues.put(QUESTIONNAIRE_ID, questionnaire_id);
        initialValues.put(PROJECT_ID, projectid);
        initialValues.put(TYPE, questionnairetype);
        initialValues.put(LOGO, logo);
        initialValues.put(TIMESTAMP, timestamp);

        return this.db.update(DATABASE_TABLE_QUESTIONNAIRE, initialValues, QUESTIONNAIRE_ID + "= ?", new String[] { questionnaire_id}) >0;
    }
    /**
     * Return a Cursor over the list of all questionnaire in the project
     *
     * @param projectid
     *
     * @return Cursor over all questionnaire in project
     * @throws SQLException if questionnaire could not be found/retrieved
     */
    public Cursor getAllQuestionnaireByProject(String projectid) throws SQLException {
        String whereClause = PROJECT_ID + "= ? ";
        String[] whereArgs = new String[] {
                projectid
        };

        return this.db.query(DATABASE_TABLE_QUESTIONNAIRE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
                TYPE, LOGO, TIMESTAMP }, whereClause ,whereArgs, null, null, null, null);
    }



    /**
     * Return a Cursor positioned at the questionnaire that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching questionnaire, if found
     * @throws SQLException if questionnaire could not be found/retrieved
     */
    public Cursor getQuestionnaire(long rowId) throws SQLException {

        Cursor mCursor =

                this.db.query(true, DATABASE_TABLE_QUESTIONNAIRE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
                        TYPE, LOGO, TIMESTAMP}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the questionnaire that matches the given rowId
     * @param questionnaireid
     * @return Cursor positioned to matching questionnaire, if found
     * @throws SQLException if questionnaire could not be found/retrieved
     */
    public Cursor getQuestionnaireById(String questionnaireid) throws SQLException {

        String whereClause = QUESTIONNAIRE_ID + "= ? ";
        String[] whereArgs = new String[] {
                questionnaireid
        };
        /*
        Cursor mCursor =

                this.db.query(true, DATABASE_TABLE_QUESTIONNAIRE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
                        TYPE, LOGO, TIMESTAMP},whereClause , whereArgs, null, null, null, null);
                        */
        String sql = "select * from "+DATABASE_TABLE_QUESTIONNAIRE+" where "+whereClause;
        //Log.d("Core",sql);
        Cursor mCursor = this.db.rawQuery(sql,whereArgs);
        if (mCursor != null) {
            mCursor.moveToFirst();
            //Log.d("Core","debug :: "+mCursor.getCount());
        }
        return mCursor;
    }

    /**
     * Update the questionnaire.
     *
     * @param rowId
     * @param questionnaireid
     * @param projectid
     * @param type
     * @param logo
     * @param timestamp
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateProject(long rowId,String questionnaireid, String projectid, String type, String logo,
                                 String timestamp){
        ContentValues args = new ContentValues();
        args.put(QUESTIONNAIRE_ID, questionnaireid);
        args.put(PROJECT_ID, projectid);
        args.put(TYPE, type);
        args.put(LOGO, logo);
        args.put(TIMESTAMP, timestamp);

        return this.db.update(DATABASE_TABLE_QUESTIONNAIRE, args, ROW_ID + "=" + rowId, null) >0;
    }

    /**
     *
     * @param prefix
     * @param fname
     * @param lname
     * @param nickname
     * @param birthdate
     * @param email
     * @return @rowid
     */
    public long createContact(String prefix,String fname, String lname, String nickname,String birthdate, String email,String prefix_vip){
        ContentValues initialValues = new ContentValues();
        initialValues.put(PREFIX, prefix);
        initialValues.put(PREFIX_VIP, prefix_vip);
        initialValues.put(FNAME, fname);
        initialValues.put(LNAME, lname);
        initialValues.put(NICKNAME, nickname);
        initialValues.put(BIRTHDATE, birthdate);
        initialValues.put(EMAIL, email);
        return this.db.insert(DATABASE_TABLE_CONTACT, null, initialValues);
    }
    public long createMobile(Long contactid,String mobile){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CONTACT_ID, contactid);
        initialValues.put(MOBILE, mobile);
        return this.db.insert(DATABASE_TABLE_MOBILES, null, initialValues);
    }
    public long createAddressWork(Long contactid,
                                  String house_id,
                                  String moo,
                                  String village,
                                  String soi,
                                  String road,
                                  String subdistrict,
                                  String district,
                                  String province,
                                  String postalcode,
                                  String country,
                                  String tel,
                                  String tel_ext,
                                  String floor,
                                  String room){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CONTACT_ID, contactid);
        initialValues.put(MOO, moo);
        initialValues.put(VILLAGE, village);
        initialValues.put(SOI, soi);
        initialValues.put(ROAD, road);
        initialValues.put(SUBDISTRICT, subdistrict);
        initialValues.put(DISTRICT, district);
        initialValues.put(PROVINCE, province);
        initialValues.put(POSTALCODE, postalcode);
        initialValues.put(COUNTRY, country);
        initialValues.put(TEL, tel);
        initialValues.put(TEL_EXT, tel_ext);
        initialValues.put(FLOOR, floor);
        initialValues.put(ROOM, room);
        return this.db.insert(DATABASE_TABLE_ADDRESS_WORK, null, initialValues);
    }
    public long createAddress(Long contactid,
                                  String house_id,
                                  String moo,
                                  String village,
                                  String soi,
                                  String road,
                                  String subdistrict,
                                  String district,
                                  String province,
                                  String postalcode,
                                  String country,
                                  String tel,
                                  String tel_ext,
                                  String floor,
                                  String room){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CONTACT_ID, contactid);
        initialValues.put(HOUSE_ID, house_id);
        initialValues.put(MOO, moo);
        initialValues.put(VILLAGE, village);
        initialValues.put(SOI, soi);
        initialValues.put(ROAD, road);
        initialValues.put(SUBDISTRICT, subdistrict);
        initialValues.put(DISTRICT, district);
        initialValues.put(PROVINCE, province);
        initialValues.put(POSTALCODE, postalcode);
        initialValues.put(COUNTRY, country);
        initialValues.put(TEL, tel);
        initialValues.put(TEL_EXT, tel_ext);
        initialValues.put(FLOOR, floor);
        initialValues.put(ROOM, room);
        return this.db.insert(DATABASE_TABLE_ADDRESS, null, initialValues);
    }
    public Cursor getContact(long rowId) throws SQLException {

        Cursor mCursor =

                this.db.query(true, DATABASE_TABLE_CONTACT, new String[] { ROW_ID,PREFIX,PREFIX_VIP,FNAME,
                        LNAME, NICKNAME,BIRTHDATE,EMAIL}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public boolean deleteContact(long contactId){
        return this.db.delete(DATABASE_TABLE_CONTACT, ROW_ID + "=" + contactId, null) > 0;
    }
    public Cursor getMobiles(long contactId){
        Cursor mCursor = this.db.query(true,DATABASE_TABLE_MOBILES,new String[] {MOBILE},
                CONTACT_ID + "=" +contactId,null,null,null,null,null);
        return mCursor;
    }
    public boolean deleteMobiles(long contactId){
        return this.db.delete(DATABASE_TABLE_MOBILES, CONTACT_ID + "=" + contactId, null) > 0;
    }
    public Cursor getAddressWork(long contactId){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_ADDRESS_WORK,new String[]{HOUSE_ID,MOO,VILLAGE,SOI,ROAD,SUBDISTRICT,DISTRICT,PROVINCE,
                POSTALCODE,COUNTRY,TEL,TEL_EXT,FLOOR,ROOM},
                CONTACT_ID + "=" +contactId,null,null,null,null,null);
        return mCursor;
    }
    public boolean deleteAddressWork(long contactId){
        return this.db.delete(DATABASE_TABLE_ADDRESS_WORK, CONTACT_ID + "=" + contactId, null) > 0;
    }
    public Cursor getAddress(long contactId){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_ADDRESS,new String[]{HOUSE_ID,MOO,VILLAGE,SOI,ROAD,SUBDISTRICT,DISTRICT,PROVINCE,
                        POSTALCODE,COUNTRY,TEL,TEL_EXT,FLOOR,ROOM},
                CONTACT_ID + "=" +contactId,null,null,null,null,null);
        return mCursor;
    }
    public boolean deleteAddress(long contactId){
        return this.db.delete(DATABASE_TABLE_ADDRESS, CONTACT_ID + "=" + contactId, null) > 0;
    }
    public long createQuestionnaireAnswer(String contactid,String staffid,String projectid,String questionnaireid,
                                           String timedevice,Integer offlinecustomer){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CUSTOMER_ID, contactid);
        initialValues.put(STAFF_ID, staffid);
        initialValues.put(PROJECT_ID, projectid);
        initialValues.put(QUESTIONNAIRE_ID, questionnaireid);
        initialValues.put(TIMEDEVICE, timedevice);
        initialValues.put(OFFLINE_CUSTOMER, offlinecustomer);
        return this.db.insert(DATABASE_TABLE_SAVEQUESTIONNAIRE, null, initialValues);
    }
    public long createAnswer(long save_questionnaire_id,String question_id,String ans_val,String ans_freetxt){
        ContentValues initialValues = new ContentValues();
        initialValues.put(SAVE_QUESTIONNAIREID,save_questionnaire_id);
        initialValues.put(QUESTION_ID,question_id);
        initialValues.put(ANS_VALUE,ans_val);
        initialValues.put(ANS_FREETXT,ans_freetxt);
        return this.db.insert(DATABASE_TABLE_SAVEANSWERS, null, initialValues);
    }
    public long createStaffAnswer(long save_questionnaire_id,String question_id,String ans_val,String ans_freetxt){
        ContentValues initialValues = new ContentValues();
        initialValues.put(SAVE_QUESTIONNAIREID,save_questionnaire_id);
        initialValues.put(QUESTION_ID,question_id);
        initialValues.put(ANS_VALUE,ans_val);
        initialValues.put(ANS_FREETXT,ans_freetxt);
        return this.db.insert(DATABASE_TABLE_SAVESTAFFANSWERS, null, initialValues);
    }
    public Cursor getAllQuestionnaireAnswers() {
        return this.db.query(DATABASE_TABLE_SAVEQUESTIONNAIRE, new String[] { ROW_ID,CUSTOMER_ID,
                STAFF_ID, PROJECT_ID, QUESTIONNAIRE_ID,TIMEDEVICE,OFFLINE_CUSTOMER }, null, null, null, null, null);
    }
    public Cursor getAnswers(long save_questionnaire_id,String question_id){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_SAVEANSWERS,new String[]{QUESTION_ID,ANS_VALUE,ANS_FREETXT},
                SAVE_QUESTIONNAIREID + "=" +save_questionnaire_id+" AND "+ QUESTION_ID +"=?",new String[] { question_id},null,null,null,null);
        return mCursor;
    }
    public Cursor getQuestions(long save_questionnaire_id){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_SAVEANSWERS,new String[]{QUESTION_ID},
                SAVE_QUESTIONNAIREID + "=" +save_questionnaire_id,null,null,null,null,null);
        return mCursor;
    }
    public Cursor getStaffAnswers(long save_questionnaire_id,String question_id){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_SAVESTAFFANSWERS,new String[]{QUESTION_ID,ANS_VALUE,ANS_FREETXT},
                SAVE_QUESTIONNAIREID + "=" +save_questionnaire_id+" AND "+ QUESTION_ID +"=?",new String[] { question_id},null,null,null,null);
        return mCursor;
    }
    public Cursor getStaffQuestions(long save_questionnaire_id){
        Cursor mCursor;
        mCursor = this.db.query(true,DATABASE_TABLE_SAVESTAFFANSWERS,new String[]{QUESTION_ID},
                SAVE_QUESTIONNAIREID + "=" +save_questionnaire_id,null,null,null,null,null);
        return mCursor;
    }
    public boolean deleteQuestionnaireAnswer(long rowId){
        return this.db.delete(DATABASE_TABLE_SAVEQUESTIONNAIRE, ROW_ID + "=" + rowId, null) > 0;
    }
    public boolean deleteAnswers(long save_questionnaire_id){
        return this.db.delete(DATABASE_TABLE_SAVEANSWERS, SAVE_QUESTIONNAIREID + "=" + save_questionnaire_id, null) > 0;
    }
    public boolean deleteStaffAnswers(long save_questionnaire_id){
        return this.db.delete(DATABASE_TABLE_SAVESTAFFANSWERS, SAVE_QUESTIONNAIREID + "=" + save_questionnaire_id, null) > 0;
    }
    public Cursor getAllProvince(){
        return this.db.query(TB_PROVINCE, new String[] { ProvinceID,ProvinceName}, ISDelete +"= 0", null, null, null, ProvinceName);
    }
    public Cursor getAllDistrictByProvince(String _province_id) throws SQLException {
        Cursor mCursor =  this.db.query(TB_DISTRICT, new String[]{DistrictID,DistrictName,PostCode},ProvinceID + "= '"+_province_id+"' ",null,null,null,DistrictName);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getSubDistrictByDistrict(String _district_id){
        return this.db.query(TB_SUBDISTRICT, new String[]{SubDistrictID,SubDistrictName,PostCode},DistrictID + "= '"+_district_id+"' ",null,null,null,SubDistrictName);
    }




    public long createTel(Long contactid,String tel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(CONTACT_ID, contactid);
        initialValues.put(TEL, tel);
        return this.db.insert(DATABASE_TABLE_TELS, null, initialValues);
    }
    public Cursor getTels(long contactId){
        Cursor mCursor = this.db.query(true,DATABASE_TABLE_TELS,new String[] {TEL},
                CONTACT_ID + "=" +contactId,null,null,null,null,null);
        return mCursor;
    }
    public boolean deleteTels(long contactId){
        return this.db.delete(DATABASE_TABLE_TELS, CONTACT_ID + "=" + contactId, null) > 0;
    }


    public Cursor getAllCountry(){
        return this.db.query(DATABASE_TABLE_COUNTRY, new String[] { "id","title"}, null, null, null, null, "title");
    }
    public Cursor getAllNationality(){
        return this.db.query(DATABASE_TABLE_NATIONALITY, new String[] { "id","title"}, null, null, null, null, "title");
    }

    public boolean deleteQuestionnairExceptId(ArrayList<String> Ids,String proId){
        String str_ids = "";
        for (int i = 0; i < Ids.size(); i++) {
            str_ids += "'"+Ids.get(i)+"',";
        }
        str_ids = str_ids.substring(0,str_ids.length() - 1);
        return this.db.delete(DATABASE_TABLE_QUESTIONNAIRE, QUESTIONNAIRE_ID + " NOT IN (" + str_ids+" ) AND "+PROJECT_ID+ " = '"+proId+"'", null) > 0;
    }
    /**
     * close the db
     * return type: void
     */
    public void close()
    {
        this.DBHelper.close();
    }
}
