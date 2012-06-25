package skaphy.intenthelper;

import java.io.IOException;
import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;

public class IntentHelperMain extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "Expanding url... Please wait...", Toast.LENGTH_SHORT).show();
		ExpandURL.showChooser(this, getExtraUrl());
		finish();
	}

	String getExtraUrl()
	{
		return getIntent().getExtras().getString(Intent.EXTRA_TEXT).toString();
	}

}

class ExpandURL extends AsyncTaskLoader
{

	// リダイレクトは最大10個先まで追う
	private final int DEFAULT_LIMIT = 10;

	private String  url;
	private Context context;
	private Handler handler;

	public ExpandURL(Context _context, String _url) {
		super(_context);
		context = _context;
		url = _url;
		handler = new Handler();
	}
	
	public static ExpandURL showChooser(Context _context, String _url)
	{
		ExpandURL eurl = new ExpandURL(_context, _url);
		eurl.forceLoad();
		return eurl;
	}

	@Override
	public Object loadInBackground()
	{
		String _redirect_to = "";
		try {
			_redirect_to = expandUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final String redirect_to = _redirect_to;

		handler.post(new Runnable(){
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, redirect_to);
				context.startActivity(Intent.createChooser(intent, redirect_to));
			}
		});
		return null;
	}

	private String expandUrl(String url) throws IOException
	{
		return expandUrl(url, DEFAULT_LIMIT);
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
