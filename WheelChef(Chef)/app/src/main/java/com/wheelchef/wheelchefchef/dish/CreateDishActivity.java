package com.wheelchef.wheelchefchef.dish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.utils.MD5Generator;

/**
 * Created by lyk on 12/4/2015.
 */
public class CreateDishActivity extends AppCompatActivity{

    private long currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_dish);
        setSupportActionBar(toolbar);
        String title = getResources().getString(R.string.title_activity_create_dish);
        toolbar.setTitle(title);
    }





    private String generateDishId(String dishName){
        currentTime = System.currentTimeMillis();
        return MD5Generator.getMD5hash(dishName+currentTime);
    }
}
