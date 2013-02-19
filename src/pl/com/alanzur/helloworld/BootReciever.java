package pl.com.alanzur.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context,UpdaterService.class));
		Log.d("BootReciever", "onRecieve");
	}

}
