package pl.com.alanzur.helloworld;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class HelloworldApp extends Application implements
		OnSharedPreferenceChangeListener {
	static final String TAG = "HelloworldApp";

	private Twitter twitter;
	SharedPreferences prefs;
	StatusData statusData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		statusData = new StatusData(this);
		
		Log.d(TAG, "OnCreate");
	}

	/**
	 * @return the twitter
	 */
	public Twitter getTwitter() {
		if (twitter == null) {
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");

			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
		}
		return twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		twitter = null;
		Log.d(TAG, "onSharedPreferenceChanged for key: " + key);
	}
}
