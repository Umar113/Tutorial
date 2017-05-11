package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Batman on 25-01-2017.
 */
public class OrderdetailsActivity extends AppCompatActivity {

    private TextView orderIdDetailsTextView, orderItemNameDetailsTextView, orderQuantityDetailsTextView, orderVar1DetailsTextView, orderVar1ValueDetailsTextView, orderVar2DetailsTextView, orderVar2ValueDetailsTextView, orderTotalDetailsTextView, orderDateDetailsTextView, orderStatusDetailsTextView;
    private JSONObject json_obj;
    public static final String ORDER_DETAILS_URL = "http://shopingguru.in/wp-webapi/orderdetails.php";
    String id, orderDate, orderTotal, orderStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_acitivity_layout);
        orderIdDetailsTextView = (TextView) findViewById(R.id.orderIdDetailsTextView);
        orderItemNameDetailsTextView = (TextView) findViewById(R.id.orderItemNameDetailsTextView);
        orderQuantityDetailsTextView = (TextView) findViewById(R.id.orderQuantityDetailsTextView);
        orderVar1DetailsTextView = (TextView) findViewById(R.id.orderVar1DetailsTextView);
        orderVar1ValueDetailsTextView = (TextView) findViewById(R.id.orderVar1ValueDetailsTextView);
        orderVar2DetailsTextView = (TextView) findViewById(R.id.orderVar2DetailsTextView);
        orderVar2ValueDetailsTextView = (TextView) findViewById(R.id.orderVar2ValueDetailsTextView);
        orderTotalDetailsTextView = (TextView) findViewById(R.id.orderTotalDetailsTextView);
        orderDateDetailsTextView = (TextView) findViewById(R.id.orderDateDetailsTextView);
        orderStatusDetailsTextView = (TextView) findViewById(R.id.orderStatusDetailsTextView);

        Bundle gotData = getIntent().getExtras();
        id = gotData.getString("KEY_ID");
        orderTotal = gotData.getString("total");
        orderStatus = gotData.getString("order_status");
        orderDate = gotData.getString("order_date");
//        Toast.makeText(OrderdetailsActivity.this, id, Toast.LENGTH_SHORT).show();
        orderDetails(id);
    }

    private void orderDetails(String userId) {
        class OrderDetails extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(OrderdetailsActivity.this);
                loading.setMessage("Loading...Please wait!");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("order_id", params[0]);
                String result = ruc.sendPostRequest(ORDER_DETAILS_URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                orderTotalDetailsTextView.setText(orderTotal);
                orderDateDetailsTextView.setText(orderDate);
                orderStatusDetailsTextView.setText(orderStatus);
                try {
                    json_obj = new JSONObject(s);
                    int status = json_obj.getInt("status");

                    Toast.makeText(OrderdetailsActivity.this, s, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
//                        Toast.makeText(OrderdetailsActivity.this, Integer.toString(status), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = json_obj.getJSONArray("data");

                        JSONObject order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_id = order_id_json_obj.getString("order_id");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_item_name = order_id_json_obj.getString("order_item_name");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String quantity = order_id_json_obj.getString("_qty");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String product_id = order_id_json_obj.getString("_product_id");

//                        orderDetailsStatusTextView.setText(order_id + "\n " + order_item_name + "\n " + quantity + " \n" + product_id);
                        orderItemNameDetailsTextView.setText(order_item_name);
                        orderQuantityDetailsTextView.setText(quantity);
                    }
                    if (status == 2) {
//                        Toast.makeText(OrderdetailsActivity.this, Integer.toString(status), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = json_obj.getJSONArray("data");

                        JSONObject order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_id = order_id_json_obj.getString("order_id");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_item_name = order_id_json_obj.getString("order_item_name");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String quantity = order_id_json_obj.getString("_qty");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String product_id = order_id_json_obj.getString("_product_id");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation = order_id_json_obj.getString("variation");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation_value = order_id_json_obj.getString("variation_value");

                        orderVar1DetailsTextView.setVisibility(View.VISIBLE);
                        orderVar1ValueDetailsTextView.setVisibility(View.VISIBLE);

//                        orderDetailsStatusTextView.setText(order_id + "\n " + order_item_name + "\n " + quantity + " \n" + product_id + "\n " + variation + " \n" + variation_value);
                        orderItemNameDetailsTextView.setText(order_item_name);
                        orderQuantityDetailsTextView.setText(quantity);
                        orderVar1DetailsTextView.setText(variation);
                        orderVar1ValueDetailsTextView.setText(variation_value);

                    }
                    if (status == 3) {
//                        Toast.makeText(OrderdetailsActivity.this, Integer.toString(status), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = json_obj.getJSONArray("data");

                        JSONObject order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_id = order_id_json_obj.getString("order_id");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String order_item_name = order_id_json_obj.getString("order_item_name");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String quantity = order_id_json_obj.getString("_qty");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String product_id = order_id_json_obj.getString("_product_id");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation1 = order_id_json_obj.getString("variation1");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation_value1 = order_id_json_obj.getString("variation_value1");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation2 = order_id_json_obj.getString("variation2");
                        order_id_json_obj = jsonArray.getJSONObject(0);
                        String variation_value2 = order_id_json_obj.getString("variation_value2");

                        orderVar1DetailsTextView.setVisibility(View.VISIBLE);
                        orderVar1ValueDetailsTextView.setVisibility(View.VISIBLE);
                        orderVar2DetailsTextView.setVisibility(View.VISIBLE);
                        orderVar2ValueDetailsTextView.setVisibility(View.VISIBLE);
//                        orderDetailsStatusTextView.setText(order_id + "\n " + order_item_name + "\n " + quantity + " \n" + product_id + variation1 + "\n " + variation_value1 + "\n " + variation2 + " \n" + variation_value2);
                        orderItemNameDetailsTextView.setText(order_item_name);
                        orderQuantityDetailsTextView.setText(quantity);
                        orderVar1DetailsTextView.setText(variation1);
                        orderVar1ValueDetailsTextView.setText(variation_value1);
                        orderVar2DetailsTextView.setText(variation2);
                        orderVar2ValueDetailsTextView.setText(variation_value2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.execute(userId);
    }


}
