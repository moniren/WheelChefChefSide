package com.wheelchef.wheelchefchef.base;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by lyk on 12/19/2015.
 */
public abstract class CustomAsyncTaskToolbarActivity extends CustomToolbarActivity {

    protected String callMethod = "POST";

    public abstract void preAsyncTask(int action);

    public abstract List<NameValuePair> setUpParams(int action);

    public abstract void doInAsyncTask(int action,int success, String msg);

    public abstract void postAsyncTask(int action,int success, String msg);

}
