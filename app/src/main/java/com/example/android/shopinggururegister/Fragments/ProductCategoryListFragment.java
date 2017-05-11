package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.CategoryAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.ProductsData;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjal on 25/11/2016.
 */
public class ProductCategoryListFragment extends Fragment {

    ListView listview;
    ArrayList<ProductsData> listarr = new ArrayList<ProductsData>();
    CategoryAdapter listadapter;

    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "status";
    //  private static final String TAG_USERS = "routers_sheet";
    private static final String TAG_USERS = "data";

    JSONParser jParser = new JSONParser();

    int status = 0;
    JSONArray categories = null;
    private String FETCH_URL = "http://shopingguru.in/wp-webapi/all_cat_id.php";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.activity_products, null);
            listview = (ListView) view.findViewById(R.id.listView_products);

            TextView countTextView = (TextView) getActivity().findViewById(R.id.countTextView);

//                startActivity(new Intent(Ma   inActivityNew.this, BuyNowActivity.class));
            ProductsListDb db = new ProductsListDb(getActivity());
            int cart_counts = db.getCartCount();
            db.close();
            countTextView.setText(Integer.toString(cart_counts));

            listview.setVisibility(View.VISIBLE);
            new Categories().execute();
        }
        return view;
    }

    class Categories extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading categories. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(FETCH_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());

            try {

                status = json.getInt(TAG_SUCCESS);
                //  Log.d("status:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {

                    categories = json.getJSONArray(TAG_USERS);

                    // looping through All Products
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);


                        // Storing each json item in variable
                        String cat_name = c.getString("cat_name");
                        String cat_id = c.getString("cat_id");


                        Log.d("cat_name", cat_name);
                        Log.d("cat_id", cat_id);

                        listarr.add(new ProductsData(cat_id, cat_name));

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
                    Toast.makeText(getActivity(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    listadapter = new CategoryAdapter(getActivity(), listarr);
                    listview.setAdapter(listadapter);

                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }

    }

}
