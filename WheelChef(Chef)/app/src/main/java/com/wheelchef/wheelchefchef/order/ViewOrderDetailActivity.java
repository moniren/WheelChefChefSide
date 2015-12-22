package com.wheelchef.wheelchefchef.order;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.base.CustomToolbarActivity;
import com.wheelchef.wheelchefchef.datamodels.OrderModel;
import com.wheelchef.wheelchefchef.dish.DishListAdapter;
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.main.OrderFragment;

import java.util.ArrayList;

/**
 * Created by MHUIQ on 12/12/2015.
 */
public class ViewOrderDetailActivity extends CustomToolbarActivity {
    public static final String ORDER_MODEL_OBJ = "orderModelObj";
    private Button bAccept, bReject, bProgress;
    private TextView tvOrderID, tvOrderTime, tvOrderPrice, tvCutomerID;
    private OrderModel orderModel = null;

    private ListView listView;


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
            bAccept.setVisibility(Button.GONE);
            bReject.setVisibility(Button.GONE);
            bProgress.setVisibility(Button.VISIBLE);
        }else if(orderModel.getAcceptanceStatus().equals(OrderModel.ACCEPTANCE_REJECTED)){
            bAccept.setVisibility(Button.GONE);
            bReject.setVisibility(Button.GONE);
        }
        bAccept.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                OrderFragment.changeStatus(orderModel.getCutomerName(), OrderModel.ACCEPTANCE_ACCEPTED);
                Intent intent = new Intent(ViewOrderDetailActivity.this, OrderProgressActivity.class);
                startActivity(intent);
                ViewOrderDetailActivity.this.finish();
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

        bProgress.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOrderDetailActivity.this, OrderProgressActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
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
