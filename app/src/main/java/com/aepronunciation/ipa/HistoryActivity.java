package com.aepronunciation.ipa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create objects
        TextView tvTime = findViewById(R.id.tvTotalTime);
        tvHighSingle = findViewById(R.id.tvHighScoreSingles);
        tvHighDouble = findViewById(R.id.tvHighScoreDoubles);

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
        new GetHighScores(this).execute();
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

    private static class GetHighScores extends AsyncTask<Void, Void, int[]> {

        private WeakReference<HistoryActivity> activityReference;

        // only retain a weak reference to the activity
        GetHighScores(HistoryActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected int[] doInBackground(Void... params) {

            int[] result = { -1, -1 };
            HistoryActivity activity = activityReference.get();

            try {
                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(activity);
                result = dbAdapter.getHighScores();
            } catch (Exception e) {
                //Log.i("app", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(int[] result) {
            HistoryActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (result[0] == -1) {
                activity.tvHighSingle.setVisibility(View.GONE);
            } else {
                activity.tvHighSingle.setText(String.format(activity.getString(R.string.history_tests_type_single), result[0]));
            }

            if (result[1] == -1) {
                activity.tvHighDouble.setVisibility(View.GONE);
            } else {
                activity.tvHighDouble.setText(String.format(activity.getString(R.string.history_tests_type_double), result[1]));
            }

        }
    }
}