package com.wheelchef.wheelchefchef.main;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
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
import com.wheelchef.wheelchefchef.dish.DishCursorAdapter;
import com.wheelchef.wheelchefchef.registerlogin.SessionManager;
import com.wheelchef.wheelchefchef.sqlitedb.DatabaseHelper;
import com.wheelchef.wheelchefchef.sqlitedb.DishesDataSource;
import com.wheelchef.wheelchefchef.sqlitedb.DishesTable;
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


        setUpWaveRefreshLayout();
        new LoadDishesFromLocal().execute();
        return forReturn;
    }

    private void setUpWaveRefreshLayout(){
        waveSwipeRefreshLayout.setWaveColor(this.getActivity().getResources().getColor(R.color.color_primary));
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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


    private void compareDishLists(ArrayList<DishModel> fromRemote, ArrayList<DishModel> fromLocal){
        ArrayList<Integer> changeList = new ArrayList<>();
        int fromRemoteSize = fromRemote.size();
        int fromLocalSize = fromLocal.size();

        if(fromRemoteSize==0){
            //Todo: remove the table rows
        } else if (fromLocalSize==0){
            //Todo: add every thing from remote
            for(int i=0; i< fromRemoteSize; i++) {
                ContentValues values = new ContentValues();
                DishModel tempRemote = fromRemote.get(i);

                values.put(DishesTable.COLUMN_DISH_ID,tempRemote.getDishId());
                values.put(DishesTable.COLUMN_DISH_NAME,tempRemote.getDishName());
                values.put(DishesTable.COLUMN_CHEF_NAME,tempRemote.getChefName());
                values.put(DishesTable.COLUMN_DESCRIPTION,tempRemote.getDescription());
                values.put(DishesTable.COLUMN_PRICE,tempRemote.getPrice());
                values.put(DishesTable.COLUMN_TIMES_ORDERED,tempRemote.getTimesOrdered());
                values.put(DishesTable.COLUMN_DISCOUNT,tempRemote.getDiscount());
                values.put(DishesTable.COLUMN_CATEGORY,tempRemote.getCategory());
                values.put(DishesTable.COLUMN_FILE_PATH,tempRemote.getFilePath());

                DishesDataSource.insertDish(HomeFragment.this.getActivity(), values);
                downloadDishImage(tempRemote.getDishId(), tempRemote.getFilePath());
            }
        } else{
            for(int i=0; i< fromRemoteSize; i++){
                boolean needToChange = true;
                boolean needToAdd = true;
                boolean needChangePhoto = true;
                DishModel tempRemote = fromRemote.get(i);
                for(int j=0; j<fromLocalSize; j++){
                    if(tempRemote.equalsTo(fromLocal.get(j)))
                        needToChange = false;
                    if(tempRemote.isSameDish(fromLocal.get(j))) {
                        needToAdd = false;
                        if(tempRemote.useSamePhoto(fromLocal.get(j)))
                            needChangePhoto = false;
                    } else if(needToChange) {
                        changeList.add(j);
                    }
                }

                if(needToChange){
                    ContentValues values = new ContentValues();
                    if(needToAdd){
                        values.put(DishesTable.COLUMN_DISH_ID,tempRemote.getDishId());
                        values.put(DishesTable.COLUMN_DISH_NAME,tempRemote.getDishName());
                        values.put(DishesTable.COLUMN_CHEF_NAME,tempRemote.getChefName());
                        values.put(DishesTable.COLUMN_DESCRIPTION,tempRemote.getDescription());
                        values.put(DishesTable.COLUMN_PRICE,tempRemote.getPrice());
                        values.put(DishesTable.COLUMN_TIMES_ORDERED,tempRemote.getTimesOrdered());
                        values.put(DishesTable.COLUMN_DISCOUNT,tempRemote.getDiscount());
                        values.put(DishesTable.COLUMN_CATEGORY,tempRemote.getCategory());
                        values.put(DishesTable.COLUMN_FILE_PATH,tempRemote.getFilePath());

                        DishesDataSource.insertDish(HomeFragment.this.getActivity(), values);

                        //Todo: async task add image
                        downloadDishImage(tempRemote.getDishId(),tempRemote.getFilePath());
                    }else {
                        int rowId = DishesDataSource.getRowIdFromDishId(tempRemote.getDishId());

                        values.put(DishesTable.COLUMN_DISH_ID,tempRemote.getDishId());
                        values.put(DishesTable.COLUMN_DISH_NAME,tempRemote.getDishName());
                        values.put(DishesTable.COLUMN_CHEF_NAME,tempRemote.getChefName());
                        values.put(DishesTable.COLUMN_DESCRIPTION,tempRemote.getDescription());
                        values.put(DishesTable.COLUMN_PRICE,tempRemote.getPrice());
                        values.put(DishesTable.COLUMN_TIMES_ORDERED,tempRemote.getTimesOrdered());
                        values.put(DishesTable.COLUMN_DISCOUNT,tempRemote.getDiscount());
                        values.put(DishesTable.COLUMN_CATEGORY,tempRemote.getCategory());
                        values.put(DishesTable.COLUMN_FILE_PATH,tempRemote.getFilePath());

                        DishesDataSource.updateDish(HomeFragment.this.getActivity(),rowId,values);

                        if(needChangePhoto){
                            //Todo: async task add image
                            downloadDishImage(tempRemote.getDishId(),tempRemote.getFilePath());
                        }

                    }
                }
            }
        }
    }

    private void downloadDishImage(String dishId, String filePath){
        int rowId = DishesDataSource.getRowIdFromDishId(dishId);

        int success = 0;
        String msg = "";
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("filepath", filePath));
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_LOAD_DISH_IMAGE, "POST", params);

        // Check your log cat for JSON response
        Log.d("Dish Image : ", json.toString());

        try {
            // Checking for SUCCESS TAG
            success = json.getInt(ConnectionParams.TAG_SUCCESS);

            if (success == 1) {
                String photo = json.getString(ConnectionParams.TAG_PHOTO);

                photo = photo.replace("\\","");

                Log.d(TAG,"the actual photo string should be : "+photo);

                ContentValues values = new ContentValues();

                values.put(DishesTable.COLUMN_PHOTO,photo);

                DishesDataSource.updateDish(HomeFragment.this.getActivity(),rowId,values);

            } else {
                Log.d(TAG,"getting image form server : "+json.getString(ConnectionParams.TAG_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private class LoadDishesFromRemote extends AsyncTask<String, String, String> {

        private Cursor cursor;

        private DishCursorAdapter dataAdapter;

        private String username;

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
            username = PrefUtil.getStringPreference(SessionManager.USERNAME, HomeFragment.this.getActivity());
            params.add(new BasicNameValuePair("chefname", username));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CHEF_LOAD_DISH, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Dishes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(ConnectionParams.TAG_SUCCESS);

                if (success == 1) {
                    //Array list of the dish data models
                    ArrayList<DishModel> dishListRemote = new ArrayList<>();
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

                        dishListRemote.add(dish);
                    }


                    cursor = DishesDataSource.getWholeCursor(username);

                    ArrayList<DishModel> dishListLocal = DishModel.getDishListFromCursor(cursor);


                    compareDishLists(dishListRemote,dishListLocal);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor = DishesDataSource.getWholeCursor(username);
            dataAdapter = new DishCursorAdapter(HomeFragment.this.getActivity(),cursor,0,HomeFragment.this);

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            listView.setAdapter(dataAdapter);
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

    private class LoadDishesFromLocal extends AsyncTask<String, String, String> {

        private Cursor cursor;

        private DishCursorAdapter dataAdapter;

        private String username;

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waveSwipeRefreshLayout.setRefreshing(true);
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            username = PrefUtil.getStringPreference(SessionManager.USERNAME, HomeFragment.this.getActivity());
            cursor = DishesDataSource.getWholeCursor(username);
            dataAdapter = new DishCursorAdapter(HomeFragment.this.getActivity(),cursor,0,HomeFragment.this);

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            listView.setAdapter(null);
            listView.setAdapter(dataAdapter);
            waveSwipeRefreshLayout.setRefreshing(false);


        }

    }

}
