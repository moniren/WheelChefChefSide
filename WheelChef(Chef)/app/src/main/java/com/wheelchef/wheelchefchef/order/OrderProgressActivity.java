package com.wheelchef.wheelchefchef.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.main.MainActivity;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class OrderProgressActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progress);

      /*  tvOrderID = (TextView) findViewById(R.id.view_order_detail_order_id);
        tvOrderTime = (TextView) findViewById(R.id.view_order_detail_order_time);
        tvOrderPrice = (TextView) findViewById(R.id.view_order_detail_total_price);
        tvCutomerID = (TextView) findViewById(R.id.view_order_detail_customer_name);

        bAccept = (Button) findViewById(R.id.btn_accept_order);
        bReject = (Button) findViewById(R.id.btn_reject_order);

        setUpButtons();

        setUpToolbar();

        setUpData();*/
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
