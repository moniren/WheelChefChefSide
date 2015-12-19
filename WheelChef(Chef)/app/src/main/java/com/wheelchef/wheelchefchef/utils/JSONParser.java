package com.wheelchef.wheelchefchef.utils;

/**
 * Created by lyk on 10/30/2015.
 */

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private static final String TAG = "JSONParser";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method,
                                      ContentValues values) {
        try {
            URL callURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) callURL.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            if(method.equals("POST"))
                conn.setRequestMethod("POST");
            else
                conn.setRequestMethod("GET");

            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(values));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                //use the input stream to build the json object
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    is.close();
                    json = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            }
            else {
                //connection not ok
                //Todo: need some handler here to show the user
                Log.d(TAG,"http connection not ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return JSON String
        return jObj;

    }

    private String getPostDataString(ContentValues values) {
        StringBuilder result = new StringBuilder();

        Set<Map.Entry<String, Object>> set =values.valueSet();
        Iterator itr = set.iterator();

        boolean first = true;

        while(itr.hasNext()){
            if (first)
                first = false;
            else
                result.append("&");

            Map.Entry current = (Map.Entry)itr.next();

            try {
                result.append(URLEncoder.encode(current.getKey().toString(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(current.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}