package cn.toddapp.andump;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import cn.toddapp.andump.layerReader.ILayerReader;
import cn.toddapp.andump.util.DumpFileReader;
import cn.toddapp.andump.util.DumpPackageHeader;
import cn.toddapp.andump.util.DumpPackageHeaderAdapter;
import cn.toddapp.andump.util.DumpPackageReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadActivity extends Activity {

	private static final int MESSAGE_CODE_DISMISS_PROGREESSDIALOG = 101;
	private static final int MESSAGE_CODE_SHOW_PRPGRESSDIALOG = 102;
	private static final int MESSAGE_CODE_UPDATE_LISTVIEW = 103;
	private static final int TASK_CODE_TOUGH_FILE_READ = 201;
	private static final int TASK_CODE_PACKAGE_READ = 202;
	private static final int MENU_ID_PREV_PAGE = Menu.FIRST;
	private static final int MENU_ID_NEXT_PAGE = Menu.FIRST + 1;
	private static final String LOGTAG = "ReadActivity";

	private ProgressDialog progressDialog = null;
	private ListView packageListView;

	private ArrayList<DumpPackageHeader> dumpPackageHeaders;
	private DumpPackageHeaderAdapter dumpPackageHeaderAdapter;

	private int position = 24;
	private int packageNumbers = 20;

	private String progressDialogTitle = "";
	private String progressDialogMessage = "";
	private int taskId = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ReadActivity.MESSAGE_CODE_DISMISS_PROGREESSDIALOG:
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				break;
			case ReadActivity.MESSAGE_CODE_SHOW_PRPGRESSDIALOG:
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(ReadActivity.this);
					progressDialog.setCancelable(false);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setTitle(progressDialogTitle);
					progressDialog.setMessage(progressDialogMessage);
					progressDialog.show();
				}
				break;
			case ReadActivity.MESSAGE_CODE_UPDATE_LISTVIEW:
				packageListView.setAdapter(dumpPackageHeaderAdapter);
				dumpPackageHeaderAdapter.notifyDataSetChanged();
				packageListView
						.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Log.d(LOGTAG, "" + arg2);
								showPackageInfo("/sdcard/dump.cap",
										dumpPackageHeaders.get(arg2));
							}
						});
				Log.d(LOGTAG, "finish update listview");
				break;
			default:
				break;
			}
			// super.handleMessage(msg);
		}

	};

	private void showPackageInfo(String dumpFilePath, DumpPackageHeader header) {
		DumpPackageReader reader;
		try {
			reader = new DumpPackageReader(dumpFilePath, header);
			reader.read();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		ILayerReader[] layerReaders = { reader.getLinkLayerReader(),
				reader.getNetworkLayerReader(),
				reader.getTransportLayerReader() };

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < layerReaders.length; i++) {
			if(layerReaders[i] == null)
				continue;
			Iterator iterator = layerReaders[i].getLayerProperties().entrySet()
					.iterator();
			builder.append("\n " + layerReaders[i].getLayerType() + " layer\n");
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				builder.append("\t" + entry.getKey() + " : " + entry.getValue());
				builder.append("\n");
			}
			builder.append("\n---------------------------------");
		}

		// http://www.cnblogs.com/luxiaofeng54/archive/2011/03/17/1987003.html
		Log.d("read activity", builder.toString());

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.package_info,
				(ViewGroup) findViewById(R.id.package_info));
		TextView textView = (TextView) view.findViewById(R.id.textView1);
		textView.setText(builder.toString());
		
		

		new AlertDialog.Builder(this).setTitle("package info")
				.setView(view).setPositiveButton("OK", null).show();
		// System.out.println("UDP Layer");
		// Iterator iterator = this.properties.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Entry entry = (Entry) iterator.next();
		// System.out
		// .println("\t" + entry.getKey() + " : " + entry.getValue());
		// }

	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.read);

		packageListView = (ListView) findViewById(R.id.packageListView);
		doTask("Processing the file", "Please Wait...",
				TASK_CODE_TOUGH_FILE_READ);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ID_PREV_PAGE, 0, "Prev").setIcon(
				android.R.drawable.ic_media_previous);
		menu.add(0, MENU_ID_NEXT_PAGE, 0, "Nexe").setIcon(
				android.R.drawable.ic_media_next);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case  MENU_ID_NEXT_PAGE:
			DumpPackageHeader header = dumpPackageHeaders
					.get(dumpPackageHeaders.size()-1);
			position = header.getPosition();
			doTask("","",ReadActivity.TASK_CODE_TOUGH_FILE_READ);
			break;
		case MENU_ID_PREV_PAGE:
			//setTitle("单击了菜单子项2");
			break;
		}
		return true;
	}

	public void doTask(String taskTitle, String taskMessage, int taskId) {
		this.progressDialogTitle = taskTitle;
		this.progressDialogMessage = taskMessage;
		this.taskId = taskId;
		new Thread() {
			public void run() {
				doTaskById();
			}
		}.start();

	}

	private void doTaskById() {
		switch (taskId) {
		case ReadActivity.TASK_CODE_PACKAGE_READ:
			//Log.d(LOGTAG, "do package read");
			/**
			 * 未实现功能
			 */
			break;
		case ReadActivity.TASK_CODE_TOUGH_FILE_READ:
			Log.d(LOGTAG, "do tough file read");
			try {
				dumpPackageHeaders = new DumpFileReader("/sdcard/dump.cap")
						.getDumpPackageHeaders(position, packageNumbers);
				dumpPackageHeaderAdapter = new DumpPackageHeaderAdapter(
						dumpPackageHeaders, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("Read Activity", "finish initial adapter");
			handler.sendEmptyMessage(MESSAGE_CODE_UPDATE_LISTVIEW);

			break;
		default:
			Log.d(LOGTAG, "unknown task id");
			break;
		}

	}

}
