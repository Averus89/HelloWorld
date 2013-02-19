package pl.com.alanzur.helloworld;

import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		//Debug.stopMethodTracing();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
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
