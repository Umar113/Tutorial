package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.ViewPagerAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.ViewPagerData;
import com.example.android.shopinggururegister.Parsers.JSONParser;
import com.example.android.shopinggururegister.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by anjal on 07/12/2016.
 */
public class VariationsActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_USERS = "data";
    private static final String TAG_IMAGES = "images";
    private static final String TAG_SPRICE = "sprice";
    private static final String TAG_EPRICE = "eprice";
    private static final String TAG_VARIATION_SIZE = "variation1";

    JSONParser jParser = new JSONParser();
    int status = 0, statusForVariation = 0;
    String attribute = null, attribute2 = null;
    int spriceInt = 0, epriceInt = 0;

    int rangeSingleVar = 0;

    int price = 0;
    String stockstatus = null;
    JSONArray variations = null, imagesJsonArray = null, sizeJsonArray = null;
    private String UPLOAD_URL = "http://shopingguru.in/wp-webapi/product_details.php";
    private String SINGLE_VARIATION_URL = "http://shopingguru.in/wp-webapi/single_variation_price.php";
    private String DOUBLE_VARIATION_URL = "http://shopingguru.in/wp-webapi/double_variation_price.php";

    private String STOCK_WITHOUT_VARIATION_URL = "http://shopingguru.in/wp-webapi/stockstatus0.php";
    private String STOCK_WITH_SINGLE_VARIATION_URL = "http://shopingguru.in/wp-webapi/stockstatus1.php";

    private String STOCK_WITH_DOUBLE_VARIATION_URL = "http://shopingguru.in/wp-webapi/stockstatus2.php";

    String imageLink;
    Bundle gotData;
    String gotId;
    ArrayList<ViewPagerData> viewpagearr = new ArrayList<ViewPagerData>();
    AutoScrollViewPager viewPager;
    ViewPagerAdapter adapter;

    String product_id = "";
    String product_name = "";
    String product_desc = "";
    String product_actual_price = "";
    String product_selling_price = "";

    TextView nameTextView, actualPriceTextView, sellingPriceTextView, descTextView;
    TextView availabilityWithoutVariationTextView;

    TextView nameVariation1TextView, descriptionVariation1TextView;
    TextView range1TextView, range2TextView;
    Spinner variationSizeSpinner;
    List<String> sizeVariationList = new ArrayList<String>();
    ArrayAdapter<String> sizeAdapter;
    String dataFromSizeSpinner;
    TextView priceSingleVariationTextView;
    TextView availabilityWithSingleVariationTextView;

    TextView nameVariation2TextView, descriptionVariation2TextView, range1VariationDoubleTextView, range2VariationDoubleTextView;
    Spinner variationWeightSpinner, variationFlavoursSpinner;
    TextView priceDoubleVariationTextView;
    JSONArray weightJsonArray = null, flavourJsonArray = null;

    List<String> weightVariationList = new ArrayList<String>();
    ArrayAdapter<String> weightAdapter;
    String dataFromWeightSpinner;

    List<String> flavourVariationList = new ArrayList<String>();
    ArrayAdapter<String> flavourAdapter;
    String dataFromFlavourSpinner, sizeAttribute;
    TextView availabilityWithDoubleVariationTextView;
    CircleIndicator indicator;

    private Button addToCartButton, buyButton;

    CardView withDoubleVariationCardview, withSingleVariationCardview, withoutVariationCardview;

    ProductsListDb productsListDb;

    byte[] photo, accImage, logoImage;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variations);
        productsListDb = new ProductsListDb(VariationsActivity.this);

        indicator = (CircleIndicator) findViewById(R.id.indicator);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        actualPriceTextView = (TextView) findViewById(R.id.actualpriceTextView);
        sellingPriceTextView = (TextView) findViewById(R.id.sellingPriceTextView);
        descTextView = (TextView) findViewById(R.id.descriptionTextView);

        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        buyButton = (Button) findViewById(R.id.buyButton);
        buyButton.setOnClickListener(this);
        addToCartButton.setOnClickListener(this);
        nameVariation1TextView = (TextView) findViewById(R.id.nameVariation1TextView);
        descriptionVariation1TextView = (TextView) findViewById(R.id.descriptionVariation1TextView);

        range1TextView = (TextView) findViewById(R.id.range1TextView);
        range2TextView = (TextView) findViewById(R.id.range2TextView);

        variationSizeSpinner = (Spinner) findViewById(R.id.variationSizeSpinner);
        priceSingleVariationTextView = (TextView) findViewById(R.id.priceSingleVariationTextView);
        availabilityWithSingleVariationTextView = (TextView) findViewById(R.id.availabilityWithSingleVariationTextView);

        nameVariation2TextView = (TextView) findViewById(R.id.nameVariation2TextView);
        descriptionVariation2TextView = (TextView) findViewById(R.id.descriptionVariation2TextView);
        range1VariationDoubleTextView = (TextView) findViewById(R.id.range1VariationDoubleTextView);
        range2VariationDoubleTextView = (TextView) findViewById(R.id.range2VariationDoubleTextView);
        variationWeightSpinner = (Spinner) findViewById(R.id.variationWeightSpinner);
        variationFlavoursSpinner = (Spinner) findViewById(R.id.variationFlavoursSpinner);

        priceDoubleVariationTextView = (TextView) findViewById(R.id.priceDoubleVariationTextView);
        availabilityWithDoubleVariationTextView = (TextView) findViewById(R.id.availabilityWithDoubleVariationTextView);

        withDoubleVariationCardview = (CardView) findViewById(R.id.withDoubleVariationCardview);
        withSingleVariationCardview = (CardView) findViewById(R.id.withSingleVariationCardview);
        withoutVariationCardview = (CardView) findViewById(R.id.withoutVariationCardview);

        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getApplicationContext(), viewpagearr);

        gotData = getIntent().getExtras();
        gotId = gotData.getString("CAT_ID");

        new VariationsTask().execute();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.buyButton:
                Intent buyIntent = new Intent(VariationsActivity.this, BuyNowActivity.class);
                Bundle bundle = getIntent().getExtras();
                if (statusForVariation == 1) {
                    String pid = bundle.getString("CAT_ID");
                    bundle.putInt("flag", 1);
                    bundle.putString("product_id", pid);
                    bundle.putString("name", product_name);
                    bundle.putString("price", product_selling_price);
                    bundle.putString("imagelink", imageLink);
                    buyIntent.putExtra("key", bundle);
                } else if (statusForVariation == 2) {
                    if (dataFromSizeSpinner.isEmpty()) {
                        bundle.putInt("flag", 2);
                        String pid = bundle.getString("CAT_ID");
                        bundle.putString("product_id", pid);
                        bundle.putString("name", product_name);
                        bundle.putString("price", Integer.toString(rangeSingleVar));
                        Toast.makeText(VariationsActivity.this, Integer.toString(rangeSingleVar), Toast.LENGTH_SHORT).show();
                        bundle.putString("imagelink", imageLink);
                        bundle.putString("sizeAttribute", sizeAttribute);
                        bundle.putString("size", "Free Size");
                        buyIntent.putExtra("key", bundle);
                        Toast.makeText(VariationsActivity.this, pid + "\n" + product_name + "\n" + String.valueOf(rangeSingleVar) +
                                "\n" + imageLink, Toast.LENGTH_SHORT).show();
                    } else {
                        bundle.putInt("flag", 2);
                        String pid = bundle.getString("CAT_ID");
                        bundle.putString("product_id", pid);
                        bundle.putString("name", product_name);
                        bundle.putString("price", Integer.toString(rangeSingleVar));
                        Toast.makeText(VariationsActivity.this, priceSingleVariationTextView.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        bundle.putString("imagelink", imageLink);
                        bundle.putString("sizeAttribute", sizeAttribute);

                        bundle.putString("size", dataFromSizeSpinner);
                        buyIntent.putExtra("key", bundle);
                    }
                } else if (statusForVariation == 3) {
                    bundle.putInt("flag", 3);
                    String pid = bundle.getString("CAT_ID");
                    bundle.putString("product_id", pid);
                    bundle.putString("name", product_name);
                    bundle.putString("price", Integer.toString(price));
                    bundle.putString("imagelink", imageLink);
                    bundle.putString("attribute1", attribute);
                    bundle.putString("attribute1_value", dataFromWeightSpinner);
                    bundle.putString("attribute2", attribute2);
                    bundle.putString("attribute2_value", dataFromFlavourSpinner);
                    buyIntent.putExtra("key", bundle);
                }
                finish();
                startActivity(buyIntent);
                break;
            case R.id.addToCartButton:
                new ImageDownloader().execute(imageLink);

                break;
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... param) {


            logoImage = getLogoImage(param[0]);

            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(VariationsActivity.this, "Wait", "Downloading Image");

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            Log.e("data:>>>>> ", product_id + "\n" + product_name + "\n" + product_selling_price + "\n" + photo);
            productsListDb.insertProductIntoCart(product_id, product_name, product_selling_price, photo);
            Toast.makeText(VariationsActivity.this, "added successfully....!", Toast.LENGTH_SHORT).show();

        }

    }

    private byte[] getLogoImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            System.out.println("11111");
            InputStream is = ucon.getInputStream();
            System.out.println("12121");

            BufferedInputStream bis = new BufferedInputStream(is);
            System.out.println("22222");

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            System.out.println("23333");

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);

            }
            photo = baf.toByteArray();
            System.out.println("photo length" + photo);

        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return accImage;
    }

    class VariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VariationsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("product_id", gotId));
            JSONObject json = jParser.makeHttpRequest(UPLOAD_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());
            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                statusForVariation = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));
                if (status == 1) {
                    variations = json.getJSONArray(TAG_USERS);
                    for (int i = 0; i < variations.length(); i++) {
                        JSONObject c = variations.getJSONObject(i);

                        product_id = c.getString("product_id");
                        product_name = c.getString("product_name");
                        product_desc = c.getString("product_desc");
                        product_actual_price = c.getString("product_actual_price");
                        product_selling_price = c.getString("product_selling_price");

                        Log.d("product_id:>>>>>>>>> ", product_id);
                        Log.d("product_name:>>>>>>>>> ", product_name);
                        Log.d("product_desc", product_desc);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_selling_price", product_selling_price);

                        imagesJsonArray = json.getJSONArray(TAG_IMAGES);

                        for (int j = 0; j < imagesJsonArray.length(); j++) {

                            JSONObject cimage = imagesJsonArray.getJSONObject(0);
                            imageLink = cimage.getString("product_image");

                            JSONObject c1 = imagesJsonArray.getJSONObject(j);
                            String product_image = c1.getString("product_image");
                            Log.d("product_image", product_image);
                            viewpagearr.add(new ViewPagerData(product_image));
                        }
                    }
                } else if (status == 2) {
                    Log.d("status", "2");
                    variations = json.getJSONArray(TAG_USERS);
                    for (int i = 0; i < variations.length(); i++) {
                        JSONObject c = variations.getJSONObject(i);

//                        JSONObject cimage = imagesJsonArray.getJSONObject(0);
//                        imageLink = cimage.getString("product_image");

                        product_id = c.getString("product_id");
                        product_name = c.getString("product_name");
                        product_desc = c.getString("product_desc");
                        product_actual_price = c.getString("product_actual_price");
                        product_selling_price = c.getString("product_selling_price");

                        Log.d("product_id:>>>>>>>>> ", product_id);
                        Log.d("product_name:>>>>>>>>> ", product_name);
                        Log.d("product_desc", product_desc);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_selling_price", product_selling_price);

                        imagesJsonArray = json.getJSONArray(TAG_IMAGES);
                        for (int j = 0; j < imagesJsonArray.length(); j++) {
                            JSONObject cimage = imagesJsonArray.getJSONObject(0);
                            imageLink = cimage.getString("product_image");

                            JSONObject c1 = imagesJsonArray.getJSONObject(j);
                            String product_image = c1.getString("product_image");
                            Log.d("product_image", product_image);

                            viewpagearr.add(new ViewPagerData(product_image));
                        }
                        spriceInt = json.getInt(TAG_SPRICE);
                        Log.d("sprice:>>>>>>>>> ", String.valueOf(spriceInt));
                        rangeSingleVar = json.getInt(TAG_SPRICE);

                        epriceInt = json.getInt(TAG_EPRICE);
                        Log.d("eprice:>>>>>>>>> ", String.valueOf(epriceInt));

                        sizeJsonArray = json.getJSONArray(TAG_VARIATION_SIZE);
                        for (int j = 0; j < sizeJsonArray.length(); j++) {

                            JSONObject c2 = sizeJsonArray.getJSONObject(j);

//                              String variation=c2.getString("variation");
//                              Log.d("variation",variation);

                            sizeVariationList.add(c2.getString("variation"));

                        }
                        sizeAttribute = json.getString("attribute1");
                        Log.d("attribute:>>>>>>>>> ", String.valueOf(attribute));
                    }

                } else if (status == 3) {

                    variations = json.getJSONArray(TAG_USERS);
                    for (int i = 0; i < variations.length(); i++) {
                        JSONObject c = variations.getJSONObject(i);

                        product_id = c.getString("product_id");
                        product_name = c.getString("product_name");
                        product_desc = c.getString("product_desc");
                        product_actual_price = c.getString("product_actual_price");
                        product_selling_price = c.getString("product_selling_price");

                        Log.d("product_id:>>>>>>>>> ", product_id);
                        Log.d("product_name:>>>>>>>>> ", product_name);
                        Log.d("product_desc", product_desc);
                        Log.d("product_actual_price", product_actual_price);
                        Log.d("product_selling_price", product_selling_price);

                        imagesJsonArray = json.getJSONArray(TAG_IMAGES);
                        for (int j = 0; j < imagesJsonArray.length(); j++) {

                            JSONObject cimage = imagesJsonArray.getJSONObject(0);
                            imageLink = cimage.getString("product_image");

                            JSONObject c1 = imagesJsonArray.getJSONObject(j);

                            String product_image = c1.getString("product_image");
                            Log.d("product_image", product_image);

                            viewpagearr.add(new ViewPagerData(product_image));
                        }

                        spriceInt = json.getInt(TAG_SPRICE);
                        Log.d("sprice:>>>>>>>>> ", String.valueOf(spriceInt));

                        epriceInt = json.getInt(TAG_EPRICE);
                        Log.d("eprice:>>>>>>>>> ", String.valueOf(epriceInt));

                        attribute = json.getString("attribute1");
                        Log.d("attribute:>>>>>>>>> ", String.valueOf(attribute));

                        attribute2 = json.getString("attribute2");
                        Log.d("attribute:>>>>>>>>> ", String.valueOf(attribute2));

                        weightJsonArray = json.getJSONArray("variation1");
                        for (int j = 0; j < weightJsonArray.length(); j++) {

                            JSONObject cweight = weightJsonArray.getJSONObject(j);

//                              String variation=c2.getString("variation");
//                              Log.d("variation",variation);
                            weightVariationList.add(cweight.getString("variation"));
                        }
                        flavourJsonArray = json.getJSONArray("variation2");
                        for (int j = 0; j < flavourJsonArray.length(); j++) {
                            JSONObject cflavour = flavourJsonArray.getJSONObject(j);

//                              String variation=c2.getString("variation");
//                              Log.d("variation",variation);
                            flavourVariationList.add(cflavour.getString("variation"));
                        }
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

                    withoutVariationCardview.setVisibility(View.VISIBLE);
                    withSingleVariationCardview.setVisibility(View.GONE);
                    withDoubleVariationCardview.setVisibility(View.GONE);

                    viewPager.setAdapter(adapter);
                    viewPager.setInterval(2000);
                    viewPager.startAutoScroll();
                    indicator.setViewPager(viewPager);

                    nameTextView.setText(product_name);
                    actualPriceTextView.setText(product_actual_price);
                    sellingPriceTextView.setText(product_selling_price);


                    if (product_desc.contentEquals("null")) {
                        descTextView.setText("Available at best price");
                    } else {
                        descTextView.setText(product_desc);
                    }
                    new StockwithoutVariationsTask().execute();

                } else if (status == 2) {
                    withoutVariationCardview.setVisibility(View.GONE);
                    withSingleVariationCardview.setVisibility(View.VISIBLE);
                    withDoubleVariationCardview.setVisibility(View.GONE);
                    viewPager.setAdapter(adapter);
                    viewPager.setInterval(2000);
                    viewPager.startAutoScroll();
                    indicator.setViewPager(viewPager);

                    nameVariation1TextView.setText(product_name);
                    descriptionVariation1TextView.setText(product_desc);

                    range1TextView.setText(getResources().getString(R.string.indian_rupee) + String.valueOf(spriceInt));
                    range2TextView.setText(getResources().getString(R.string.indian_rupee) + String.valueOf(epriceInt));

                    if (sizeVariationList.size() == 1) {
                        //Toast.makeText(getApplicationContext(), "spinner empty", Toast.LENGTH_SHORT).show();
                        ArrayList<String> emptyArray = new ArrayList<String>();
                        emptyArray.add("Free size");
                        sizeAdapter = new ArrayAdapter<String>(VariationsActivity.this, android.R.layout.simple_list_item_1, emptyArray);
                        variationSizeSpinner.setAdapter(sizeAdapter);
                    } else if (sizeVariationList.size() > 1) {

                        sizeAdapter = new ArrayAdapter<String>(VariationsActivity.this, android.R.layout.simple_list_item_1, sizeVariationList);
                        variationSizeSpinner.setAdapter(sizeAdapter);
                    }

//                    sizeAdapter = new ArrayAdapter<String>(VariationsActivity.this, android.R.layout.simple_spinner_item, sizeVariationList);
//                    variationSizeSpinner.setAdapter(sizeAdapter);
                    variationSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//                            dataFromSizeSpinner = parent.getItemAtPosition(position).toString();
//                            // Showing selected spinner item
//                            // Toast.makeText(parent.getContext(), product_id + item, Toast.LENGTH_LONG).show();
//                            new SingleVariationsTask().execute();
//                            new StockwithSingleVariationsTask().execute();

                            dataFromSizeSpinner = parent.getItemAtPosition(position).toString();

                            if (dataFromSizeSpinner.contentEquals("Free size")) {
                                priceSingleVariationTextView.setText(range1TextView.getText().toString());
                                availabilityWithSingleVariationTextView.setText("In Stock");
                                availabilityWithSingleVariationTextView.setTextColor(Color.parseColor("#8BC348"));

                            } else {
                                new SingleVariationsTask().execute();
                                new StockwithSingleVariationsTask().execute();
                            }
                            // Showing selected spinner item
                            // Toast.makeText(parent.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
//                            new SingleVariationsTask().execute();
//                            new StockwithSingleVariationsTask().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } else if (status == 3) {

                    withoutVariationCardview.setVisibility(View.GONE);
                    withSingleVariationCardview.setVisibility(View.GONE);
                    withDoubleVariationCardview.setVisibility(View.VISIBLE);


                    viewPager.setAdapter(adapter);
                    viewPager.setInterval(2000);
                    viewPager.startAutoScroll();
                    indicator.setViewPager(viewPager);

                    nameVariation2TextView.setText(product_name);
                    descriptionVariation2TextView.setText(product_desc);

                    range1VariationDoubleTextView.setText(getResources().getString(R.string.indian_rupee) + String.valueOf(spriceInt));
                    range2VariationDoubleTextView.setText(getResources().getString(R.string.indian_rupee) + String.valueOf(epriceInt));


                    if (weightVariationList.size() == 1 || weightVariationList.size() == 0) {
                        //Toast.makeText(getApplicationContext(), "spinner empty", Toast.LENGTH_SHORT).show();
                        ArrayList<String> emptyArray = new ArrayList<String>();
                        emptyArray.add("Weight not available");
                        weightAdapter = new ArrayAdapter<String>(VariationsActivity.this, android.R.layout.simple_list_item_1, emptyArray);
                        variationWeightSpinner.setAdapter(weightAdapter);
                    } else if (weightVariationList.size() > 1) {

                        weightAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, weightVariationList);
                        variationWeightSpinner.setAdapter(weightAdapter);
                    }


//                    weightAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, weightVariationList);
//                    variationWeightSpinner.setAdapter(weightAdapter);

                    variationWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                            dataFromWeightSpinner = parent.getItemAtPosition(position).toString();


                            if (flavourVariationList.size() == 1 || flavourVariationList.size() == 0) {
                                //Toast.makeText(getApplicationContext(), "spinner empty", Toast.LENGTH_SHORT).show();
                                ArrayList<String> emptyArray = new ArrayList<String>();
                                emptyArray.add("Flavours not available");
                                flavourAdapter = new ArrayAdapter<String>(VariationsActivity.this, android.R.layout.simple_list_item_1, emptyArray);
                                variationFlavoursSpinner.setAdapter(flavourAdapter);
                            } else if (flavourVariationList.size() > 1) {

                                flavourAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, flavourVariationList);
                                variationFlavoursSpinner.setAdapter(flavourAdapter);
                            }


//                            flavourAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, flavourVariationList);
//                            variationFlavoursSpinner.setAdapter(flavourAdapter);

                            variationFlavoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                                    dataFromFlavourSpinner = parent.getItemAtPosition(position).toString();


                                    if (dataFromWeightSpinner.contentEquals("Weight not available") || dataFromFlavourSpinner.contentEquals("Flavours not available")) {
                                        priceDoubleVariationTextView.setText(range1VariationDoubleTextView.getText().toString());
                                        availabilityWithDoubleVariationTextView.setText("In Stock");
                                        availabilityWithDoubleVariationTextView.setTextColor(Color.parseColor("#8BC348"));
                                    } else {
                                        new DoubleVariationsTask().execute();
                                        new StockwithDoubleVariationsTask().execute();
                                    }

                                }

                                //
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

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


    class SingleVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VariationsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            params.add(new BasicNameValuePair("variation", dataFromSizeSpinner));
            JSONObject json = jParser.makeHttpRequest(SINGLE_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {

                    // products found
                    // Getting Array of Products
                    price = json.getInt("price");
                    Log.d("price:>>>>>>>>> ", String.valueOf(price));


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
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    priceSingleVariationTextView.setText(String.valueOf(price));
                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }
    }


    class DoubleVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VariationsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            params.add(new BasicNameValuePair("variation1", dataFromWeightSpinner));
            params.add(new BasicNameValuePair("variation2", dataFromFlavourSpinner));
            JSONObject json = jParser.makeHttpRequest(DOUBLE_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {

                    // products found
                    // Getting Array of Products
                    price = json.getInt("price");
                    Log.d("price:>>>>>>>>> ", String.valueOf(price));


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
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    priceDoubleVariationTextView.setText(String.valueOf(price));
                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }

    }


    class DoubleFlavourVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VariationsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            params.add(new BasicNameValuePair("variation1", dataFromWeightSpinner));
            params.add(new BasicNameValuePair("variation2", dataFromFlavourSpinner));
            JSONObject json = jParser.makeHttpRequest(DOUBLE_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {

                    // products found
                    // Getting Array of Products
                    price = json.getInt("price");
                    Log.d("price:>>>>>>>>> ", String.valueOf(price));


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
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    priceDoubleVariationTextView.setText(String.valueOf(price));
                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }

    }


    class StockwithoutVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VariationsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            JSONObject json = jParser.makeHttpRequest(STOCK_WITHOUT_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {
                    // products found
                    // Getting Array of Products
                    stockstatus = json.getString("stock_status");
                    Log.d("stockstatus:>>>>>>>>> ", String.valueOf(stockstatus));


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
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    String stockcheck = String.valueOf(stockstatus);

                    if (stockcheck.equals("instock")) {
                        availabilityWithoutVariationTextView.setText("In Stock");
                        availabilityWithoutVariationTextView.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        availabilityWithoutVariationTextView.setText("Out of Stock");
                        availabilityWithoutVariationTextView.setTextColor(getResources().getColor(R.color.red));
                    }

                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }
    }


    ProgressDialog pDialog1;

    class StockwithSingleVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(VariationsActivity.this);
            pDialog1.setMessage("Please wait...");
            pDialog1.setIndeterminate(false);
            pDialog1.setCancelable(false);
            pDialog1.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            params.add(new BasicNameValuePair("variation", dataFromSizeSpinner));
            JSONObject json = jParser.makeHttpRequest(STOCK_WITH_SINGLE_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {
                    // products found
                    // Getting Array of Products
                    stockstatus = json.getString("stock_status");
                    Log.d("stockstatus:>>>>>>>>> ", String.valueOf(stockstatus));


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
            pDialog1.dismiss();
            try {

                if (status == 1) {
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    String stockcheck = String.valueOf(stockstatus);

                    if (stockcheck.equals("instock")) {
                        availabilityWithSingleVariationTextView.setText("In Stock");
                        availabilityWithSingleVariationTextView.setTextColor(Color.parseColor("#8BC348"));
                    } else {
                        availabilityWithSingleVariationTextView.setText("Out of Stock");
                        availabilityWithSingleVariationTextView.setTextColor(Color.parseColor("#FF9800"));
                    }

                } else {
                    //Toast.makeText(getApplicationContext(),"Wrong credentials..", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", "Error");
            }

        }
    }


    ProgressDialog pDialog2;

    class StockwithDoubleVariationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(VariationsActivity.this);
            pDialog2.setMessage("Please wait...");
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(false);
            pDialog2.show();

        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("product_id", product_id));
            params.add(new BasicNameValuePair("variation1", dataFromWeightSpinner));
            params.add(new BasicNameValuePair("variation2", dataFromFlavourSpinner));
            JSONObject json = jParser.makeHttpRequest(STOCK_WITH_DOUBLE_VARIATION_URL, "GET", params);
            Log.d("All Products:>>>>>>>>> ", json.toString());


            try {
                // Checking for SUCCESS TAG
                status = json.getInt(TAG_SUCCESS);

                Log.d("Response:>>>>>>>>> ", String.valueOf(status));

                if (status == 1) {
                    // products found
                    // Getting Array of Products
                    stockstatus = json.getString("stock_status");
                    Log.d("stockstatus:>>>>>>>>> ", String.valueOf(stockstatus));
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
            pDialog2.dismiss();
            try {

                if (status == 1) {
//                    Toast.makeText(getApplicationContext(), "Successfully Taken", Toast.LENGTH_SHORT).show();
                    String stockcheck = String.valueOf(stockstatus);

                    if (stockcheck.equals("instock")) {
                        availabilityWithDoubleVariationTextView.setText("In Stock");
                        availabilityWithDoubleVariationTextView.setTextColor(Color.parseColor("#8BC348"));
                    } else {
                        availabilityWithDoubleVariationTextView.setText("Out of Stock");
                        availabilityWithDoubleVariationTextView.setTextColor(Color.parseColor("#FF9800"));
                    }

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

