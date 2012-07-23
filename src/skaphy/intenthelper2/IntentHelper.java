package skaphy.intenthelper2;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;

public class IntentHelper extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, getString(R.string.expanding_pleasewait), Toast.LENGTH_SHORT).show();
		ExpandURL.showChooser(this, getExtraUrl());
		finish();
	}

	String getExtraUrl()
	{
		return getIntent().getExtras().getString(Intent.EXTRA_TEXT).toString();
	}

}

class ExpandURL extends AsyncTask<String, Integer, Integer>
{

	private Context context;
	private Handler handler;
	
	public ExpandURL(Context _context)
	{
		super();
		context = _context;
	}

	@Override
	protected void onPreExecute()
	{
		handler = new Handler();
	}
	
	@Override
	protected Integer doInBackground(String... url)
	{
		String redirect_to = "";
		try {
			redirect_to = expandUrl(url[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		showChooser(redirect_to);
		return 0;
	}
	
	public static ExpandURL showChooser(Context _context, String _url)
	{
		ExpandURL eurl = new ExpandURL(_context);
		eurl.execute(_url);
		return eurl;
	}
	
	private void showChooser(final String _url)
	{
		handler.post(new Runnable(){
			public void run() {
				IntentHelperPreferences pref = new IntentHelperPreferences(context);
				if (pref.getOnetapIntent() == null)
				{
					Intent intent = new Intent(context, IntentHelperChooser.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS|Intent.FLAG_ACTIVITY_NO_HISTORY|
							Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Intent.EXTRA_TEXT, _url);
					context.startActivity(intent);
				}
				else
				{
					Intent intent = pref.getOnetapIntent();
					intent.putExtra(Intent.EXTRA_TEXT, _url);
					context.startActivity(intent);
				}
			}
		});
	}
	
	private String expandUrl(String url) throws IOException
	{
		IntentHelperPreferences pref = new IntentHelperPreferences(context);
		return expandUrl(url, pref.getMaxRedirectCount());
	}

	private String expandUrl(String url, int limit) throws IOException
	{
		DefaultHttpClient httpclient;
		HttpHead request;
		HttpResponse response;
		String redirect_to = url;
		
		for (int i = 0; i < limit; i++)
		{
			httpclient = new DefaultHttpClient();
			HttpClientParams.setRedirecting(httpclient.getParams(), false);
			request = new HttpHead(redirect_to);
			response = httpclient.execute(request);
			if (response.getFirstHeader("Location") == null) break;
			redirect_to = response.getFirstHeader("Location").getValue();
		};
		
		return redirect_to;
	}

};
