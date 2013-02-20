package pl.com.alanzur.helloworld;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class HelloworldApp extends Application implements
		OnSharedPreferenceChangeListener {
	static final String TAG = "HelloworldApp";
	public static final String ACTION_NEW_STATUS = "pl.com.alanzur.helloworld.NEW_STATUS";
	public static final String ACTION_REFRESH = "pl.com.alanzur.helloworld.RefreshService";
	public static final String ACTION_REFRESH_ALARM = "pl.com.alanzur.helloworld.RefreshAlarm";

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
	
	static final Intent refreshAlarm = new Intent(ACTION_REFRESH_ALARM);

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		twitter = null;
		sendBroadcast(refreshAlarm);
		Log.d(TAG, "onSharedPreferenceChanged for key: " + key);
	}

	public int pullAndInsert() {
		int count = 0;
		long latestStatusCreatedAtTime = statusData.getLatestStatusCreatedAtTime();
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();
			for (Status status : timeline) {
				statusData.insert(status);
				if (status.createdAt.getTime() > latestStatusCreatedAtTime) {
					count++;
				}
				Log.d(TAG,
						String.format("%s %s", status.user.name, status.text));
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: ", e);
		}

		if (count > 0) {
			sendBroadcast(new Intent(ACTION_NEW_STATUS)
					.putExtra("count", count));
		}

		return count;
	}
}
