package skaphy.intenthelper2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class IntentHelperPreferencesActivity extends PreferenceActivity
{

	private final Uri howtouse_uri = Uri.parse("http://skaphylog.tumblr.com/");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
		findPreference("howtouse").setOnPreferenceClickListener(new OnPreferenceClickListener(){
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(Intent.ACTION_VIEW, howtouse_uri));
				return true;
			}
		});
	}
}