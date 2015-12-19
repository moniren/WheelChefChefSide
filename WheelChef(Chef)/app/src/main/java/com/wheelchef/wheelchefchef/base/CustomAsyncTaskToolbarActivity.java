package com.wheelchef.wheelchefchef.base;

import android.content.ContentValues;

/**
 * Created by lyk on 12/19/2015.
 */
public abstract class CustomAsyncTaskToolbarActivity extends CustomToolbarActivity {
    //action -1 means nothing will be filtered
    //action is used to filter different tasks, an activity can have different tasks

    protected String callMethod = "POST";

    public abstract void preAsyncTask(int action);

    public abstract ContentValues setUpParams(int action);

    public abstract void doInAsyncTask(int action,int success, String msg);

    public abstract void postAsyncTask(int action,int success, String msg);

}
