package pl.com.alanzur.helloworld;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusData {
	static final String TAG = "StatusData";
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "status";
	public static final String C_ID = "_id";
	public static final String C_CREATED_AT = "created_at";
	public static final String C_USER = "user_name";
	public static final String C_TEXT = "status_text";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max("
			+ StatusData.C_CREATED_AT + ")" };

	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;

	public StatusData(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
	}

	public void insert(Status status) {
		db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_USER, status.user.name);
		values.put(C_TEXT, status.text);

		// db.insert(TABLE, null, values);
		//db.insertWithOnConflict(TABLE, null, values,
		//		SQLiteDatabase.CONFLICT_IGNORE);
		
		context.getContentResolver().insert(StatusProvider.CONTENT_URI, values);
	}

	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null,
					null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}

	public Cursor query() {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null,
				C_CREATED_AT + " DESC");
		return cursor;
	}
}

class DbHelper extends SQLiteOpenHelper {
	public static final String TAG = DbHelper.class.getSimpleName();

	public DbHelper(Context context) {
		super(context, StatusData.DB_NAME, null, StatusData.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = String.format("create table %s "
				+ "(%s int primary key, %s int, %s text, %s text)", StatusData.TABLE,
				StatusData.C_ID, StatusData.C_CREATED_AT, StatusData.C_USER, StatusData.C_TEXT);
		Log.d(TAG, "Create with SQL: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// usually alter table
		db.execSQL("drop table if exists " + StatusData.TABLE);
		onCreate(db);
	}

}
 