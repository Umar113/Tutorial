package com.example.android.shopinggururegister.Fragments;

/**
 * Created by Android on 12-12-2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.Activities.MainActivityNew;
import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 12-12-2016.
 */
public class EditShippingAddressFragment extends Fragment implements View.OnClickListener {
    public static final String EDIT_SHIPPING_ADDRESS_URL = "http://shopingguru.in/wp-webapi/addshippingaddress.php";
    private EditText firstNameBillingAddEditText, lastNameBillingAddEditText, comapnyNameBillingAddEditText, emailBillingAddEditText, phoneBillingAddEditText, vastssnBillingAddEditText, addressBillingAddEditText, apartmentNameBillingAddEditText, cityNameBillingAddEditText, stateNameBillingAddEditText, postcodeBillingAddEditText;
    private Button editBillingAddButton;
    private TextView billingAddEditTitleTextView, jsonResponseAddressTextView;
    private JSONObject json_obj;
    private ImageView cancelAddEditingImageView;
    private SessionManager sessionManager;
    private String id;
    private FragmentManager profileFragmentManager;
    private FragmentTransaction profileFragmentTransaction;
    private int value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_billing_address_fragment_layout, null);

        value = getArguments().getInt("data");

        sessionManager = new SessionManager(getActivity());
        billingAddEditTitleTextView = (TextView) view.findViewById(R.id.billingAddEditTitleTextView);
        billingAddEditTitleTextView.setText("Shipping Address");
        firstNameBillingAddEditText = (EditText) view.findViewById(R.id.firstNameBillingAddEditText);
        lastNameBillingAddEditText = (EditText) view.findViewById(R.id.lastNameBillingAddEditText);

        emailBillingAddEditText = (EditText) view.findViewById(R.id.emailBillingAddEditText);
        phoneBillingAddEditText = (EditText) view.findViewById(R.id.phoneBillingAddEditText);

        comapnyNameBillingAddEditText = (EditText) view.findViewById(R.id.comapnyNameBillingAddEditText);
        vastssnBillingAddEditText = (EditText) view.findViewById(R.id.vastssnBillingAddEditText);
        addressBillingAddEditText = (EditText) view.findViewById(R.id.addressBillingAddEditText);
        apartmentNameBillingAddEditText = (EditText) view.findViewById(R.id.apartmentNameBillingAddEditText);
        cityNameBillingAddEditText = (EditText) view.findViewById(R.id.cityNameBillingAddEditText);
        stateNameBillingAddEditText = (EditText) view.findViewById(R.id.stateNameBillingAddEditText);
        postcodeBillingAddEditText = (EditText) view.findViewById(R.id.postcodeBillingAddEditText);
        editBillingAddButton = (Button) view.findViewById(R.id.editBillingAddButton);
        jsonResponseAddressTextView = (TextView) view.findViewById(R.id.jsonResponseAddressTextView);
        cancelAddEditingImageView = (ImageView) view.findViewById(R.id.cancelAddEditingImageView);


        cancelAddEditingImageView.setOnClickListener(this);
        editBillingAddButton.setOnClickListener(this);

        emailBillingAddEditText.setVisibility(View.GONE);
        phoneBillingAddEditText.setVisibility(View.GONE);

        editBillingAddButton.setText("Edit Shipping Address");
        editBillingAddButton.setOnClickListener(this);

        return view;
    }

    private void addShippingAddress() {
        id = sessionManager.getUserDetails().get(SessionManager.KEY_ID);
        String firstname = firstNameBillingAddEditText.getText().toString().trim();
        String lastName = lastNameBillingAddEditText.getText().toString().trim();
        String company = comapnyNameBillingAddEditText.getText().toString().trim();
        String vatssn = vastssnBillingAddEditText.getText().toString().trim();
        String address = addressBillingAddEditText.getText().toString().trim();
        String apartment = apartmentNameBillingAddEditText.getText().toString().trim();
        String city = cityNameBillingAddEditText.getText().toString().trim();
        String state = stateNameBillingAddEditText.getText().toString().trim();
        String postcode = postcodeBillingAddEditText.getText().toString().trim();
        updateShippingAddressToServer(id, firstname, lastName, company, vatssn, address, apartment, city, state, postcode);
    }

    private void updateShippingAddressToServer(String id, String firstname, String lastName, String company, String vatssn, String address, String apartment, String city, String state, String postcode) {


        class BillingAddress extends AsyncTask<String, Void, String> {
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
                loading.dismiss();
                jsonResponseAddressTextView.setVisibility(View.VISIBLE);
                jsonResponseAddressTextView.setText(s);
                try {
                    json_obj = new JSONObject(s);
                    if (s.equals("successfully added")) {
                        jsonResponseAddressTextView.setVisibility(View.VISIBLE);
                        firstNameBillingAddEditText.getText().clear();
                        lastNameBillingAddEditText.getText().clear();
                        comapnyNameBillingAddEditText.getText().clear();
                        vastssnBillingAddEditText.getText().clear();
                        addressBillingAddEditText.getText().clear();
                        apartmentNameBillingAddEditText.getText().clear();
                        cityNameBillingAddEditText.getText().clear();
                        stateNameBillingAddEditText.getText().clear();
                        postcodeBillingAddEditText.getText().clear();
                        jsonResponseAddressTextView.setText(s);
                        jsonResponseAddressTextView.setTextColor(getResources().getColor(R.color.green));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s.equals("oops! Please try again!")) {
                }
                if (s.equals("successfully updated")) {
                    jsonResponseAddressTextView.setVisibility(View.VISIBLE);
                    jsonResponseAddressTextView.setTextColor(getResources().getColor(R.color.green));
                    jsonResponseAddressTextView.setText(s);
                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("user_id", params[0]);
                data.put("shipping_first_name", params[1]);
                data.put("shipping_last_name", params[2]);
                data.put("shipping_company", params[3]);
                data.put("shipping_vat", params[4]);
                data.put("shipping_address_1", params[5]);
                data.put("shipping_address_2", params[6]);
                data.put("shipping_city", params[7]);
                data.put("shipping_state", params[8]);
                data.put("shipping_postcode", params[9]);

                String result = ruc.sendPostRequest(EDIT_SHIPPING_ADDRESS_URL, data);

                return result;
            }
        }
// 'user_id', 'shipping_first_name', 'shipping_last_name', 'shipping_company', 'shipping_vat',
// 'shipping_address_1', 'shipping_address_2', 'shipping_city', 'shipping_state', 'shipping_postcode'
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.execute(id, firstname, lastName, company, vatssn, address, apartment, city, state, postcode);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.editBillingAddButton:
                addShippingAddress();
                break;
            case R.id.cancelAddEditingImageView:
                if (value == 1) {
                    Bundle args = new Bundle();
                    args.putInt("data", 1);
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(args);
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.buyNowFrameLayout, profileFragment);
                    profileFragmentTransaction.commit();
                } else {
                    Bundle args = new Bundle();
                    args.putInt("data", 2);
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(args);
                    profileFragmentManager = getFragmentManager();
                    profileFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    profileFragmentTransaction.replace(R.id.mainFrameLayout, profileFragment);
                    profileFragmentTransaction.commit();
                }
                break;
        }
    }
}
