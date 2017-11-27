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

    // Wrong answers are stored in a string in the following format
    // ipa + # + times wrong + ; substituted ipa + # times substituted + ,
    // example: ɪ#3;i#2;ɛ#1,f#1;θ#1
    // comma (,) separates each main sound that was gotten wrong
    // semicolon (;) separates main sound from substituted sounds
    // first sound is always main, following are all subs
    // number sign (#) separates ipa sounds from their count values

    //ListView listView;
    private static String userName;
    private static long timeLength;
    public static SoundMode testMode;
    private static int score;
    private static ArrayList<Answer> answers;
    private static StringBuilder wrong;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
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
        TextView tvName = (TextView) findViewById(R.id.tvResultName);
        TextView tvDate = (TextView) findViewById(R.id.tvResultDate);
        TextView tvPercent = (TextView) findViewById(R.id.tvResultPercent);
        TextView tvCorrect = (TextView) findViewById(R.id.tvResultCorrect);
        TextView tvWrong = (TextView) findViewById(R.id.tvResultWrong);
        TextView tvTime = (TextView) findViewById(R.id.tvResultTime);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvTestResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        adapter = new TestResultsRecyclerViewAdapter(this, answers, testMode);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        singleSound = new SingleSound();
        doubleSound = new DoubleSound();
        wrong = new StringBuilder();

        // calculate score
        int numberCorrect = calculateScore(answers);
        int totalNumber = answers.size();
        if (testMode == SoundMode.Double) {
            totalNumber *= 2;
        }
        score = (numberCorrect * 100) / totalNumber; // round down

        // hide practice button if all correct
        if (numberCorrect == totalNumber) {
            RelativeLayout practiceButton = (RelativeLayout) findViewById(R.id.rlPracticeDifficultButton);
            practiceButton.setVisibility(View.INVISIBLE);
        }

        // get date/time
        Locale locale = Locale.US; // default
        if (getString(R.string.locale).equals(Locale.CHINESE.toString())) {
            locale = Locale.CHINESE;
        }
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, locale);
        String formattedDate = df.format(date);

        // update textviews
        tvName.setText(userName);
        tvDate.setText(formattedDate);
        tvPercent.setText(String.format(getString(R.string.test_results_percent), score));
        tvCorrect.setText(String.format(getString(R.string.test_results_right), numberCorrect));
        tvWrong.setText(String.format(getString(R.string.test_results_wrong), totalNumber - numberCorrect));
        tvTime.setText(String.format(getString(R.string.test_results_time), TimeUtil.getTimeString(timeLength)));

        // update the database (only on first creation)
        if (savedInstanceState == null) {
            new AddTestToDb().execute();
        }

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


    private int calculateScore(ArrayList<Answer> answers) {

        Answer tempAnswer;
        String correct;
        String user;
        int numCorrect = 0;
        for (int i = 0; i < answers.size(); i++) {

            tempAnswer = answers.get(i);
            correct = tempAnswer.getCorrectAnswer();
            user = tempAnswer.getUserAnswer();

            if (testMode == SoundMode.Double) {
                String[] parsedCorrect;
                String[] parcedUser;

                if (correct.equals(user)) {
                    numCorrect += 2;
                } else {
                    parsedCorrect = Answer.parseDouble(correct);
                    parcedUser = Answer.parseDouble(user);
                    if (parsedCorrect == null || parcedUser == null) {
                        return 0;
                    }
                    if (parsedCorrect[0].equals(parcedUser[0])) {
                        numCorrect++;
                    } else {
                        if (wrong.length() > 0) {
                            wrong.append(",");
                        }
                        wrong.append(parsedCorrect[0]).append(";").append(parcedUser[0]);
                    }
                    if (parsedCorrect[1].equals(parcedUser[1])) {
                        numCorrect++;
                    } else {
                        if (wrong.length() > 0) {
                            wrong.append(",");
                        }
                        wrong.append(parsedCorrect[1]).append(";").append(parcedUser[1]);
                    }
                }
            } else { // single
                if (correct.equals(user)) {
                    numCorrect++;
                } else {
                    if (wrong.length() > 0) {
                        wrong.append(",");
                    }
                    wrong.append(correct).append(";").append(user);
                }
            }
        }

        return numCorrect;

    }


    private Set<String> findNeedToPracticeSounds(ArrayList<Answer> answers) {

        String userAnswer;
        String correctAnswer;
        String[] parsedCorrect;
        String[] parcedUser;

        Set<String> practiceSet = new HashSet<>();
        for (Answer answer : answers) {

            userAnswer = answer.getUserAnswer();
            correctAnswer = answer.getCorrectAnswer();

            if (testMode == SoundMode.Single) {

                if (!userAnswer.equals(correctAnswer)) {
                    practiceSet.add(userAnswer);
                    practiceSet.add(correctAnswer);
                }

            } else if (testMode == SoundMode.Double) {

                parsedCorrect = Answer.parseDouble(correctAnswer);
                parcedUser = Answer.parseDouble(userAnswer);
                if (parcedUser == null || parsedCorrect == null) return practiceSet;
                if (!parcedUser[0].equals(parsedCorrect[0])) {
                    practiceSet.add(parcedUser[0]);
                    practiceSet.add(parsedCorrect[0]);
                }
                if (!parcedUser[1].equals(parsedCorrect[1])) {
                    practiceSet.add(parcedUser[1]);
                    practiceSet.add(parsedCorrect[1]);
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
            for (int i = 0; i < answers.size(); i++) {
                if (correctAnswersConcat.length() > 0) {
                    // comma separated values
                    correctAnswersConcat.append(",");
                    userAnswersConcat.append(",");
                }
                correctAnswersConcat.append(answers.get(i).getCorrectAnswer());
                userAnswersConcat.append(answers.get(i).getUserAnswer());
            }

            // sort wrong ones by frequency
            String sortedWrong = new Wrong().getIpaSortedByFrequency(wrong
                    .toString());

            try {

                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
                        getApplicationContext());
                dbAdapter.addTest(userName, timeLength, testMode.getPersistentMemoryString(), score,
                        correctAnswersConcat.toString(),
                        userAnswersConcat.toString(), sortedWrong);
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
        } else if (testMode == SoundMode.Single){
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