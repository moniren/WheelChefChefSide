package com.wheelchef.wheelchefchef.sqlitedb;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lyk on 12/11/2015.
 */
public class DishesTable {
    // Database table, the columns are created in the same order
    public static final String TABLE_DISHES = "Dishes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DISH_ID = "DishId";
    public static final String COLUMN_DISH_NAME = "DishName";
    public static final String COLUMN_CHEF_NAME = "ChefName";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_TIMES_ORDERED = "TimesOrdered";
    public static final String COLUMN_DISCOUNT = "Discount";
    public static final String COLUMN_CATEGORY = "Category";
    public static final String COLUMN_FILE_PATH = "FilePath";
    public static final String COLUMN_PHOTO = "Photo";

    // Database creation SQL statement
    private static final String TABLE_COLUMNS = " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_DISH_ID + " TEXT, "
            + COLUMN_DISH_NAME + " TEXT, " + COLUMN_CHEF_NAME
            + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, "+ COLUMN_PRICE + " TEXT, "
            + COLUMN_TIMES_ORDERED + " TEXT, "
            + COLUMN_DISCOUNT + " TEXT, "
            + COLUMN_CATEGORY + " TEXT, "+ COLUMN_FILE_PATH + " TEXT, "+COLUMN_PHOTO+ " TEXT " + " )";

    public static void create(SQLiteDatabase database) {
        DataAccessWrapper.create(database, TABLE_DISHES, TABLE_COLUMNS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        DataAccessWrapper.upgrade(database, TABLE_DISHES, TABLE_COLUMNS, oldVersion,
                newVersion);
    }

    public static void removeTable(SQLiteDatabase db) {
        DataAccessWrapper.removeTable(db, TABLE_DISHES);
    }

    public static void clearTable(SQLiteDatabase db) {
        DataAccessWrapper.clearTable(db, TABLE_DISHES);
    }
}
