package com.cloud9worldwide.questionnaire.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cloud9 on 3/31/14.
 */
public class ProjectsDBAdapter {
    public static final String ROW_ID = "_id";
    public static final String PROJECT_ID = "projectid";
    public static final String NAME = "name";
    public static final String LOGO = "logo";
    public static final String BACKGROUND = "background";

    private static final String DATABASE_TABLE = "projects";
    //private static final String DATABASE_NAME = "projects.db";
    //private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends MySQLiteHelper {
        public static final String CREATE_TABLE =
                "create table "+DATABASE_TABLE+" (_id integer primary key autoincrement, "
                        + PROJECT_ID+ " TEXT,"
                        + NAME+ " TEXT,"
                        + LOGO+ " TEXT,"
                        + BACKGROUND+ " TEXT" + ");";

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
    public ProjectsDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the projects database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public ProjectsDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }
    public void deleteAll(){
        this.mDb.delete(DATABASE_TABLE, ROW_ID + " > 1", null);
    }
    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
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
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the project with the given rowId
     *
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteProject(long rowId) {

        return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all projects in the database
     *
     * @return Cursor over all projects
     */
    public Cursor getAllProjects() {

        return this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID,PROJECT_ID,
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

                this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,PROJECT_ID, NAME,
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

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0;
    }

}