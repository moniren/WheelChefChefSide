package com.wheelchef.wheelchefchef.base;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 12/19/2015.
 */
public abstract class CustomToolbarActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected void setUpToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
