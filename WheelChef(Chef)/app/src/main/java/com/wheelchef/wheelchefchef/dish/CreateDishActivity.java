package com.wheelchef.wheelchefchef.dish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.utils.MD5Generator;

/**
 * Created by lyk on 12/4/2015.
 */
public class CreateDishActivity extends AppCompatActivity{

    protected static final int ACTION_TAKE_PHOTO = 111;
    protected static final int ACTION_CHOOSE_PHOTO = 222;

    private static final String TAG = "CreateDishActivity";
    private ImageButton btnAddPhoto;
    private Spinner spCategory;
    private String[] items;
    private long currentTime;
    private ImageView dishPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);

        spCategory = (Spinner) findViewById(R.id.spinner_dish_category);
        btnAddPhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo);
        dishPhoto = (ImageView) findViewById(R.id.imageview_dish_photo);

        setUpButtons();
        setUpToolbar();
        setUpSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case ACTION_TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    dishPhoto.setImageBitmap(photo);
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
            case ACTION_CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri  photo = imageReturnedIntent.getData();
                    dishPhoto.setImageURI(photo);
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
        }
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

    private  void setUpButtons(){
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog addDishPhotoDialog = new AddDishPhotoDialog(CreateDishActivity.this);
                addDishPhotoDialog.show();
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
                Log.d(TAG, "selected item at: " + position);
                spCategory.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
