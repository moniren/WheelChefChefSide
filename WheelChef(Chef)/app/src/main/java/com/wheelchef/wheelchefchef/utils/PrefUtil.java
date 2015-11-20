package com.wheelchef.wheelchefchef.utils;

/**
 * Created by lyk on 11/7/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public abstract class PrefUtil {
    //keys

    public static void clearPreference(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
    }

    public static void setBooleanPreference(String key,boolean info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, info);
        editor.commit();
    }

    public static boolean getBooleanPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key,false);
    }

    public static void setStringPreference(String key,String info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, info);
        editor.commit();
    }

    public static String getStringPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, null);
    }


    public static void setIntPreference(String key,int info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, info);
        editor.commit();
    }

    public static int getIntPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, -1);
    }

    public static void updateLocale(String abbrev,Activity activity){
        Locale locale = new Locale(abbrev);
        if(abbrev.equals("zh_CN")){
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        else if (abbrev.equals("zh_TW")){
            locale = Locale.TRADITIONAL_CHINESE;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getApplicationContext().getResources().updateConfiguration(config, null);
    }
}