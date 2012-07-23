package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShownApplicationsPreference extends Activity {
	
	private ListView lv;
	private ArrayList<ActivityInfo> ailist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shown_applications_preference);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);
		
		lv = (ListView) findViewById(R.id.shownapps_listView);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);
		
		IntentHelperPreferences prefs = new IntentHelperPreferences(this);
		addActivitiesToAdapter(adapter, prefs.getShownApplications());
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				IntentHelperPreferences pref = new IntentHelperPreferences(getApplicationContext());
				List<String> apps = new ArrayList<String>();
				// アプリが増えるほど遅くなると思う
				SparseBooleanArray arr = lv.getCheckedItemPositions();
				for (int i = 0; i < arr.size(); i++) {
					if (arr.valueAt(i)) {
						apps.add(ailist.get(arr.keyAt(i)).name);
					}
				}
				pref.setShownApplications((String[]) apps.toArray(new String[0]));
			}
		});
	}
	
	private void addActivitiesToAdapter(ArrayAdapter<String> adapter, String[] selectedIntentNames)
	{
		int i = 0;
		List<Integer> checks = new ArrayList<Integer>();
		PackageManager pm = this.getPackageManager();
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("text/plain");
		
		ailist = new ArrayList<ActivityInfo>();
		
		List<ResolveInfo> resolveinfo = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo app : resolveinfo) {
			ailist.add(app.activityInfo);
			adapter.add((String) app.activityInfo.loadLabel(pm));
			// たくさんアプリが選択されてると遅そう
			for (String selected : selectedIntentNames) {
				if (selected.equals(app.activityInfo.name)) {
					checks.add(i);
				}
			}
			i++;
		}
		
		for (Integer c : checks)
		{
			lv.setItemChecked(c, true);
		}
	}

}
