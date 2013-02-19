package pl.com.alanzur.helloworld;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService";
	boolean running = false;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		new Thread() {
			public void run() {
				try {
					while (running) {
						List<Status> timeline = ((HelloworldApp) getApplication())
								.getTwitter().getPublicTimeline();
						for (Status status : timeline) {
							((HelloworldApp)getApplication()).statusData.insert(status);
							Log.d(TAG, String.format("%s %s", status.user.name,
									status.text));
						}
						Thread.sleep(Integer.parseInt(((HelloworldApp)getApplication()).prefs.getString("delay", "30000")));
					}
				} catch (TwitterException e) {
					Log.e(TAG, "Died: ", e);
				} catch (InterruptedException e) {
					Log.e(TAG, "Interrupted: ", e);
				}
			}
		}.start();
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		running = false;
		super.onDestroy();
	}

}
