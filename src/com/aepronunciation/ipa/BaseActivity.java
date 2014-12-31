package com.aepronunciation.ipa;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseActivity extends ActionBarActivity implements
		KeyboardFragment.Communicator {
	
	protected static final String PREFS_NAME = "MyPrefsFile";
	protected static final String TEST_NAME_KEY = "testName";
	protected static final String NUMBER_OF_QUESTIONS_KEY = "numberOfQuestions";
	protected static final String TEST_MODE_KEY = "testMode";
	protected static final String TIME_LEARN_SINGLE_KEY = "timeLearnSingle";
	protected static final String TIME_LEARN_DOUBLE_KEY = "timeLearnDouble";
	protected static final String TIME_PRACTICE_SINGLE_KEY = "timePracticeSingle";
	protected static final String TIME_PRACTICE_DOUBLE_KEY = "timePracticeDouble";
	protected static final String TIME_TEST_SINGLE_KEY = "timeTestSingle";
	protected static final String TIME_TEST_DOUBLE_KEY = "timeTestDouble";
	protected static final long TIME_DEFAULT = 0;

	public static final String KEYBOARD_FRAGMENT_TAG = "keyboard";
	public static final char BACKSPACE = '\u232b';
	public static final char ENTER = '\n';

	KeyboardFragment keyboardFragment;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the hardware buttons to control the volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// TODO as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent;

		// Handle presses on the action bar items
		switch (id) {
		case R.id.action_history:
			// Start history activity
			intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_about:
			// Start about activity
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	// converts time in nanoseconds to hours, minutes, and seconds
	protected CharSequence getTimeString(long time) {

		int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(time);
		int hours = seconds / 3600;
		seconds = seconds % 3600;
		int minutes = seconds / 60;
		seconds = seconds % 60;

		StringBuilder timeString = new StringBuilder();
		if (hours > 1) {
			timeString.append(hours + " hours");
		} else if (hours == 1) {
			timeString.append("1 hour");
		}
		if (timeString.length() > 0) {
			if (minutes > 1) {
				timeString.append(", " + minutes + " minutes");
			} else if (minutes == 1) {
				timeString.append(", 1 minute");
			}
		} else {
			if (minutes > 1) {
				timeString.append(minutes + " minutes");
			} else if (minutes == 1) {
				timeString.append("1 minute");
			}
		}
		if (timeString.length() > 0) {
			if (seconds > 1) {
				timeString.append(", " + seconds + " seconds");
			} else if (seconds == 1) {
				timeString.append(", 1 second");
			}
		} else {
			if (seconds > 1) {
				timeString.append(seconds + " seconds");
			} else if (minutes == 1) {
				timeString.append("1 second");
			}
		}

		return timeString.toString();
	}

}
