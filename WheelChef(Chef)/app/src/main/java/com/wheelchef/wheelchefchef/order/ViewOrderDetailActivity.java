package com.wheelchef.wheelchefchef.order;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.registerlogin.LoginActivity;
import com.wheelchef.wheelchefchef.utils.JSONParser;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class ViewOrderDetailActivity extends AppCompatActivity {
    private Button bAccept, bReject;
    private TextView tvOrderID, tvOrderTime, tvOrderPrice, tvCutomerID;

    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bAccept = (Button) findViewById(R.id.btn_accept_order);
        bReject = (Button) findViewById(R.id.btn_reject_order);

        setUpButtons();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setUpButtons() {

        bAccept.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOrderDetailActivity.this,OrderProgressActivity.class);
                startActivity(intent);
            }
        });

        bReject.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            }
        });
    }
}
