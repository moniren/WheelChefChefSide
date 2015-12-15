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

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.utils.BitmapUtil;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;
import com.wheelchef.wheelchefchef.utils.HashGenerator;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                        findViewById(R.id.textview_add_dish_photo).setVisibility(View.INVISIBLE);
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
                    ((android.widget.TextView)findViewById(R.id.textview_add_dish_photo)).setText("Please upload an image");
                    ((android.widget.TextView)findViewById(R.id.textview_add_dish_photo)).setTextColor(CreateDishActivity.this.getResources().getColor(android.R.color.holo_red_light));
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
                        new CreateDishTask().execute();
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
            pDialog.setMessage("Creating dish...");
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


            String filePath = username+"/dishes/"+dishId+".png";
            params.add(new BasicNameValuePair("photo", filePath));


            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_CREATE_DISH, "POST", params);
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
                new UploadImageTask().execute();

            } else {
                Log.d(TAG,"Create dish failed!");
                Toast.makeText(CreateDishActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }


    }

    private class UploadImageTask extends AsyncTask<String, String, String> {

        RequestParams picParams = new RequestParams();
        int success = 0;
        String msg = "";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateDishActivity.this);
            pDialog.setMessage("Uploading dish image...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            picParams.put("chefname", username);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            dishPhotoBitmap = BitmapUtil.getResizedBitmap(dishPhotoBitmap,dishPhotoBitmap.getWidth()/5,dishPhotoBitmap.getHeight()/5);

            dishPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            byte[] byteArr1 = stream.toByteArray();

            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap middleBitmap = BitmapFactory.decodeByteArray(byteArr1, 0, byteArr1.length, options);

            //if(options.outHeight > options.outWidth)
                //options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 720, 1280);
            //else
                //options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
            options.inSampleSize = 4;
            options.inJustDecodeBounds = false;
            middleBitmap = BitmapFactory.decodeByteArray(byteArr1, 0, byteArr1.length, options);

            //Bitmap finalBitmap = BitmapUtil.getResizedBitmap(middleBitmap,middleBitmap.getWidth()/10,middleBitmap.getHeight()/10);
                    //Bitmap.createScaledBitmap(middleBitmap, middleBitmap.getWidth()/10, middleBitmap.getHeight()/10, false);
            //finalBitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);

            byte[] byteArr2 = stream.toByteArray();*/

            // Encode Image to String
            String encodedString = Base64.encodeToString(byteArr1, 0);
            picParams.put("data", encodedString);

            String filePath = username+"/dishes/"+dishId+".png";
            picParams.put("photo", filePath);


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("chefname", username));
            params.add(new BasicNameValuePair("photo", filePath));
            params.add(new BasicNameValuePair("data", encodedString));

            Log.d(TAG, "with the chefname: " + username);
            Log.d(TAG, "with the photo: " + filePath);
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_UPLOAD_DISH_IMAGE, "POST", params);
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
            /*AsyncHttpClient client = new AsyncHttpClient();
            // Don't forget to change the IP address to your LAN address. Port no as well.
            client.post(ConnectionParams.URL_CHEF_UPLOAD_DISH_IMAGE,
                    picParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // Hide Progress Dialog
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Status: "+statusCode+" Header: "+headers.toString()
                                    +" Response: "+responseBody.toString(),
                                    Toast.LENGTH_LONG).show();

                            Log.d(TAG,"Status: "+statusCode+" Header: "+headers.toString()
                                    +" Response: "+responseBody.toString());

                            CreateDishActivity.this.finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            // Hide Progress Dialog
                            pDialog.dismiss();
                            // When Http response code is '404'
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                                + statusCode, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });*/
            // dismiss the dialog after getting all products
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
