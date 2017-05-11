package com.example.android.shopinggururegister.Preferences;

/**
 * Created by Android on 28-11-2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android on 11-11-2016.
 */
public class WelcomePrefManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "welcomeKiranaPref";
    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    public WelcomePrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
