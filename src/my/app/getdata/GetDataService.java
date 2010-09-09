package my.app.getdata;

import java.util.Date;

import my.app.R;
import my.app.editdata.provider.Calllog;
import my.app.getdata.callback.ICallbackListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class GetDataService extends Service {

	private final Uri myCalllogUri = Calllog.Calllogs.CONTENT_URI;
	private final Uri calllogUri = android.provider.CallLog.Calls.CONTENT_URI;

	private final int DUMMY_INTERVAL = 3000;

	public static final int ON_CREATE = 0;
	public static final int ON_START = 1;
	public static final int ON_STOP = 2;
	public static final int ON_FINISH = 3;

	private RemoteCallbackList<ICallbackListener> listeners = new RemoteCallbackList<ICallbackListener>();
	private boolean isStart;

	private Thread threadGetData = null;

	private final IGetDataService.Stub binder = new IGetDataService.Stub() {

		public void addListener(ICallbackListener listener)
				throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			listeners.register(listener);
		}

		public void removeListener(ICallbackListener listener)
				throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			listeners.unregister(listener);
		}

		public void stop() throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			isStart = false;
			threadGetData.interrupt();
		}

		public void start() throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			startThread();
		}

		public String echo(String message) throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			return message;
		}

		public void notifyStatus() throws RemoteException {
			// TODO 自動生成されたメソッド・スタブ
			if (isStart) {
				callbackMessage(ON_START);
			}
			else {
				if (threadGetData == null) {
					callbackMessage(ON_CREATE);
				}
				else {
					callbackMessage(ON_FINISH);
				}
			}
		}
	};

	/* (非 Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate();
		isStart = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onUnbind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		startThread();
	}

	private void startThread() {
		if (!isStart) {
			threadGetData = createThread();
			threadGetData.start();
		}
	}

	private Thread createThread() {
		return new Thread() {
			public void run() {
				isStart = true;

				copyDBCalllogToSchedule();
//				sleepTest();

				isStart = false;
			}

			private void copyDBCalllogToSchedule() {
				try {
					callbackMessage(ON_START);

					String[] col = { android.provider.CallLog.Calls.DATE,
							android.provider.CallLog.Calls.TYPE,
							android.provider.CallLog.Calls.NUMBER };

					Date d = null;
					Cursor c = getContentResolver().query(calllogUri, col, null, null, null);
					ContentValues values = new ContentValues();
					while ( c.moveToNext() ) {

						Thread.sleep(DUMMY_INTERVAL);

						if (!isStart) {
							throw new InterruptedException();
						}

						values.clear();
						d = new Date( c.getLong(0) );
						values.put(Calllog.Calllogs.DATE, d.toLocaleString() );
						values.put(Calllog.Calllogs.TYPE, c.getString(1) );
						values.put(Calllog.Calllogs.NUMBER, c.getString(2) );
						getContentResolver().insert(myCalllogUri, values);

					}

					callbackMessage(ON_FINISH);
					showMessageToStatusBar( getString(R.string.msg_getfinish) );
				}
				catch(InterruptedException e) {
					callbackMessage(ON_STOP);
				}
			}

//			private void sleepTest() {
//				try {
//					int i = 0;
//					while (i <= 30) {
//						Thread.sleep(100);
//						i++;
//
//						if (bStop) {
//							throw new InterruptedException();
//						}
//					}
//					callbackMessage(NOTIFY_FINISH);
//					showMessageToStatusBar( getString(R.string.msg_getfinish) );
//				} catch(InterruptedException e) {
//					callbackMessage(NOTIFY_STOP);
//				}
//			}

		};
	}

	/* ステータスバーに通知 */
    private void showMessageToStatusBar(String message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon,
                message, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(), 0);
        notification.setLatestEventInfo(this, getText(R.string.app_name), message, contentIntent);

        manager.notify(R.string.app_name, notification);
    }

    /* コールバック */
	private void callbackMessage(int msg) {
	    int numListeners = listeners.beginBroadcast();

	    for (int i = 0; i < numListeners; i++) {
	        try {
	            listeners.getBroadcastItem(i).callbackMessage(msg);
	        } catch (RemoteException e) {
	            Log.e("CallbackService", e.getMessage(), e);
	        }
	    }

	    listeners.finishBroadcast();
	}
}
