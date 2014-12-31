package com.aepronunciation.ipa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TestResultsActivity extends BaseActivity implements
		OnItemClickListener, SoundPool.OnLoadCompleteListener {

	// Wrong answers are stored in a string in the folling format
	// ipa + # + times wrong + ; substituted ipa + # times substituted + ,
	// example: ɪ#3;i#2;ɛ#1,f#1;θ#1
	// comma (,) separates each main sound that was gotten wrong
	// semicolon (;) separates main sound from substituted sounds
	// first sound is always main, following are all subs
	// number sign (#) separates ipa sounds from their count values
	
	static final String STATE_SCROLL_POSITION = "scrollPosition";

	ListView listView;
	private static String userName;
	private static long timeLength;
	public static String testMode;
	private static int score;
	private static ArrayList<Answer> answers;
	private static StringBuilder wrong;

	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;
	private DoubleSound doubleSound;
	private SingleSound singleSound;
	int savedPosition = 0;
	long startTime = 0;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_results);

		// Get extras from Test Activity
		Bundle bundle = getIntent().getExtras();
		userName = bundle.getString("name");
		testMode = bundle.getString("test_mode");
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
		MyTestDetailsListAdapter adapter = new MyTestDetailsListAdapter(this,
				answers, testMode);
		listView = (ListView) findViewById(R.id.lvTestResults);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		singleSound = new SingleSound();
		doubleSound = new DoubleSound();
		wrong = new StringBuilder();

		// calculate score
		int numberCorrect = calculateScore(answers);
		int totalNumber = answers.size();
		if (testMode.equals("double")) {
			totalNumber *= 2;
		}
		score = (numberCorrect * 100) / totalNumber; // round down

		// get date/time
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy, h:mma",
				Locale.US);
		String formattedDate = sdf.format(date);
		formattedDate = formattedDate.replace("AM", "am").replace("PM", "pm");

		// update textviews
		tvName.setText(userName);
		tvDate.setText(formattedDate);
		tvPercent.setText(Integer.toString(score) + "%");
		tvCorrect.setText("Right: " + Integer.toString(numberCorrect));
		tvWrong.setText("Wrong: "
				+ Integer.toString(totalNumber - numberCorrect));
		tvTime.setText("Time:\n" + getTimeString(timeLength));
		

		// update the database (only on first creation)
		if (savedInstanceState == null) {
			new AddTestToDb().execute();
			// new AddWrongToDb().execute();
		}

	}

	@Override
	protected void onResume() {

		// start timing (count toward test taking time)
		startTime = System.nanoTime();

		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
		soundPool.setOnLoadCompleteListener(this);

		super.onResume();
	}

	@Override
	protected void onPause() {

		// Increment stored time by elapsed time
		long formerTime;
		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		long elapsedTime = System.nanoTime() - startTime;
		if (testMode.equals("double")) {
			formerTime = settings.getLong(TIME_TEST_DOUBLE_KEY, TIME_DEFAULT);
			editor.putLong(TIME_TEST_DOUBLE_KEY, formerTime + elapsedTime);
		} else {
			formerTime = settings.getLong(TIME_TEST_SINGLE_KEY, TIME_DEFAULT);
			editor.putLong(TIME_TEST_SINGLE_KEY, formerTime + elapsedTime);
		}
		editor.commit();

		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}

		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save the user's current game state
		int currentPosition = listView.getFirstVisiblePosition();
		savedInstanceState.putInt(STATE_SCROLL_POSITION,
				currentPosition);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		savedPosition = savedInstanceState
				.getInt(STATE_SCROLL_POSITION);
		listView.setSelection(savedPosition);

	}

	private int calculateScore(ArrayList<Answer> answers) {

		Answer tempAnswer;
		String correct;
		String user;
		// int total=userAnswers.length;
		int numCorrect = 0;
		for (int i = 0; i < answers.size(); i++) {

			tempAnswer = answers.get(i);
			correct = tempAnswer.getCorrectAnswer();
			user = tempAnswer.getUserAnswer();

			if (testMode.equals("double")) {
				String[] parsedCorrect;
				String[] parcedUser;
				// total = total * 2;

				if (correct.equals(user)) {
					numCorrect += 2;
				} else {
					parsedCorrect = Answer.parseDouble(correct);
					parcedUser = Answer.parseDouble(user);
					if (parsedCorrect[0].equals(parcedUser[0])) {
						numCorrect++;
					} else {
						if (wrong.length() > 0) {
							wrong.append(",");
						}
						wrong.append(parsedCorrect[0] + ";" + parcedUser[0]);
					}
					if (parsedCorrect[1].equals(parcedUser[1])) {
						numCorrect++;
					} else {
						if (wrong.length() > 0) {
							wrong.append(",");
						}
						wrong.append(parsedCorrect[1] + ";" + parcedUser[1]);
					}
				}
			} else { // single
				if (correct.equals(user)) {
					numCorrect++;
				} else {
					if (wrong.length() > 0) {
						wrong.append(",");
					}
					wrong.append(correct + ";" + user);
				}
			}
		}

		return numCorrect;

	}

	public void tryAgainClick(View v) {

		// Start test activity
		Intent intent = new Intent(this, TestSetupActivity.class);
		startActivity(intent);
	}

	@Override
	public void onKeyTouched(String keyString) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowId) {

		// Get the text from the listview item
		TextView tvCorrect = (TextView) view
				.findViewById(R.id.tvCorrectAnswerItem);
		TextView tvUser = (TextView) view.findViewById(R.id.tvUserAnswerItem);
		String correctIpa = tvCorrect.getText().toString();
		final String userIpa = tvUser.getText().toString();

		// play the sounds
		playSound(correctIpa);
		if (!TextUtils.isEmpty(userIpa)) {
			int delay = 1000;
			if (correctIpa.equals("l")) {
				delay = 2000; // this sound needs a longer delay
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

	// call: new AddTestToDb().execute();
	private class AddTestToDb extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			// android.os.Debug.waitForDebugger();

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
				dbAdapter.addTest(userName, timeLength, testMode, score,
						correctAnswersConcat.toString(),
						userAnswersConcat.toString(), sortedWrong);
			} catch (Exception e) {
				//Log.e("app", e.toString());
			} finally {

			}
			return null;

		}

	}

	private void playSound(String ipaSound) {

		int soundId = -1;

		// look up audio resource id for that sound
		if (testMode.equals("double")) {
			soundId = doubleSound.getSoundResourceId(ipaSound);
		} else { // single
			soundId = singleSound.getSoundResourceId(ipaSound);
		}

		if (soundId == -1) {

			// error checking on input
			Bundle args = new Bundle();
			args.putString("errorMessage", Answer.getErrorMessage(ipaSound));
			DialogFragment dialog = new ErrorDialogFragment();
			dialog.setArguments(args);
			dialog.show(getSupportFragmentManager(), "ErrorDialogFragmentTag");

		} else {
			// load (and play) sound
			soundPool.load(this, soundId, PRIORITY);
		}
	}

	@Override
	public void onLoadComplete(SoundPool sPool, int sid, int status) {

		if (status != 0) // 0=success
			return;

		soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
		soundPool.unload(sid);

	}

}
