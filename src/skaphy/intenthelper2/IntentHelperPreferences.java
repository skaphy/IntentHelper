package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class IntentHelperPreferences
{

	private Context context;
	private SharedPreferences pref;

	private final int DEFAULT_REDIRECT_LIMIT = 10;
	
	public IntentHelperPreferences(Context ctx)
	{
		context = ctx;
		pref = context.getSharedPreferences("skaphy.intenthelper2_preferences", Context.MODE_PRIVATE);
	}
	
	int getMaxRedirectCount()
	{
		String val = pref.getString("max_redirect_count", String.valueOf(DEFAULT_REDIRECT_LIMIT));
		if (val.matches("^[0-9]+$"))
		{
			return Integer.parseInt(val);
		}
		setMaxRedirectCount(10);
		return getMaxRedirectCount();
	}
	
	void setMaxRedirectCount(int num)
	{
		setMaxRedirectCount(String.valueOf(num));
	}
	
	void setMaxRedirectCount(String num)
	{
		if (num.matches("^[0-9]+$"))
		{
			pref.edit().putString("max_redirect_count", String.valueOf(DEFAULT_REDIRECT_LIMIT)).commit();
		}
		else
		{
			pref.edit().putString("max_redirect_count", String.valueOf(DEFAULT_REDIRECT_LIMIT)).commit();
		}
	}
	
	public Intent getOnetapIntent()
	{
		String packageName = pref.getString("onetap_intent_package", null);
		String activityName = pref.getString("onetap_intent_activity", null);
		
		if (packageName == null || activityName == null)
		{
			return null;
		}
		
		Intent intent = new Intent();
		intent.setClassName(packageName, activityName);
		
		if (!isExistActivity(intent))
		{
			return null;
		}
		
		return intent;
	}
	
	public void setOnetapIntent(String packageName, String activityName)
	{
		if (packageName == null)
		{
			pref.edit().remove("onetap_intent_package").commit();
		}
		else
		{
			pref.edit().putString("onetap_intent_package", packageName).commit();
		}
		if (activityName == null)
		{
			pref.edit().remove("onetap_intent_activity").commit();
		}
		else
		{
			pref.edit().putString("onetap_intent_activity", activityName).commit();
		}
	}
	
	private boolean isExistActivity(Intent intent)
	{
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveinfo = pm.queryIntentActivities(intent, 0);
		return (resolveinfo.size() == 0)?false:true;
	}
	
	public String[] getShownApplications()
	{
		String[] apps;
		String appspref = pref.getString("shownapps", "");
		apps = appspref.split(",");
		
		// 存在するアクティビティのみにする
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("text/plain");
		List<String> apps_exists = new ArrayList<String>();
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveinfos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo ri : resolveinfos)
		{
			for (String app : apps)
			{
				if (ri.activityInfo.name.equals(app))
				{
					apps_exists.add(app);
				}
			}
		}
		
		return apps_exists.toArray(new String[0]);
	}
	
	public void setShownApplications(String[] apps)
	{
		StringBuilder appsjoined = new StringBuilder();
		for (String app : apps)
		{
			appsjoined.append(app);
			appsjoined.append(",");
		}
		pref.edit().putString("shownapps", appsjoined.toString()).commit();
	}

}
