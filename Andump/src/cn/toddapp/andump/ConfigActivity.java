package cn.toddapp.andump;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import cn.toddapp.andump.data.DumpSettings;

public class ConfigActivity extends Activity {

	private String dumpType;
	private boolean filterDebug;
	private DumpSettings settings;

	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);

		settings = DumpSettings.getSettings();
		dumpType = settings.getDumpType();
		filterDebug = settings.isFilterDebug();

		ArrayList<String> typeArray = new ArrayList<String>();
		typeArray.add("ALL");
		typeArray.add("TCP");
		typeArray.add("IP");
		typeArray.add("UDP");

		ArrayList<String> filterArray = new ArrayList<String>();
		filterArray.add("Yes");
		filterArray.add("No");

		Spinner typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		Spinner filterSpinner = (Spinner) findViewById(R.id.filter_spinner);
		Button backButton = (Button) findViewById(R.id.button1);

		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				onBackPressed();

			}
		});

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeArray);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setPrompt("Choose Type:");
		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setSelection(typeArray.indexOf(dumpType));

		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				String type = spinner.getSelectedItem().toString();
				setDumpType(type);
				Toast.makeText(ConfigActivity.this, "Choose " + type,
						Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, filterArray);
		filterAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterSpinner.setPrompt("Filter ADB-Debug");
		filterSpinner.setAdapter(filterAdapter);
		filterSpinner.setSelection((filterDebug) ? 0 : 1);
		filterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				if (spinner.getSelectedItem().toString().equals("Yes")) {
					setFilterDebug(true);
					Toast.makeText(ConfigActivity.this, "Filter ADB-Debug",
							Toast.LENGTH_SHORT).show();
				} else {
					setFilterDebug(false);
					Toast.makeText(ConfigActivity.this, "Not Filter ADB-Debug",
							Toast.LENGTH_SHORT).show();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
		settings.setDumpType(dumpType);
	}

	public boolean isFilterDebug() {
		return filterDebug;
	}

	public void setFilterDebug(boolean filterDebug) {
		this.filterDebug = filterDebug;
		settings.setFilterDebug(filterDebug);
	}

}
