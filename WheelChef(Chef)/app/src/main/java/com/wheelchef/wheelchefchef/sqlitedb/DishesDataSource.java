package com.wheelchef.wheelchefchef.sqlitedb;

/**
 * Created by lyk on 12/11/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DishesDataSource {
    private static final String LOGTAG = "MAIN_DATA_SOURCE";
    // need to use "" to surround the column name otherwise it will be treated
    // as a string, use DESC so the newest will be on top
//    private static final String ORDER_BY_TIME = "datetime(\""
//            + DishesTable.COLUMN_TIME + "\") DESC";
        private static final String ORDER_BY_DISH_NAME = DishesTable.COLUMN_DISH_NAME+ " COLLATE NOCASE ASC";
    // the SQL "where" condition to filter the not acknowledged alerts, use ''
    // to surround the string
//    private static final String null = "\""
//            + MainTable.COLUMN_ACKNOWLEDGED + "\" = 'false'";
    public static SQLiteDatabase database;

    private DishesDataSource() {
    }

    private static final String[] allColumns = {DishesTable.COLUMN_ID,DishesTable.COLUMN_DiSH_ID, DishesTable.COLUMN_DISH_NAME,
            DishesTable.COLUMN_CHEF_NAME, DishesTable.COLUMN_DESCRIPTION, DishesTable.COLUMN_PRICE, DishesTable.COLUMN_TIMES_ORDERED,
            DishesTable.COLUMN_DISCOUNT, DishesTable.COLUMN_CATEGORY, DishesTable.COLUMN_PHOTO};

    public static synchronized void createDishesTable() {
        DishesTable.create(DishesDataSource.database);
    }

    public static synchronized  Cursor getWholeCursor(){
        return DataAccessWrapper.queryDB(database,
                DishesTable.TABLE_DISHES, allColumns, null, null, null,
                null, ORDER_BY_DISH_NAME);
    }

    public static synchronized int getTableSize() {
        int size = 0;
        Cursor cursor = DataAccessWrapper.queryDB(database,
                DishesTable.TABLE_DISHES, allColumns, null, null, null, null,
                ORDER_BY_DISH_NAME);
        if (cursor != null) {
            size = cursor.getCount();
        }

        return size;
    }

//    public static synchronized  String getCircleBg(Context ctx, long rowId){
//        String color = null;
//        Cursor cursor = DataAccessWrapper.queryDB(database,
//                DishesTable.TABLE_DISHES, allColumns, null, null, null, null,
//                ORDER_BY_TIME);
//        if (cursor != null) {
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                color = cursor.getString(cursor.getColumnIndex(DishesTable.COLUMN_BACKGROUND));
//            }
//        }=                                                              s
//        return color;
//    }

    public static synchronized void updateDish(Context ctx, long rowId,
                                               ContentValues values) {
        DataAccessWrapper.update(database, ctx, DishesTable.TABLE_DISHES, rowId,
                values);
    }

    public static synchronized long instertDish(Context ctx,
                                               ContentValues values) {
        return DataAccessWrapper.insert(database, ctx, DishesTable.TABLE_DISHES,
                DishesTable.COLUMN_ID, values);
    }

    public static synchronized void removeDish(Context ctx, long rowId) {
        if (ctx != null && rowId != -1) {
            DataAccessWrapper.remove(DishesDataSource.database, ctx,
                    DishesTable.TABLE_DISHES, rowId);
        } else {
            throw new IllegalArgumentException(
                    "Context is null or rowId is incorrect");
        }
    }

    public static void setDatabase(SQLiteDatabase database){
        DishesDataSource.database = database;
    }

}