package my.app.getdata;

import my.app.getdata.callback.ICallbackListener;

interface IGetDataService {
	String echo(String message);

	void stop();
	void start();

	void notifyStatus();
	void addListener(ICallbackListener listener);
	void removeListener(ICallbackListener listener);
}
