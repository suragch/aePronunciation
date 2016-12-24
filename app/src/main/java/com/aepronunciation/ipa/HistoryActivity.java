package com.aepronunciation.ipa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_SINGLE_KEY;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHighSingle;
    TextView tvHighDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create objects
        TextView tvTime = (TextView) findViewById(R.id.tvTotalTime);
        tvHighSingle = (TextView) findViewById(R.id.tvHighScoreSingles);
        tvHighDouble = (TextView) findViewById(R.id.tvHighScoreDoubles);

        // get times from preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long learnSingleTime = settings.getLong(TIME_LEARN_SINGLE_KEY, TIME_DEFAULT);
        long learnDoubleTime = settings.getLong(TIME_LEARN_DOUBLE_KEY, TIME_DEFAULT);
        long practiceSingleTime = settings.getLong(TIME_PRACTICE_SINGLE_KEY, TIME_DEFAULT);
        long practiceDoubleTime = settings.getLong(TIME_PRACTICE_DOUBLE_KEY, TIME_DEFAULT);
        long testSingleTime = settings.getLong(TIME_TEST_SINGLE_KEY, TIME_DEFAULT);
        long testDoubleTime = settings.getLong(TIME_TEST_DOUBLE_KEY, TIME_DEFAULT);
        // add up the times
        long totalTime = learnSingleTime + learnDoubleTime + practiceSingleTime
                + practiceDoubleTime + testSingleTime + testDoubleTime;
        // format in human readable
        tvTime.setText(TimeUtil.getTimeString(totalTime));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetHighScores extends AsyncTask<Void, Void, int[]> {

        @Override
        protected int[] doInBackground(Void... params) {

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
                tvHighSingle.setText(String.format(getString(R.string.history_tests_type_single), result[0]));
            }

            if (result[1] == -1) {
                tvHighDouble.setVisibility(View.GONE);
            } else {
                tvHighDouble.setText(String.format(getString(R.string.history_tests_type_double), result[1]));
            }

        }
    }
}