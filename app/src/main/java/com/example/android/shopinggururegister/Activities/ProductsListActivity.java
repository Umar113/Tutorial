package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.CategoryGridAdapter;
import com.example.android.shopinggururegister.Adapter.CategoryListAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Fragments.WishListFragment;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.Preferences.SharedData;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by anjal on 26/11/2016.
 */
public class ProductsListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_USERS = "data";
    JSONParser jParser = new JSONParser();
    String product_id, product_name, product_image, product_desc, product_actual_price, product_selling_price;
    int success = 0;
    JSONArray categories = null;
    Boolean isOddClicked = true;
    double price1, price2;
    private String UPLOAD_URL = "http://shopingguru.in/wp-webapi/product_by_cat_id.php";
    String cat_id;
    ImageButton settingImage;
    SharedData sharedData;

    ListView listView;
    GridView gridview;
    ArrayList<CategoryListData> arrayList = new ArrayList<>();
    CategoryListAdapter adapterList;
    CategoryGridAdapter adapterGrid;
    private Spinner sortSpinner;
    List<String> sortList;
    String sortString[] = {"Sort ", "Price : Low to High ", "Price : High to Low ", "Newest First "};
    Comparator comparator;
    CategoryListData categoryListData;
    private Bundle sendData;

    private ProductsListDb productsListDb;
    Boolean list_grid_sharedata = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        gridview = (GridView) findViewById(R.id.grid_cat);
        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener(this);
        comparator = Collections.reverseOrder();

        productsListDb = new ProductsListDb(this);
        sharedData = new SharedData(getApplicationContext());

        List<String> categories = new ArrayList<String>();
        for (int i = 0; i < sortString.length; i++) {
            categories.add(sortString[i]);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sortSpinner.setAdapter(dataAdapter);

        Bundle bundle = getIntent().getExtras();
        cat_id = bundle.getString("KEY_ID");
        sortList = new ArrayList<String>();
        settingImage = (ImageButton) findViewById(R.id.settingImage);
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOddClicked) {
                    settingImage.setImageResource(R.drawable.ic_list_black_24dp);
                    isOddClicked = false;

                    adapterGrid = new CategoryGridAdapter(ProductsListActivity.this, arrayList);
                    gridview.setAdapter(adapterGrid);
                    gridview.setNumColumns(2);

                } else {
                    settingImage.setImageResource(R.drawable.ic_apps_black_24dp);
                    isOddClicked = true;
                    adapterList = new CategoryListAdapter(ProductsListActivity.this, arrayList);
                    gridview.setAdapter(adapterList);
                    gridview.setNumColumns(1);
                }
            }
        });
        new Category().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();

        switch (position) {
            case 1:
                Collections.sort(arrayList, new PriceSortLowToHigh());
                Toast.makeText(adapterView.getContext(), item, Toast.LENGTH_LONG).show();
                adapterList.notifyDataSetChanged();
                gridview.setAdapter(adapterList);
                break;
            case 2:
                Collections.sort(arrayList, new PriceSortHighToLow());
                Toast.makeText(adapterView.getContext(), item, Toast.LENGTH_LONG).show();
                adapterList.notifyDataSetChanged();
                gridview.setAdapter(adapterList);

                break;
            case 3:
//                Collections.sort(arrayList, Collections.reverseOrder());
//                Collections.sort(arrayList, new Comparator<CategoryListData>() {
//                    public int compare(CategoryListData emp1, CategoryListData emp2) {
//                        // ## Ascending order
////                        return emp1.getProduct_id().compareToIgnoreCase(emp2.getProduct_id()); // To compare string values
//                        // return Integer.valueOf(emp1.getId()).compareTo(emp2.getId()); // To compare integer values
//
//                        // ## Descending order
//                        return emp2.product_id.compareToIgnoreCase(emp1.product_id); // To compare string values
//
//                        // return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
//                    }
//                });


//                Collections.sort(arrayList, new NewestFirst());
//                Toast.makeText(adapterView.getContext(), item, Toast.LENGTH_LONG).show();
//                adapterList.notifyDataSetChanged();
//                gridview.setAdapter(adapterList);


                startActivity(new Intent(getApplicationContext(), WishListFragment.class));


                break;

        }

        // On selecting a spinner item
        // Showing selected spinner item
    }

    public class NewestFirst implements Comparator<CategoryListData> {
        public int compare(CategoryListData c, CategoryListData d) {
//            return a.getProduct_selling_price().compareTo(b.getProduct_selling_price());

            CategoryListData f1 = (CategoryListData) c;

            CategoryListData f2 = (CategoryListData) d;

            if (f1.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price1 = 0.0;
            } else {
                price1 = Double.parseDouble(f1.product_id);
            }
            if (f2.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price2 = 0.0;
            } else {
                price2 = Double.parseDouble(f2.product_id);
            }

            if (price1 == price2)
                return 0;
            else if (price1 < price2)
                return 1;
            else
                return -1;
        }

        @Override
        public Comparator<CategoryListData> reversed() {
            return null;
        }
    }

    public class PriceSortLowToHigh implements Comparator<CategoryListData> {
        public int compare(CategoryListData a, CategoryListData b) {
//            return a.getProduct_selling_price().compareTo(b.getProduct_selling_price());

            CategoryListData e1 = (CategoryListData) a;

            CategoryListData e2 = (CategoryListData) b;

            if (e1.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price1 = 0.0;
            } else {
                price1 = Double.parseDouble(e1.product_selling_price.replace("₹", ""));
            }
            if (e2.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price2 = 0.0;
            } else {
                price2 = Double.parseDouble(e2.product_selling_price.replace("₹", ""));
            }

            if (price1 == price2)
                return 0;
            else if (price1 > price2)
                return 1;
            else
                return -1;
        }

        @Override
        public Comparator<CategoryListData> reversed() {
            return null;
        }
    }

    public class PriceSortHighToLow implements Comparator<CategoryListData> {
        public int compare(CategoryListData e, CategoryListData f) {
//            return a.getProduct_selling_price().compareTo(b.getProduct_selling_price());

            CategoryListData e1 = (CategoryListData) e;

            CategoryListData e2 = (CategoryListData) f;

            if (e1.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price1 = 0.0;
            } else {
                price1 = Double.parseDouble(e1.product_selling_price.replace("₹", ""));
            }
            if (e2.getProduct_selling_price() == null) {
//                e1.product_selling_price = "1";
                price2 = 0.0;
            } else {
                price2 = Double.parseDouble(e2.product_selling_price.replace("₹", ""));
            }

            if (price1 == price2)
                return 0;
            else if (price1 > price2)
                return 1;
            else
                return -1;
        }

        @Override
        public Comparator<CategoryListData> reversed() {
            return null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    class Category extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductsListActivity.this);
            pDialog.setMessage("Loading Categories. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("cat_id", cat_id));
            JSONObject json = jParser.makeHttpRequest(UPLOAD_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(success));

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    categories = json.getJSONArray(TAG_USERS);

                    // looping through All Products
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

                        // Storing each json item in variable
                        product_id = c.getString("product_id");
                        product_name = c.getString("product_name");
                        product_image = c.getString("product_image");
                        product_desc = c.getString("product_desc");
                        product_actual_price = c.getString("product_actual_price");
                        product_selling_price = c.getString("product_selling_price");

                        Log.d("product_id:>>>>>>>>> ", product_id);
                        Log.d("product_name:>>>>>>>>> ", product_name);
                        Log.d("product_image:>>>>>>>> ", product_image);
                        Log.d("product_desc:>>>>>>>>> ", product_desc);
                        Log.d("product_actual:>>>>> ", product_actual_price);
                        Log.d("product_selling:>>>>> ", product_selling_price);

                        if (product_actual_price.contentEquals("0") || product_selling_price.contentEquals("0")) {
                            product_selling_price = "According to range";
                            product_actual_price = "";
                        } else {
                            product_selling_price = getResources().getString(R.string.indian_rupee) + product_selling_price;
                            product_actual_price = getResources().getString(R.string.indian_rupee) + product_actual_price;
                        }
                        arrayList.add(new CategoryListData(product_id, product_name, product_image, product_desc, product_actual_price, product_selling_price, 0));

                        // arrayList.add(new CategoryListData(product_id, product_name, product_image, product_desc, getResources().getString(R.string.indian_rupee) + product_actual_price, getResources().getString(R.string.indian_rupee) + product_selling_price, 0));
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
                if (success == 1) {
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    adapterList = new CategoryListAdapter(ProductsListActivity.this, arrayList);
                    gridview.setAdapter(adapterList);


//                    productsListDb
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
