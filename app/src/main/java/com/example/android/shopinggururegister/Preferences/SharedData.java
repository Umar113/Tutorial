package com.example.android.shopinggururegister.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedData 
{


	public static String KEY_EMAIL="KEY_EMAIL";
	public static Boolean KEY_ITEM_LIST_GRID=false;

	
	Context con;
	private SharedPreferences prefs ;
	
	 public String stringPref;
	
	public SharedData(Context con) 
	{
		// TODO Auto-generated constructor stub
		this.con =con;
		setPrefs(PreferenceManager.getDefaultSharedPreferences(con));
		
	}
	
	public void addStringSharedPrefs(String key,String value)
	{
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void addIntSharedPrefs(String key,int value)
	{
		Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void addBooleanSharedPrefs(String key,boolean value)
	{
		Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public SharedPreferences getPrefs() 
	{
		return prefs;
	}
	
	

	public void setPrefs(SharedPreferences prefs) 
	{
		this.prefs = prefs;
	}
	
	public void deleteAllSharedPrefs()
	{
		prefs.edit().clear().commit();
	}
}
