package skaphy.intenthelper;

import java.io.IOException;
import java.net.URI;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import android.net.http.AndroidHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class IntentHelperMain extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Context context = this;
		Intent intent = getIntent();
		String action = intent.getAction();
		Bundle extras = intent.getExtras();
		final String url = extras.getCharSequence(Intent.EXTRA_TEXT).toString();
		Handler handler = new Handler();
		
		Toast.makeText(context, "Expanding url... Please wait...", Toast.LENGTH_SHORT).show();

		ExpandURL eu = new ExpandURL(this, url);
		eu.setHandler(handler);
		eu.forceLoad();
		
		finish();
	}

}

class ExpandURL extends AsyncTaskLoader<String>
{

	// リダイレクトは最大10個先まで追う
	private final int DEFAULT_LIMIT = 10;
	private final String USERAGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.4 Safari/537.1";

	private String  url;
	private Context context;
	private Handler handler;
	private String __url__;

	public ExpandURL(Context _context, String _url) {
		super(_context);
		url = _url;
		context = _context;
	}
	
	public void setHandler(Handler _handler)
	{
		handler = _handler;
	}

	@Override
	public String loadInBackground() {
		try {
			__url__ = expandUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.post(new Runnable(){
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, __url__);
				context.startActivity(Intent.createChooser(intent, __url__));
				Toast.makeText(context, __url__, Toast.LENGTH_LONG).show();
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
		//AndroidHttpClient httpclient;
		DefaultHttpClient httpclient;
		HttpHead request;
		HttpResponse response;
		String redirect_to = url;
		
		for (int i = 0; i < limit; i++)
		{
			//httpclient = AndroidHttpClient.newInstance(USERAGENT);
			httpclient = new DefaultHttpClient();
			HttpClientParams.setRedirecting(httpclient.getParams(), false);
			request = new HttpHead(redirect_to);
			response = httpclient.execute(request);
			//httpclient.close();
			if (response.getFirstHeader("Location") == null) break;
			redirect_to = response.getFirstHeader("Location").getValue();
		};
		
		return redirect_to;
	}

};
