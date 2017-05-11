package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Dos.RegisterUserClass;
import com.example.android.shopinggururegister.Helpers.ConnectionDetector;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 25-11-2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String LOGIN_URL = "http://shopingguru.in/wp-webapi/login.php";
    private EditText emailLoginEditText, passwordLoginEditText;
    private Button loginSessionButton;
    private TextView jsonResponseTextView, forgotPasswordTextView, signUpTextView, skipTextView;
    JSONObject json_obj;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        emailLoginEditText = (EditText) findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = (EditText) findViewById(R.id.passwordLoginEditText);
        loginSessionButton = (Button) findViewById(R.id.loginSessionButton);
        jsonResponseTextView = (TextView) findViewById(R.id.jsonResponseTextView);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        skipTextView = (TextView) findViewById(R.id.skipTextView);

        ConnectionDetector connectivityManager = new ConnectionDetector(getApplicationContext());
        Boolean isInternetAvailable = connectivityManager.isConnectingToInternet();
        if (isInternetAvailable) {
            sessionManager = new SessionManager(getApplicationContext());
            if (sessionManager.isLoggedIn()) {
                finish();
                Toast.makeText(LoginActivity.this, "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_SHORT).show();
                sessionManager.checkLogin();
            }
//        HashMap<String, String> user = sessionManager.getUserDetails();
//        String id = user.get(SessionManager.KEY_ID);
            loginSessionButton.setOnClickListener(this);
            forgotPasswordTextView.setOnClickListener(this);
            signUpTextView.setOnClickListener(this);
            skipTextView.setOnClickListener(this);
        } else {
            AlertDialog.Builder internetBuilder = new AlertDialog.Builder(LoginActivity.this);
            internetBuilder.setTitle("Internet not Available");
            internetBuilder.setMessage("Would you like to turn on Internet Connectivity for better usage?");
            internetBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });
            internetBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
            AlertDialog internetAlertDialog = internetBuilder.create();
            internetAlertDialog.show();
            internetAlertDialog.setCancelable(false);
        }
    }

    private void loginUser() {
        String email = emailLoginEditText.getText().toString().trim();
        String password = passwordLoginEditText.getText().toString().trim();
        login(email, password);
    }

    private void login(String email, String password) {
        class LoginUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                    if (json_obj.has("status") || json_obj.has("1")) {
//                        JSONArray jsonArray = new JSONArray(`s);
                        JSONObject jsonObject = new JSONObject(s);
//                        JSONObject userJsonObject = jsonObject.getJSONObject("id");
                        int id = jsonObject.getInt("id");
//                        int idInt = Integer.parseInt(id);
//                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                        jsonResponseTextView.setVisibility(View.VISIBLE);
                        jsonResponseTextView.setTextColor(getResources().getColor(R.color.green));
                        jsonResponseTextView.setText("Sucessfully Logged In");
                        sessionManager.createLoginSession(id);
                        Intent i = new Intent(getApplicationContext(), MainActivityNew.class);
                        startActivity(i);
                        finish();
//                        SharedPreferences loginSharedPreferences = getSharedPreferences("LoginPref", 0);
//                        SharedPreferences.Editor loginEditor= loginSharedPreferences.edit();
//                        loginEditor.putInt("id", Integer.parseInt(id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s.equals("please fill all values")) {
                    emailLoginEditText.setError("e-mail required");
                    passwordLoginEditText.setError("password required");
                    jsonResponseTextView.setVisibility(View.VISIBLE);
                    jsonResponseTextView.setTextColor(getResources().getColor(R.color.red));
                    jsonResponseTextView.setText(s);
                }
                if (s.equals("email is not Registered")) {
                    emailLoginEditText.setError("e-mail is not registered");
                    jsonResponseTextView.setVisibility(View.VISIBLE);
                    jsonResponseTextView.setTextColor(getResources().getColor(R.color.red));
                    jsonResponseTextView.setText(s);
                }
                if (s.equals("Email and Password does not match")) {
                    jsonResponseTextView.setVisibility(View.VISIBLE);
                    jsonResponseTextView.setText(s);
                    jsonResponseTextView.setTextColor(getResources().getColor(R.color.red));
                    jsonResponseTextView.setText(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);
                data.put("password", params[1]);
                String result = ruc.sendPostRequest(LOGIN_URL, data);
                return result;
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute(email, password);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.loginSessionButton:
                loginUser();
                break;
            case R.id.forgotPasswordTextView:
                Intent i = new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(i);
                break;
            case R.id.signUpTextView:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.skipTextView:
                finish();
                Intent mainIntent = new Intent(LoginActivity.this, MainActivityNew.class);
                startActivity(mainIntent);
                break;
        }
    }
}
