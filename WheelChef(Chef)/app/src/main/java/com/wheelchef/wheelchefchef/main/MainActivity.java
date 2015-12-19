package com.wheelchef.wheelchefchef.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.dish.CreateDishActivity;
import com.wheelchef.wheelchefchef.account.LoginActivity;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LeftDrawerLayout mLeftDrawerLayout;
    private NavigationView navigationView;
    private NavigationMenuFragment navigationMenuFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private static final String TAG = "LoginActivity";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MSG = "message";
    private MenuFragment menuFragment;
    private OrderFragment orderFragment;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private enum Fragments {
        MENU,CURRENT_ORDER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.getBooleanExtra(LoginActivity.NEED_VERIFY,false)){
            String username = PrefUtil.getStringPreference(SessionManager.USERNAME, this);
            String password = PrefUtil.getStringPreference(SessionManager.PASSWORD,this);
            new VerifyTask(username, password).execute();
        }

        setContentView(R.layout.activity_main);
        setUpToolbar();

        fragmentManager = getSupportFragmentManager();

        setUpDrawer();

        menuFragment = new MenuFragment();
        orderFragment = new OrderFragment();

        switchFragment(Fragments.CURRENT_ORDER);
//        getSupportActionBar().setTitle(R.string.current_order_fragment);
        toolbarTitle.setText(getResources().getString(R.string.current_order_fragment));
    }

    @Override
    public void onBackPressed() {
        if (mLeftDrawerLayout.isShownMenu()) {
            mLeftDrawerLayout.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setUpNavigationView();
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbarTitle = (TextView) findViewById(R.id.custom_toolbar_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });
    }

    private void setUpDrawer(){
        mLeftDrawerLayout = (LeftDrawerLayout)findViewById(R.id.drawer_layout);
        navigationMenuFragment = (NavigationMenuFragment) fragmentManager.findFragmentById(R.id.container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.flowing_view);
        if (navigationMenuFragment == null) {
            fragmentManager.beginTransaction().add(R.id.container_menu, navigationMenuFragment = new NavigationMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(navigationMenuFragment);

    }


    private void setUpNavigationView(){
        navigationView = navigationMenuFragment.getNavigationView();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        // This method will trigger on item Click of navigation menu
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {


            //Checking if the item is in checked state or not, if not make it in checked state
            if(menuItem.isChecked()) menuItem.setChecked(false);
            else menuItem.setChecked(true);

            //Closing drawer on item click
            mLeftDrawerLayout.closeDrawer();

            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()){

                case R.id.home_item:
                    switchFragment(Fragments.MENU);
//                    getSupportActionBar().setTitle(R.string.home_fragment);
                    toolbarTitle.setText(getResources().getString(R.string.home_fragment));
                    return true;
                case R.id.order_item:
                    switchFragment(Fragments.CURRENT_ORDER);
//                    getSupportActionBar().setTitle(R.string.current_order_fragment);
                    toolbarTitle.setText(getResources().getString(R.string.current_order_fragment));
                    return true;
                case R.id.logout_item:
                    SessionManager.logout(MainActivity.this);
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                default:
                    return true;

            }
        }
        });
    }

    private void switchFragment(Fragments target){
        switch (target){
            case MENU:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder_main, menuFragment);
                fragmentTransaction.commit();
                break;
            case CURRENT_ORDER:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder_main, orderFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    //listener for the createDish fab
    public void createDish(View view) {
        try{
            Intent intent = new Intent(this, CreateDishActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
            toast.show();
        }
    }


    private class VerifyTask extends AsyncTask<String, String, String> {
        int success = 0;
        String msg = "";
        String username;
        String password;

        VerifyTask(String username,String password){
            this.username = username;
            this.password = password;
        }
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_LOGIN, "POST", params);
            Log.d(TAG, "with the username: " + username);
            Log.d(TAG,"with the password: "+password);
            Log.d(TAG,"json received is: "+json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);
                msg = json.getString(TAG_MSG);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            if (success == 1) {
                Log.d(TAG, "login succeed!");
            } else {
                SessionManager.logout(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Log.d(TAG, "login failed!");
                finish();
            }
        }

    }
}
