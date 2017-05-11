package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.CategoryAdapter;
import com.example.android.shopinggururegister.Adapter.MyOrdersAdapter;
import com.example.android.shopinggururegister.Dos.ProductsData;
import com.example.android.shopinggururegister.Dos.RecentOrdersDataObject;
import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 23-01-2017.
 */
public class MyOrderFragment extends Fragment {
    private ProgressDialog pDialog;
    private String RECENT_ORDERS_URL = "http://shopingguru.in/wp-webapi/recentorders.php";
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "status";
    //  private static final String TAG_USERS = "routers_sheet";
    private static final String TAG_USERS = "data";
    JSONArray categories = null;
    int status = 0;
    private String id;
    private SessionManager sessionManager;
    private TextView orderJsonTextView;
    private JSONObject jsonObject;
    ArrayList<RecentOrdersDataObject> recentOrdersDOArrayLists = new ArrayList<RecentOrdersDataObject>();
    MyOrdersAdapter myOrdersAdapter;
    ListView listViewCommon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_layout, null);
//        orderJsonTextView = (TextView) view.findViewById(R.id.orderJsonTextView);
        listViewCommon = (ListView) view.findViewById(R.id.listViewCommon);
        sessionManager = new SessionManager(getActivity());
        id = sessionManager.getUserDetails().get(SessionManager.KEY_ID);
        new RecentOrders().execute();
        return view;
    }

    class RecentOrders extends AsyncTask<String, String, String> {

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
            params.add(new BasicNameValuePair("user_id", id));

            JSONObject json = jParser.makeHttpRequest(RECENT_ORDERS_URL, "GET", params);
            Log.d("orders:>>>>>>>>> ", json.toString());

            try {

                status = json.getInt(TAG_SUCCESS);
                //  Log.d("status:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {
                    Log.d("status:>>>>>>>>> ", Integer.toString(status));
                }

                try {
                    categories = json.getJSONArray("data");
                    Log.d("categories>>>>", categories.toString());

                    // looping through All Products
                    for (int i = 1; i < categories.length(); i++) {

                        JSONObject c = categories.getJSONObject(i);

                        String order_total = c.getString("order_total");
                        String order_date = c.getString("order_date");
//                        // Storing each json item in variable
                        String order_id = c.getString("order_id");
                        String order_status = c.getString("order_status");
//
//                        Log.d(">>>>>>>>>i", Integer.toString(i));

                        Log.d("order_total>>>>", order_total);
                        Log.d(">>>>order_date", order_date);
                        Log.d(">>>>order_id", order_id);
                        Log.d(">>>>order_status", order_status);


                        recentOrdersDOArrayLists.add(new RecentOrdersDataObject(order_total, order_date, order_id, order_status));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//
//                } else {
//                    Log.e("ServiceHandler", "Couldn't get any data from the url");
//                }
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
                    myOrdersAdapter = new MyOrdersAdapter(getActivity(), recentOrdersDOArrayLists);
                    listViewCommon.setAdapter(myOrdersAdapter);
                    listViewCommon.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getActivity(), "Wrong credentials..", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }
        }

    }


}