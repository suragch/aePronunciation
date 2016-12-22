package com.aepronunciation.ipa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import static com.aepronunciation.ipa.MainActivity.PRACTICE_MODE_IS_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_DOUBLE_KEY;

public class PracticeContentFragment extends Fragment implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener, SelectSoundDialogFragment.SelectSoundDialogListener {


    public interface PracticeScreenListener {
        public void updateKeyboardKeysFor(SoundMode mode);
        public void updateKeyboardKeySelectionFor(ArrayList<String> selectedSounds);
    }

//    static final String STATE_READY_FOR_NEW_SOUND = "ready";
//    static final String STATE_IPA = "ipaSymbol";
//    static final String STATE_ALLOWED_SOUND = "allowedSounds";
//    static final int SETTINGS_CODE = 1000;

    private PracticeScreenListener mListener;
    private SingleSound singleSound;
    private DoubleSound doubleSound;
    private TextView tvInputWindow;
    private TextView tvPracticeMode;
    private TextView tvRight;
    private TextView tvPercent;
    private TextView tvWrong;
    private String currentIpa = "";
    TransitionDrawable rightAnswerTransistion;
    TransitionDrawable wrongAnswerTransistion;
    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;
    boolean readyForNewSound = true;
    long startTime;
    SharedPreferences settings;
    private SoundMode practiceMode = SoundMode.Single;
    private int numberCorrect = 0;
    private int numberWrong = 0;
    private int inputKeyCounter = 0;
    private boolean alreadyMadeWrongAnswerForThisIpa = false;
    ArrayList<String> previouslyChosenVowels;
    ArrayList<String> previouslyChosenConsonants;

    // single only keys

    static final int MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED = 4;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_practice_screen, container, false);

        // create objects
        tvInputWindow = (TextView) layout.findViewById(R.id.tvInputWindow);
        tvPracticeMode = (TextView) layout.findViewById(R.id.tvPracticeMode);
        tvRight = (TextView) layout.findViewById(R.id.tvPracticeNumberRight);
        tvPercent = (TextView) layout.findViewById(R.id.tvPracticePercentRight);
        tvWrong = (TextView) layout.findViewById(R.id.tvPracticeNumberWrong);
        singleSound = new SingleSound();
        doubleSound = new DoubleSound();
        //allowedSounds = PhonemeTable.INSTANCE.getAllVowelsWithoutUnstressed();
        //allowedSounds.addAll(PhonemeTable.INSTANCE.getAllConsonants());
        RelativeLayout rlPlayButton = (RelativeLayout) layout.findViewById(R.id.playButtonLayout);
        RelativeLayout rlSettingsButton = (RelativeLayout) layout.findViewById(R.id.settingsButtonLayout);
        RelativeLayout rlTellMeButton = (RelativeLayout) layout.findViewById(R.id.tellMeButtonLayout);

        // set listeners
        rlPlayButton.setOnClickListener(this);
        rlSettingsButton.setOnClickListener(this);
        rlTellMeButton.setOnClickListener(this);

        // Create the green and red effects for right/wrong answers
        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_normal);
        backgrounds[1] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_right);
        rightAnswerTransistion = new TransitionDrawable(backgrounds);
        backgrounds[1] = ContextCompat.getDrawable(getActivity(), R.drawable.input_window_wrong);
        wrongAnswerTransistion = new TransitionDrawable(backgrounds);

        // get saved practice mode
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSingle = settings.getBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
        if (isSingle) {
            practiceMode = SoundMode.Single;
        } else {
            practiceMode = SoundMode.Double;
        }

        // Initialize UI
        resetToInitialValues();
        updateStatLabels();

        return layout;
    }

    @Override
    public void onResume() {

        // start timing
        // TODO do different timings for single double
        //startTime = System.nanoTime();

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    public void onPause() {

        // Increment stored time by elapsed time
        // TODO do different timings for single double
//        settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        long formerTime = settings
//                .getLong(TIME_PRACTICE_DOUBLE_KEY, TIME_DEFAULT);
//        long elapsedTime = System.nanoTime() - startTime;
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putLong(TIME_PRACTICE_DOUBLE_KEY, formerTime + elapsedTime);
//        editor.commit();

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if parent Fragment implements listener
        if (getParentFragment() instanceof PracticeScreenListener) {
            mListener = (PracticeScreenListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement PracticeScreenListener");
        }
    }


    public void playClick() {

        if (readyForNewSound) {

            String ipa;
            do {
                if (practiceMode == SoundMode.Single) {
                    ipa = singleSound.getRandomIpa(getActivity());
                } else {
                    ipa = doubleSound.getRandomIpa(getActivity());
                }

                // allow repeated sounds for small population sizes
                if (practiceMode==SoundMode.Single &&
                        singleSound.getSoundCount() < MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED) {
                    break;
                } else if (practiceMode==SoundMode.Double &&
                        doubleSound.getSoundCount() < MINIMUM_POPULATION_SIZE_FOR_WHICH_REPEATS_NOT_ALLOWED) {
                    break;
                }
            } while (currentIpa.equals(ipa)); // don't allow


            currentIpa = ipa;
            readyForNewSound = false;
            alreadyMadeWrongAnswerForThisIpa = false;
            rightAnswerTransistion.resetTransition();
            tvInputWindow.setText("");
        }

        playSound(currentIpa);

    }

    public void tellMeClick() {
        if (readyForNewSound) {
            return;
        }

        if (!alreadyMadeWrongAnswerForThisIpa) {
            numberWrong++;
            updateStatLabels();
        }

        tvInputWindow.setText(currentIpa);
        animateBackground(true);
        playSound(currentIpa);
        readyForNewSound = true;
    }

    public void onKeyTouched(String keyString) {

        if (TextUtils.isEmpty(keyString)) {
            return;
        }

        // don't allow more clicks when green
        if (readyForNewSound) {
            return;
        }

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
            // if right then animate backgound to green and back
            animateBackground(true);

            // update label
            if (!alreadyMadeWrongAnswerForThisIpa) {
                numberCorrect++;
            }

            readyForNewSound = true;

        } else { // wrong answer

            // if wrong then animate to red and back
            animateBackground(false);

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

        int soundId = -1;
        if (practiceMode == SoundMode.Single) {

            soundId = singleSound.getSoundResourceId(ipaSound);
            if (soundId == -1) {
                return;
            }

        } else { // Double

            soundId = doubleSound.getSoundResourceId(ipaSound);

            if (soundId == -1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(Answer.getErrorMessage(getActivity(), ipaSound));
                builder.setPositiveButton(R.string.error_dialog_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
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
        rightAnswerTransistion.resetTransition();
        if (practiceMode == SoundMode.Single) {
            tvPracticeMode.setText(getString(R.string.practice_mode_single));
        } else {
            tvPracticeMode.setText(getString(R.string.practice_mode_double));
        }

    }

    private void updateStatLabels() {
        tvRight.setText(Integer.toString(numberCorrect));
        tvWrong.setText(Integer.toString(numberWrong));
        if (numberCorrect + numberWrong > 0) {
            int percent = (int) (100 * numberCorrect/(double) (numberCorrect + numberWrong));
            tvPercent.setText(Integer.toString(percent) + "%");
        } else {
            tvPercent.setText("0%");
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

    public void settingsClick() {
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

    public void updateUiForSelectedSounds(SoundMode numberSounds,
                                          ArrayList<String> chosenVowels,
                                          ArrayList<String> chosenConsonants) {

        // clear old values
        resetToInitialValues();

        // update the single/double setting
        practiceMode = numberSounds;

        // update keyboard
        mListener.updateKeyboardKeysFor(practiceMode);
        ArrayList<String> allChosenSounds = new ArrayList<String>();
        allChosenSounds.addAll(chosenVowels);
        allChosenSounds.addAll(chosenConsonants);
        if (practiceMode == SoundMode.Double) {
            if (chosenVowels.isEmpty()) {
                allChosenSounds.addAll(PhonemeTable.INSTANCE.getAllVowels());
            }
            if (chosenConsonants.isEmpty()) {
                allChosenSounds.addAll(PhonemeTable.INSTANCE.getAllConsonants());
            }
        }
        mListener.updateKeyboardKeySelectionFor(allChosenSounds);

        // update user preferences and time
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
        editor.commit();

        // update practice mode label
        if (practiceMode== SoundMode.Single) {
            tvPracticeMode.setText(getString(R.string.practice_mode_single));
        } else {
            tvPracticeMode.setText(getString(R.string.practice_mode_double));
        }

        // update the allowed sounds
        if (practiceMode == SoundMode.Single) {
            singleSound.restrictListTo(chosenConsonants, chosenVowels);
        } else { // Double
            if ((chosenVowels.size()==0 && chosenConsonants.size() == 0) ||
                    (chosenVowels.size() == PhonemeTable.NUMBER_OF_VOWELS_FOR_DOUBLES &&
                            chosenConsonants.size() == PhonemeTable.NUMBER_OF_CONSONANTS_FOR_DOUBLES)) {
                // all or none selected
                doubleSound.includeAllSounds();
            } else if (chosenVowels.size()==0 || chosenConsonants.size() == 0 ||
                    chosenVowels.size() == PhonemeTable.NUMBER_OF_VOWELS_FOR_DOUBLES ||
                    chosenConsonants.size() == PhonemeTable.NUMBER_OF_CONSONANTS_FOR_DOUBLES ) {
                // if all/none of one kind and a few of the other kind, then do inclusive match (any containing pair)
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
