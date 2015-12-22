package com.wheelchef.wheelchefchef.base;

import android.content.ContentValues;

import org.json.JSONObject;

/**
 * Created by lyk on 12/19/2015.
 */
public interface PhpAsyncTaskComponent {
    //action -1 means nothing will be filtered
    //action is used to filter different tasks, an activity can have different tasks


    void preAsyncTask(int action);

    ContentValues setUpParams(int action);

    void doInAsyncTask(int action,int success,JSONObject json);

    void postAsyncTask(int action,int success, String msg);

}
