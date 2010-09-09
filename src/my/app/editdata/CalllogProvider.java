package my.app.editdata;

import java.util.HashMap;

import my.app.editdata.provider.Calllog;
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

public class CalllogProvider extends ContentProvider {

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 1;
	private static final String CALLLOG_TABLE = "calllog";

	private static final int CALLLOGS = 1;
	private static final int CALLLOG_ID = 2;

	private static final UriMatcher URI_MATCHER;

	private static HashMap<String, String> CALLLOGS_PROJECTION_MAP;

	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(Calllog.AUTHORITY, "calllogs", CALLLOGS);
		URI_MATCHER.addURI(Calllog.AUTHORITY, "calllogs/#", CALLLOG_ID);

		CALLLOGS_PROJECTION_MAP = new HashMap<String, String>();
		CALLLOGS_PROJECTION_MAP.put(BaseColumns._ID, "_id");
		CALLLOGS_PROJECTION_MAP.put(Calllog.Calllogs.DATE, "date");
		CALLLOGS_PROJECTION_MAP.put(Calllog.Calllogs.TYPE, "type");
		CALLLOGS_PROJECTION_MAP.put(Calllog.Calllogs.NUMBER, "number");
	}

	private DatabaseHelper dbHelper;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final String CREATE_CALLLOG_TABLE_SQL = "create table "
				+ CALLLOG_TABLE + " ("
				+ BaseColumns._ID + " integer primary key autoincrement, "
				+ Calllog.Calllogs.DATE + " text not null, "
				+ Calllog.Calllogs.TYPE + " text not null, "
				+ Calllog.Calllogs.NUMBER + " text not null, "
				+ " importance integer default 1)";

		private static final String DROP_CALLLOG_TABLE_SQL = "drop table if exists " + CALLLOG_TABLE;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_CALLLOG_TABLE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_CALLLOG_TABLE_SQL);
			onCreate(db);
		}

	}

	@Override
	public boolean onCreate() {
		dbHelper = new CalllogProvider.DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (URI_MATCHER.match(uri) != CALLLOGS) {
			throw new IllegalArgumentException("Unknown URL *** " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long rowID = db.insert(CALLLOG_TABLE, "NULL", values);
		if (rowID > 0) {
			Uri newUri = ContentUris.withAppendedId(
					Calllog.Calllogs.CONTENT_URI, rowID);
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
		case CALLLOGS:
			count = db.delete(CALLLOG_TABLE, where, whereArgs);
			break;

		case CALLLOG_ID:
			String calllogId = uri.getPathSegments().get(1);
			count = db.delete(CALLLOG_TABLE,
					"_id="
					+ calllogId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')'
									: ""), whereArgs);
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
		case CALLLOGS:
			count = db.update(CALLLOG_TABLE, values, where, whereArgs);
			break;

		case CALLLOG_ID:
			String scheduleId = uri.getPathSegments().get(1);
			count = db.update(CALLLOG_TABLE, values,
					"_id="
					+ scheduleId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')'
									: ""), whereArgs);
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
		case CALLLOGS:
			qb.setTables(CALLLOG_TABLE);
			qb.setProjectionMap(CALLLOGS_PROJECTION_MAP);
			break;

		case CALLLOG_ID:
			qb.setTables(CALLLOG_TABLE);
			qb.setProjectionMap(CALLLOGS_PROJECTION_MAP);
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
	    case CALLLOGS:
	      return "vnd.android.cursor.dir/vnd.my.app.calllog";
	    case CALLLOG_ID:
	      return "vnd.android.cursor.item/vnd.my.app.calllog";
	    default:
	      throw new IllegalArgumentException("Unknown URL " + uri);
	    }
	}

}
