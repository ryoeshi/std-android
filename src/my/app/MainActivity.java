package my.app;

import my.app.editdata.EditDataActivity;
import my.app.getdata.GetDataActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int MAIN_MENU = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // データ取得ボタン設定
        Button button_getdata = (Button) findViewById(R.id.btn_getdata);
        button_getdata.setOnClickListener(listenerGetData);

        // DB編集ボタン設定
        Button button_editdata = (Button) findViewById(R.id.btn_editdata);
        button_editdata.setOnClickListener(listenerEditData);
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		boolean ret = super.onCreateOptionsMenu(menu);

		menu.add(MAIN_MENU, Menu.FIRST, Menu.NONE, R.string.main_menu_my_location);
		menu.add(MAIN_MENU, Menu.FIRST + 1 ,Menu.NONE , R.string.main_menu_version);
		menu.add(MAIN_MENU, Menu.FIRST + 2 ,Menu.NONE , R.string.main_menu_exit);
		return ret;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		boolean ret = super.onMenuItemSelected(featureId, item);

		switch ( item.getItemId() ) {
		case Menu.FIRST:

			Toast.makeText(this, R.string.main_menu_my_location, Toast.LENGTH_SHORT).show();

			String action = "android.intent.action.VIEW";
			String uri = "http://maps.google.co.jp/";

			Intent intent = new Intent(action, Uri.parse(uri) );
			startActivity(intent);

			break;

		case Menu.FIRST + 1:

			Toast.makeText(this, R.string.main_menu_version, Toast.LENGTH_SHORT).show();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_version);
			builder.setMessage(R.string.app_version);
			builder.setPositiveButton(R.string.lbl_close, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					setResult(RESULT_OK);
				}
			});
			builder.create();
			builder.show();

			break;

		case Menu.FIRST + 2:

			// Toast.makeText(this, R.string.main_menu_exit, Toast.LENGTH_SHORT).show();
			finishApplication();
			break;

		default:
			break;
		}

		return ret;
	}

	private View.OnClickListener listenerGetData = new View.OnClickListener() {

		public void onClick(View view) {
			// TODO 自動生成されたメソッド・スタブ
			Intent intent = new Intent(MainActivity.this, GetDataActivity.class);
			startActivity(intent);
		}
	};

	private View.OnClickListener listenerEditData = new View.OnClickListener() {

		public void onClick(View view) {
			// TODO 自動生成されたメソッド・スタブ
			Intent intent = new Intent(MainActivity.this, EditDataActivity.class);
			startActivity(intent);
		}
	};

	private void finishApplication() {
		setResult(RESULT_OK);
		finish();
	}
}