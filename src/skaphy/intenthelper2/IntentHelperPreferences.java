package skaphy.intenthelper2;

import android.content.Context;
import android.content.SharedPreferences;

public class IntentHelperPreferences
{

	private SharedPreferences pref;

	private final int DEFAULT_REDIRECT_LIMIT = 10;
	
	public IntentHelperPreferences(Context context)
	{
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

}
