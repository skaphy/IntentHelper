package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	private final Uri howtouse_uri = Uri.parse("http://skaphylog.tumblr.com/");

	private final int LVID_HOWTOUSE = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init_listview();
	}
	
	private void init_listview()
	{
		ListView lv = (ListView) this.findViewById(R.id.listView1);
		List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
		SimpleAdapter adapter = new SimpleAdapter(
			this,
			dataList,
			android.R.layout.simple_expandable_list_item_2,
			new String[]{"title", "description"},
			new int[]{android.R.id.text1, android.R.id.text2}
		);
		lv.setAdapter(adapter);

		Map<String, String> data;

		/*
		data = new HashMap<String, String>();
		data.put("title", "リダイレクトの回数");
		data.put("description", "一度に追うリダイレクトの回数の指定");
		dataList.add(data);

		// 常に指定のアプリを起動するようにする
		data = new HashMap<String, String>();
		data.put("title", "表示しないアプリ");
		data.put("description", "chooserに表示しないアプリの選択");
		dataList.add(data);
		*/

		data = new HashMap<String, String>();
		data.put("title", getString(R.string.menu_howtouse));
		data.put("description", getString(R.string.menu_howtouse_description));
		dataList.add(LVID_HOWTOUSE, data);

		data = new HashMap<String, String>();
		data.put("title", getString(R.string.menu_version));
		data.put("description", getString(R.string.app_version));
		dataList.add(data);

		lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
			{
				if (id == LVID_HOWTOUSE)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, howtouse_uri));
				}
			}
		});
	}

}
