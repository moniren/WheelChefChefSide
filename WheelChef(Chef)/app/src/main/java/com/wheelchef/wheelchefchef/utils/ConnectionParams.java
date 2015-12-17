package com.wheelchef.wheelchefchef.utils;

/**
 * Created by lyk on 11/8/2015.
 */
public abstract class ConnectionParams {
    //school: 10.27.53.225  http://10.27.53.225/wheelchef/
    //room: 192.168.1.22  http://www.wheelchef.com/
    public static final String URL_WEBSITE = "http://10.27.53.225/wheelchef/";
    public static final String CHEF_FOLDER = "chef/";
    public static final String URL_CHEF_LOGIN = URL_WEBSITE+"db_chef_login.php";
    public static final String URL_CHEF_REGISTER = URL_WEBSITE+"db_chef_register.php";
    public static final String URL_CHEF_CREATE_DISH = URL_WEBSITE+"db_chef_create_dish.php";
    public static final String URL_CHEF_UPLOAD_DISH_IMAGE = URL_WEBSITE+"db_chef_upload_dish_image.php";
    public static final String URL_CHEF_LOAD_DISH = URL_WEBSITE+"db_chef_load_dish.php";
    public static final String URL_CHEF_LOAD_DISH_IMAGE = URL_WEBSITE+"db_chef_load_dish_image.php";
    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MSG = "message";
    public static final String TAG_DISHES = "dishes";
    public static final String TAG_PHOTO = "photo";
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
