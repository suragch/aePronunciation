package com.aepronunciation.ipa;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.aepronunciation.ipa.MainActivity.NUMBER_OF_QUESTIONS_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_NAME_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_RESULTS_RESULT;

public class TestContentFragment extends Fragment implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener {

    private String studentName;
    private SoundMode testMode;
    private int totalNumberOfQuestions = 50;
    ArrayList<Answer> answers;

    private SingleSound singleSound;
    private DoubleSound doubleSound;
    private TextView tvInputWindow;
    private TextView tvQuestionNumber;
    private RelativeLayout playButton;
    private RelativeLayout nextButton;
    private String currentIpa = "";
    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    boolean readyForNewSound = true;
    int inputKeyCounter = 0;
    int questionNumber = 0; // zero based
    long startTime;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_test_content, container, false);

        // get arguments
        if (getArguments() == null) {
            studentName = getString(R.string.test_default_name);
            totalNumberOfQuestions = 50;
            testMode = SoundMode.Single;
        } else {
            studentName = getArguments().getString(TEST_NAME_KEY);
            totalNumberOfQuestions = getArguments().getInt(NUMBER_OF_QUESTIONS_KEY, 50);
            testMode = SoundMode.fromString(getArguments().getString(TEST_MODE_KEY));
        }


        // 2D answer array initialization
        answers = new ArrayList<>();

        // create objects
        tvQuestionNumber = layout.findViewById(R.id.tvQuestionNumber);
        tvInputWindow = layout.findViewById(R.id.tvInputWindow);
        playButton = layout.findViewById(R.id.playButtonLayout);
        nextButton = layout.findViewById(R.id.nextButtonLayout);
        ImageView clearButton = layout.findViewById(R.id.ivClear);

        // set mode label
        TextView tvPracticeMode = layout.findViewById(R.id.tvTestMode);
        if (testMode == SoundMode.Single) {
            tvPracticeMode.setText(getString(R.string.practice_mode_single));
        } else {
            tvPracticeMode.setText(getString(R.string.practice_mode_double));
        }

        singleSound = new SingleSound();
        doubleSound = new DoubleSound();

        // set listeners
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        // disable sound effects
        playButton.setSoundEffectsEnabled(false);
        nextButton.setSoundEffectsEnabled(false);
        clearButton.setSoundEffectsEnabled(false);

        // get ready to play first sound
        prepareForNextSound();
        //tvQuestionNumber.setText(String.valueOf(questionNumber + 1));

        // start timing the test (separate from StudyTimer)
        if (savedInstanceState == null) {
            startTime = System.nanoTime();
        }

        return layout;
    }

    @Override
    public void onResume() {

        StudyTimer timer = StudyTimer.getInstance();
        if (testMode == SoundMode.Single) {
            timer.start(getActivity(), StudyTimer.StudyType.TestSingle);
        } else if (testMode == SoundMode.Double) {
            timer.start(getActivity(), StudyTimer.StudyType.TestDouble);
        }

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    public void onPause() {

        StudyTimer timer = StudyTimer.getInstance();
        timer.stop(getActivity());

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        super.onPause();
    }


    public void playClick() {

        if (readyForNewSound) {
            prepareForNextSound();
            return;
        }

        playSound(currentIpa);
    }

    private void prepareForNextSound() {
        currentIpa = getRandomIpa();
        readyForNewSound = false;
        tvInputWindow.setText("");
        tvQuestionNumber.setText(String.valueOf(questionNumber + 1));
    }

    private String getRandomIpa() {
        String ipa;
        do {
            if (testMode == SoundMode.Single) {
                ipa = singleSound.getRandomIpa();
            } else {
                ipa = doubleSound.getRandomIpa();
            }
        } while (currentIpa.equals(ipa)); // don't allow repeat questions
        return ipa;
    }

    public void clearClick() {

        tvInputWindow.setText("");
        inputKeyCounter = 0;
        nextButton.setVisibility(View.INVISIBLE);
    }

    public void nextClick() {
        if (getActivity() == null) return;

        String userAnswer = tvInputWindow.getText().toString();

        // record correct answer and user answer
        Answer thisAnswer = new Answer();
        thisAnswer.setCorrectAnswer(currentIpa);
        thisAnswer.setUserAnswer(userAnswer);
        answers.add(thisAnswer);

        // reset values
        tvInputWindow.setText("");
        inputKeyCounter = 0;
        readyForNewSound = true;
        nextButton.setVisibility(View.INVISIBLE);

        questionNumber++;

        if (questionNumber == totalNumberOfQuestions) {

            // Start test results activity
            Intent intent = new Intent(getActivity(), TestResultsActivity.class);
            intent.putExtra(TEST_NAME_KEY, studentName);
            intent.putExtra(TEST_MODE_KEY, testMode.getPersistentMemoryString());
            intent.putExtra("time", System.nanoTime() - startTime);
            intent.putParcelableArrayListExtra(
                    "com.aepronunciation.ipa.testAnswers", answers);
            getActivity().startActivityForResult(intent, TEST_RESULTS_RESULT);
        } else {

            // Auto play next sound
            prepareForNextSound();
            playSound(currentIpa);
        }
    }

    private void playSound(String ipaSound) {

        int soundId;
        if (testMode == SoundMode.Single) {
            soundId = singleSound.getSoundResourceId(ipaSound);
        } else { // Double
            soundId = doubleSound.getSoundResourceId(ipaSound);
        }

        if (soundId == -1) return;

        // load (and play) sound
        soundPool.load(getActivity(), soundId, PRIORITY);
    }

    public void onKeyTouched(String keyString) {

        if (TextUtils.isEmpty(keyString)) {
            return;
        }

        if (readyForNewSound) {
            return;
        }

        inputKeyCounter++;

        // add text to input window
        if (testMode == SoundMode.Single) {
            tvInputWindow.setText(keyString);

        } else if (testMode == SoundMode.Double && inputKeyCounter <= 2) {

            String oldText = tvInputWindow.getText().toString();
            String newText = oldText + keyString;
            tvInputWindow.setText(newText);
            if (TextUtils.isEmpty(oldText)) return;
        }
        nextButton.setVisibility(View.VISIBLE);


    }


    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {

        if (status != 0) // 0=success
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.playButtonLayout) {
            playClick();
        } else if (view.getId() == R.id.nextButtonLayout) {
            nextClick();
        } else if (view.getId() == R.id.ivClear) {
            clearClick();
        }

    }

}
