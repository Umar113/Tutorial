package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.CategoryGridAdapter;
import com.example.android.shopinggururegister.Adapter.CategoryListAdapter;
import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 10-12-2016.
 */
public class SearchResultActivity extends AppCompatActivity {
    private GridView grid_cat;
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_USERS = "data";
    JSONParser jParser = new JSONParser();
    int status = 0;
    JSONArray categories = null;
    private String SEARCH_URL = "http://shopingguru.in/wp-webapi/search.php";
    String keywordString;
    ArrayList<CategoryListData> arrayList = new ArrayList<>();
    CategoryListAdapter adapterList;
    Bundle gotData;
    String product_id, product_name, product_image, product_desc, product_actual_price, product_selling_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        grid_cat = (GridView) findViewById(R.id.grid_cat);

        gotData = getIntent().getExtras();
        keywordString = gotData.getString(MainActivityNew.KEY_SEARCH);

        Toast.makeText(SearchResultActivity.this, keywordString, Toast.LENGTH_SHORT).show();
        new SearchTask().execute();
    }

    class SearchTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchResultActivity.this);
            pDialog.setMessage("Loading your search. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("keywords", keywordString));
            JSONObject json = jParser.makeHttpRequest(SEARCH_URL, "POST", params);
            Log.d("Search:>>>>>>>>> ", json.toString());
            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);
                Log.d("Response:>>>>>>>>> ", String.valueOf(status));
                if (status == 1) {
                    // products found
                    // Getting Array of Products
                    categories = json.getJSONArray(TAG_USERS);
                    // looping through All Products
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

//                        Storing each json item in variable
//                        String product_id = c.getString("product_id");
//                        String product_name = c.getString("product_name");
//                        String product_image = c.getString("product_image");
//                        String product_desc = c.getString("product_desc");
//                        String product_actual_price = c.getString("product_actual_price");
//                        String product_selling_price = c.getString("product_selling_price");
//
//                        Log.d("product_id:>>>>>>>>> ", product_id);
//                        Log.d("product_name:>>>>>>>>> ", product_name);
//                        Log.d("product_image:>>>>>>>> ", product_image);
//                        Log.d("product_desc:>>>>>>>>> ", product_desc);
//                        Log.d("product_actual:>>>>> ", product_actual_price);
//                        Log.d("product_selling:>>>>> ", product_selling_price);
                        product_desc = c.getString("product_desc");
                        product_image = c.getString("product_image");
                        product_selling_price = c.getString("product_selling_price");
                        product_id = c.getString("product_id");
                        product_actual_price = c.getString("product_actual_price");
                        product_name = c.getString("product_name");

                        Log.d("product_id:>>>>>>>>> ", product_id);
                        Log.d("product_name:>>>>>>>>> ", product_name);
                        Log.d("product_image:>>>>>>>> ", product_image);
                        Log.d("product_desc:>>>>>>>>> ", product_desc);
                        Log.d("product_actual:>>>>> ", product_actual_price);
                        Log.d("product_selling:>>>>> ", product_selling_price);
//                        arrayList.add(new CategoryListData(product_id, product_name, product_image, product_desc, product_actual_price, product_selling_price, 0));
                        arrayList.add(new CategoryListData(product_id, product_name, product_image, product_desc, getResources().getString(R.string.indian_rupee) + product_actual_price, getResources().getString(R.string.indian_rupee) + product_selling_price, 0));
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            try {
                if (status == 1) {
                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    adapterList = new CategoryListAdapter(SearchResultActivity.this, arrayList);
                    grid_cat.setAdapter(adapterList);
                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
