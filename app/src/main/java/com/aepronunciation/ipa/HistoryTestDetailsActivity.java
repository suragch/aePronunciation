package com.aepronunciation.ipa;

import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryTestDetailsActivity extends AppCompatActivity implements
        TestResultsRecyclerViewAdapter.ItemClickListener, SoundPool.OnLoadCompleteListener {

    static final String STATE_SCROLL_POSITION = "scrollPosition";

    int savedPosition = 0;
    private TestResultsRecyclerViewAdapter adapter;
    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    private DoubleSound doubleSound;
    private SingleSound singleSound;

    TextView tvName;
    TextView tvDate;
    TextView tvPercent;
    TextView tvNumberOfQuestions;
    SoundMode testMode;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_test_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // create objects
        tvName = (TextView) findViewById(R.id.tvResultName);
        tvDate = (TextView) findViewById(R.id.tvResultDate);
        tvPercent = (TextView) findViewById(R.id.tvResultPercent);
        tvNumberOfQuestions = (TextView) findViewById(R.id.tvResultNumberQuestions);
        tvTime = (TextView) findViewById(R.id.tvResultTime);

        // Get extras from Test Activity
        Bundle bundle = getIntent().getExtras();
        long id = bundle.getLong("id", 0);

        new GetTest(this).execute(id);

        singleSound = new SingleSound();
        doubleSound = new DoubleSound();
    }


    @Override
    protected void onResume() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    protected void onPause() {

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        super.onPause();
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

    // call: new GetTest().execute();
    private class GetTest extends AsyncTask<Long, Void, Test> {

        private HistoryTestDetailsActivity activity;

        public GetTest(HistoryTestDetailsActivity activity) {
            this.activity = activity;
        }

        @Override
        protected Test doInBackground(Long... params) {

            long id = params[0];

            Test test = new Test();

            try {

                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
                        getApplicationContext());
                test = dbAdapter.getTest(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return test;

        }

        @Override
        protected void onPostExecute(Test test) {



            // Get answers from test
            String userName = test.getUserName();
            long dateMilliseconds = test.getDate();
            Locale locale = Locale.US; // default
            if (getString(R.string.locale).equals(Locale.CHINESE.toString())) {
                locale = Locale.CHINESE;
            }
            Date date = new Date(dateMilliseconds);
            DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, locale);
            String formattedDate = df.format(date);


            long timeLength = test.getTimeLength();
            testMode = test.getMode();
            int score = test.getScore();
            String[] correctAnswers = test.getCorrectAnswers().split(",");
            String[] userAnswers = test.getUserAnswers().split(",");

            // error checking
            int length = correctAnswers.length;
            if (length != userAnswers.length) {
                if (length > userAnswers.length) {
                    length = userAnswers.length;
                }
            }

            // make answer array object to update recyclerview list
            ArrayList<Answer> answers = new ArrayList<Answer>();
            for (int i = 0; i < length; i++) {
                Answer answer = new Answer();
                answer.setCorrectAnswer(correctAnswers[i]);
                answer.setUserAnswer(userAnswers[i]);
                answers.add(answer);
            }


            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvTestResults);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.divider));
            adapter = new TestResultsRecyclerViewAdapter(activity, answers, testMode);
            adapter.setClickListener(activity);
            recyclerView.setAdapter(adapter);

//          // update textviews
            tvName.setText(userName);
            tvDate.setText(formattedDate);
            tvPercent.setText(String.format(getString(R.string.test_results_percent), score));
            tvNumberOfQuestions.setText(String.format(getString(R.string.history_test_details_specific_test_number_of_questions), length));
            tvTime.setText(String.format(getString(R.string.test_results_time), TimeUtil.getTimeString(timeLength)));

        }

    }


    private void playSound(String ipaSound) {

        int soundId = -1;

        // look up audio resource id for that sound
        if (testMode == SoundMode.Double) {
            soundId = doubleSound.getSoundResourceId(ipaSound);
        } else if (testMode == SoundMode.Single){
            soundId = singleSound.getSoundResourceId(ipaSound);
        } else if (testMode == null) {
            return;
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

}