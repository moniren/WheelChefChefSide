package com.wheelchef.wheelchefchef.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;

/**
 * Created by lyk on 12/16/2015.
 */
public class EditAccountActivity extends AppCompatActivity {

    private static final String TAG = "EditAccountActivity";

    protected static final int ACTION_TAKE_PHOTO = 111;
    protected static final int ACTION_CHOOSE_PHOTO = 222;

    private Uri profilePicUri;

    private AddProfilePicDialog addProfilePicDialog;
    private ConfirmProfilePicDialog confirmProfilePicDialog;

    private ImageButton btnEditProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        btnEditProfilePic = (ImageButton) findViewById(R.id.btn_add_profile_photo);

        setUpToolbar();
        setUpButtons();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getResources().getString(R.string.title_activity_edit_account);
        toolbar.setTitle(title);
    }

    private void setUpButtons(){
        btnEditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfilePicDialog = new AddProfilePicDialog(EditAccountActivity.this);
                addProfilePicDialog.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case ACTION_TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    confirmProfilePicDialog = new ConfirmProfilePicDialog(this);
                    confirmProfilePicDialog.grabImage(ACTION_TAKE_PHOTO);
                    confirmProfilePicDialog.show();
                    Log.d(TAG, "Successfully got the photo!");
                }
                break;
            case ACTION_CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    profilePicUri = imageReturnedIntent.getData();
                    confirmProfilePicDialog = new ConfirmProfilePicDialog(this);
                    confirmProfilePicDialog.grabImage(ACTION_CHOOSE_PHOTO);
                    confirmProfilePicDialog.show();
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
            default:
                break;
        }
    }

    public Uri getProfilePicUri() {
        return profilePicUri;
    }

    public void setProfilePicUri(Uri profilePicUri) {
        this.profilePicUri = profilePicUri;
    }
}
