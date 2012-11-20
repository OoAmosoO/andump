package cn.toddapp.andump.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.toddapp.andump.R;

public class DumpPackageAdapter extends BaseAdapter {

	private ArrayList<DumpPackage> dumpPackageArrayList;
	private Context context;

	public DumpPackageAdapter(ArrayList<DumpPackage> dumpPackageArrayList,
			Context context) {
		this.dumpPackageArrayList = dumpPackageArrayList;
		this.context = context;
	}

	public int getCount() {
		return this.dumpPackageArrayList.size();
	}

	public Object getItem(int arg0) {
		return this.dumpPackageArrayList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// refer to
		// http://www.d-android.com/developer/forum.php?mod=viewthread&tid=21777
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DumpPackageHeader dumpPackageHeader = this.dumpPackageArrayList.get(
				arg0).getDumpPackageHeader();
		View view = layoutInflater.inflate(R.layout.list_item, null);
		TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
		timeTextView.setText(String.valueOf(dumpPackageHeader.getGmtTime()));
		TextView lengthTextView = (TextView) view
				.findViewById(R.id.lengthTextView);
		lengthTextView
				.setText(String.valueOf(dumpPackageHeader.getDumpLength()));
		return view;
	}

}
