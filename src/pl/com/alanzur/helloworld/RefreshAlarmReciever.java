package pl.com.alanzur.helloworld;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class RefreshAlarmReciever extends BroadcastReceiver {

	static PendingIntent lastOperation;

	@Override
	public void onReceive(Context context, Intent intent) {

		long interval = Long.parseLong(PreferenceManager
				.getDefaultSharedPreferences(context).getString("delay",
						"60000"));

		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(HelloworldApp.ACTION_REFRESH),
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		if (lastOperation != null)
			alarmManager.cancel(lastOperation);

		if (interval > 0) {
			alarmManager.setInexactRepeating(AlarmManager.RTC,
					System.currentTimeMillis(), interval, operation);
		}

		lastOperation = operation;
		// context.startService(new Intent(context,UpdaterService.class));
		Log.d("BootReciever", "onRecieve");
	}

}
