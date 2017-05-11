package com.example.android.shopinggururegister.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.Adapter.RegisterUserClass;
import com.example.android.shopinggururegister.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 25-07-2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://shopingguru.in/wp-webapi/registeruser.php";
    private EditText usernameEditText, emailEditText, passwordEditText, confirmpasswordEditText;
    private Button registerUserButton;
    private ImageView fbImageView, twitterImageView, youtubeImageView, linkedInImageView, cancelImageView;
    private TextView jsonResponseRegisterTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initViews();
        registerEvents();
    }

    private void initViews() {
        usernameEditText = (EditText) findViewById(R.id.username);
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        registerUserButton = (Button) findViewById(R.id.registerUserButton);
        fbImageView = (ImageView) findViewById(R.id.fbImageView);
        twitterImageView = (ImageView) findViewById(R.id.twitterImageView);
        youtubeImageView = (ImageView) findViewById(R.id.youtubeImageView);
        linkedInImageView = (ImageView) findViewById(R.id.linkedInImageView);
        cancelImageView = (ImageView) findViewById(R.id.cancelImageView);
        jsonResponseRegisterTextView = (TextView) findViewById(R.id.jsonResponseRegisterTextView);
    }

    private void registerEvents() {
        cancelImageView.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
        fbImageView.setOnClickListener(this);
        twitterImageView.setOnClickListener(this);
        youtubeImageView.setOnClickListener(this);
        linkedInImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.registerUserButton:
                registerUser();
                break;
            case R.id.cancelImageView:
                break;
            case R.id.fbImageView:
                Uri uriFacebook = Uri.parse("http://www.facebook.com"); // missing 'http://' will cause crash
                Intent intentFaceBook = new Intent(Intent.ACTION_VIEW, uriFacebook);
                startActivity(intentFaceBook);
                break;
            case R.id.twitterImageView:
                Uri uriTwitter = Uri.parse("http://www.twitter.com"); // missing 'http://' will cause crash
                Intent intentTwitter = new Intent(Intent.ACTION_VIEW, uriTwitter);
                startActivity(intentTwitter);
                break;
            case R.id.youtubeImageView:
                Uri uriYoutube = Uri.parse("http://www.youtube.com"); // missing 'http://' will cause crash
                Intent intentYoutube = new Intent(Intent.ACTION_VIEW, uriYoutube);
                startActivity(intentYoutube);
                break;
            case R.id.linkedInImageView:
                Uri uri = Uri.parse("http://www.linkedin.com"); // missing 'http://' will cause crash
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmpassword = confirmpasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmpassword)) {
            jsonResponseRegisterTextView.setVisibility(View.VISIBLE);
            jsonResponseRegisterTextView.setText("Please fill all values");

        } else {
            if (TextUtils.equals(password, confirmpassword)) {
                register(username, email, password);

            } else {
                jsonResponseRegisterTextView.setVisibility(View.VISIBLE);
                jsonResponseRegisterTextView.setText("Password does not match");
            }
        }
    }

    private void register(String username, String email, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass registerUserClass = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(RegisterActivity.this);
                loading.setMessage("Loading...Please wait!");
                loading.show();
                loading.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                jsonResponseRegisterTextView.setText(s);

                if (s.equals("successfully registered")) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", params[0]);
                data.put("email", params[1]);
                data.put("password", params[2]);

                String result = registerUserClass.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username, email, password);

    }
}
