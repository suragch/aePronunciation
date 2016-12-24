package com.aepronunciation.ipa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.aepronunciation.ipa.MainActivity.NUMBER_OF_QUESTIONS_KEY;
import static com.aepronunciation.ipa.MainActivity.PRACTICE_MODE_IS_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_NAME_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_RESULTS_RESULT;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_SINGLE_KEY;

public class TestContentFragment extends Fragment implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener {



    private String studentName;
    private SoundMode testMode;
    private int totalNumberOfQuestions = 50;
    ArrayList<Answer> answers;

    private SingleSound singleSound;
    private DoubleSound doubleSound;
    private TextView tvInputWindow;
    private ImageView clearButton;
    private TextView tvQuestionNumber;
    // private ImageView playButtonImage;
    private RelativeLayout playButton;
    private ImageView nextButtonImage;
    private RelativeLayout nextButton;
    private String currentIpa = "";
//    TransitionDrawable rightAnswerTransistion;
//    TransitionDrawable wrongAnswerTransistion;
    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    boolean readyForNewSound = true;
    int questionNumber = 0; // zero based
//    private int numberCorrect = 0;
//    private int numberWrong = 0;
    private int inputKeyCounter = 0;
    //private boolean alreadyMadeWrongAnswerForThisIpa = false;
    ArrayList<String> previouslyChosenVowels;
    ArrayList<String> previouslyChosenConsonants;
    long startTime;

    // single only keys

    static final int MINIMUM_POPLATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED = 4;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_test_content, container, false);

        // get arguments
        studentName = getArguments().getString(TEST_NAME_KEY);
        totalNumberOfQuestions = getArguments().getInt(NUMBER_OF_QUESTIONS_KEY, 50);
        testMode = SoundMode.fromString(getArguments().getString(TEST_MODE_KEY));
//        if (testModeString.equals(SoundMode.Single.getPersistentMemoryString())) {
//            testMode = SoundMode.Single;
//        } else {
//            testMode = SoundMode.Double;
//        }

        // 2D answer array initialization
        answers = new ArrayList<Answer>();

        // create objects
        tvQuestionNumber = (TextView) layout.findViewById(R.id.tvQuestionNumber);
        tvInputWindow = (TextView) layout.findViewById(R.id.tvInputWindow);
        playButton = (RelativeLayout) layout.findViewById(R.id.playButtonLayout);
        nextButtonImage = (ImageView) layout.findViewById(R.id.ivNext);
        nextButton = (RelativeLayout) layout.findViewById(R.id.nextButtonLayout);
        clearButton = (ImageView) layout.findViewById(R.id.ivClear);

        // set mode label
        TextView tvPracticeMode = (TextView) layout.findViewById(R.id.tvTestMode);
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


        // question number
        tvQuestionNumber.setText(Integer.toString(questionNumber + 1));

        // start timing the test (seperate from StudyTimer
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


            String ipa;
            do {
                if (testMode == SoundMode.Single) {
                    ipa = singleSound.getRandomIpa();
                } else {
                    ipa = doubleSound.getRandomIpa();
                }
            } while (currentIpa.equals(ipa)); // don't allow repeat questions


            currentIpa = ipa;
            readyForNewSound = false;
            tvInputWindow.setText("");
        }

        playSound(currentIpa);

    }

    public void clearClick() {

        tvInputWindow.setText("");
        inputKeyCounter = 0;
        //nextButtonImage.setImageResource(R.drawable.right_caret_disabled);
        nextButton.setVisibility(View.INVISIBLE);
    }

    public void nextClick() {

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
        //nextButtonImage.setImageResource(R.drawable.right_caret_disabled);
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
            playButton.performClick();
            tvQuestionNumber.setText(Integer.toString(questionNumber + 1));
        }

    }


    private void playSound(String ipaSound) {

        int soundId = -1;
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

        } else {
            String oldText = tvInputWindow.getText().toString();
            tvInputWindow.setText(oldText + keyString);
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
