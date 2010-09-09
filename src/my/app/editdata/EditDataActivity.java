package my.app.editdata;

import my.app.R;
import my.app.editdata.provider.Calllog;
//import my.app.editdata.provider.Schedule;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditDataActivity extends Activity {

	private final Uri contentUri = Calllog.Calllogs.CONTENT_URI;
//	private final Uri contentUri = Schedule.Schedules.CONTENT_URI;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editdata);

		updateResultText();

		Button button_close = (Button) findViewById(R.id.editdata_btn_close);
		button_close.setOnClickListener(listenerClose);

		Button button_delete_all = (Button) findViewById(R.id.editdata_btn_delete_all);
		button_delete_all.setOnClickListener(listenerDeleteAll);

		Button button_auto_insert = (Button) findViewById(R.id.editdata_btn_auto_insert);
		button_auto_insert.setOnClickListener(listenerAutoInsert);

		Button button_reload = (Button) findViewById(R.id.editdata_btn_reload);
		button_reload.setOnClickListener(listenerReload);
}

	private View.OnClickListener listenerClose = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ
			setResult(RESULT_OK);
			finish();
		}
	};

	private View.OnClickListener listenerDeleteAll = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ
			getContentResolver().delete(contentUri, null, null);

			updateResultText();
		}
	};

	private View.OnClickListener listenerAutoInsert = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ
			ContentValues values = new ContentValues();
			values.put(Calllog.Calllogs.DATE, "2008-02-19");
			values.put(Calllog.Calllogs.TYPE, "1");
			values.put(Calllog.Calllogs.NUMBER, "01234567890");
			getContentResolver().insert(contentUri, values);

			values.clear();
			values.put(Calllog.Calllogs.DATE, "2008-03-10");
			values.put(Calllog.Calllogs.TYPE, "2");
			values.put(Calllog.Calllogs.NUMBER, "01234567890");
			getContentResolver().insert(contentUri, values);

			values.clear();
			values.put(Calllog.Calllogs.DATE, "2007-07-28");
			values.put(Calllog.Calllogs.TYPE, "3");
			values.put(Calllog.Calllogs.NUMBER, "01234567890");
			getContentResolver().insert(contentUri, values);

			values.clear();
			values.put(Calllog.Calllogs.DATE, "2007-09-22");
			values.put(Calllog.Calllogs.TYPE, "1");
			values.put(Calllog.Calllogs.NUMBER, "01234567890");
			getContentResolver().insert(contentUri, values);

			ContentValues newValues = new ContentValues();
			newValues.put(Calllog.Calllogs.TYPE, "9");

			getContentResolver().update(Uri.withAppendedPath(contentUri, "4"),
					newValues, null, null);

			getContentResolver().delete(Uri.withAppendedPath(contentUri, "3"),
					null, null);

			updateResultText();
		}
	};

	private View.OnClickListener listenerReload = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ
			updateResultText();
		}
	};

	private void updateResultText() {
		TextView tv = (TextView) findViewById(R.id.text_hello);
		tv.setText( getSelectResult() );
	}

	private String getSelectResult() {
		String[] col = { Calllog.Calllogs._ID,
				Calllog.Calllogs.DATE,
				Calllog.Calllogs.TYPE,
				Calllog.Calllogs.NUMBER };

//		String[] col = { Schedule.Schedules._ID,
//				Schedule.Schedules.DATE
//		};

		Cursor managedCursor = managedQuery(contentUri, col, null, null, null);
		String str =
			Calllog.Calllogs._ID + "|"
			+ Calllog.Calllogs.DATE + "|"
			+ Calllog.Calllogs.TYPE + "|"
			+ Calllog.Calllogs.NUMBER
			+ System.getProperty("line.separator");

//		String str =
//			Schedule.Schedules._ID + "|"
//			+ Schedule.Schedules.DATE
//			+ System.getProperty("line.separator");

		int count = 0;
		while (managedCursor.moveToNext()) {
			str += managedCursor.getString(0);
			str += "|";
			str += managedCursor.getString(1);
			str += "|";
			str += managedCursor.getString(2);
			str += "|";
			str += managedCursor.getString(3);
			str += System.getProperty("line.separator");

//			str += managedCursor.getString(0);
//			str += "|";
//			str += managedCursor.getString(1);
//			str += System.getProperty("line.separator");
			count++;
		}
		str = String.valueOf(count) + " record selected." + System.getProperty("line.separator") + str;

		return str;
	}
}