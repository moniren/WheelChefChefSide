package com.wheelchef.wheelchefchef.account;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.wheelchef.wheelchefchef.base.PhpAsyncTaskComponent;
import com.wheelchef.wheelchefchef.base.CustomToolbarActivity;
import com.wheelchef.wheelchefchef.base.PhpRequestAsyncTask;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends CustomToolbarActivity implements PhpAsyncTaskComponent {

    private Button bRegister;
    private EditText etUsername, etPassword, etConfirmPassword, etPhoneNumber, etAddressRoad, etAddressBlk, etAddressUnit, etZipcode;
    private Spinner spCategory;
    private CheckBox cbInHome;

    private String username, password, confirmPassword, phoneNumber, addressString, category;
    private int zipcode;
    private float latitude, longitude;
    private String[] items;


    private static final String TAG = "RegisterActivity";
    // Progress Dialog
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);


        bRegister = (Button) findViewById(R.id.bRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etRePassword);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etAddressRoad = (EditText) findViewById(R.id.etAddressRoad);
        etAddressBlk = (EditText) findViewById(R.id.etAddressBlk);
        etAddressUnit = (EditText) findViewById(R.id.etAddressUnit);
        etZipcode = (EditText) findViewById(R.id.etZipCode);
        spCategory = (Spinner) findViewById(R.id.spinner_category);
        cbInHome = (CheckBox) findViewById(R.id.checkbox_in_home);

        setUpToolbar();
        setUpButtons();
        setUpSpinner();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        String title = getResources().getString(R.string.title_activity_register);
        toolbar.setTitle(title);
    }

    private void setUpButtons() {
        etZipcode.setOnFocusChangeListener(new EditTextFocusListener(etZipcode));
        etUsername.setOnFocusChangeListener(new EditTextFocusListener(etUsername));
        etPassword.setOnFocusChangeListener(new EditTextFocusListener(etPassword));
        etConfirmPassword.setOnFocusChangeListener(new EditTextFocusListener(etConfirmPassword));

        bRegister.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                boolean valid = true;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();
                addressString = etAddressRoad.getText().toString() + " blk " + etAddressBlk.getText().toString() + " " + etAddressUnit.getText().toString();
                if(etZipcode.getText().toString().length()!=0)
                    zipcode = Integer.parseInt(etZipcode.getText().toString());
                else{
                    etZipcode.setError("your zip code cannot be empty!");
                    valid = false;
                }


                category = items[spCategory.getSelectedItemPosition()];

                if (cbInHome.isChecked()) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    int apiLevel = Build.VERSION.SDK_INT;
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

                if(!isValidEmail(username)){
                    etUsername.setError("invalid email address.");
                }
                else if(password.length() < 6 || password.length()>20){
                    etPassword.setError("invalid password.");
                }
                else if(!password.equals(confirmPassword)){
                    etConfirmPassword.setError("passwords do not match. Please enter again.");
                }
                else{
                    if(valid)
                        new PhpRequestAsyncTask(RegisterActivity.this,ConnectionParams.URL_CHEF_REGISTER
                        ,"POST",-1).execute();
                }
            }
        });
    }

    private void setUpSpinner(){
        items = new String[7];
        items[0] = "Indian";
        items[1] = "Chinese";
        items[2] = "Malaysian";
        items[3] = "Indonesian";
        items[4] = "Vietnamese";
        items[5] = "Western";
        items[6] = "Other";

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

    @Override
    public void preAsyncTask(int action) {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Creating account...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public ContentValues setUpParams(int action) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("phone", phoneNumber);
        values.put("address_string", addressString);
        values.put("zipcode", String.valueOf(zipcode));
        values.put("latitude", String.valueOf(latitude));
        values.put("longitude", String.valueOf(longitude));
        values.put("category", category);
        return values;
    }

    @Override
    public void doInAsyncTask(int action, int success,JSONObject json) {
        // do nothing in this case
    }

    @Override
    public void postAsyncTask(int action, int success, String msg) {
        pDialog.dismiss();
        if (success == 1) {
            SessionManager.login(RegisterActivity.this,username,password);
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            Log.d(TAG, "login succeed!");
            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            //finish this activity once registered successfully
            RegisterActivity.this.finish();
        } else {
            Log.d(TAG,"login failed!");
            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private class EditTextFocusListener implements View.OnFocusChangeListener{
        private EditText wrapper;

        EditTextFocusListener(EditText wrapper){
            this.wrapper = wrapper;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                wrapper.clearError();
            }
        }
    }

}
