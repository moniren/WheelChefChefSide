package com.wheelchef.wheelchefchef.base;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lyk on 12/19/2015.
 */
public class PhpRequestAsyncTask extends AsyncTask<Void,Void,Void> {

    private static final String TAG = "PhpRequestAsyncTask";

    private int success = 0;
    private String msg = null;

    private static JSONParser jsonParser = null;

    private CustomAsyncTaskToolbarActivity customAsyncTaskToolbarActivity;

    private String requestedService;

    private String callMethod;

    private int action;

    public PhpRequestAsyncTask(CustomAsyncTaskToolbarActivity customAsyncTaskToolbarActivity,
                               String requestedService, String callMethod,int action){

        this.customAsyncTaskToolbarActivity = customAsyncTaskToolbarActivity;
        this.requestedService = requestedService;
        this.callMethod = callMethod;
        this.action = action;

        if (jsonParser == null)
            jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        customAsyncTaskToolbarActivity.preAsyncTask(action);
    }

    @Override
    protected Void doInBackground(Void... args) {
        ContentValues values = customAsyncTaskToolbarActivity.setUpParams(action);
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(requestedService, callMethod, values);

        // Check your log cat for JSON response
        Log.d(TAG, json.toString());

        try {
            // Checking for SUCCESS TAG
            success = json.getInt(ConnectionParams.TAG_SUCCESS);
            msg = json.getString(ConnectionParams.TAG_MSG);
            customAsyncTaskToolbarActivity.doInAsyncTask(action,success,msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        customAsyncTaskToolbarActivity.postAsyncTask(action,success,msg);
    }
}
