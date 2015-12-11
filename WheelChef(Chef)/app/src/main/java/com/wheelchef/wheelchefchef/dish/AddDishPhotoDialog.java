package com.wheelchef.wheelchefchef.dish;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;

import java.io.File;

/**
 * Created by lyk on 12/7/2015.
 */
public class AddDishPhotoDialog extends Dialog {
    private ImageButton btnTakePhoto;
    private ImageButton btnChoosePhoto;
    private CreateDishActivity createDishActivity;
    private static final String TAG = "AddDishPhotoDialog";

    public AddDishPhotoDialog(CreateDishActivity createDishActivity) {
        super(createDishActivity);
        this.setOwnerActivity(createDishActivity);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_select_dish_pic);
        this.createDishActivity = createDishActivity;
        btnTakePhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo_camera);
        btnChoosePhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo_library);
        setUpButtons();
    }


    private void setUpButtons(){
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog.this.dismiss();
                File photo = null;
                try
                {
                    // place where to store camera taken picture
                    photo = AddDishPhotoDialog.this.createTemporaryFile("picture", ".jpg");
                    photo.delete();
                    createDishActivity.setDishPhotoUri(Uri.fromFile(photo));
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, createDishActivity.getDishPhotoUri());
                    AddDishPhotoDialog.this.getOwnerActivity().startActivityForResult(takePicture, CreateDishActivity.ACTION_TAKE_PHOTO);
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Can't create file to take picture!");
                    Toast.makeText(AddDishPhotoDialog.this.getOwnerActivity(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog.this.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                AddDishPhotoDialog.this.getOwnerActivity().startActivityForResult(pickPhoto, CreateDishActivity.ACTION_CHOOSE_PHOTO);
            }
        });
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/DCIM");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }
}
