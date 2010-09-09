package my.app.getdata;

import my.app.R;
import my.app.editdata.provider.Calllog;
import my.app.getdata.callback.ICallbackListener;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GetDataActivity extends Activity {

	private Button button_getstart;
	private Button button_getstop;
	private TextView text_status;
	private TextView text_result;

    private IGetDataService service;

    private boolean isBind;

	private Handler handle = new Handler() {
		/* (非 Javadoc)
		 * @see android.os.Handler#dispatchMessage(android.os.Message)
		 */
		@Override
		public void dispatchMessage(Message msg) {
			// TODO 自動生成されたメソッド・スタブ
			switch (msg.what) {
			case GetDataService.ON_CREATE:
				setButtonEnabled(button_getstart, true);
				setButtonEnabled(button_getstop, false);

				break;
			case GetDataService.ON_START:
//				Toast.makeText(GetDataActivity.this, R.string.msg_getstart, Toast.LENGTH_SHORT).show();
				text_status.setText(R.string.status_process);
				setButtonEnabled(button_getstart, false);
				setButtonEnabled(button_getstop, true);

				break;
			case GetDataService.ON_STOP:
//				Toast.makeText(GetDataActivity.this, R.string.msg_getstop, Toast.LENGTH_SHORT).show();
				text_status.setText(R.string.status_stop);
				setButtonEnabled(button_getstart, true);
				setButtonEnabled(button_getstop, false);

				break;
			case GetDataService.ON_FINISH:
//				Toast.makeText(GetDataActivity.this, R.string.msg_getfinish, Toast.LENGTH_SHORT).show();
				text_status.setText(R.string.status_finish);
				text_result.setText( getResultText() );
				setButtonEnabled(button_getstart, true);
				setButtonEnabled(button_getstop, false);

				break;
			default:
				super.dispatchMessage(msg);

				break;
			}
		}
	};

	private ICallbackListener listenerCallback = new ICallbackListener.Stub() {
		public void callbackMessage(int msg) throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			handle.sendEmptyMessage(msg);
		}
	};

	/* 生成時 */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.getdata);

		/* 閉じるボタン設定 */
		Button button_close = (Button) findViewById(R.id.getdata_btn_close);
		button_close.setOnClickListener(listenerClose);

		/* 開始ボタン設定 */
		button_getstart = (Button) findViewById(R.id.getdata_btn_getstart);
		button_getstart.setOnClickListener(listenerGetStart);

		/* 停止ボタン設定 */
		button_getstop = (Button) findViewById(R.id.getdata_btn_getstop);
		button_getstop.setOnClickListener(listenerGetStop);

		/* ステータス表示 */
		text_status = (TextView) findViewById(R.id.getdata_status);

		/* 取得結果表示 */
		text_result = (TextView) findViewById(R.id.getdata_result);

		isBind = false;

		/* 画面起動と同時にサービスに接続 */
		Intent intent = new Intent(IGetDataService.class.getName() );
		bindService(intent, conn, BIND_AUTO_CREATE);
	}

	/* 開放時
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ

		/* 画面開放時に接続終了 */
		if (isBind) {
			unbindService(conn);
		}

		super.onDestroy();
	}


	/* 閉じるボタン */
	private View.OnClickListener listenerClose = new View.OnClickListener() {

		public void onClick(View view) {
			// TODO 自動生成されたメソッド・スタブ
			setResult(RESULT_OK);
			finish();
		}
	};

	/* 開始ボタン */
	private View.OnClickListener listenerGetStart = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ

			/* 連打防止 */
			try {
				// Toast.makeText(context, service.echo("hoge"), Toast.LENGTH_SHORT).show();
				service.start();
			} catch (RemoteException e) {
				// TODO 自動生成された catch ブロック
				Log.v("GetDataActivity", e.getMessage(), e);
			}
		}
	};

	/* 中止ボタン */
	private View.OnClickListener listenerGetStop = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ

			/* 連打防止 */
			setButtonEnabled(button_getstop, false);

			try {
				service.stop();
			} catch (RemoteException e) {
				// TODO 自動生成された catch ブロック
				Log.v("GetData", e.getMessage(), e);
			}
		}
	};

	/* サービス接続用内部クラス */
	private ServiceConnection conn = new ServiceConnection() {

		/* 接続時 */
		public void onServiceConnected(ComponentName componentName, IBinder binder) {
			// TODO 自動生成されたメソッド・スタブ
			service = IGetDataService.Stub.asInterface(binder);

			try {
				service.addListener(listenerCallback);
				service.notifyStatus();
			} catch (RemoteException e) {
				// TODO 自動生成された catch ブロック
                Log.e("GetDataActivity", e.getMessage(), e);
                //Toast.makeText(context, "connection failed.", Toast.LENGTH_SHORT);
			}

			isBind = true;
		}

		/* 切断時 */
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自動生成されたメソッド・スタブ
			try {
				service.removeListener(listenerCallback);
			} catch (RemoteException e) {
				// TODO 自動生成された catch ブロック
                Log.e("GetDataActivity", e.getMessage(), e);
			}

			service = null;
		}
	};

	/* ボタン有効化設定処理 */
	private void setButtonEnabled(Button button, boolean enabled) {
		button.setEnabled(enabled);
		button.invalidate();
	}

	/* 取得結果表示 */
	private String getResultText() {

		String[] col = { Calllog.Calllogs._ID,
				Calllog.Calllogs.DATE,
				Calllog.Calllogs.TYPE,
				Calllog.Calllogs.NUMBER };

		Cursor managedCursor = managedQuery(Calllog.Calllogs.CONTENT_URI, col, null, null, null);
		String str =
			Calllog.Calllogs._ID + "|"
			+ Calllog.Calllogs.DATE + "|"
			+ Calllog.Calllogs.TYPE + "|"
			+ Calllog.Calllogs.NUMBER
			+ System.getProperty("line.separator");

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

			count++;
		}
		str = String.valueOf(count) + " record selected." + System.getProperty("line.separator") + str;

		return str;
	}
}
