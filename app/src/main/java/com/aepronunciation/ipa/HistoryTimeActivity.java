package com.aepronunciation.ipa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_SINGLE_KEY;

public class HistoryTimeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        // TODO localize
        tvTimeLearning.setText(TimeUtil.getTimeString(learnSingleTime+learnDoubleTime));
        tvTimeLearningSingles.setText(TimeUtil.getTimeString(learnSingleTime));
        tvTimeLearningDoubles.setText(TimeUtil.getTimeString(learnDoubleTime));
        tvTimePracticing.setText(TimeUtil.getTimeString(practiceSingleTime+practiceDoubleTime));
        tvTimePracticingSingles.setText(TimeUtil.getTimeString(practiceSingleTime));
        tvTimePracticingDoubles.setText(TimeUtil.getTimeString(practiceDoubleTime));
        tvTimeTesting.setText(TimeUtil.getTimeString(testSingleTime+testDoubleTime));
        tvTimeTestingSingles.setText(TimeUtil.getTimeString(testSingleTime));
        tvTimeTestingDoubles.setText(TimeUtil.getTimeString(testDoubleTime));
        tvTotalTime.setText(TimeUtil.getTimeString(totalTime));

    }


}