package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Activities.MainActivityNew;
import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by Android on 03-01-2017.
 */
public class ConfirmProductFragment extends Fragment implements View.OnClickListener {
    private TextView confirmProductNameTextView, quantityTextView, confirmProductPriceTextView, totalPriceTextView, quantityMultiplyTextView, confirmProductSizeSingleVarTextView, confirmProductFlavorSingleVarTextView;
    private Button decreaseQuatityButton, increaseQuatityButton, placeOrderButton;
    private int quantityInInt, total;
    private ImageView confirmProductImageView;
    private byte[] photo;
    private byte[] accImage;
    private byte[] logoImage;
    private String price, quantity, totalInString, quantityString;
    private JSONObject json_obj;

    public static final String PLACE_ORDER_NO_VARTIATION_URL = "http://shopingguru.in/wp-webapi/placeorder.php";

    public static final String PLACE_ORDER_SINGLE_VARTIATION_URL = "http://shopingguru.in/wp-webapi/placesinglevariationorder.php";

    public static final String PLACE_ORDER_DOUBLE_VARTIATION_URL = "http://shopingguru.in/wp-webapi/placedoublevariationorder.php";

    private SessionManager sessionManager;
    private String userID, productName, productID, imageLink, size, weightAttribute, cakeWeight, flavorAttribute, cakeFlavor, sizeAttr;
    private int flag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_product_list_content_layout, container, false);
        confirmProductNameTextView = (TextView) view.findViewById(R.id.confirmProductNameTextView);
        quantityTextView = (TextView) view.findViewById(R.id.quantityTextView);
        confirmProductPriceTextView = (TextView) view.findViewById(R.id.confirmProductPriceTextView);
        confirmProductImageView = (ImageView) view.findViewById(R.id.confirmProductImageView);
        totalPriceTextView = (TextView) view.findViewById(R.id.totalPriceTextView);
        quantityMultiplyTextView = (TextView) view.findViewById(R.id.quantityMultiplyTextView);
        decreaseQuatityButton = (Button) view.findViewById(R.id.decreaseQuatityButton);
        increaseQuatityButton = (Button) view.findViewById(R.id.increaseQuatityButton);
        placeOrderButton = (Button) view.findViewById(R.id.placeOrderButton);
        confirmProductSizeSingleVarTextView = (TextView) view.findViewById(R.id.confirmProductSizeSingleVarTextView);
        confirmProductFlavorSingleVarTextView = (TextView) view.findViewById(R.id.confirmProductFlavorSingleVarTextView);
        sessionManager = new SessionManager(getActivity());
        userID = sessionManager.getUserDetails().get(SessionManager.KEY_ID);
        flag = getArguments().getInt("flag");
        Toast.makeText(getActivity(), Integer.toString(flag), Toast.LENGTH_SHORT).show();
        if (flag == 1) {
            productID = getArguments().getString("product_id");
            productName = getArguments().getString("name");
            price = getArguments().getString("price");
            imageLink = getArguments().getString("imagelink");
        } else if (flag == 2) {
            confirmProductFlavorSingleVarTextView.setVisibility(View.VISIBLE);
            productID = getArguments().getString("product_id");
            productName = getArguments().getString("name");
            price = getArguments().getString("price");
            imageLink = getArguments().getString("imagelink");
            size = getArguments().getString("size");
            sizeAttr = getArguments().getString("sizeAttribute");
            //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            confirmProductFlavorSingleVarTextView.setText(size);
        } else if (flag == 3) {
            confirmProductFlavorSingleVarTextView.setVisibility(View.VISIBLE);
            confirmProductSizeSingleVarTextView.setVisibility(View.VISIBLE);
            productID = getArguments().getString("product_id");
            productName = getArguments().getString("name");
            price = getArguments().getString("price");
            imageLink = getArguments().getString("imagelink");
            size = getArguments().getString("size");
            weightAttribute = getArguments().getString("attribute1");
            cakeWeight = getArguments().getString("attribute1_value");
            flavorAttribute = getArguments().getString("attribute2");
            cakeFlavor = getArguments().getString("attribute2_value");
            confirmProductFlavorSingleVarTextView.setText(cakeFlavor);
            confirmProductSizeSingleVarTextView.setText(cakeWeight);
        }
        decreaseQuatityButton.setOnClickListener(this);
        increaseQuatityButton.setOnClickListener(this);
        placeOrderButton.setOnClickListener(this);
        quantityString = quantityTextView.getText().toString().trim();
        quantityMultiplyTextView.setText("  *  " + quantityString);
        quantityInInt = Integer.parseInt(quantityString);
        new ImageDownloader().execute(imageLink);
//        Toast.makeText(getActivity(), imageLink, Toast.LENGTH_SHORT).show();
        confirmProductNameTextView.setText(productName);
        confirmProductPriceTextView.setText(price);
        quantityMultiplyTextView.setText("  *  " + "1\n" + "  +  50  \n    (shipping)");
        totalPriceTextView.setText(Integer.toString(Integer.parseInt(price) + 50));
        totalInString = totalPriceTextView.getText().toString().trim();
        quantity = quantityTextView.getText().toString().trim();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.increaseQuatityButton:
                quantity = Integer.toString(quantityInInt = quantityInInt + 1);
                quantityTextView.setText(quantity);
                quantityMultiplyTextView.setText("  *  " + quantity + "\n  +50   \n    (shipping)");
                price = totalPriceTextView.getText().toString();
                if (quantityInInt == 1) {
                    total = Integer.parseInt(price) + Integer.parseInt(confirmProductPriceTextView.getText().toString()) + 50;
                } else {
                    total = Integer.parseInt(price) + Integer.parseInt(confirmProductPriceTextView.getText().toString());
                }
                totalInString = Integer.toString(total);
                totalPriceTextView.setText(totalInString);
                break;
            case R.id.decreaseQuatityButton:
                if (quantityInInt <= 1) {
                    Toast.makeText(getActivity(), "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                    totalPriceTextView.setText(Integer.toString(Integer.parseInt(confirmProductPriceTextView.getText().toString()) + 50));
                } else {
                    quantity = Integer.toString(quantityInInt = quantityInInt - 1);
                    quantityTextView.setText(quantity);
                    quantityMultiplyTextView.setText("  *  " + quantity + "\n  +50  \n    (shipping)");
                    price = totalPriceTextView.getText().toString().trim();

                    if (quantityInInt == 2) {
                        totalPriceTextView.setText(Integer.toString(Integer.parseInt(price) + 50));
                    } else {
                        total = Integer.parseInt(totalPriceTextView.getText().toString()) - Integer.parseInt(confirmProductPriceTextView.getText().toString());
                    }
                    totalInString = Integer.toString(total);
                    totalPriceTextView.setText(totalInString);
                }
                break;
            case R.id.placeOrderButton:
                if (flag == 1) {
                    placeOrderWithoutVariation(userID, totalInString, productName, quantity, productID);
//                    Toast.makeText(getActivity(), userID + totalInString + productName + quantity + productID, Toast.LENGTH_SHORT).show();
                } else if (flag == 2) {
                    placeOrderSingleVariation(userID, totalInString, productName, quantity, productID, sizeAttr, size);
//                    Toast.makeText(getActivity(), size, Toast.LENGTH_SHORT).show();
                } else if (flag == 3) {
                    placeOrderDoubleVariation(userID, totalInString, productName, quantity, productID, weightAttribute, cakeWeight, flavorAttribute, cakeFlavor);
//                    Toast.makeText(getActivity(), userID + " " + totalInString + " " + productName + " " + quantity + " " + productID + " " + weightAttribute + " " + cakeWeight + " " + flavorAttribute + " " + cakeFlavor, Toast.LENGTH_SHORT).show();
                }
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
            progressDialog = ProgressDialog.show(getActivity(), "Wait", "Downloading Image");
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
//            Toast.makeText(getActivity(), "downloaded successfully....!", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getLogoImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            photo = baf.toByteArray();
            System.out.println("photo length" + photo);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            confirmProductImageView.setImageBitmap(theImage);
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return accImage;
    }

    private void placeOrderWithoutVariation(String userId, String totalPrice, String productName, String productQuantity, String productID) {

        class PlaceOrderWithoutVariation extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
                loading.setMessage("Loading...Please wait!");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                loading.dismiss();
//                try {
//                    json_obj = new JSONObject(s);
//                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                    if (json_obj.has("status") || json_obj.has("1")) {
//
//                        JSONArray jsonArray = new JSONArray(s);
//                        JSONObject jsonObject = new JSONObject(s);
//                        JSONObject userJsonObject = jsonObject.getJSONObject("id");
//                        int id = jsonObject.getInt("id");
//                        int idInt = Integer.parseInt(id);
//                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if (s.contains("Order Placed")) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivityNew.class));
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("id", params[0]);
                data.put("order_total", params[1]);
                data.put("order_item_name", params[2]);
                data.put("qty", params[3]);
                data.put("product_id", params[4]);
                String result = ruc.sendPostRequest(PLACE_ORDER_NO_VARTIATION_URL, data);
                return result;
            }
        }
        PlaceOrderWithoutVariation loginUser = new PlaceOrderWithoutVariation();
        loginUser.execute(userId, totalPrice, productName, productQuantity, productID);
    }

    private void placeOrderSingleVariation(String userId, String totalPrice, String productName, String productQuantity, String productID, String attribute, String size) {

        class PlaceOrderSingleVariation extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
                loading.setMessage("Loading...Please wait!");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                loading.dismiss();
//                try {
//                    json_obj = new JSONObject(s);
//                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                    if (json_obj.has("status") || json_obj.has("1")) {
//
//                        JSONArray jsonArray = new JSONArray(s);
//                        JSONObject jsonObject = new JSONObject(s);
//                        JSONObject userJsonObject = jsonObject.getJSONObject("id");
//                        int id = jsonObject.getInt("id");
//                        int idInt = Integer.parseInt(id);
//                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if (s.contains("Order Placed")) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivityNew.class));
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("id", params[0]);
                data.put("order_total", params[1]);
                data.put("order_item_name", params[2]);
                data.put("qty", params[3]);
                data.put("product_id", params[4]);


                data.put("attribute", params[5]);
                data.put("attribute_value", params[6]);


                String result = ruc.sendPostRequest(PLACE_ORDER_SINGLE_VARTIATION_URL, data);

                return result;
            }
        }
        PlaceOrderSingleVariation loginUser = new PlaceOrderSingleVariation();
        loginUser.execute(userId, totalPrice, productName, productQuantity, productID, attribute, size);
    }


    private void placeOrderDoubleVariation(String userId, String totalPrice, String productName, String productQuantity, String productID, String attribute1, String attribute1_value, String attribute2, String attribute2_value) {

        class PlaceOrderDoubleVariation extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
                loading.setMessage("Loading...Please wait!");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                loading.dismiss();
//                try {
//                    json_obj = new JSONObject(s);
//                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                    if (json_obj.has("status") || json_obj.has("1")) {
//
//                        JSONArray jsonArray = new JSONArray(s);
//                        JSONObject jsonObject = new JSONObject(s);
//                        JSONObject userJsonObject = jsonObject.getJSONObject("id");
//                        int id = jsonObject.getInt("id");
//                        int idInt = Integer.parseInt(id);
//                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if (s.contains("Order Placed")) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivityNew.class));
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("id", params[0]);
                data.put("order_total", params[1]);
                data.put("order_item_name", params[2]);
                data.put("qty", params[3]);
                data.put("product_id", params[4]);
                data.put("attribute1", params[5]);
                data.put("attribute1_value", params[6]);
                data.put("attribute2", params[7]);
                data.put("attribute2_value", params[8]);

                String result = ruc.sendPostRequest(PLACE_ORDER_DOUBLE_VARTIATION_URL, data);

                return result;
            }
        }


        PlaceOrderDoubleVariation loginUser = new PlaceOrderDoubleVariation();
        loginUser.execute(userId, totalPrice, productName, productQuantity, productID, attribute1, attribute1_value, attribute2, attribute2_value);
    }


}