package cn.toddapp.andump.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.toddapp.andump.R;

public class DumpPackageHeaderAdapter extends BaseAdapter {

	private ArrayList<DumpPackageHeader> dumpPackageHeaderArrayList;
	private Context context;

	public DumpPackageHeaderAdapter(
			ArrayList<DumpPackageHeader> dumpPackageHeaderArrayList,
			Context context) {
		this.dumpPackageHeaderArrayList = dumpPackageHeaderArrayList;
		this.context = context;
	}

	public int getCount() {
		return this.dumpPackageHeaderArrayList.size();
	}

	public Object getItem(int arg0) {
		return this.dumpPackageHeaderArrayList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// refer to
		// http://www.d-android.com/developer/forum.php?mod=viewthread&tid=21777
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DumpPackageHeader dumpPackageHeader = this.dumpPackageHeaderArrayList
				.get(arg0);
		View view = layoutInflater.inflate(R.layout.list_item, null);
		TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
		long gmtTime = dumpPackageHeader.getGmtTime();
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date(
				gmtTime * 1000));
		timeTextView.setText(String.valueOf("Time: " + time + "."
				+ dumpPackageHeader.getMicroTime()));
		TextView lengthTextView = (TextView) view
				.findViewById(R.id.lengthTextView);
		lengthTextView.setText("Size: " + dumpPackageHeader.getDumpLength());
		return view;
	}
}
