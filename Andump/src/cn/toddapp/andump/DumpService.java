package cn.toddapp.andump;

import java.io.IOException;

import cn.toddapp.andump.data.DumpSettings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

public class DumpService extends Service {

	// Context context = this;
	public static final int REQUEST_CODE_START_DUMP = 10;
	public static final int REQUEST_CODE_STOP_DUMP = 20;
	private static final int NOTIFICATION_ID = 101;
	
	//private boolean isDumping = false;
	private Process dumpProcess = null; 

	private DumpServiceBinder dumpServiceBinder = new DumpServiceBinder();

	public IBinder getBinder() {
		return dumpServiceBinder;
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("d", "DumpService bind");
		return dumpServiceBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("d", "DumpService unbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		Log.d("d", "DumpService create");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("d", "DumpService start");
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d("d", "DumpService destroy");
		super.onDestroy();
	}

	private void showNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon,
				"start dump", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent();
		intent.setClass(this, ControlActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(this, "Andump",
				"The Andump is running...", contentIntent);
		notificationManager.notify(DumpService.NOTIFICATION_ID, notification);
	}

	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	private boolean startDump(){
		if(dumpProcess == null){
			try {
				Log.v("dumpcmd",DumpSettings.getSettings().getDumpCommand());
				dumpProcess = Runtime.getRuntime().exec(DumpSettings.getSettings().getDumpCommand());
			} catch (IOException e) {
				e.printStackTrace();
				dumpProcess = null;
				//isDumping = false;
				return (dumpProcess == null);
			}
			//isDumping = true;
		}
		return (dumpProcess == null);
	}
	
	private boolean stopDump(){
		if(dumpProcess != null){
			dumpProcess.destroy();
			//isDumping = false;
			dumpProcess = null;
		}
		return (dumpProcess == null);
	}
	
	public class DumpServiceBinder extends Binder {

		public DumpService getService() {
			return DumpService.this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
			// super.onTransact(code, data, reply, flags);
			// http://www.d-android.com/developer/thread-32777-1-1.html
			switch (code) {
			case DumpService.REQUEST_CODE_START_DUMP:
				Log.d("d", "start dump");
				startDump();
				showNotification();
				break;
			case DumpService.REQUEST_CODE_STOP_DUMP:
				Log.d("d", "stop dump");
				stopDump();
				cancelNotification();
				break;
			default:
				break;
			}
			return (dumpProcess == null);
		}

	}

}