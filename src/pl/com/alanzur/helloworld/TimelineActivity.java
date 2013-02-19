package pl.com.alanzur.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT,
			StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text,
			R.id.text_created_at };
	Cursor cursor;
	SimpleCursorAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cursor = ((HelloworldApp) getApplication()).statusData.query();

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		
		setTitle(R.string.timeline_title);
		getListView().setAdapter(adapter);
	}

	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.text_created_at)
				return false;

			long time = cursor.getLong(cursor
					.getColumnIndex(StatusData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils
					.getRelativeTimeSpanString(time);
			((TextView) view).setText(relativeTime);
			return true;
		}
	};
	
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
}
