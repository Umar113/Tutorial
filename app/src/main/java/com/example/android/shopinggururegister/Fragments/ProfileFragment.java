package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.shopinggururegister.Activities.LoginActivity;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 08-12-2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private int value;
    private TextView billingAddTextView, deliveryAddTextView;
    private String id;
    private SessionManager sessionManager;
    private static final String USER_ID_KEY = "id";
    private static final String FETCH_BILLING_ADDRESS_URL = "http://shopingguru.in/wp-webapi/getbillingaddress.php";
    private static final String FETCH_DELIVERY_ADDRESS_URL = "http://shopingguru.in/wp-webapi/getshippingaddress.php";
    private FragmentTransaction profileFragmentTransaction;
    private FragmentManager profileFragmentManager;
    private Button editNewBillingAddButton, editNewShippingAddButton;

    private JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, null);
        billingAddTextView = (TextView) view.findViewById(R.id.billingFirstNameTextView);
        deliveryAddTextView = (TextView) view.findViewById(R.id.deliveryAddTextView);
        editNewBillingAddButton = (Button) view.findViewById(R.id.editNewBillingAddButton);
        editNewShippingAddButton = (Button) view.findViewById(R.id.editNewShippingAddButton);
        editNewBillingAddButton.setOnClickListener(this);
        editNewShippingAddButton.setOnClickListener(this);
        sessionManager = new SessionManager(getActivity());
        id = sessionManager.getUserDetails().get(SessionManager.KEY_ID);
        if (sessionManager.isLoggedIn()) {
            fetchUserBillingAddress(id);
            fetchUserDeliveryAddress(id);
        } else {
            AlertDialog.Builder profileBuilder = new AlertDialog.Builder(getActivity());
            profileBuilder.setTitle("Not Logged in");
            profileBuilder.setMessage("Wouldyou like to Log in for better experience with shopingguru.in?");
            profileBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            profileBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    dialogInterface.dismiss();
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.mainFrameLayout, new HomeFragment());
                    profileFragmentTransaction.commit();
                }
            });
            AlertDialog profileAlertDialog = profileBuilder.create();
            profileAlertDialog.show();
            profileAlertDialog.setCancelable(false);
        }
        return view;
    }

    private void fetchUserBillingAddress(String id) {

        class FetchUserBillingAddress extends AsyncTask<String, Void, String> {
            private ProgressDialog progressDialog;
            RegisterUserClass fetchBillingAddRegisterUserClass = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading Address... Please wait!");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> billingAddData = new HashMap<>();
                billingAddData.put("id", params[0]);
                String billingAddResult = fetchBillingAddRegisterUserClass.sendPostRequest(FETCH_BILLING_ADDRESS_URL, billingAddData);
                return billingAddResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.cancel();
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.has("status") || jsonObject.has("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String billing_first_name_string = data.getString("billing_first_name") + "\n" + data.getString("billing_company") + "\n" + data.getString("billing_email") + "\n" + data.getString("billing_phone") + "\n" + data.getString("billing_address_1") + "\n" + data.getString("billing_city") + "\n" + data.getString("billing_country") + "\n" + data.getString("billing_state") + "\n" + data.getString("billing_postcode");
                        billingAddTextView.setText(billing_first_name_string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        new FetchUserBillingAddress().execute(id);

    }

    private void fetchUserDeliveryAddress(String id) {

        class FetchUserDeliveryAddress extends AsyncTask<String, Void, String> {
            private ProgressDialog progressDialog;
            RegisterUserClass fetchDeliveryAddRegisterUserClass = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading Address... Please wait!");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> deliveryAddData = new HashMap<>();
                deliveryAddData.put("id", params[0]);
                String billingAddResult = fetchDeliveryAddRegisterUserClass.sendPostRequest(FETCH_DELIVERY_ADDRESS_URL, deliveryAddData);
                return billingAddResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.cancel();
                editNewBillingAddButton.setVisibility(View.VISIBLE);
                editNewShippingAddButton.setVisibility(View.VISIBLE);
                try {
                    jsonObject = new JSONObject(s);

                    if (jsonObject.has("status") || jsonObject.has("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        String billing_first_name_string = data.getString("shipping_first_name") + "\n" + data.getString("shipping_last_name") + "\n" + data.getString("shipping_company") + "\n" + data.getString("shipping_vat") + "\n" + data.getString("shipping_address_1") + "\n" + data.getString("shipping_city") + "\n" + data.getString("shipping_country") + "\n" + data.getString("shipping_state") + "\n" + data.getString("shipping_postcode");
                        deliveryAddTextView.setText(billing_first_name_string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        new FetchUserDeliveryAddress().execute(id);
    }

    @Override
    public void onClick(View view) {
        value = getArguments().getInt("data");

        int id = view.getId();
        switch (id) {
            case R.id.editNewBillingAddButton:

                if (value == 1) {
                    Bundle args = new Bundle();
                    args.putInt("data", 1);
                    EditBillingAddressFragment editBillingAddressFragment = new EditBillingAddressFragment();
                    editBillingAddressFragment.setArguments(args);
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.buyNowFrameLayout, editBillingAddressFragment);
                    profileFragmentTransaction.commit();
                } else if (value == 2) {
                    Bundle args = new Bundle();
                    args.putInt("data", 2);
                    EditBillingAddressFragment editBillingAddressFragment = new EditBillingAddressFragment();
                    editBillingAddressFragment.setArguments(args);
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.mainFrameLayout, editBillingAddressFragment);
                    profileFragmentTransaction.commit();
                }
                break;
            case R.id.editNewShippingAddButton:
                if (value == 1) {
                    EditShippingAddressFragment editShippingAddressFragment = new EditShippingAddressFragment();
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putInt("data", 1);
                    editShippingAddressFragment.setArguments(args);

                    profileFragmentTransaction.replace(R.id.buyNowFrameLayout, editShippingAddressFragment);
                    profileFragmentTransaction.commit();
                } else if (value == 2) {
                    Bundle args = new Bundle();
                    args.putInt("data", 2);
                    EditShippingAddressFragment editShippingAddressFragment = new EditShippingAddressFragment();
                    editShippingAddressFragment.setArguments(args);

                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.mainFrameLayout, editShippingAddressFragment);
                    profileFragmentTransaction.commit();
                }

                break;
        }
    }
}