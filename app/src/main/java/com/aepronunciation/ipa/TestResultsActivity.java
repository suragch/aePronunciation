package com.aepronunciation.ipa;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.text.DateFormat;

import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.aepronunciation.ipa.MainActivity.CONSONANT_ARRAY_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_NAME_KEY;
import static com.aepronunciation.ipa.MainActivity.VOWEL_ARRAY_KEY;


public class TestResultsActivity extends AppCompatActivity implements TestResultsRecyclerViewAdapter.ItemClickListener,
        SoundPool.OnLoadCompleteListener {

    private String userName;
    private long timeLength;
    private SoundMode testMode;
    private int score;
    private ArrayList<Answer> answers;
    private TestResultsRecyclerViewAdapter adapter;

    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    private DoubleSound doubleSound;
    private SingleSound singleSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get extras from Test Activity
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString(TEST_NAME_KEY);
        testMode = SoundMode.fromString(bundle.getString(TEST_MODE_KEY));
        timeLength = bundle.getLong("time", 0);
        answers = bundle
                .getParcelableArrayList("com.aepronunciation.ipa.testAnswers");

        // create objects
        TextView tvName = findViewById(R.id.tvResultName);
        TextView tvDate = findViewById(R.id.tvResultDate);
        TextView tvPercent = findViewById(R.id.tvResultPercent);
        TextView tvCorrect = findViewById(R.id.tvResultCorrect);
        TextView tvWrong = findViewById(R.id.tvResultWrong);
        TextView tvTime = findViewById(R.id.tvResultTime);

        RecyclerView recyclerView = findViewById(R.id.rvTestResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        adapter = new TestResultsRecyclerViewAdapter(this, answers, testMode);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        singleSound = new SingleSound();
        doubleSound = new DoubleSound();
        //wrong = new StringBuilder();

        // calculate score
        int numberCorrect = calculateNumberCorrect(answers);
        int totalNumber = answers.size();
        if (testMode == SoundMode.Double) {
            totalNumber *= 2;
        }
        score = (numberCorrect * 100) / totalNumber; // round down

        // hide practice button if all correct
        if (numberCorrect == totalNumber) {
            RelativeLayout practiceButton = findViewById(R.id.rlPracticeDifficultButton);
            practiceButton.setVisibility(View.INVISIBLE);
        }


        // update textviews
        tvName.setText(userName);
        tvDate.setText(getFormattedDate());
        tvPercent.setText(String.format(getString(R.string.test_results_percent), score));
        tvCorrect.setText(String.format(getString(R.string.test_results_right), numberCorrect));
        tvWrong.setText(String.format(getString(R.string.test_results_wrong), totalNumber - numberCorrect));
        tvTime.setText(String.format(getString(R.string.test_results_time), TimeUtil.getTimeString(timeLength)));

        // update the database (only on first creation)
        if (savedInstanceState == null) {
            new AddTestToDb().execute();
        }

    }

    private String getFormattedDate() {
        Locale locale = AppLocale.getLocale(this);
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, locale);
        return df.format(date);
    }

    @Override
    protected void onResume() {

        StudyTimer timer = StudyTimer.getInstance();
        if (testMode == SoundMode.Single) {
            timer.start(this, StudyTimer.StudyType.TestSingle);
        } else if (testMode == SoundMode.Double) {
            timer.start(this, StudyTimer.StudyType.TestDouble);
        }

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    protected void onPause() {

        StudyTimer timer = StudyTimer.getInstance();
        timer.stop(this);

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        super.onPause();
    }


    private int calculateNumberCorrect(ArrayList<Answer> answers) {

        int numCorrect = 0;
        for (Answer answer : answers) {
            String correct = answer.getCorrectAnswer();
            String user = answer.getUserAnswer();

            // Single
            if (testMode == SoundMode.Single) {
                if (correct.equals(user)) numCorrect++;
                continue;
            }

            // Double
            Pair<String, String> parsedCorrect = DoubleSound.parse(correct);
            Pair<String, String> parsedUser = DoubleSound.parse(user);
            if (parsedCorrect == null || parsedUser == null) {
                continue;
            }
            if (parsedCorrect.first.equals(parsedUser.first)) {
                numCorrect++;
            }
            if (parsedCorrect.second.equals(parsedUser.second)) {
                numCorrect++;
            }
        }
        return numCorrect;
    }


    private Set<String> findNeedToPracticeSounds(ArrayList<Answer> answers) {

        Set<String> practiceSet = new HashSet<>();
        for (Answer answer : answers) {

            String userAnswer = answer.getUserAnswer();
            String correctAnswer = answer.getCorrectAnswer();

            if (testMode == SoundMode.Single) {

                if (!userAnswer.equals(correctAnswer)) {
                    practiceSet.add(userAnswer);
                    practiceSet.add(correctAnswer);
                }

            } else if (testMode == SoundMode.Double) {

                Pair<String, String> parsedCorrect = DoubleSound.parse(correctAnswer);
                Pair<String, String> parsedUser = DoubleSound.parse(userAnswer);
                if (parsedUser == null || parsedCorrect == null) continue;
                if (!parsedUser.first.equals(parsedCorrect.first)) {
                    practiceSet.add(parsedUser.first);
                    practiceSet.add(parsedCorrect.first);
                }
                if (!parsedUser.second.equals(parsedCorrect.second)) {
                    practiceSet.add(parsedUser.second);
                    practiceSet.add(parsedCorrect.second);
                }

            }
        }

        return practiceSet;
    }

    @Override
    public void onItemClick(View view, int position) {

        Answer answer = adapter.getItem(position);
        String correctIpa = answer.getCorrectAnswer();
        final String userIpa = answer.getUserAnswer();

        // play the sounds
        playSound(correctIpa);
        if (!correctIpa.equals(userIpa)) {
            int delay = 1000;
            if (Ipa.hasTwoPronunciations(correctIpa)) {
                delay = 2000; // these sounds need a longer delay
            }
            // delay playing second sound
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    playSound(userIpa);
                }
            }, delay);
        }
    }

    private class AddTestToDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            StringBuilder correctAnswersConcat = new StringBuilder();
            StringBuilder userAnswersConcat = new StringBuilder();
            for (Answer answer : answers) {
                if (correctAnswersConcat.length() > 0) {
                    // comma separated values
                    correctAnswersConcat.append(",");
                    userAnswersConcat.append(",");
                }
                correctAnswersConcat.append(answer.getCorrectAnswer());
                userAnswersConcat.append(answer.getUserAnswer());
            }

            try {
                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
                        getApplicationContext());
                    dbAdapter.addTest(userName, timeLength, testMode.getPersistentMemoryString(), score,
                        correctAnswersConcat.toString(),
                        userAnswersConcat.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }

    private void playSound(String ipaSound) {

        int soundId = -1;

        // look up audio resource id for that sound
        if (testMode == SoundMode.Double) {
            soundId = doubleSound.getSoundResourceId(ipaSound);
        } else if (testMode == SoundMode.Single) {
            soundId = singleSound.getSoundResourceId(ipaSound);
        }

        if (soundId == -1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Answer.getErrorMessage(this, ipaSound));
            builder.setPositiveButton(R.string.error_dialog_ok_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        // load (and play) sound
        soundPool.load(this, soundId, PRIORITY);
    }

    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {

        if (status != 0) // 0=success
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)

        }

        return super.onOptionsItemSelected(item);
    }

    public void practiceDifficultClick(View view) {

        // get vowel and consonant sounds
        ArrayList<String> vowelArray = new ArrayList<>();
        ArrayList<String> consonantArray = new ArrayList<>();
        Set<String> allSounds = findNeedToPracticeSounds(answers);
        for (String sound : allSounds) {
            if (Ipa.isConsonant(sound)) {
                consonantArray.add(sound);
            } else {
                vowelArray.add(sound);
            }
        }

        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra(TEST_MODE_KEY, testMode);
        intent.putStringArrayListExtra(VOWEL_ARRAY_KEY, vowelArray);
        intent.putStringArrayListExtra(CONSONANT_ARRAY_KEY, consonantArray);
        setResult(RESULT_OK, intent);
        finish();
    }
}