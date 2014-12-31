package com.aepronunciation.ipa;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		
		
		TextView tvAboutAppName = (TextView) findViewById(R.id.tvAboutAppName);
		TextView tvVersion = (TextView) findViewById(R.id.tvAboutVersion);
		String appName = getResources().getString(R.string.app_name);
		String appVersion = "";
		try {
			appVersion = getApplicationContext()
					.getPackageManager()
					.getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		tvAboutAppName.setText(appName);
		tvVersion.setText("Version " + appVersion);
		
		
		
	}

	public void contactClick(View v) {

		// Start test activity
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aepronunciation.com/app/"));
		startActivity(browserIntent);
	}
	
	@Override
	public void onKeyTouched(String keyString) {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// hide the menu
		return false;
	}


}
