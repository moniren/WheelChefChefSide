package com.wheelchef.wheelchefchef.registerlogin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private Button bRegister;
    private EditText etUsername, etPassword, etConfirmPassword, etAddress, etZipcode;
    private Spinner spCategory;
    private CheckBox cbInHome;

    private String username, password, confirmPassword, addressString, category;
    private int zipcode;
    private float latitude, longitude;
    private String[] items;


    // url to get all products list
    private static final String TAG = "RegisterActivity";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegister = (Button) findViewById(R.id.bRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etRePassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etZipcode = (EditText) findViewById(R.id.etZipCode);
        spCategory = (Spinner) findViewById(R.id.spinner_category);
        cbInHome = (CheckBox) findViewById(R.id.checkbox_in_home);

        setUpButtons();
        setUpSpinner();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setUpButtons() {
        bRegister.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                addressString = etAddress.getText().toString();
                zipcode = Integer.parseInt(etZipcode.getText().toString());
                category = items[spCategory.getSelectedItemPosition()];

                if (cbInHome.isChecked()) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    int apiLevel = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if(apiLevel>=23){
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = (float)location.getLatitude();
                    longitude = (float)location.getLongitude();
                }
                //if the chef is not at his/her home
                else {
                    Geocoder coder = new Geocoder(RegisterActivity.this);
                    List<Address> address;

                    try {
                        address = coder.getFromLocationName(addressString, 5);
                        if (address == null) {
                            return;
                        }
                        else{
                            Address location = address.get(0);
                            latitude = (float)location.getLatitude();
                            longitude = (float)location.getLongitude();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                }

                if(username.length() < 6 || username.length() > 20){

                }
                else if(password.length() < 6 || password.length()>20){

                }
                else if(!password.equals(confirmPassword)){

                }
                else{
                    new RegisterTask().execute();
                }
            }
        });
    }

    private void setUpSpinner(){
        items = new String[3];
        items[0] = "Chinese";
        items[1] = "Indian";
        items[2] = "Indonesian";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spCategory.setAdapter(adapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"selected item at: "+position);
                spCategory.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class RegisterTask extends AsyncTask<String, String, String> {
        int success = 0;
        String msg = "";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("address_string", addressString));
            params.add(new BasicNameValuePair("zipcode", String.valueOf(zipcode)));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            params.add(new BasicNameValuePair("category", category));

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_REGISTER, "POST", params);
            Log.d(TAG, "with the username: " + username);
            Log.d(TAG,"with the password: "+password);
            Log.d(TAG,"json received is: "+json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(ConnectionParams.TAG_SUCCESS);
                msg = json.getString(ConnectionParams.TAG_MSG);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success == 1) {
                SessionManager.login(RegisterActivity.this,username,password);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                Log.d(TAG, "login succeed!");
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG,"login failed!");
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }

    }

}
