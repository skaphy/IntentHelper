package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class OnetapIntentPreference extends Activity {

	private ListView lv;
	private List<ActivityInfo> ailist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onetap_intent_preference);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);

		lv = (ListView) findViewById(R.id.onetap_applications_listview);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setAdapter(adapter);

		IntentHelperPreferences pref = new IntentHelperPreferences(getApplicationContext());
		if (pref.getOnetapIntent() != null) {
			addActivitiesToAdapter(adapter, pref.getOnetapIntent().getComponent().getClassName());
		} else {
			addActivitiesToAdapter(adapter, "");
		}
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				IntentHelperPreferences pref = new IntentHelperPreferences(getApplicationContext());
				ActivityInfo ai = ailist.get(pos);
				if (ai == null) {
					pref.setOnetapIntent(null, null);
				} else {
					pref.setOnetapIntent(ai.packageName, ai.name);
				}
			}
		});
	}
	
	private void addActivitiesToAdapter(ArrayAdapter<String> adapter, String selectedIntentName)
	{
		int i = 1, checked = 0;
		PackageManager pm = this.getPackageManager();

		ActivityInfo[] ais = Util.getActivities(this, "text/plain", Intent.ACTION_SEND);
		ailist = new ArrayList<ActivityInfo>();
		
		adapter.add("Disable");
		ailist.add(null);
		
		for (ActivityInfo ai : ais) {
			ailist.add(ai);
			adapter.add((String) ai.loadLabel(pm));
			if (selectedIntentName.equals(ai.name)) {
				checked = i;
			}
			i++;
		}

		lv.setItemChecked(checked, true);
	}

}

