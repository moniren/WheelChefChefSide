package com.wheelchef.wheelchefchef.account;

import android.content.Context;

import com.wheelchef.wheelchefchef.utils.PrefUtil;


/**
 * Created by lyk on 11/7/2015.
 */
public abstract class SessionManager {
    public static final String LOGGED_IN = "logged_in";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static void setLoggedIn(boolean info, Context context){
        PrefUtil.setBooleanPreference(LOGGED_IN, info, context);
    }

    public static void login(Context context,String username, String password){
        PrefUtil.setBooleanPreference(LOGGED_IN,true,context);
        PrefUtil.setStringPreference(USERNAME, username, context);
        PrefUtil.setStringPreference(PASSWORD,password,context);
    }
    public static void logout(Context context){
        PrefUtil.setBooleanPreference(LOGGED_IN,false,context);
        PrefUtil.clearPreference(context);
    }

}
