package com.wheelchef.wheelchefchef.dish;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.base.PhpAsyncTaskComponent;
import com.wheelchef.wheelchefchef.base.CustomToolbarActivity;
import com.wheelchef.wheelchefchef.base.PhpRequestAsyncTask;
import com.wheelchef.wheelchefchef.utils.BitmapUtil;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.HashGenerator;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by lyk on 12/4/2015.
 */
public class CreateDishActivity extends CustomToolbarActivity implements PhpAsyncTaskComponent {

    private static final int TASK_CREATE_DISH = 11;
    private static final int TASK_UPLOAD_IMAGE = 22;


    private Uri dishPhotoUri;

    protected static final int ACTION_TAKE_PHOTO = 111;
    protected static final int ACTION_CHOOSE_PHOTO = 222;

    private static final String TAG = "CreateDishActivity";
    // Progress Dialog
    private ProgressDialog pDialog;

    private ImageButton btnAddPhoto;
    private Button btnCreateDish;
    private Spinner spCategory;
    private ImageView dishPhoto;
    private EditText etDishName, etDishPrice, etDishDesc, etDishDiscount;
    private String dishId, dishName, dishDesc, dishCategory, username;
    private float dishPrice, dishDiscount;

    private Bitmap dishPhotoBitmap;


    private String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);

        spCategory = (Spinner) findViewById(R.id.spinner_dish_category);
        btnAddPhoto = (ImageButton) findViewById(R.id.btn_add_dish_photo);
        btnCreateDish = (Button) findViewById(R.id.btn_create_dish);
        dishPhoto = (ImageView) findViewById(R.id.imageview_dish_photo);

        etDishName = (EditText) findViewById(R.id.etDishname);
        etDishPrice = (EditText) findViewById(R.id.etDishPrice);
        etDishDesc = (EditText) findViewById(R.id.etDishDescription);
        etDishDiscount = (EditText) findViewById(R.id.etDishDiscount);

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
                    this.grabImage(dishPhoto);
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
            case ACTION_CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    dishPhotoUri = imageReturnedIntent.getData();
                    try {
                        dishPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dishPhotoUri);
                        findViewById(R.id.textview_add_dish_photo).setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dishPhoto.setImageURI(dishPhotoUri);
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
        }
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        String title = getResources().getString(R.string.title_activity_create_dish);
        toolbar.setTitle(title);
    }





    private String generateDishId(String dishName){
        long currentTime = System.currentTimeMillis();
        return HashGenerator.getSHA256hash(dishName + currentTime);
    }

    private  void setUpButtons(){
        etDishName.setOnFocusChangeListener(new EditTextFocusListener(etDishName));
        etDishDesc.setOnFocusChangeListener(new EditTextFocusListener(etDishDesc));
        etDishPrice.setOnFocusChangeListener(new EditTextFocusListener(etDishPrice));
        etDishDiscount.setOnFocusChangeListener(new EditTextFocusListener(etDishDiscount));
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDishPhotoDialog addDishPhotoDialog = new AddDishPhotoDialog(CreateDishActivity.this);
                addDishPhotoDialog.show();
            }
        });

        btnCreateDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                username = PrefUtil.getStringPreference(SessionManager.USERNAME, CreateDishActivity.this);
                dishName = etDishName.getText().toString();
                dishDesc = etDishDesc.getText().toString();

                dishCategory = items[spCategory.getSelectedItemPosition()];
                dishId = generateDishId(dishName);

                if(etDishDiscount.getText().toString().length()==0)
                    dishDiscount = 0;
                else if (!(0<=Float.parseFloat(etDishDiscount.getText().toString())||(Float.parseFloat(etDishDiscount.getText().toString())<=100))){

                    etDishDiscount.setError("enter dish discount between 0-100");
                    valid = false;
                }
                else{
                    dishDiscount = Float.parseFloat(etDishDiscount.getText().toString());

                }

                if(findViewById(R.id.textview_add_dish_photo).getVisibility()!= View.INVISIBLE){
                    //Todo: change the string to be xml resource
                    ((android.widget.TextView)findViewById(R.id.textview_add_dish_photo)).setText("Please upload an image");
                    ((android.widget.TextView)findViewById(R.id.textview_add_dish_photo)).setTextColor(ContextCompat.getColor(CreateDishActivity.this,android.R.color.holo_red_light));
                }
                else if(dishName.length()==0) {
                    etDishName.setError("cannot be empty!");

                }
                else if(dishDesc.length()==0) {
                    etDishDesc.setError("cannot be empty!");

                }
                else if(etDishPrice.getText().toString().length()==0){
                    etDishPrice.setError("cannot be empty!");

                }
                else{
                    dishPrice = Float.parseFloat(etDishPrice.getText().toString());
                    if (valid)
                        new PhpRequestAsyncTask(CreateDishActivity.this,ConnectionParams.URL_CHEF_CREATE_DISH,
                                "POST",TASK_CREATE_DISH).execute();
                }


            }
        });


    }


    private void setUpSpinner(){
        items = new String[7];
        items[0] = "Indian";
        items[1] = "Chinese";
        items[2] = "Malaysian";
        items[3] = "Indonesian";
        items[4] = "Vietnamese";
        items[5] = "Western";
        items[6] = "Other";

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

    private void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(dishPhotoUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, dishPhotoUri);
            dishPhotoBitmap = bitmap;
            imageView.setImageBitmap(bitmap);
            findViewById(R.id.textview_add_dish_photo).setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to load", e);
        }
    }

    @Override
    public void preAsyncTask(int action) {
        switch(action){
            case TASK_CREATE_DISH:
                pDialog = new ProgressDialog(CreateDishActivity.this);
                pDialog.setMessage("Creating dish...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                break;
            case TASK_UPLOAD_IMAGE:
                pDialog = new ProgressDialog(CreateDishActivity.this);
                pDialog.setMessage("Uploading dish image...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public ContentValues setUpParams(int action) {
        ContentValues values = new ContentValues();
        switch(action){
            case TASK_CREATE_DISH:
                values.put("id", dishId);
                values.put("name", dishName);
                values.put("chefname", username);
                values.put("price", String.valueOf(dishPrice));
                values.put("desc", dishDesc);
                values.put("discount", String.valueOf(dishDiscount));
                values.put("category", dishCategory);
                String filePath = username+"/dishes/"+dishId+".png";
                values.put("photo", filePath);
                break;
            case TASK_UPLOAD_IMAGE:
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                dishPhotoBitmap = BitmapUtil.getResizedBitmap(dishPhotoBitmap,dishPhotoBitmap.getWidth()/5,dishPhotoBitmap.getHeight()/5);
                dishPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArr = stream.toByteArray();

                // Encode Image to String
                String encodedString = Base64.encodeToString(byteArr, 0);

                filePath = username+"/dishes/"+dishId+".png";


                values.put("chefname", username);
                values.put("photo", filePath);
                values.put("data", encodedString);


                break;
            default:
                break;
        }
        return values;
    }

    @Override
    public void doInAsyncTask(int action, int success, JSONObject json) {
        switch(action){
            case TASK_CREATE_DISH:
                break;
            case TASK_UPLOAD_IMAGE:
                break;
            default:
                break;
        }
    }

    @Override
    public void postAsyncTask(int action, int success, String msg) {
        switch(action){
            case TASK_CREATE_DISH:
                pDialog.dismiss();
                if (success == 1) {
                    Log.d(TAG, "Create dish succeed!");
                    Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
                    //finish this activity once registered successfully
                    new PhpRequestAsyncTask(CreateDishActivity.this,ConnectionParams.URL_CHEF_UPLOAD_DISH_IMAGE,
                            "POST",TASK_UPLOAD_IMAGE).execute();

                } else {
                    Log.d(TAG,"Create dish failed!");
                    Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
                }
                break;
            case TASK_UPLOAD_IMAGE:
                pDialog.dismiss();
                if (success == 1) {
                    Log.d(TAG, "Upload dish image succeed!");
                    Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
                    //finish this activity once registered successfully
                    CreateDishActivity.this.finish();

                } else {
                    Log.d(TAG,"Upload dish image failed!");
                    Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private class EditTextFocusListener implements View.OnFocusChangeListener{
        private EditText wrapper;

        EditTextFocusListener(EditText wrapper){
            this.wrapper = wrapper;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                wrapper.clearError();
            }
        }
    }


    public Uri getDishPhotoUri() {
        return dishPhotoUri;
    }

    public void setDishPhotoUri(Uri dishPhotoUri) {
        this.dishPhotoUri = dishPhotoUri;
    }
}
