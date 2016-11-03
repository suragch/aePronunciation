package com.aepronunciation.ipa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PracticeDoubleActivity extends BaseActivity implements
		SoundPool.OnLoadCompleteListener {

	static final String STATE_READY_FOR_NEW_SOUND = "ready";
	static final String STATE_IPA = "ipaSymbol";
    static final String STATE_ALLOWED_SOUND = "allowedSounds";
	static final int SETTINGS_CODE = 1000;

	private DoubleSound doubleSound;
	private TextView tvInputWindow;
	private ImageView playButtonImage;
	private String currentIpa = "";
	TransitionDrawable rightAnswerTransistion;
	TransitionDrawable wrongAnswerTransistion;
	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;
	boolean readyForNewSound = true;
	String lastPlayedSound;
	long startTime;
	SharedPreferences settings;
	private ArrayList<String> allowedSounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_double);

		// create objects
		tvInputWindow = (TextView) findViewById(R.id.tvInputWindow);
		playButtonImage = (ImageView) findViewById(R.id.ivPlay);
		doubleSound = new DoubleSound();
		allowedSounds = PhonemeTable.INSTANCE.getAllVowelsWithoutUnstressed();
		allowedSounds.addAll(PhonemeTable.INSTANCE.getAllConsonants());

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

		// Create the green and red effects for right/wrong answers
		Drawable backgrounds[] = new Drawable[2];
		Resources res = getResources();
		backgrounds[0] = res.getDrawable(R.drawable.input_window_normal);
		backgrounds[1] = res.getDrawable(R.drawable.input_window_right);
		rightAnswerTransistion = new TransitionDrawable(backgrounds);
		backgrounds[1] = res.getDrawable(R.drawable.input_window_wrong);
		wrongAnswerTransistion = new TransitionDrawable(backgrounds);

		// Hide function keys after layout done
		tvInputWindow.post(new Runnable() {
			public void run() {
				keyboardFragment.hideFunctionKeys();
				keyboardFragment.showKeysInList(allowedSounds);
                keyboardFragment.hideUnstressedVowels();
			}
		});
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

		// Increment stored time by elapsed time
		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		long formerTime = settings
				.getLong(TIME_PRACTICE_DOUBLE_KEY, TIME_DEFAULT);
		long elapsedTime = System.nanoTime() - startTime;
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(TIME_PRACTICE_DOUBLE_KEY, formerTime + elapsedTime);
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
		savedInstanceState.putBoolean(STATE_READY_FOR_NEW_SOUND,
				readyForNewSound);
		savedInstanceState.putString(STATE_IPA, currentIpa);
        savedInstanceState.putStringArrayList(STATE_ALLOWED_SOUND, allowedSounds);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		readyForNewSound = savedInstanceState
				.getBoolean(STATE_READY_FOR_NEW_SOUND);
		currentIpa = savedInstanceState.getString(STATE_IPA);
        allowedSounds = savedInstanceState.getStringArrayList(STATE_ALLOWED_SOUND);

		if (!readyForNewSound) {

			// show the replay icon
			playButtonImage.setImageResource(R.drawable.ic_action_replay);
		}
	}

	public void playClick(View v) {

		if (readyForNewSound) {
			String ipa = doubleSound.getRandomIpaFromAllowedSounds(getApplicationContext(), allowedSounds);
            // String ipa = doubleSound.getRandomIpa(getApplicationContext());

			// look up audio resource id for that sound
			playSound(ipa);
			// int soundId = doubleSound.getSoundResourceId(ipa);
			// load (and play) sound
			// soundPool.load(this, soundId, PRIORITY);

			currentIpa = ipa;
			readyForNewSound = false;
			rightAnswerTransistion.resetTransition();
			tvInputWindow.setText("");

			// change the icon to repeat
			playButtonImage.setImageResource(R.drawable.ic_action_replay);

		} else {

			// play the old sound again
			playSound(currentIpa);

		}

		lastPlayedSound = currentIpa;
	}

	public void tellMeClick(View v) {
		if (readyForNewSound == true) {
			return;
		}
		tvInputWindow.setText(currentIpa);
		animateBackground(true);

		// play sound
		playSound(currentIpa);
		// int soundId = doubleSound.getSoundResourceId(currentIpa);
		// soundPool.load(this, soundId, PRIORITY);

		// change the play button to next
		playButtonImage.setImageResource(R.drawable.ic_action_next);

		readyForNewSound = true;
	}

	@Override
	public void onKeyTouched(String keyString) {

		if (TextUtils.isEmpty(keyString)) {
			return;
		}
		
		// don't allow more clicks when green
		if (readyForNewSound) {
			return;
		}

		String windowText = tvInputWindow.getText().toString();
		if (windowText.equals("")) {
			tvInputWindow.setText(keyString);
			return;
		} else {
			tvInputWindow.setText(windowText + keyString);
		}
		windowText = tvInputWindow.getText().toString();

		// check if right or not
		if (windowText.equals(currentIpa)) {
			// if right then animate backgound to green and back
			animateBackground(true);

			// change the play button to next
			playButtonImage.setImageResource(R.drawable.ic_action_next);

			// play sound that was pressed if different that last played
			if (!lastPlayedSound.equals(currentIpa)) {
				playSound(windowText);
			}

			readyForNewSound = true;
		} else {
			// if wrong then animate to red and back, play wrong sound
			animateBackground(false);

			// play sound that was pressed
			playSound(windowText);

		}
		lastPlayedSound = "";
	}

	private void playSound(String ipaDoubleSound) {

		// look up audio resource id for that sound
		int soundId = doubleSound.getSoundResourceId(ipaDoubleSound);
		if (soundId == -1) {

			// do error checking on input
			Bundle args = new Bundle();
			args.putString("errorMessage", Answer.getErrorMessage(ipaDoubleSound));
			DialogFragment dialog = new ErrorDialogFragment();
			dialog.setArguments(args);
			dialog.show(getSupportFragmentManager(), "ErrorDialogFragmentTag");

		} else {
			// load (and play) sound
			soundPool.load(this, soundId, PRIORITY);
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void animateBackground(boolean answerIsCorrect) {

		if (answerIsCorrect) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				tvInputWindow.setBackground(rightAnswerTransistion);
			} else {
				tvInputWindow.setBackgroundDrawable(rightAnswerTransistion);
			}

			rightAnswerTransistion.startTransition(300);
			// rightAnswerTransistion.reverseTransition(300);

		} else {

			final int TRANSITION_START_TIME = 300;
			final int TRANSITION_REVERSE_TIME = 300;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				tvInputWindow.setBackground(wrongAnswerTransistion);
			} else {
				tvInputWindow.setBackgroundDrawable(wrongAnswerTransistion);
			}

			wrongAnswerTransistion.startTransition(300);
			wrongAnswerTransistion.reverseTransition(300);

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					tvInputWindow.setText("");
				}
			}, TRANSITION_START_TIME + TRANSITION_REVERSE_TIME);
		}
	}

	@Override
	public void onLoadComplete(SoundPool sPool, int sid, int status) {

		if (status != 0) // 0=success
			return;

		soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
		soundPool.unload(sid);

	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, SelectSoundActivity.class);
        intent.putExtra("doubleSounds", true);
		intent.putExtra("allowedSounds", allowedSounds);
		startActivityForResult(intent, SETTINGS_CODE);
	}

	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (requestCode != SETTINGS_CODE || resultCode != RESULT_OK || data == null) {
			return;
		}
		ArrayList<String> selected = data.getStringArrayListExtra("selected");
		if (selected == null) {
			return;
		}

        allowedSounds = selected;
		keyboardFragment.showKeysInList(allowedSounds);
        keyboardFragment.hideUnstressedVowels();

        // redo sound and display
        resetSoundAndDisplay();
	}

	private void resetSoundAndDisplay() {
		readyForNewSound = true;
		lastPlayedSound = "";
		playButtonImage.setImageResource(R.drawable.ic_action_play);
		rightAnswerTransistion.resetTransition();
		tvInputWindow.setText("");
	}
}
