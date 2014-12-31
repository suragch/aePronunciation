package com.aepronunciation.ipa;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends BaseActivity implements
		SoundPool.OnLoadCompleteListener {

	static final String STATE_READY_FOR_NEW_SOUND = "ready";
	static final String STATE_IPA = "ipaSymbol";
	static final String STATE_KEY_TOUCHES = "keyTouches";
	static final String STATE_QUESTION_NUMBER = "questionNum";
	static final String STATE_INPUT_WINDOW = "inputWindow";
	static final String STATE_ANSWERS = "answers";

	private DoubleSound doubleSound;
	private SingleSound singleSound;
	private String currentIpa = "";
	String lastQuestionIpa = "";
	private TextView tvInputWindow;
	private TextView tvQuestionNumber;
	// private ImageView playButtonImage;
	private RelativeLayout playButton;
	private ImageView nextButtonImage;
	private RelativeLayout nextButton;
	private String studentName = "me";
	private String testMode = "double";
	private int totalNumberOfQuestions = 50;
	// private boolean strictModeWasFollowed = true;

	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;
	boolean readyForNewSound = false;
	int keyTouches = 0;
	ArrayList<Answer> answers;
	int questionNumber = 0; // zero based

	long startTime = 0;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		// Get extras from Test Setup Activity
		Intent intent = getIntent();
		studentName = intent.getStringExtra("name");
		totalNumberOfQuestions = intent.getIntExtra("number_of_questions", 50);
		testMode = intent.getStringExtra("test_mode");

		// 2D answer array initialization
		answers = new ArrayList<Answer>();

		// create objects
		tvQuestionNumber = (TextView) findViewById(R.id.tvQuestionNumber);
		tvInputWindow = (TextView) findViewById(R.id.tvInputWindow);
		// playButtonImage = (ImageView) findViewById(R.id.ivPlay);
		playButton = (RelativeLayout) findViewById(R.id.playButtonLayout);
		nextButtonImage = (ImageView) findViewById(R.id.ivNext);
		nextButton = (RelativeLayout) findViewById(R.id.nextButtonLayout);
		singleSound = new SingleSound();
		doubleSound = new DoubleSound();

		// Set up fragment
		fragmentManager = getSupportFragmentManager();
		keyboardFragment = (KeyboardFragment) fragmentManager
				.findFragmentByTag(KEYBOARD_FRAGMENT_TAG);
		if (keyboardFragment == null) {
			keyboardFragment = new KeyboardFragment();
			fragmentManager
					.beginTransaction()
					.add(R.id.keyboardContainer, keyboardFragment,
							KEYBOARD_FRAGMENT_TAG).commit();
		}

		// do different things depending if first creation or not
		if (savedInstanceState == null) {
			currentIpa = getRandomIpa();
			nextButton.setClickable(false);
		} else {
			readyForNewSound = savedInstanceState
					.getBoolean(STATE_READY_FOR_NEW_SOUND);
			currentIpa = savedInstanceState.getString(STATE_IPA);
			questionNumber = savedInstanceState.getInt(STATE_QUESTION_NUMBER);
			keyTouches = savedInstanceState.getInt(STATE_KEY_TOUCHES);
			tvInputWindow.setText(savedInstanceState
					.getString(STATE_INPUT_WINDOW));
			answers = savedInstanceState.getParcelableArrayList(STATE_ANSWERS);
			// determine whether to enable next button 
			if (keyTouches == 1 && !testMode.equals("double")) { // single mode
				nextButtonImage.setImageResource(R.drawable.right_caret);
				nextButton.setClickable(true);
			} else if (keyTouches == 2) {
				nextButtonImage.setImageResource(R.drawable.right_caret);
				nextButton.setClickable(true);
			} else {
				nextButton.setClickable(false);
			}
		}

		// run after layout done
		tvInputWindow.post(new Runnable() {
			public void run() {
				// Hide keys
				keyboardFragment.hideFunctionKeys();
				keyboardFragment.hideUnstressedVowels();

				// play first sound
				playSound(currentIpa);
			}
		});

		// question number
		tvQuestionNumber.setText(Integer.toString(questionNumber + 1));

	}

	@Override
	protected void onResume() {

		// start timing
		startTime = System.nanoTime();

		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
		soundPool.setOnLoadCompleteListener(this);

		super.onResume();
	}

	@Override
	protected void onPause() {
		long formerTime;

		// Increment stored time by elapsed time
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

		// TODO if the activity is paused for any reason then record
		// strictModeWasFollowed = false;

		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save the user's current game state
		savedInstanceState.putBoolean(STATE_READY_FOR_NEW_SOUND,
				readyForNewSound);
		savedInstanceState.putString(STATE_IPA, currentIpa);
		savedInstanceState.putString(STATE_INPUT_WINDOW, tvInputWindow
				.getText().toString());
		savedInstanceState.putInt(STATE_KEY_TOUCHES, keyTouches);
		savedInstanceState.putInt(STATE_QUESTION_NUMBER, questionNumber);
		savedInstanceState.putParcelableArrayList(STATE_ANSWERS, answers);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	public void playClick(View v) {

		if (readyForNewSound) {

			keyTouches = 0;

			// Get random sound
			currentIpa = getRandomIpa();
			lastQuestionIpa = currentIpa;

			// play sound
			playSound(currentIpa);

			readyForNewSound = false;
			tvInputWindow.setText("");

			// change the icon to repeat
			// playButtonImage.setImageResource(R.drawable.ic_action_replay);

		} else {

			// play the old sound again
			playSound(currentIpa);

		}

	}

	private String getRandomIpa() {
		String ipa;
		do {
			if (testMode.equals("double")) {
				ipa = doubleSound.getRandomIpa(getApplicationContext());
			} else if (testMode.equals("single_consonants")) {
				ipa = singleSound
						.getRandomConsonantIpa(getApplicationContext());
			} else if (testMode.equals("single_vowels")) {
				ipa = singleSound.getRandomVowelIpa(getApplicationContext());
			} else {// single_both
				ipa = singleSound.getRandomIpa(getApplicationContext());
			}
			// don't allow consecutive repeat sounds
		} while (lastQuestionIpa.equals(ipa));
		return ipa;
	}

	public void clearClick(View v) {

		tvInputWindow.setText("");
		keyTouches = 0;
		nextButtonImage.setImageResource(R.drawable.right_caret_disabled);
		nextButton.setClickable(false);
	}

	public void nextClick(View v) {

		String userAnswer = tvInputWindow.getText().toString();

		// record correct answer and user answer
		Answer thisAnswer = new Answer();
		thisAnswer.setCorrectAnswer(currentIpa);
		thisAnswer.setUserAnswer(userAnswer);
		answers.add(thisAnswer);

		// reset values
		tvInputWindow.setText("");
		keyTouches = 0;
		readyForNewSound = true;
		nextButtonImage.setImageResource(R.drawable.right_caret_disabled);
		nextButton.setClickable(false);

		questionNumber++;

		if (questionNumber == totalNumberOfQuestions) {

			// Start test results activity
			Intent intent = new Intent(this, TestResultsActivity.class);
			intent.putExtra("name", studentName);
			intent.putExtra("test_mode", testMode);
			intent.putExtra("time", System.nanoTime() - startTime);
			intent.putParcelableArrayListExtra(
					"com.aepronunciation.ipa.testAnswers", answers);
			startActivity(intent);
		} else {

			// Auto play next sound
			playButton.performClick();
			tvQuestionNumber.setText(Integer.toString(questionNumber + 1));
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

			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();

		} else {
			// load (and play) sound
			soundPool.load(this, soundId, PRIORITY);
		}
	}

	@Override
	public void onKeyTouched(String keyString) {

		if (TextUtils.isEmpty(keyString)) {
			return;
		}
		if (testMode.equals("double")) {
			if (keyTouches == 2) {
				return;
			}
		} else { // single mode
			if (keyTouches == 1) {
				return;
			}
		}

		keyTouches++;

		String windowText = tvInputWindow.getText().toString();
		if (keyTouches == 1) {
			tvInputWindow.setText(keyString);
			if (!testMode.equals("double")) { // single mode
				nextButtonImage.setImageResource(R.drawable.right_caret);
				nextButton.setClickable(true);
			}
		} else { // keyTouches = 2
			tvInputWindow.setText(windowText + keyString);
			nextButtonImage.setImageResource(R.drawable.right_caret);
			nextButton.setClickable(true);
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
