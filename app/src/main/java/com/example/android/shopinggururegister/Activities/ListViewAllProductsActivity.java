package com.example.android.shopinggururegister.Activities;

/**
 * Created by Android on 26-11-2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.ProductListViewAdapter;
import com.example.android.shopinggururegister.Dos.ProductDo;
import com.example.android.shopinggururegister.R;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListViewAllProductsActivity extends AppCompatActivity {

    ListView listView;
    ProductDo adapter;
    ArrayList<ProductDo> actorsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_category_list_view_layout);
        listView = (ListView) findViewById(R.id.list);
        actorsArrayList = new ArrayList<ProductDo>();
        new ActorsAsyncTask().execute("http://ssinfotech.org/android_connect/modem.php");
    }

    public class ActorsAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ListViewAllProductsActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpGet post = new HttpGet(params[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) client.execute(post);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jobj = new JSONObject(data);
                    JSONArray jArray = jobj.getJSONArray("routers_sheet");

                    for (int i = 0; i < jArray.length(); i++) {
                        ProductDo productDo = new ProductDo();
                        JSONObject jRealObject = jArray.getJSONObject(i);

                        productDo.setProduct(jRealObject.getString("product"));
                        productDo.setCategory(jRealObject.getString("category"));
                        productDo.setPrice(jRealObject.getString("price"));
                        productDo.setOrignal_price(jRealObject.getString("original_price"));
                        productDo.setSelling_price(jRealObject.getString("selling_price"));
                        productDo.setImage(jRealObject.getString("images_urls"));
                        actorsArrayList.add(productDo);
                    }
                    return true;
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == false) {

                Toast.makeText(getApplicationContext(), "This is Bad", Toast.LENGTH_LONG).show();

            } else {
                ProductListViewAdapter adapter = new ProductListViewAdapter(getApplicationContext(), R.layout.product_listview_item_layout, actorsArrayList);
                listView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "This is Good", Toast.LENGTH_LONG).show();
            }

        }
    }
}