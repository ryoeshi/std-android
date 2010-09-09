package my.app.editdata;

import java.util.HashMap;

import my.app.editdata.provider.Schedule;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class ScheduleProvider extends ContentProvider {

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 1;
	private static final String SCHEDULE_TABLE = "schedule";

	private static final int SCHEDULES = 1;
	private static final int SCHEDULE_ID = 2;

	private static final UriMatcher URI_MATCHER;

	private static HashMap<String, String> SCHEDULES_PROJECTION_MAP;

	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(Schedule.AUTHORITY, "schedules", SCHEDULES);
		URI_MATCHER.addURI(Schedule.AUTHORITY, "schedules/#", SCHEDULE_ID);

		SCHEDULES_PROJECTION_MAP = new HashMap<String, String>();
		SCHEDULES_PROJECTION_MAP.put(BaseColumns._ID, "_id");
		SCHEDULES_PROJECTION_MAP.put(Schedule.Schedules.DATE, "date");
		SCHEDULES_PROJECTION_MAP.put(Schedule.Schedules.PLAN, "plan");
	}

	private DatabaseHelper dbHelper;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final String CREATE_SCHEDULE_TABLE_SQL = "create table "
				+ SCHEDULE_TABLE + " (_id integer primary key autoincrement,"
				+ " date text not null," + " plan text not null,"
				+ " importance integer default 1)";

		private static final String DROP_SCHEDULE_TABLE_SQL = "drop table if exists schedule";

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_SCHEDULE_TABLE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_SCHEDULE_TABLE_SQL);
			onCreate(db);
		}

	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (URI_MATCHER.match(uri) != SCHEDULES) {
			throw new IllegalArgumentException("Unknown URL *** " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long rowID = db.insert(SCHEDULE_TABLE, "NULL", values);
		if (rowID > 0) {
			Uri newUri = ContentUris.withAppendedId(
					Schedule.Schedules.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Failed to insert row into " + uri);

	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;

		switch (URI_MATCHER.match(uri)) {
		case SCHEDULES:
			count = db.delete(SCHEDULE_TABLE, where, whereArgs);
			break;

		case SCHEDULE_ID:
			String scheduleId = uri.getPathSegments().get(1);
			count = db.delete(SCHEDULE_TABLE,
					"_id="
							+ scheduleId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (URI_MATCHER.match(uri)) {
		case SCHEDULES:
			count = db.update(SCHEDULE_TABLE, values, where, whereArgs);
			break;

		case SCHEDULE_ID:
			String scheduleId = uri.getPathSegments().get(1);
			count = db.update(SCHEDULE_TABLE, values,
					"_id="
							+ scheduleId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (URI_MATCHER.match(uri)) {
		case SCHEDULES:
			qb.setTables(SCHEDULE_TABLE);
			qb.setProjectionMap(SCHEDULES_PROJECTION_MAP);
			break;

		case SCHEDULE_ID:
			qb.setTables(SCHEDULE_TABLE);
			qb.setProjectionMap(SCHEDULES_PROJECTION_MAP);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = "date";
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
	    case SCHEDULES:
	      return "vnd.android.cursor.dir/vnd.mamezou.schedule";
	    case SCHEDULE_ID:
	      return "vnd.android.cursor.item/vnd.mamezou.schedule";
	    default:
	      throw new IllegalArgumentException("Unknown URL " + uri);
	    }
	}

}
