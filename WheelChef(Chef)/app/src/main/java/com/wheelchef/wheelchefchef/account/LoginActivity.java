package com.wheelchef.wheelchefchef.account;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rey.material.widget.EditText;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.base.CustomAsyncTaskToolbarActivity;
import com.wheelchef.wheelchefchef.base.PhpRequestAsyncTask;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends CustomAsyncTaskToolbarActivity{

    private Button bLogin, bRegisterLink;
    private EditText etUsername, etPassword;
    private String username, password;

    // url to get all products list
    private static final String TAG = "LoginActivity";

    public static final String NEED_VERIFY = "need_verify";

    // Progress Dialog
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loggedIn = PrefUtil.getBooleanPreference(SessionManager.LOGGED_IN, this);
        if(loggedIn){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(NEED_VERIFY,true);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegisterLink = (Button) findViewById(R.id.bRegisterLink);

        setUpButtons();
    }

    private void setUpButtons(){
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                // action -1 means nothing to filter
                new PhpRequestAsyncTask(LoginActivity.this,ConnectionParams.URL_CHEF_LOGIN,callMethod,-1).execute();
            }
        });

        bRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void preAsyncTask(int action) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging in...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public ContentValues setUpParams(int action) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return values;
    }

    @Override
    public void doInAsyncTask(int action, int success, String msg) {
        //empty in this case
    }

    @Override
    public void postAsyncTask(int action, int success, String msg) {
        pDialog.dismiss();
        if (success == 1) {
            SessionManager.login(LoginActivity.this,username,password);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Log.d(TAG, "login succeed!");
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG,"login failed!");
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        String title = getResources().getString(R.string.title_activity_login);
        toolbar.setTitle(title);
    }


}
