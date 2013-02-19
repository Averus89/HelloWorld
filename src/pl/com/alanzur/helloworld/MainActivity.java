package pl.com.alanzur.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static String TAG = "MainActivity";
	Button buttonUpdate;
	EditText editStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Debug.startMethodTracing("Yamba.Trace");
		buttonUpdate = (Button) findViewById(R.id.button_send);
		editStatus = (EditText) findViewById(R.id.edit_status);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		//Debug.stopMethodTracing();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intentUpdate = new Intent(this, UpdaterService.class);
		Intent intentRefresh = new Intent(this, RefreshService.class);
		
		switch (item.getItemId()) {
		case R.id.item_start_service:
			startService(intentUpdate);
			return true;
		case R.id.item_stop_service:
			stopService(intentUpdate);
			return true;
		case R.id.item_refresh:
			startService(intentRefresh);
			return true;
		case R.id.item_prefs:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.item_timeline:
			startActivity(new Intent(this, TimelineActivity.class));
			return true;
		default:
			return false;
		}

		//return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		String statusTextString = editStatus.getText().toString();

		new PostToTwitter().execute(statusTextString);

		Log.d("StatusActivity", "onClick with text: " + statusTextString);
	}

	// input, progress, result
	class PostToTwitter extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				/*Twitter twitter = new Twitter("averus", "");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");*/
				((HelloworldApp)getApplication()).getTwitter().setStatus(params[0]);
				Log.d(TAG, "Posted");
				return "Posted: " + params[0];
			} catch (Exception e) {
				Log.e(TAG, "Died: " + e);
				return "Died: " + params[0];
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}

	}
}
