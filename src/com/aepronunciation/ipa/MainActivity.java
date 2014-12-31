package com.aepronunciation.ipa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * if (savedInstanceState == null) {
		 * getSupportFragmentManager().beginTransaction() .add(R.id.container,
		 * new PlaceholderFragment()).commit(); }
		 */
	}

	public void learnSingleClick(View v) {

		// Start learn activity
		Intent intent = new Intent(this, LearnSingleActivity.class);
		startActivity(intent);
	}
	
	public void learnDoubleClick(View v) {

		// Start learn activity
		Intent intent = new Intent(this, LearnDoubleActivity.class);
		startActivity(intent);
	}

	public void practiceSingleClick(View v) {

		// Start practice activity
		Intent intent = new Intent(this, PracticeSingleActivity.class);
		startActivity(intent);
	}
	
	public void practiceDoubleClick(View v) {

		// Start practice activity
		Intent intent = new Intent(this, PracticeDoubleActivity.class);
		startActivity(intent);
	}
	
	public void testClick(View v) {

		// Start test activity
		Intent intent = new Intent(this, TestSetupActivity.class);
		startActivity(intent);
	}
	
	/*public void testDoubleClick(View v) {

		// Start test activity
		Intent intent = new Intent(this, TestActivity.class);
		startActivity(intent);
	}*/

	@Override
	public void onKeyTouched(String keyString) {
		
	}

}
