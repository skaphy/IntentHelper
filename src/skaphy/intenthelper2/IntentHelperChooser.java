package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class IntentHelperChooser extends Activity {
	
	private Uri expanded_uri;
	private IntentHelperPreferences prefs;
	private ChooserAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_helper_chooser);
		
		prefs = new IntentHelperPreferences(this);
		
		// 表示するアプリを決める
		String[] apps = prefs.getShownApplications();
		ActivityInfo[] all_activityinfos = Util.getActivities(this, "text/plain", Intent.ACTION_SEND);
		List<ActivityInfo> activityinfos = new ArrayList<ActivityInfo>();
		for (ActivityInfo ainfo : all_activityinfos) {
			if (apps.length == 0) {
				activityinfos.add(ainfo);
			} else {
				for (String app : apps) {
					if (app.equals(ainfo.name)) {
						activityinfos.add(ainfo);
						break;
					}
				}
			}
		}
		
		// リストビュー設定
		ListView lv = (ListView) findViewById(R.id.chooser_applications_listview);
		adapter = new ChooserAdapter(this, activityinfos);
		lv.setAdapter(adapter);
		
		// 展開後のURLを取得・表示
		expanded_uri = Uri.parse(getIntent().getExtras().getString(Intent.EXTRA_TEXT));
		((TextView) findViewById(R.id.chooser_fullurl)).setText(expanded_uri.toString());
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				ActivityInfo ai = (ActivityInfo) adapter.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra(Intent.EXTRA_TEXT, expanded_uri.toString());
				intent.setClassName(ai.packageName, ai.name);
				view.getContext().startActivity(intent);
			}
		});
	}

}

class ChooserAdapter extends BaseAdapter {

	private Context context;
	private List<ActivityInfo> list;
	private LayoutInflater inflater;
	
	public ChooserAdapter(Context ctx, List<ActivityInfo> li) {
		context = ctx;
		list = li;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		if (view == null) {
			view = inflater.inflate(R.layout.chooser_row, null);
		}
		PackageManager pm = context.getPackageManager();
		ActivityInfo ai = list.get(position);
		if (ai != null) {
			ImageView icon = (ImageView) view.findViewById(R.id.chooser_row_icon);
			TextView name = (TextView) view.findViewById(R.id.chooser_row_name);
			icon.setImageDrawable(pm.getDrawable(ai.packageName, ai.getIconResource(), ai.applicationInfo));
			name.setText(ai.loadLabel(pm));
		}
		return view;
	}
	
}
