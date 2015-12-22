package com.wheelchef.wheelchefchef.main;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.account.LoginActivity;
import com.wheelchef.wheelchefchef.account.SessionManager;
import com.wheelchef.wheelchefchef.base.PhpAsyncTaskComponent;
import com.wheelchef.wheelchefchef.base.CustomToolbarActivity;
import com.wheelchef.wheelchefchef.base.PhpRequestAsyncTask;
import com.wheelchef.wheelchefchef.dish.CreateDishActivity;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.json.JSONObject;

public class MainActivity extends CustomToolbarActivity implements PhpAsyncTaskComponent {

    private LeftDrawerLayout mLeftDrawerLayout;
    private NavigationView navigationView;
    private NavigationMenuFragment navigationMenuFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private static final String TAG = "LoginActivity";


    private MenuFragment menuFragment;
    private OrderFragment orderFragment;

    private TextView toolbarTitle;

    private String username, password;

    @Override
    public void preAsyncTask(int action) {

    }

    @Override
    public ContentValues setUpParams(int action) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return values;
    }

    @Override
    public void doInAsyncTask(int action, int success,JSONObject json) {

    }

    @Override
    public void postAsyncTask(int action, int success, String msg) {
        if (success == 1) {
            Log.d(TAG, "login succeed!");
        } else {
            SessionManager.logout(MainActivity.this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Log.d(TAG, "login failed!");
            finish();
        }
    }

    private enum Fragments {
        MENU,CURRENT_ORDER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.getBooleanExtra(LoginActivity.NEED_VERIFY,false)){
            username = PrefUtil.getStringPreference(SessionManager.USERNAME, this);
            password = PrefUtil.getStringPreference(SessionManager.PASSWORD,this);
            new PhpRequestAsyncTask(MainActivity.this,ConnectionParams.URL_CHEF_LOGIN,
                    "POST",-1).execute();
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

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        toolbarTitle = (TextView) findViewById(R.id.custom_toolbar_title);
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
}
