package pl.com.alanzur.helloworld;

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
						((HelloworldApp)getApplication()).pullAndInsert();
						Thread.sleep(Integer.parseInt(((HelloworldApp)getApplication()).prefs.getString("delay", "30000")));
					}
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
