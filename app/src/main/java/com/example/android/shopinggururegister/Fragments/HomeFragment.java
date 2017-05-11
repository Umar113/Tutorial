package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Activities.ProductsListActivity;
import com.example.android.shopinggururegister.Activities.VariationsActivity;
import com.example.android.shopinggururegister.Adapter.HorizontalListViewAdapter;
import com.example.android.shopinggururegister.Adapter.ViewPagerAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.ViewPagerData;
import com.example.android.shopinggururegister.Helpers.ConnectionDetector;
import com.example.android.shopinggururegister.Helpers.HorizontalListView;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Android on 08-12-2016.
 */
public class HomeFragment extends Fragment {
    private ViewPager bannerViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<ViewPagerData> viewpagearr = new ArrayList<ViewPagerData>();
    private View view;
    private int status = 0;
    private JSONArray viewarr = null;
    private String VIEWPAGER_FETCH_URL = "http://shopingguru.in/wp-webapi/slider.php";


    public static final String MORE_IMAGE_URL = "http://shopingguru.in/wp-webapi/1.png";


    // private static final String TAG_SUCCESS = "status";
    private static final String TAG_USERS = "data";
    //private JSONParser jParser = new JSONParser();
    private int count = 0;
    private Timer timer;
    private CircleIndicator bannerCircleIndicator;

    private ProgressDialog sliderDialog, productSliderDialog;
    private static final String TAG_SUCCESS = "status";
    //  private static final String TAG_USERS = "routers_sheet";
    private static final String TAG_PRODUCTS = "data1";
    private static final String TAG_MUSTHAVE = "data2";
    private static final String TAG_BESTSELLER = "data3";
    private static final String TAG_SPECIALOFFERS = "data4";

    JSONParser jParser = new JSONParser();

    // int status = 0;
    JSONArray products = null;
    JSONArray musthave = null;
    JSONArray bestseller = null;
    JSONArray specialoffers = null;
    private String UPLOAD_URL = "http://shopingguru.in/wp-webapi/product_slider.php";

    private HorizontalListView producthlv, musthavehlv, bestSellerhlv, specialOffershlv;
    private HorizontalListViewAdapter producthlvAdapter, mustHavehlvAdapter, bestSellerhlvAdapter, specialOffershlvAdapter;

    ArrayList<String> titleProduct = new ArrayList<>();
    ArrayList<String> priceProduct = new ArrayList<>();
    ArrayList<String> priceActualProduct = new ArrayList<>();
    ArrayList<String> descProduct = new ArrayList<>();
    ArrayList<String> idProduct = new ArrayList<>();
    ArrayList<String> imageProduct = new ArrayList<>();


    ArrayList<String> titleMusthave = new ArrayList<>();
    ArrayList<String> priceMusthave = new ArrayList<>();
    ArrayList<String> priceActualMusthave = new ArrayList<>();
    ArrayList<String> descMusthave = new ArrayList<>();
    ArrayList<String> idMusthave = new ArrayList<>();
    ArrayList<String> imageMusthave = new ArrayList<>();


    ArrayList<String> titleBestseller = new ArrayList<>();
    ArrayList<String> priceBestSeller = new ArrayList<>();
    ArrayList<String> priceActualBestSeller = new ArrayList<>();
    ArrayList<String> descBestSeller = new ArrayList<>();
    ArrayList<String> idBestSeller = new ArrayList<>();
    ArrayList<String> imageBestSeller = new ArrayList<>();

    ArrayList<String> titleSpecialOffers = new ArrayList<>();
    ArrayList<String> priceSpecialOffers = new ArrayList<>();
    ArrayList<String> priceActualSpecialOffers = new ArrayList<>();
    ArrayList<String> descSpecialOffers = new ArrayList<>();
    ArrayList<String> idSpecialOffers = new ArrayList<>();
    ArrayList<String> imageSpecialOffers = new ArrayList<>();


    ArrayList<String> a = new ArrayList<>();

    Bundle sendData;
    private JSONObject json_obj;
    private ConnectionDetector connectionDetector;
    private Boolean isInternetAvailable;

    String cat_id1, cat_id2, cat_id3, cat_id4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, null);
        bannerViewPager = (ViewPager) view.findViewById(R.id.bannerViewPager);
        bannerCircleIndicator = (CircleIndicator) view.findViewById(R.id.bannerCircleIndicator);
        TextView countTextView = (TextView) getActivity().findViewById(R.id.countTextView);
//                startActivity(new Intent(MainActivityNew.this, BuyNowActivity.class));
        ProductsListDb db = new ProductsListDb(getActivity());
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));


        connectionDetector = new ConnectionDetector(getActivity());
        isInternetAvailable = connectionDetector.isConnectingToInternet();
        if (isInternetAvailable) {


            bannerViewPager.setOffscreenPageLimit(3);
            viewPagerAdapter = new ViewPagerAdapter(getActivity(), viewpagearr);
            new ViewPagerTask().execute();


            bannerViewPager.setCurrentItem(0);
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (count <= 5) {
                                bannerViewPager.setCurrentItem(count);
                                count++;
                            } else {
                                count = 0;
                                bannerViewPager.setCurrentItem(count);
                            }
                        }
                    });
                }
            }, 3000, 3000);

            sendData = new Bundle();

            producthlv = (HorizontalListView) view.findViewById(R.id.hlvProducts);
            musthavehlv = (HorizontalListView) view.findViewById(R.id.hlvMusthave);
            bestSellerhlv = (HorizontalListView) view.findViewById(R.id.hlvBestSeller);
            specialOffershlv = (HorizontalListView) view.findViewById(R.id.hlvSpecialOffers);


            new ProductSlider().execute();

        } else {
            Toast.makeText(getActivity(), "Internet is not available", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }

    // ProgressDialog pDialog;

    class ViewPagerTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sliderDialog = new ProgressDialog(getActivity());
            sliderDialog.setMessage("Loading... Please wait!");
            sliderDialog.setIndeterminate(false);
            sliderDialog.setCancelable(false);
            sliderDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params_viewpager = new ArrayList<NameValuePair>();
            JSONObject json_viewpager = jParser.makeHttpRequest(VIEWPAGER_FETCH_URL, "POST", params_viewpager);
//            Log.d("All Products:>>>>>>>>> ", json_viewpager.toString());

            try {
                // Checking for SUCCESS TAG
                status = json_viewpager.getInt(TAG_SUCCESS);
                Log.d("Response:>>>>>>>>> ", String.valueOf(status));
                if (status == 1) {

                    // products found
                    // Getting Array of Products
                    viewarr = json_viewpager.getJSONArray(TAG_USERS);

                    // looping through All Products
                    for (int i = 0; i < viewarr.length(); i++) {
                        JSONObject c = viewarr.getJSONObject(i);
                        // Storing each json item in variable
                        String banner = c.getString("banner");
                        Log.d("banner:>>>>>>>>> ", banner);
                        viewpagearr.add(new ViewPagerData(banner));
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
            sliderDialog.dismiss();
            sliderDialog.cancel();
            try {
                if (status == 1) {
                    // Binds the Adapter to the ViewPager
                    bannerViewPager.setAdapter(viewPagerAdapter);
                    bannerCircleIndicator.setViewPager(bannerViewPager);
                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }
        }
    }

    class ProductSlider extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productSliderDialog = new ProgressDialog(getActivity());
            productSliderDialog.setMessage("Loading... Please wait!");
            productSliderDialog.setIndeterminate(false);
            productSliderDialog.setCancelable(false);
            productSliderDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(UPLOAD_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());

            try {
                status = json.getInt(TAG_SUCCESS);
                //  Log.d("status:>>>>>>>>> ", String.valueOf(status));
                if (status == 1) {

                    products = json.getJSONArray(TAG_PRODUCTS);
                    musthave = json.getJSONArray(TAG_MUSTHAVE);
                    bestseller = json.getJSONArray(TAG_BESTSELLER);
                    specialoffers = json.getJSONArray(TAG_SPECIALOFFERS);


                    cat_id1 = json.getString("cat_id1");
                    Log.d("cat_id1:>>>>>>>>> ", String.valueOf(cat_id1));

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String product_image = c.getString("product_image");
                        String product_selling_price = c.getString("product_selling_price");
                        String product_actual_price = c.getString("product_actual_price");
                        String product_desc = c.getString("product_desc");

                        Log.d("product_id", product_id);
                        Log.d("product_name", product_name);
                        Log.d("product_image", product_image);
                        Log.d("product_selling_price", product_selling_price);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_desc", product_desc);


                        if (product_actual_price.contentEquals("0") || product_selling_price.contentEquals("0")) {
                            product_selling_price = "According to range";
                            product_actual_price = "According to range";
                        }


                        titleProduct.add(product_name);
                        priceProduct.add(getResources().getString(R.string.indian_rupee) + product_selling_price);
                        priceActualProduct.add(product_actual_price);
                        descProduct.add(product_desc);
                        idProduct.add(product_id);
                        imageProduct.add(product_image);
                    }
//                    titleProduct.add("More");
//                    priceProduct.add("More");
//                    imageProduct.add(null);

                    cat_id2 = json.getString("cat_id2");
                    Log.d("cat_id2:>>>>>>>>> ", String.valueOf(cat_id2));
                    for (int i = 0; i < musthave.length(); i++) {
                        JSONObject c = musthave.getJSONObject(i);

                        // Storing each json item in variable
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String product_image = c.getString("product_image");
                        String product_selling_price = c.getString("product_selling_price");
                        String product_actual_price = c.getString("product_actual_price");
                        String product_desc = c.getString("product_desc");


                        Log.d("product_id", product_id);
                        Log.d("product_name", product_name);
                        Log.d("product_image", product_image);
                        Log.d("product_selling_price", product_selling_price);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_desc", product_desc);


                        if (product_actual_price.contentEquals("0") || product_selling_price.contentEquals("0")) {
                            product_selling_price = "According to range";
                            product_actual_price = "According to range";
                        }


                        titleMusthave.add(product_name);
                        priceMusthave.add(getResources().getString(R.string.indian_rupee) + product_selling_price);
                        priceActualMusthave.add(product_actual_price);
                        descMusthave.add(product_desc);
                        idMusthave.add(product_id);
                        imageMusthave.add(product_image);

                    }
//                    titleMusthave.add("More");
//                    priceMusthave.add("More");
//                    imageMusthave.add(null);

                    cat_id3 = json.getString("cat_id3");
                    Log.d("cat_id3:>>>>>>>>> ", String.valueOf(cat_id3));
                    for (int i = 0; i < bestseller.length(); i++) {
                        JSONObject c = bestseller.getJSONObject(i);

                        // Storing each json item in variable
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String product_image = c.getString("product_image");
                        String product_selling_price = c.getString("product_selling_price");
                        String product_actual_price = c.getString("product_actual_price");
                        String product_desc = c.getString("product_desc");

                        Log.d("product_id", product_id);
                        Log.d("product_name", product_name);
                        Log.d("product_image", product_image);
                        Log.d("product_selling_price", product_selling_price);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_desc", product_desc);

                        if (product_actual_price.contentEquals("0") || product_selling_price.contentEquals("0")) {
                            product_selling_price = "According to range";
                            product_actual_price = "According to range";
                        }


                        titleBestseller.add(product_name);
                        priceBestSeller.add(getResources().getString(R.string.indian_rupee) + product_selling_price);
                        priceActualBestSeller.add(product_actual_price);
                        descBestSeller.add(product_desc);
                        idBestSeller.add(product_id);
                        imageBestSeller.add(product_image);

                    }
//                    titleBestseller.add("More");
//                    priceBestSeller.add("More");
//                    imageBestSeller.add("");

                    cat_id4 = json.getString("cat_id4");
                    Log.d("cat_id4:>>>>>>>>> ", String.valueOf(cat_id4));
                    for (int i = 0; i < specialoffers.length(); i++) {
                        JSONObject c = specialoffers.getJSONObject(i);

                        // Storing each json item in variable
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String product_image = c.getString("product_image");
                        String product_selling_price = c.getString("product_selling_price");
                        String product_actual_price = c.getString("product_actual_price");
                        String product_desc = c.getString("product_desc");


                        Log.d("product_id", product_id);
                        Log.d("product_name", product_name);
                        Log.d("product_image", product_image);
                        Log.d("product_selling_price", product_selling_price);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_desc", product_desc);

                        if (product_actual_price.contentEquals("0") || product_selling_price.contentEquals("0")) {
                            product_selling_price = "According to range";
                            product_actual_price = "According to range";
                        }

                        titleSpecialOffers.add(product_name);
                        priceSpecialOffers.add(getResources().getString(R.string.indian_rupee) + product_selling_price);
                        priceActualSpecialOffers.add(product_actual_price);
                        descSpecialOffers.add(product_desc);
                        idSpecialOffers.add(product_id);
                        imageSpecialOffers.add(product_image);

                    }
//                    titleSpecialOffers.add("More");
//                    priceSpecialOffers.add("More");
//                    imageSpecialOffers.add(null);
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
            productSliderDialog.dismiss();
            try {
                if (status == 1) {


                    titleProduct.add("MORE");
                    priceProduct.add("");
                    priceActualProduct.add("");
                    descProduct.add("");
                    idProduct.add("");
                    imageProduct.add(MORE_IMAGE_URL);

                    producthlvAdapter = new HorizontalListViewAdapter(getActivity(), titleProduct, priceProduct, priceActualProduct, descProduct, idProduct, imageProduct);
                    producthlv.setAdapter(producthlvAdapter);

                    producthlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String title = titleProduct.get(position).toString();
                            String price = priceProduct.get(position).toString();
                            String priceActual = priceActualProduct.get(position).toString();
                            String desc = descProduct.get(position).toString();
                            String idd = idProduct.get(position).toString();
                            String image = imageProduct.get(position).toString();

                            if (idd == "") {
                                //  Toast.makeText(MainActivityNew.this,cat_id1,Toast.LENGTH_SHORT).show();
                                sendData.putString("KEY_ID", cat_id1);
                                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getActivity(), idd, Toast.LENGTH_SHORT).show();
                                sendData.putString("CAT_ID", idd);
                                Intent intent = new Intent(getActivity(), VariationsActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);
                            }

//                            sendData.putString("KEY_TITLE", String.valueOf(titleProduct));
//                            sendData.putString("KEY_PRICE", String.valueOf(priceProduct));
//                            sendData.putString("KEY_PRICE_ACTUAL", String.valueOf(priceActualProduct));
//                            sendData.putString("KEY_DESC", String.valueOf(descProduct));
//                            sendData.putString("KEY_IMAGE", String.valueOf(imageProduct));

//                            sendData.putString("KEY_TITLE", title);
//                            sendData.putString("KEY_PRICE", price);
//                            sendData.putString("KEY_PRICE_ACTUAL", priceActual);
//                            sendData.putString("KEY_DESC", desc);
//                            sendData.putString("KEY_IMAGE", image);
//
//
//                            Intent intent=new Intent(MainActivityNew.this,DeatilsViewPagerActivity.class);
//                            intent.putExtras(sendData);
//                            startActivity(intent);


                        }
                    });

                    titleMusthave.add("MORE");
                    priceMusthave.add("");
                    priceActualMusthave.add("");
                    descMusthave.add("");
                    idMusthave.add("");
                    imageMusthave.add(MORE_IMAGE_URL);

                    mustHavehlvAdapter = new HorizontalListViewAdapter(getActivity(), titleMusthave, priceMusthave, priceActualMusthave, descMusthave, idMusthave, imageMusthave);
                    musthavehlv.setAdapter(mustHavehlvAdapter);

                    musthavehlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String title = titleMusthave.get(position).toString();
                            String price = priceMusthave.get(position).toString();
                            String priceActual = priceActualMusthave.get(position).toString();
                            String desc = descMusthave.get(position).toString();
                            String idd = idMusthave.get(position).toString();
                            String image = imageMusthave.get(position).toString();

                            if (idd == "") {
                                // Toast.makeText(MainActivityNew.this,cat_id2,Toast.LENGTH_SHORT).show();
                                sendData.putString("KEY_ID", cat_id2);
                                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);

                            } else {
                                sendData.putString("CAT_ID", idd);
                                Intent intent = new Intent(getActivity(), VariationsActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);
                            }

//                            sendData.putString("KEY_TITLE", title);
//                            sendData.putString("KEY_PRICE", price);
//                            sendData.putString("KEY_PRICE_ACTUAL", priceActual);
//                            sendData.putString("KEY_DESC", desc);
//                            sendData.putString("KEY_IMAGE", image);

//                            Intent intent=new Intent(MainActivityNew.this,DetailsActivity.class);
//                            intent.putExtras(sendData);
//                            startActivity(intent);

                        }
                    });

                    titleBestseller.add("MORE");
                    priceBestSeller.add("");
                    priceActualBestSeller.add("");
                    descBestSeller.add("");
                    idBestSeller.add("");
                    imageBestSeller.add(MORE_IMAGE_URL);


                    bestSellerhlvAdapter = new HorizontalListViewAdapter(getActivity(), titleBestseller, priceBestSeller, priceActualBestSeller, descBestSeller, idBestSeller, imageBestSeller);
                    bestSellerhlv.setAdapter(bestSellerhlvAdapter);

                    bestSellerhlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String title = titleBestseller.get(position).toString();
                            String price = priceBestSeller.get(position).toString();
                            String priceActual = priceActualBestSeller.get(position).toString();
                            String desc = descBestSeller.get(position).toString();
                            String idd = idBestSeller.get(position).toString();
                            String image = imageBestSeller.get(position).toString();

                            if (idd == "") {
                                //  Toast.makeText(MainActivityNew.this,cat_id3,Toast.LENGTH_SHORT).show();
                                sendData.putString("KEY_ID", cat_id3);
                                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);

                            } else {
                                sendData.putString("CAT_ID", idd);
                                Intent intent = new Intent(getActivity(), VariationsActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);
                            }


//                            sendData.putString("KEY_TITLE", title);
//                            sendData.putString("KEY_PRICE", price);
//                            sendData.putString("KEY_PRICE_ACTUAL", priceActual);
//                            sendData.putString("KEY_DESC", desc);
//                            sendData.putString("KEY_IMAGE", image);

//                            Intent intent=new Intent(MainActivityNew.this,DetailsActivity.class);
//                            intent.putExtras(sendData);
//                            startActivity(intent);

                        }
                    });

                    titleSpecialOffers.add("MORE");
                    priceSpecialOffers.add("");
                    priceActualSpecialOffers.add("");
                    descSpecialOffers.add("");
                    idSpecialOffers.add("");
                    imageSpecialOffers.add(MORE_IMAGE_URL);


                    specialOffershlvAdapter = new HorizontalListViewAdapter(getActivity(), titleSpecialOffers, priceSpecialOffers, priceActualSpecialOffers, descSpecialOffers, idSpecialOffers, imageSpecialOffers);
                    specialOffershlv.setAdapter(specialOffershlvAdapter);

                    specialOffershlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String title = titleSpecialOffers.get(position).toString();
                            String price = priceSpecialOffers.get(position).toString();
                            String priceActual = priceActualSpecialOffers.get(position).toString();
                            String desc = descSpecialOffers.get(position).toString();
                            String idd = idSpecialOffers.get(position).toString();
                            String image = imageSpecialOffers.get(position).toString();

                            if (idd == "") {
                                //  Toast.makeText(MainActivityNew.this,cat_id4,Toast.LENGTH_SHORT).show();
                                sendData.putString("KEY_ID", cat_id4);
                                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);

                            } else {
                                sendData.putString("CAT_ID", idd);
                                Intent intent = new Intent(getActivity(), VariationsActivity.class);
                                intent.putExtras(sendData);
                                startActivity(intent);
                            }

//                            sendData.putString("KEY_TITLE", title);
//                            sendData.putString("KEY_PRICE", price);
//                            sendData.putString("KEY_PRICE_ACTUAL", priceActual);
//                            sendData.putString("KEY_DESC", desc);
//                            sendData.putString("KEY_IMAGE", image);

                            //  Intent intent=new Intent(MainActivityNew.this,DetailsActivity.class);
                            //  intent.putExtras(sendData);
                            //  startActivity(intent);

                        }
                    });


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

