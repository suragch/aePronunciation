package com.aepronunciation.ipa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class HistoryTimeActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_time);

		// Create objects
		TextView tvTimeLearning = (TextView) findViewById(R.id.tvTimeLearning);
		TextView tvTimeLearningSingles = (TextView) findViewById(R.id.tvTimeLearningSingles);
		TextView tvTimeLearningDoubles = (TextView) findViewById(R.id.tvTimeLearningDoubles);
		TextView tvTimePracticing = (TextView) findViewById(R.id.tvTimePracticing);
		TextView tvTimePracticingSingles = (TextView) findViewById(R.id.tvTimePracticingSingles);
		TextView tvTimePracticingDoubles = (TextView) findViewById(R.id.tvTimePracticingDoubles);
		TextView tvTimeTesting = (TextView) findViewById(R.id.tvTimeTesting);
		TextView tvTimeTestingSingles = (TextView) findViewById(R.id.tvTimeTestingSingles);
		TextView tvTimeTestingDoubles = (TextView) findViewById(R.id.tvTimeTestingDoubles);
		TextView tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);

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
		tvTimeLearning.setText(getTimeString(learnSingleTime+learnDoubleTime));
		tvTimeLearningSingles.setText(getTimeString(learnSingleTime));
		tvTimeLearningDoubles.setText(getTimeString(learnDoubleTime));
		tvTimePracticing.setText(getTimeString(practiceSingleTime+practiceDoubleTime));
		tvTimePracticingSingles.setText(getTimeString(practiceSingleTime));
		tvTimePracticingDoubles.setText(getTimeString(practiceDoubleTime));
		tvTimeTesting.setText(getTimeString(testSingleTime+testDoubleTime));
		tvTimeTestingSingles.setText(getTimeString(testSingleTime));
		tvTimeTestingDoubles.setText(getTimeString(testDoubleTime));
		tvTotalTime.setText(getTimeString(totalTime));

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// hide the menu
		return false;
	}

	@Override
	public void onKeyTouched(String keyString) {

	}

}