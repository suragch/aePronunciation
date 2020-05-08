package com.aepronunciation.ipa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.aepronunciation.ipa.MainActivity.PRACTICE_MODE_IS_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;

public class PracticeContentFragment extends Fragment
        implements
        View.OnClickListener,
        SoundPool.OnLoadCompleteListener,
        SelectSoundDialogFragment.SelectSoundDialogListener {


    public interface PracticeScreenListener {
        void updateKeyboardKeysFor(SoundMode mode);

        void updateKeyboardKeySelectionFor(ArrayList<String> selectedSounds);
    }

    private PracticeScreenListener mListener;
    private SingleSound singleSound;
    private DoubleSound doubleSound;
    private LinearLayout llInputWindowBorder;
    private TextView tvInputWindow;
    private TextView tvPracticeMode;
    private TextView tvRight;
    private TextView tvPercent;
    private TextView tvWrong;
    private String currentIpa = "";
    private TransitionDrawable rightAnswerTransition;
    private TransitionDrawable wrongAnswerTransition;
    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    private boolean readyForNewSound = true;
    private SoundMode practiceMode = SoundMode.Single;
    private int numberCorrect = 0;
    private int numberWrong = 0;
    private int inputKeyCounter = 0;
    private boolean alreadyMadeWrongAnswerForThisIpa = false;
    private ArrayList<String> previouslyChosenVowels;
    private ArrayList<String> previouslyChosenConsonants;
    private static final int SOUND_POOL_LOAD_SUCCESS = 0;

    private static final int MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED = 4;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_practice_screen, container, false);

        // create objects
        tvInputWindow = layout.findViewById(R.id.tvInputWindow);
        llInputWindowBorder = layout.findViewById(R.id.llInputWindowBorder);
        tvPracticeMode = layout.findViewById(R.id.tvPracticeMode);
        tvRight = layout.findViewById(R.id.tvPracticeNumberRight);
        tvPercent = layout.findViewById(R.id.tvPracticePercentRight);
        tvWrong = layout.findViewById(R.id.tvPracticeNumberWrong);
        singleSound = new SingleSound();
        doubleSound = new DoubleSound();
        RelativeLayout rlPlayButton = layout.findViewById(R.id.playButtonLayout);
        RelativeLayout rlSettingsButton = layout.findViewById(R.id.settingsButtonLayout);
        RelativeLayout rlTellMeButton = layout.findViewById(R.id.tellMeButtonLayout);
        ImageView clearButton = layout.findViewById(R.id.ivClear);

        // set listeners
        rlPlayButton.setOnClickListener(this);
        rlSettingsButton.setOnClickListener(this);
        rlTellMeButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        // Create the green and red effects for right/wrong answers
        if (getActivity() != null) {
            Drawable[] backgrounds = new Drawable[2];
            backgrounds[0] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_normal);
            backgrounds[1] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_right);
            rightAnswerTransition = new TransitionDrawable(backgrounds);
            backgrounds[1] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_wrong);
            wrongAnswerTransition = new TransitionDrawable(backgrounds);
        }

        // get saved practice mode
        updatePracticeModeFromSharedPreferences();

        // Initialize UI
        resetToInitialValues();
        updateStatLabels();

        return layout;
    }

    @Override
    public void onResume() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // check if parent Fragment implements listener
        if (getParentFragment() instanceof PracticeScreenListener) {
            mListener = (PracticeScreenListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement PracticeScreenListener");
        }
    }

    private void updatePracticeModeFromSharedPreferences() {
        if (getActivity() == null) return;
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSingle = settings.getBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
        if (isSingle) {
            practiceMode = SoundMode.Single;
        } else {
            practiceMode = SoundMode.Double;
        }
    }


    private void playClick() {

        if (readyForNewSound) {

            String ipa;
            do {
                if (practiceMode == SoundMode.Single) {
                    ipa = singleSound.getRandomIpa();
                } else {
                    ipa = doubleSound.getRandomIpa();
                }

                // allow repeated sounds for small population sizes
                if (practiceMode == SoundMode.Single &&
                        singleSound.getSoundCount() < MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED) {
                    break;
                } else if (practiceMode == SoundMode.Double &&
                        doubleSound.getSoundCount() < MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED) {
                    break;
                }
            } while (currentIpa.equals(ipa)); // don't allow


            currentIpa = ipa;
            readyForNewSound = false;
            alreadyMadeWrongAnswerForThisIpa = false;
            rightAnswerTransition.resetTransition();
            tvInputWindow.setText("");
        }

        playSound(currentIpa);
    }

    private void tellMeClick() {
        if (readyForNewSound) {
            return;
        }

        if (!alreadyMadeWrongAnswerForThisIpa) {
            numberWrong++;
            updateStatLabels();
        }

        tvInputWindow.setText(currentIpa);
        animateBackgroundForCorrectAnswer();
        playSound(currentIpa);
        readyForNewSound = true;
    }

    private void clearClick() {
        tvInputWindow.setText("");
        inputKeyCounter = 0;
        rightAnswerTransition.resetTransition();
    }

    void onKeyTouched(String keyString) {

        if (TextUtils.isEmpty(keyString)) {
            return;
        }

        // don't allow more clicks when green
        if (readyForNewSound) return;

        if (practiceMode == SoundMode.Double && inputKeyCounter >= 2) {
            tvInputWindow.setText("");
            inputKeyCounter = 0;
        }
        inputKeyCounter++;

        // add text to input window
        if (practiceMode == SoundMode.Single) {
            tvInputWindow.setText(keyString);
        } else {
            String oldText = tvInputWindow.getText().toString();
            tvInputWindow.setText(oldText + keyString);
            if (TextUtils.isEmpty(oldText)) return;
        }

        String userAnswer = tvInputWindow.getText().toString();

        // check if right or not
        if (userAnswer.equals(currentIpa)) {
            // if right then animate background to green and back
            animateBackgroundForCorrectAnswer();

            // update label
            if (!alreadyMadeWrongAnswerForThisIpa) {
                numberCorrect++;
            }

            readyForNewSound = true;

        } else { // wrong answer

            // if wrong then animate to red and back
            animateBackgroundForWrongAnswer();

            // update label
            if (!alreadyMadeWrongAnswerForThisIpa) {
                numberWrong++;
                alreadyMadeWrongAnswerForThisIpa = true;
            }

            // play sound that was pressed
            playSound(userAnswer);

        }

        updateStatLabels();
    }

    private void playSound(String ipaSound) {

        int soundId;
        if (practiceMode == SoundMode.Single) {

            soundId = singleSound.getSoundResourceId(ipaSound);
            if (soundId == -1) {
                return;
            }

        } else { // Double

            soundId = doubleSound.getSoundResourceId(ipaSound);

            if (soundId == -1) {
                if (getActivity() == null) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setMessage(Answer.getErrorMessage(getActivity(), ipaSound));
                builder.setPositiveButton(R.string.error_dialog_ok_button, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
        }

        // load (and play) sound
        soundPool.load(getActivity(), soundId, PRIORITY);
    }


    private void resetToInitialValues() {

        readyForNewSound = true;
        numberCorrect = 0;
        numberWrong = 0;
        updateStatLabels();
        inputKeyCounter = 0;
        tvInputWindow.setText("");
        rightAnswerTransition.resetTransition();
        if (practiceMode == SoundMode.Single) {
            tvPracticeMode.setText(getString(R.string.practice_mode_single));
        } else {
            tvPracticeMode.setText(getString(R.string.practice_mode_double));
        }
    }

    private void updateStatLabels() {
        tvRight.setText(String.valueOf(numberCorrect));
        tvWrong.setText(String.valueOf(numberWrong));
        if (numberCorrect + numberWrong > 0) {
            int percent = (int) (100 * numberCorrect / (double) (numberCorrect + numberWrong));
            tvPercent.setText(String.valueOf(percent) + "%");
        } else {
            tvPercent.setText("0%");
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void animateBackgroundForCorrectAnswer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llInputWindowBorder.setBackground(rightAnswerTransition);
        } else {
            llInputWindowBorder.setBackgroundDrawable(rightAnswerTransition);
        }
        rightAnswerTransition.startTransition(300);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void animateBackgroundForWrongAnswer() {

        final int TRANSITION_START_TIME = 300;
        final int TRANSITION_REVERSE_TIME = 300;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llInputWindowBorder.setBackground(wrongAnswerTransition);
        } else {
            llInputWindowBorder.setBackgroundDrawable(wrongAnswerTransition);
        }

        wrongAnswerTransition.startTransition(300);
        wrongAnswerTransition.reverseTransition(300);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvInputWindow.setText("");
            }
        }, TRANSITION_START_TIME + TRANSITION_REVERSE_TIME);

    }


    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {

        if (status != SOUND_POOL_LOAD_SUCCESS)
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);

    }

    private void settingsClick() {
        Bundle args = new Bundle();
        args.putBoolean(SelectSoundDialogFragment.KEY_DIALOG_IS_SINGLE_MODE, practiceMode == SoundMode.Single);
        args.putStringArrayList(SelectSoundDialogFragment.KEY_DIALOG_VOWEL_LIST, previouslyChosenVowels);
        args.putStringArrayList(SelectSoundDialogFragment.KEY_DIALOG_CONSONANT_LIST, previouslyChosenConsonants);
        SelectSoundDialogFragment dialog = new SelectSoundDialogFragment();
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), "MyTag");
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.playButtonLayout) {
            playClick();
        } else if (view.getId() == R.id.settingsButtonLayout) {
            settingsClick();
        } else if (view.getId() == R.id.tellMeButtonLayout) {
            tellMeClick();
        } else if (view.getId() == R.id.ivClear) {
            clearClick();
        }

    }

    // Interface callback methods for Select sounds dialog

    // OK button
    @Override
    public void onDialogPositiveClick(SoundMode numberSounds,
                                      ArrayList<String> chosenVowels,
                                      ArrayList<String> chosenConsonants) {

        // putting this in another method so that it can be called separately
        updateUiForSelectedSounds(numberSounds, chosenVowels, chosenConsonants);

    }

    // CANCEL button
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.i("TAG", "onDialogPositiveClick: Cancel clicked");
    }

    void updateUiForSelectedSounds(SoundMode numberSounds,
                                          ArrayList<String> chosenVowels,
                                          ArrayList<String> chosenConsonants) {

        // clear old values
        resetToInitialValues();

        // update the single/double setting
        practiceMode = numberSounds;

        // update keyboard
        mListener.updateKeyboardKeysFor(practiceMode);
        ArrayList<String> allChosenSounds = new ArrayList<>();
        allChosenSounds.addAll(chosenVowels);
        allChosenSounds.addAll(chosenConsonants);
        if (practiceMode == SoundMode.Double) {
            if (chosenVowels.isEmpty()) {
                allChosenSounds.addAll(Ipa.getAllVowels());
            }
            if (chosenConsonants.isEmpty()) {
                allChosenSounds.addAll(Ipa.getAllConsonants());
            }
        }
        mListener.updateKeyboardKeySelectionFor(allChosenSounds);

        // update user preferences and time
        if (getActivity() != null) {
            SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            StudyTimer timer = StudyTimer.getInstance();
            if (practiceMode == SoundMode.Single) {
                editor.putBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
                timer.start(getActivity(), StudyTimer.StudyType.PracticeSingle);
            } else {
                editor.putBoolean(PRACTICE_MODE_IS_SINGLE_KEY, false);
                timer.start(getActivity(), StudyTimer.StudyType.PracticeDouble);
            }
            editor.apply();
        }

        // update practice mode label
        if (practiceMode == SoundMode.Single) {
            tvPracticeMode.setText(getString(R.string.practice_mode_single));
        } else {
            tvPracticeMode.setText(getString(R.string.practice_mode_double));
        }

        // update the allowed sounds
        if (practiceMode == SoundMode.Single) {
            singleSound.restrictListTo(chosenConsonants, chosenVowels);
        } else { // Double
            if ((chosenVowels.size() == 0 && chosenConsonants.size() == 0) ||
                    (chosenVowels.size() == Ipa.NUMBER_OF_VOWELS_FOR_DOUBLES &&
                            chosenConsonants.size() == Ipa.NUMBER_OF_CONSONANTS_FOR_DOUBLES)) {
                // all or none selected
                doubleSound.includeAllSounds();
            } else if (chosenVowels.size() == 0 || chosenConsonants.size() == 0) {
                // if none of one kind and a few of the other kind, then do inclusive match (any containing pair)
                doubleSound.restrictListToPairsContainingAtLeastOneSoundFrom(chosenConsonants, chosenVowels);
            } else {
                // if a few of both kinds, then do exact match (both members of pair must match)
                doubleSound.restrictListToPairsContainingBothSoundsFrom(chosenConsonants, chosenVowels);
            }
        }

        // save the state so that it can be reset if dialog called again.
        previouslyChosenConsonants = chosenConsonants;
        previouslyChosenVowels = chosenVowels;
    }
}
