package com.example.android.shopinggururegister.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
public class EditBillingAddressFragment extends Fragment implements View.OnClickListener {
    public static final String EDIT_BILLING_ADDRESS_URL = "http://shopingguru.in/wp-webapi/addbillingaddress.php";
    private EditText firstNameBillingAddEditText, lastNameBillingAddEditText, comapnyNameBillingAddEditText, emailBillingAddEditText, phoneBillingAddEditText, vastssnBillingAddEditText, addressBillingAddEditText, apartmentNameBillingAddEditText, cityNameBillingAddEditText, stateNameBillingAddEditText, postcodeBillingAddEditText;
    private Button editBillingAddButton;
    private ImageView cancelAddEditingImageView;
    private JSONObject json_obj;
    private SessionManager sessionManager;
    private FragmentManager profileFragmentManager;
    private FragmentTransaction profileFragmentTransaction;
    int value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_billing_address_fragment_layout, null);
        value = getArguments().getInt("data");

        sessionManager = new SessionManager(getActivity());

        firstNameBillingAddEditText = (EditText) view.findViewById(R.id.firstNameBillingAddEditText);
        lastNameBillingAddEditText = (EditText) view.findViewById(R.id.lastNameBillingAddEditText);
        comapnyNameBillingAddEditText = (EditText) view.findViewById(R.id.comapnyNameBillingAddEditText);
        emailBillingAddEditText = (EditText) view.findViewById(R.id.emailBillingAddEditText);
        phoneBillingAddEditText = (EditText) view.findViewById(R.id.phoneBillingAddEditText);
        vastssnBillingAddEditText = (EditText) view.findViewById(R.id.vastssnBillingAddEditText);
        addressBillingAddEditText = (EditText) view.findViewById(R.id.addressBillingAddEditText);
        apartmentNameBillingAddEditText = (EditText) view.findViewById(R.id.apartmentNameBillingAddEditText);
        cityNameBillingAddEditText = (EditText) view.findViewById(R.id.cityNameBillingAddEditText);
        stateNameBillingAddEditText = (EditText) view.findViewById(R.id.stateNameBillingAddEditText);
        postcodeBillingAddEditText = (EditText) view.findViewById(R.id.postcodeBillingAddEditText);
        editBillingAddButton = (Button) view.findViewById(R.id.editBillingAddButton);
        cancelAddEditingImageView = (ImageView) view.findViewById(R.id.cancelAddEditingImageView);

        editBillingAddButton.setOnClickListener(this);
        cancelAddEditingImageView.setOnClickListener(this);
        return view;
    }

    private void addBillingAddress() {
        String id = sessionManager.getUserDetails().get(SessionManager.KEY_ID);

        String firstname = firstNameBillingAddEditText.getText().toString().trim();
        String lastName = lastNameBillingAddEditText.getText().toString().trim();
        String company = comapnyNameBillingAddEditText.getText().toString().trim();
        String email = emailBillingAddEditText.getText().toString().trim();
        String phone = phoneBillingAddEditText.getText().toString().trim();
        String vatssn = vastssnBillingAddEditText.getText().toString().trim();
        String address = addressBillingAddEditText.getText().toString().trim();
        String apartment = apartmentNameBillingAddEditText.getText().toString().trim();
        String city = cityNameBillingAddEditText.getText().toString().trim();
        String state = stateNameBillingAddEditText.getText().toString().trim();
        String postcode = postcodeBillingAddEditText.getText().toString().trim();
        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(city) || TextUtils.isEmpty(state) || TextUtils.isEmpty(postcode)) {
            if (TextUtils.isEmpty(firstname)) {
                firstNameBillingAddEditText.setError("First name is required");
            }
            if (TextUtils.isEmpty(lastName)) {
                firstNameBillingAddEditText.setError("Last name is required");
            }
            if (TextUtils.isEmpty(city)) {
                lastNameBillingAddEditText.setError("City is required");
            }
            if (TextUtils.isEmpty(state)) {
                stateNameBillingAddEditText.setError("State is required");
            }
            if (TextUtils.isEmpty(postcode)) {
                postcodeBillingAddEditText.setError("Postcode is required");
            }
        } else {
            updateBillingAddressToServer(id, firstname, lastName, company, email, phone, vatssn, address, apartment, city, state, postcode);
        }
    }

    private void updateBillingAddressToServer(String id, String firstname, String lastName, String company, String email, String phone, String vatssn, String address, String apartment, String city, String state, String postcode) {
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

                try {
                    json_obj = new JSONObject(s);
                    if (s.equals("successfully added")) {

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
                            profileFragmentTransaction.replace(R.id.buyNowFrameLayout, profileFragment);
                            profileFragmentTransaction.commit();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s.equals("oops! Please try again!")) {
                }
                if (s.equals("successfully updated")) {
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
                        profileFragmentTransaction.replace(R.id.buyNowFrameLayout, profileFragment);
                        profileFragmentTransaction.commit();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("user_id", params[0]);
                data.put("billing_first_name", params[1]);
                data.put("billing_last_name", params[2]);
                data.put("billing_company", params[3]);
                data.put("billing_email", params[4]);
                data.put("billing_phone", params[5]);
                data.put("billing_vat", params[6]);
                data.put("billing_address_1", params[7]);
                data.put("billing_address_2", params[8]);
                data.put("billing_city", params[9]);
                data.put("billing_state", params[10]);
                data.put("billing_postcode", params[11]);

                String result = ruc.sendPostRequest(EDIT_BILLING_ADDRESS_URL, data);

                return result;
            }
        }
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.execute(id, firstname, lastName, company, email, phone, vatssn, address, apartment, city, state, postcode);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.editBillingAddButton:
                addBillingAddress();
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
