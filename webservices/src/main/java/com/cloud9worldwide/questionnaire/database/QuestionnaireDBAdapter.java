package com.cloud9worldwide.questionnaire.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cloud9 on 3/31/14.
 */
public class QuestionnaireDBAdapter {
    public static final String ROW_ID = "_id";
    public static final String QUESTIONNAIRE_ID = "questionnaireid";
    public static final String PROJECT_ID = "projectid";
    public static final String TYPE = "questionnairetype";
    public static final String LOGO = "logo";
    public static final String TIMESTAMP = "timestamp";

    private static final String DATABASE_TABLE = "questionnaires";
    //private static final String DATABASE_NAME = "questionnaire.db";
    //private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends MySQLiteHelper {
        public static final String CREATE_TABLE = "create table "+DATABASE_TABLE+" (_id integer primary key autoincrement, "
                +QUESTIONNAIRE_ID+" TEXT,"
                +PROJECT_ID+" TEXT,"
                +TYPE+" TEXT,"
                +LOGO+" TEXT,"
                +TIMESTAMP+" TEXT"+ ");";

        DatabaseHelper(Context context) {
            super(context);
        }
        /*
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("Core", CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        */
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx
     *            the Context within which to work
     */
    public QuestionnaireDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the questionnaires database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws android.database.SQLException
     *             if the database could be neither opened or created
     */
    public QuestionnaireDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
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
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the questionnaire with the given rowId
     *
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteQuestionnaire(long rowId) {
        return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
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
        return this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
                TYPE, LOGO, TIMESTAMP }, PROJECT_ID +" = '"+projectid+"' " ,null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the questionnaire that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching questionnaire, if found
     * @throws SQLException if questionnaire could not be found/retrieved
     */
    public Cursor getQuestionnaire(long rowId) throws SQLException {

        Cursor mCursor =

                this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
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

        Cursor mCursor =

                this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,QUESTIONNAIRE_ID,PROJECT_ID,
                        TYPE, LOGO, TIMESTAMP}, QUESTIONNAIRE_ID + "= '" + questionnaireid +"' ", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
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

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0;
    }
}
