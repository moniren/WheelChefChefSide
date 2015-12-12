package com.wheelchef.wheelchefchef.utils;

/**
 * Created by lyk on 11/8/2015.
 */
public abstract class ConnectionParams {
    //school: 10.27.53.225
    //room: 192.168.0.17
    public static final String URL_WEBSITE = "http://10.27.53.225/wheelchef/";
    public static final String CHEF_FOLDER = "chef/";
    public static final String URL_CHEF_LOGIN = "http://10.27.53.225/wheelchef/db_chef_login.php";
    public static final String URL_CHEF_REGISTER = "http://10.27.53.225/wheelchef/db_chef_register.php";
    public static final String URL_CHEF_CREATE_DISH = "http://10.27.53.225/wheelchef/db_chef_create_dish.php";
    public static final String URL_CHEF_UPLOAD_DISH_IMAGE = "http://10.27.53.225/wheelchef/db_chef_upload_dish_image.php";
    public static final String URL_CHEF_LOAD_DISH = "http://10.27.53.225/wheelchef/db_chef_load_dish.php";
    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MSG = "message";
    public static final String TAG_DISHES = "dishes";
    //column names
    public static final String COL_DISH_ID = "dishId";
    public static final String COL_DISH_NAME = "dishName";
    public static final String COL_CHEF_NAME = "chefName";
    public static final String COL_DESC = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_TIMES_ORDERED = "timesOrdered";
    public static final String COL_DISCOUNT = "discount";
    public static final String COL_CATEGORY = "category";
    public static final String COL_FILE_PATH = "filePath";
}
