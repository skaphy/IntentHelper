package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;

public class MainActivity extends Activity {

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
		data.put("title", "使い方");
		data.put("description", "使い方をブラウザで開く");
		dataList.add(data);

		data = new HashMap<String, String>();
		data.put("title", "Version");
		data.put("description", "IntentHelper2(Pocketfxxker) 2.00");
		dataList.add(data);
	}

}
