package com.wheelchef.wheelchefchef.order;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;
import com.wheelchef.wheelchefchef.dish.DishListAdapter;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.main.OrderFragment;
import com.wheelchef.wheelchefchef.registerlogin.LoginActivity;
import com.wheelchef.wheelchefchef.utils.JSONParser;

import java.util.ArrayList;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class ViewOrderDetailActivity extends AppCompatActivity {
    public static final String ORDER_MODEL_OBJ = "orderModelObj";
    private Button bAccept, bReject, bProgress;
    private TextView tvOrderID, tvOrderTime, tvOrderPrice, tvCutomerID;
    private OrderModel orderModel = null;

    private ListView listView;
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_detail);
        listView = (ListView) findViewById(R.id.dish_list);

        tvOrderID = (TextView) findViewById(R.id.view_order_detail_order_id);
        tvOrderTime = (TextView) findViewById(R.id.view_order_detail_order_time);
        tvOrderPrice = (TextView) findViewById(R.id.view_order_detail_total_price);
        tvCutomerID = (TextView) findViewById(R.id.view_order_detail_customer_name);

        bAccept = (Button) findViewById(R.id.btn_accept_order);
        bReject = (Button) findViewById(R.id.btn_reject_order);
        bProgress = (Button)findViewById(R.id.btn_order_progress);
        setUpData();
        setUpButtons();
        setUpToolbar();

        ArrayList<String> dataList = orderModel.getDishId();
        DishListAdapter adapter = new DishListAdapter(this,R.layout.row_dish_list,dataList,orderModel);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setUpButtons() {
        if(orderModel.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_ACCEPTED)) {
            bAccept.setVisibility(Button.INVISIBLE);
            bReject.setVisibility(Button.INVISIBLE);
            bProgress.setVisibility(Button.VISIBLE);
        }else if(orderModel.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_REJECTED)){
            Button buttonAccept = (Button)findViewById(R.id.btn_accept_order);
            buttonAccept.setVisibility(Button.INVISIBLE);
            Button buttonReject = (Button)findViewById(R.id.btn_reject_order);
            buttonReject.setVisibility(Button.INVISIBLE);
        }
        bAccept.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                OrderFragment.changeStatus(orderModel.getCutomerName(), OrderModel.ACCEPTANCE_ACCEPTED);
                Intent intent = new Intent(ViewOrderDetailActivity.this, OrderProgressActivity.class);
                startActivity(intent);
            }
        });

        bReject.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                OrderFragment.changeStatus(orderModel.getCutomerName(), OrderModel.ACCEPTANCE_REJECTED);
                ViewOrderDetailActivity.this.startActivity(new Intent(ViewOrderDetailActivity.this, MainActivity.class));
                ViewOrderDetailActivity.this.finish();
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view_order_detail);
        setSupportActionBar(toolbar);
        String title = getResources().getString(R.string.title_activity_view_order_detail);
        toolbar.setTitle(title);
    }

    private void setUpData(){
        Intent intent = getIntent();

        orderModel = (OrderModel) intent.getSerializableExtra(ORDER_MODEL_OBJ);

        if(orderModel!=null){
            tvOrderID.setText(orderModel.getOrderId());
            tvCutomerID.setText(orderModel.getCutomerName());
            tvOrderPrice.setText(String.valueOf(orderModel.getFoodPrice()));
            tvOrderTime.setText(orderModel.getOrderTime());
        }

    }
}
