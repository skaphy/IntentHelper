package skaphy.intenthelper2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class Util {

	public static ActivityInfo[] getActivities(Context context, String type, String action) {
		Intent intent = new Intent();
		List<ActivityInfo> ais = new ArrayList<ActivityInfo>();

		PackageManager pm = context.getPackageManager();
		intent.setType(type);
		intent.setAction(action);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			ais.add(resolveInfo.activityInfo);
		}

		return ais.toArray(new ActivityInfo[0]);
	}

}
