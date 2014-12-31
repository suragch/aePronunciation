package com.aepronunciation.ipa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends BaseActivity {

	protected static final String PREFS_NAME = "MyPrefsFile";
	protected static final String TIME_LEARN_SINGLE_KEY = "timeLearnSingle";
	protected static final String TIME_LEARN_DOUBLE_KEY = "timeLearnDouble";
	protected static final String TIME_PRACTICE_SINGLE_KEY = "timePracticeSingle";
	protected static final String TIME_PRACTICE_DOUBLE_KEY = "timePracticeDouble";
	protected static final String TIME_TEST_SINGLE_KEY = "timeTestSingle";
	protected static final String TIME_TEST_DOUBLE_KEY = "timeTestDouble";
	protected static final long TIME_DEFAULT = 0;

	// TextView tvName;
	// Spinner spinner;
	TextView tvHighSingle;
	TextView tvHighDouble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		// Create objects
		TextView tvTime = (TextView) findViewById(R.id.tvTotalTime);
		tvHighSingle = (TextView) findViewById(R.id.tvHighScoreSingles);
		tvHighDouble = (TextView) findViewById(R.id.tvHighScoreDoubles);

		// get times from preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				MODE_PRIVATE);
		long learnSingleTime = settings.getLong(TIME_LEARN_SINGLE_KEY,
				TIME_DEFAULT);
		long learnDoubleTime = settings.getLong(TIME_LEARN_DOUBLE_KEY,
				TIME_DEFAULT);
		long practiceSingleTime = settings.getLong(TIME_PRACTICE_SINGLE_KEY,
				TIME_DEFAULT);
		long practiceDoubleTime = settings.getLong(TIME_PRACTICE_DOUBLE_KEY,
				TIME_DEFAULT);
		long testSingleTime = settings.getLong(TIME_TEST_SINGLE_KEY,
				TIME_DEFAULT);
		long testDoubleTime = settings.getLong(TIME_TEST_DOUBLE_KEY,
				TIME_DEFAULT);
		// add up the times
		long totalTime = learnSingleTime + learnDoubleTime + practiceSingleTime
				+ practiceDoubleTime + testSingleTime + testDoubleTime;
		// format in human readable
		tvTime.setText(getTimeString(totalTime));

		// Get the high scores
		new GetHighScores().execute();

	}

	public void timeDetailsClick(View v) {

		// Start learn activity
		Intent intent = new Intent(this, HistoryTimeActivity.class);
		startActivity(intent);
	}

	public void testDetailsClick(View v) {

		// Start learn activity
		Intent intent = new Intent(this, HistoryTestsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// hide the menu
		return false;
	}

	private class GetHighScores extends AsyncTask<Void, Void, int[]> {

		@Override
		protected int[] doInBackground(Void... params) {

			// android.os.Debug.waitForDebugger();

			int[] result = { -1, -1 };

			try {

				MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
						getApplicationContext());
				result = dbAdapter.getHighScores();
			} catch (Exception e) {
				//Log.i("app", e.toString());
			}

			return result;

		}

		@Override
		protected void onPostExecute(int[] result) {

			if (result[0] == -1) {
				tvHighSingle.setVisibility(View.GONE);
			} else {
				tvHighSingle.setText("Singles: "
						+ Integer.toString(result[0]) + "%");
			}
			
			if (result[1] == -1) {
				tvHighDouble.setVisibility(View.GONE);
			} else {
				tvHighDouble.setText("Doubles: "
						+ Integer.toString(result[1]) + "%");
			}

		}
	}

	@Override
	public void onKeyTouched(String keyString) {
	}

}
