package com.wheelchef.wheelchefchef.sqlitedb;

/**
 * Created by lyk on 12/11/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOGTAG = "WHEELCHEFCHEF_DB";

    private static final String DATABASE_NAME = "chef.wheelchef.db";

    private static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase db;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // this will wipe the database every time the app restarts
        // echo-mobile is using it, but currently it's disabled here for
        // debugging use
        // wipeDB(context);
        DatabaseHelper.db = getWritableDatabase();

    }

    public void wipeDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    // Method is called during creation of the database

    @Override
    public void onCreate(SQLiteDatabase database) {
    }

    public SQLiteDatabase getDB() {
        return DatabaseHelper.db;
    }

    // Method is called during an upgrade of the database,

    // e.g. if you increase the database version

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {

    }

}
