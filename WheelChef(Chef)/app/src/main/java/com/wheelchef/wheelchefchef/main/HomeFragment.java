package com.wheelchef.wheelchefchef.main;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wheelchef.wheelchefchef.R;
import com.wheelchef.wheelchefchef.datamodels.DishModel;
import com.wheelchef.wheelchefchef.registerlogin.SessionManager;
import com.wheelchef.wheelchefchef.sqlitedb.DatabaseHelper;
import com.wheelchef.wheelchefchef.sqlitedb.DishesDataSource;
import com.wheelchef.wheelchefchef.utils.ConnectionParams;
import com.wheelchef.wheelchefchef.utils.JSONParser;
import com.wheelchef.wheelchefchef.utils.PrefUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by lyk on 11/20/2015.
 */
public class HomeFragment extends Fragment {

    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    private ListView listView;

    private static DatabaseHelper myDBHelper = null;
    private static SQLiteDatabase database = null;

    private static final String TAG = "DishesFragment";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();
    // products JSONArray
    JSONArray products = null;
    //Array list of the dish data models
    ArrayList<DishModel> dishList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(myDBHelper == null){
            myDBHelper = new DatabaseHelper(this.getActivity().getApplication());
            database = myDBHelper.getDB();
            DishesDataSource.setDatabase(database);
            DishesDataSource.setDatabase(database);
            DishesDataSource.createDishesTable();
        }
        View forReturn = inflater.inflate(R.layout.fragment_home, container, false);
        waveSwipeRefreshLayout = (WaveSwipeRefreshLayout) forReturn.findViewById(R.id.layout_home_swipe);
        listView = (ListView) forReturn.findViewById(R.id.dishes_list);


        dishList = new ArrayList<>();

        setUpWaveRefreshLayout();
        return forReturn;
    }

    private void setUpWaveRefreshLayout(){
        waveSwipeRefreshLayout.setWaveColor(this.getActivity().getResources().getColor(R.color.color_primary));
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        waveSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 3000);
                new LoadDishesFromRemote().execute();
            }
        });

    }


    private class LoadDishesFromRemote extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(HomeFragment.this.getActivity());
//            pDialog.setMessage("Loading products. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            int success = 0;
            String msg = "";
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String username = PrefUtil.getStringPreference(SessionManager.USERNAME, HomeFragment.this.getActivity());
            params.add(new BasicNameValuePair("chefname", username));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_LOAD_DISH, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Dishes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(ConnectionParams.TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(ConnectionParams.TAG_DISHES);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String dishId = c.getString(ConnectionParams.COL_DISH_ID);
                        String dishName = c.getString(ConnectionParams.COL_DISH_NAME);
                        String chefName = c.getString(ConnectionParams.COL_CHEF_NAME);
                        String description = c.getString(ConnectionParams.COL_DESC);

                        float price = Float.parseFloat(c.getString(ConnectionParams.COL_PRICE));
                        int timesOrdered = Integer.parseInt(c.getString(ConnectionParams.COL_TIMES_ORDERED));
                        float discount = Float.parseFloat(c.getString(ConnectionParams.COL_DISCOUNT))/100;
                        String category = c.getString(ConnectionParams.COL_CATEGORY);
                        String photo = c.getString(ConnectionParams.COL_FILE_PATH);


                        DishModel dish = new DishModel(dishId,dishName,chefName,description,price,timesOrdered
                        ,discount,category,photo);

                        dishList.add(dish);
                    }
                }
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
//            pDialog.dismiss();
            waveSwipeRefreshLayout.setRefreshing(false);
            // updating UI from Background Thread
//            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            HomeFragment.this.getActivity(), dishList,
//                            R.layout.list_item, new String[]{TAG_PID,
//                            TAG_NAME},
//                            new int[]{R.id.pid, R.id.name});
//                    // updating listview
//                    listView.setAdapter(adapter);
//                }
//            });

        }

    }

}
