package pl.com.alanzur.helloworld;

import android.app.IntentService;
import android.content.Intent;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";

	public RefreshService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		((HelloworldApp)getApplication()).pullAndInsert();
	}


}
