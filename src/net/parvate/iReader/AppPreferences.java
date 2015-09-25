package net.parvate.iReader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	private static final String APP_SHARED_PREFS = "net.parvate.iReader.app_preferences"; 
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public AppPreferences(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public void saveInt(String key, int value) {
		prefsEditor.putInt(key, value);
		prefsEditor.commit();
	}

	public void saveString(String key, String value) {
		prefsEditor.putString(key, value);
		prefsEditor.commit();
	}

	public void saveBoolean(String key, boolean value) {
		prefsEditor.putBoolean(key, value);
		prefsEditor.commit();
	}

	public int getInt(String key) {
		int returnint = appSharedPrefs.getInt(key, 42);
		return returnint;

	}

	public String getString(String key) {

		String returnstring = appSharedPrefs.getString(key, null);
		return returnstring;
	}

	public boolean getBoolean(String key) {
		return appSharedPrefs.getBoolean(key, false);
	}
}
