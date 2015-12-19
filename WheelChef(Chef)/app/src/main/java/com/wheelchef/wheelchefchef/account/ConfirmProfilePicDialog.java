package com.wheelchef.wheelchefchef.account;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 12/18/2015.
 */
public class ConfirmProfilePicDialog extends Dialog {

    private static final String TAG = "ConfirmProfilePicDialog";
    private EditAccountActivity editAccountActivity;

    private ImageView ivProfilePic;

    private Button btnConfirm, btnCancel;

    public ConfirmProfilePicDialog (EditAccountActivity editAccountActivity){
        super(editAccountActivity);
        this.editAccountActivity = editAccountActivity;

        this.setOwnerActivity(editAccountActivity);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_confirm_pic);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        ivProfilePic = (ImageView) findViewById(R.id.imageview_dialog_pic);

        setUpButtons();
    }

    private void setUpButtons(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmProfilePicDialog.this.dismiss();
            }
        });
    }


    public void grabImage(int mode)
    {
        if(mode == EditAccountActivity.ACTION_TAKE_PHOTO){
            editAccountActivity.getContentResolver().notifyChange(editAccountActivity.getProfilePicUri(), null);
            ContentResolver cr = editAccountActivity.getContentResolver();
            Bitmap bitmap;
            try
            {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, editAccountActivity.getProfilePicUri());
                ivProfilePic.setImageBitmap(bitmap);
                findViewById(R.id.textview_add_dish_photo).setVisibility(View.INVISIBLE);
            }
            catch (Exception e)
            {
                Toast.makeText(editAccountActivity, "Failed to load", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to load", e);
            }
        }
        else{
            ivProfilePic.setImageURI(editAccountActivity.getProfilePicUri());
        }
    }
}
