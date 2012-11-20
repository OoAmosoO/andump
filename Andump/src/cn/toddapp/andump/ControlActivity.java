package cn.toddapp.andump;

import cn.toddapp.andump.data.DumpSettings;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ControlActivity extends Activity {

	private Button dumpButton;
	private Button readButton;
	private ImageButton configImageButton;

	private boolean isDumping = false;

	private DumpService dumpService;
	private ServiceConnection dumpServiceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			Log.d("d", "service connected");
			System.out.println("have a test");
			dumpService = ((DumpService.DumpServiceBinder) binder).getService();
		}

		public void onServiceDisconnected(ComponentName componentName) {
			Log.d("d", "service disconnected");
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);

		this.bindService(new Intent("cn.toddapp.andump.dumpservice"),
				dumpServiceConnection, BIND_AUTO_CREATE);

		readIntent();
		findViews();
		setListeners();
		startTask();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//Toast.makeText(this, DumpSettings.getSettings().getDumpCommand(), Toast.LENGTH_LONG).show();
		Log.v("dumpcmd",DumpSettings.getSettings().getDumpCommand());
	}
	
	@Override
	protected void onDestroy() {
		try {
			this.stopDump();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.unbindService(dumpServiceConnection);
		super.onDestroy();
	}

	private void readIntent() {

	}

	private void findViews() {
		dumpButton = (Button) findViewById(R.id.dumpButton);
		readButton = (Button) findViewById(R.id.readButton);
		configImageButton = (ImageButton) findViewById(R.id.imageButton1);
	}

	private void setListeners() {
		dumpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					if (!isDumping) {
						startDump();
						isDumping = true;
						dumpButton.setText("Stop");
						readButton.setEnabled(false);
						configImageButton.setEnabled(false);
					} else {
						stopDump();
						isDumping = false;
						dumpButton.setText("Start");
						readButton.setEnabled(true);
						configImageButton.setEnabled(true);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		});

		readButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent().setClass(ControlActivity.this,
						ReadActivity.class));
			}
		});

		configImageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent().setClass(ControlActivity.this,
						ConfigActivity.class));
			}
		});
	}

	private void startTask() {

	}

	private void startDump() throws RemoteException {
		dumpService.getBinder().transact(DumpService.REQUEST_CODE_START_DUMP,
				null, null, 0);
	}

	private void stopDump() throws RemoteException {
		dumpService.getBinder().transact(DumpService.REQUEST_CODE_STOP_DUMP,
				null, null, 0);
	}

}