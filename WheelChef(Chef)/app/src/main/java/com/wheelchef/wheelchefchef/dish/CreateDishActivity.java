package com.wheelchef.wheelchefchef.dish;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.wheelchef.wheelchefchef.main.MainActivity;
import com.wheelchef.wheelchefchef.registerlogin.SessionManager;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;
import com.wheelchef.wheelchefchef.utils.MD5Generator;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyk on 12/4/2015.
 */
public class CreateDishActivity extends AppCompatActivity{


    private Uri dishPhotoUri;

    protected static final int ACTION_TAKE_PHOTO = 111;
    protected static final int ACTION_CHOOSE_PHOTO = 222;

    private static final String TAG = "CreateDishActivity";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();

    private ImageButton btnAddPhoto;
    private Button btnCreateDish;
    private Spinner spCategory;
    private ImageView dishPhoto;
    private EditText etDishName, etDishPrice, etDishDesc, etDishDiscount;
    private String dishId, dishName, dishDesc, dishCategory, username;
    private float dishPrice, dishDiscount;

    private Bitmap dishPhotoBitmap;


    private String[] items;
    private long currentTime;


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
//                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    dishPhoto.setImageBitmap(photo);
                    this.grabImage(dishPhoto);
                    Log.d(TAG,"Successfully got the photo!");
                }
                break;
            case ACTION_CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri  photo = imageReturnedIntent.getData();
                    try {
                        dishPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

        btnCreateDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = PrefUtil.getStringPreference(SessionManager.USERNAME, CreateDishActivity.this);
                dishName = etDishName.getText().toString();
                dishDesc = etDishDesc.getText().toString();
                dishPrice = Float.parseFloat(etDishPrice.getText().toString());
                dishDiscount = Float.parseFloat(etDishDiscount.getText().toString());
                dishCategory = items[spCategory.getSelectedItemPosition()];
                dishId = generateDishId(dishName);
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

    private class CreateDishTask extends AsyncTask<String, String, String> {
        int success = 0;
        String msg = "";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateDishActivity.this);
            pDialog.setMessage("Creating account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", dishId));
            params.add(new BasicNameValuePair("name", dishName));
            params.add(new BasicNameValuePair("chefname", username));
            params.add(new BasicNameValuePair("price", String.valueOf(dishPrice)));
            params.add(new BasicNameValuePair("desc", dishDesc));
            params.add(new BasicNameValuePair("discount", String.valueOf(dishDiscount)));
            params.add(new BasicNameValuePair("category", dishCategory));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            dishPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            String encodedString = Base64.encodeToString(byte_arr, 0);
            params.add(new BasicNameValuePair("data", encodedString));

            String filePath = ConnectionParams.URL_WEBSITE+ConnectionParams.CHEF_FOLDER+username+"/"+dishId+".png";
            params.add(new BasicNameValuePair("photo", filePath));


            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_REGISTER, "POST", params);
            Log.d(TAG, "with the dishId: " + dishId);
            Log.d(TAG, "with the dishName: " + dishName);
            Log.d(TAG, "json received is: " + json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(ConnectionParams.TAG_SUCCESS);
                msg = json.getString(ConnectionParams.TAG_MSG);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success == 1) {
                Log.d(TAG, "Create dish succeed!");
                Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
                //finish this activity once registered successfully
                CreateDishActivity.this.finish();
            } else {
                Log.d(TAG,"Create dish failed!");
                Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
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
