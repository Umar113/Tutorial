package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Preferences.SharedData;
import com.example.android.shopinggururegister.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 30-11-2016.
 */
public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView jsonResponseForgotTextView;
    private EditText forgotEmailEditText, confirmPasswordEditText, otpEmailEditText, newPasswordEditText;
    private Button confirmEmailButton, checkOTPButton, confirmPasswordEmailButton;
    private int otp;
    private SharedData sharedData;
    private JSONObject jsonObject;
    public static final String FORGOT_PASSWORD_URL = "http://shopingguru.in/wp-webapi/forgotpassword.php";
    public static final String CHANGE_PASSWORD_URL = "http://shopingguru.in/wp-webapi/chengepassword.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset_activity_layout);
        sharedData = new SharedData(getApplicationContext());
        initViews();
        registerEvents();
    }

    private void initViews() {
        jsonResponseForgotTextView = (TextView) findViewById(R.id.jsonResponseForgotTextView);
        forgotEmailEditText = (EditText) findViewById(R.id.forgotEmailEditText);
        confirmEmailButton = (Button) findViewById(R.id.confirmEmailButton);
        checkOTPButton = (Button) findViewById(R.id.checkOTPButton);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        confirmPasswordEmailButton = (Button) findViewById(R.id.confirmPasswordEmailButton);
        otpEmailEditText = (EditText) findViewById(R.id.otpEmailEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
    }

    private void registerEvents() {
        confirmEmailButton.setOnClickListener(this);
        checkOTPButton.setOnClickListener(this);
        confirmPasswordEmailButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.confirmEmailButton:
                confirmUserEmail();
                break;
            case R.id.checkOTPButton:
                String otpConfirmation = otpEmailEditText.getText().toString().trim();

                if (TextUtils.equals(Integer.toString(otp), otpConfirmation)) {
                    Toast.makeText(getApplicationContext(), Integer.toString(otp), Toast.LENGTH_SHORT).show();
                    checkOTPButton.setVisibility(View.GONE);
                    confirmPasswordEmailButton.setVisibility(View.VISIBLE);
                    newPasswordEditText.setVisibility(View.VISIBLE);
                    confirmPasswordEditText.setVisibility(View.VISIBLE);
                    jsonResponseForgotTextView.setTextColor(getResources().getColor(R.color.green));
                    jsonResponseForgotTextView.setText(sharedData.getPrefs().getString(SharedData.KEY_EMAIL, null));
                }
                break;
            case R.id.confirmPasswordEmailButton:
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    jsonResponseForgotTextView.setText("Please Fill All Values");

                } else {
                    if (TextUtils.equals(newPassword, confirmPassword)) {
                        confirmUserPassword();
                    } else {
                        newPasswordEditText.setError("new password required");
                        confirmPasswordEditText.setError("you need to confirm your password");
                        jsonResponseForgotTextView.setText("Password does not match");
                    }
                }
                break;
        }
    }

    private void confirmUserPassword() {
        String emailPrefString = sharedData.getPrefs().getString(SharedData.KEY_EMAIL, null);
        String passwordString = newPasswordEditText.getText().toString().trim();
        confirmPassword(emailPrefString, passwordString);
    }

    private void confirmUserEmail() {
        String email = forgotEmailEditText.getText().toString().trim();
        sharedData.addStringSharedPrefs(SharedData.KEY_EMAIL, email);
        confirmEmail(email);
    }


    private void confirmEmail(String email) {
        class ConfirmEmail extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PasswordResetActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {

                    jsonObject = new JSONObject(s);
                    if (jsonObject.has("status") || jsonObject.has("1")) {
                        otp = jsonObject.getInt("otp");
                        Log.d(">>>>>OTP", String.valueOf(otp));
                        forgotEmailEditText.setVisibility(View.GONE);
                        otpEmailEditText.setVisibility(View.VISIBLE);
                        confirmEmailButton.setVisibility(View.GONE);
                        checkOTPButton.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s.equals("Email is not registered")) {
                    jsonResponseForgotTextView.setText(s);
                }
                if (s.equals("email is not Registered")) {
                    jsonResponseForgotTextView.setText(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);
                String result = ruc.sendPostRequest(FORGOT_PASSWORD_URL, data);
                return result;
            }
        }
        ConfirmEmail confirmEmail = new ConfirmEmail();
        confirmEmail.execute(email);
    }


    //    AsyncTask for Password confirmation
    private void confirmPassword(String emailPrefString, String passwordString) {
        class ConfirmPassword extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PasswordResetActivity.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                jsonResponseForgotTextView.setText(response);

                if (response.equals("please POST all values")) {
                    jsonResponseForgotTextView.setText(response);


                }
                if (response.equals("password successfully changed")) {
                    finish();
                    jsonResponseForgotTextView.setTextColor(getResources().getColor(R.color.green));
                    jsonResponseForgotTextView.setText(response);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
                if (response.equals("password not changed")) {
                    jsonResponseForgotTextView.setText(response);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);
                data.put("password", params[1]);
                String result = ruc.sendPostRequest(CHANGE_PASSWORD_URL, data);
                return result;
            }
        }
        ConfirmPassword confirmPassword = new ConfirmPassword();
        confirmPassword.execute(emailPrefString, passwordString);
    }

}
