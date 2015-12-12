package com.wheelchef.wheelchefchef.order;

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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.registerlogin.LoginActivity;
import com.wheelchef.wheelchefchef.utils.JSONParser;

import java.io.IOException;
import java.util.List;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class ViewOrderDetailActivity {
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
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etAddressRoad = (EditText) findViewById(R.id.etAddressRoad);
        etAddressBlk = (EditText) findViewById(R.id.etAddressBlk);
        etAddressUnit = (EditText) findViewById(R.id.etAddressUnit);
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
                if (etZipcode.getText().toString().length() != 0)
                    zipcode = Integer.parseInt(etZipcode.getText().toString());
                else {
                    etZipcode.setError("your zip code cannot be empty!");
                    valid = false;
                }


                category = items[spCategory.getSelectedItemPosition()];

                if (cbInHome.isChecked()) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    int apiLevel = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if (apiLevel >= 23) {
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
                    latitude = (float) location.getLatitude();
                    longitude = (float) location.getLongitude();
                }
                //if the chef is not at his/her home
                else {
                    Geocoder coder = new Geocoder(RegisterActivity.this);
                    List<Address> address;

                    try {
                        address = coder.getFromLocationName(addressString, 5);
                        if (address == null) {
                            return;
                        } else {
                            Address location = address.get(0);
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                }

                if (!isValidEmail(username)) {
                    //til.setErrorEnabled(true);
                    etUsername.setError("invalid email address.");

                    // etUsername.getBackground().setColorFilter(getResources().getColor(R.color.color_primary), PorterDuff.Mode.SRC_ATOP);
                    //Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    //til.startAnimation(shake);
                    //til.setError("Please enter an email address");
                } else if (password.length() < 6 || password.length() > 20) {
                    etPassword.setError("invalid password.");
                } else if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("passwords do not match. Please enter again.");
                } else {
                    if (valid)
                        new RegisterTask().execute();
                }
            }
        });
}
