package com.wheelchef.wheelchefchef.dish;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 12/7/2015.
 */
public class AddDishPhotoDialog extends Dialog {
    private ImageButton btnTakePhoto;
    private ImageButton btnChoosePhoto;


    public AddDishPhotoDialog(Context context) {
        super(context);
        this.setOwnerActivity((Activity)context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_select_dish_pic);
        btnTakePhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo_camera);
        btnChoosePhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo_library);
        setUpButtons();
    }


    private void setUpButtons(){
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog.this.dismiss();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                AddDishPhotoDialog.this.getOwnerActivity().startActivityForResult(takePicture, CreateDishActivity.ACTION_TAKE_PHOTO);
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog.this.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                AddDishPhotoDialog.this.getOwnerActivity().startActivityForResult(pickPhoto, CreateDishActivity.ACTION_CHOOSE_PHOTO);
            }
        });
    }
}
