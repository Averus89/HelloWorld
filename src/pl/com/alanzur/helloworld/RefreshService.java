package pl.com.alanzur.helloworld;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";

	public RefreshService() {
		super(TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		StatusData statusData = ((HelloworldApp)getApplication()).statusData;
		try {
			List<Status> timeline = ((HelloworldApp) getApplication())
					.getTwitter().getPublicTimeline();
			for (Status status : timeline) {
				statusData.insert(status);
				Log.d(TAG,
						String.format("%s %s", status.user.name, status.text));
			}
		} catch (TwitterException e) {
			Log.e(TAG, "Failed to access twitter service", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

}
