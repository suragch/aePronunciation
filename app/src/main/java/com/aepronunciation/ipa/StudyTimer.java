package com.aepronunciation.ipa;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_PRACTICE_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_DOUBLE_KEY;
import static com.aepronunciation.ipa.MainActivity.TIME_TEST_SINGLE_KEY;

// Timer for handling learning, practice, and test times
// WARNING: Singletons are not thread safe. Only instantiate on the UI thread.
final class StudyTimer {

    private long startTime;
    enum StudyType {
        LearnSingle,
        LearnDouble,
        PracticeSingle,
        PracticeDouble,
        TestSingle,
        TestDouble
    }
    private StudyType studyType;

    private static StudyTimer instance = null;
    private StudyTimer() {}

    static StudyTimer getInstance() {
        if(instance == null) {
            instance = new StudyTimer();
        }
        return instance;
    }

    public void start(Context context, StudyType type) {

        // no need to restart the timer for the same type
        if (this.studyType == type) {
            return;
        }

        // stop and record time for previous type (if any)
        stop(context);

        // start time for this type
        startTime = System.nanoTime();
        this.studyType = type;
    }

    void stop(Context context) {

        if (studyType == null) {
            return;
        }

        String key;
        switch (studyType) {
            case LearnSingle:
                key = TIME_LEARN_SINGLE_KEY;
                break;
            case LearnDouble:
                key = TIME_LEARN_DOUBLE_KEY;
                break;
            case PracticeSingle:
                key = TIME_PRACTICE_SINGLE_KEY;
                break;
            case PracticeDouble:
                key = TIME_PRACTICE_DOUBLE_KEY;
                break;
            case TestSingle:
                key = TIME_TEST_SINGLE_KEY;
                break;
            case TestDouble:
                key = TIME_TEST_DOUBLE_KEY;
                break;
            default:
                return;
        }

        // save time increment to user preferences
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long formerTime = settings.getLong(key, TIME_DEFAULT);
        long elapsedTime = System.nanoTime() - startTime;
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, formerTime + elapsedTime);
        editor.apply();

        // set to null so that not saving multiple times
        studyType = null;
    }

}