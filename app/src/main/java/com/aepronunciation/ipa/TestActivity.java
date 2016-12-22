package com.aepronunciation.ipa;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import static com.aepronunciation.ipa.MainActivity.NUMBER_OF_QUESTIONS_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_NAME_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_RESULTS_RESULT;


public class TestActivity extends AppCompatActivity  implements KeyboardFragment.KeyboardListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Get extras from Test Fragment (setup)
        Intent intent = getIntent();
        SoundMode testMode = SoundMode.fromString(intent.getStringExtra(TEST_MODE_KEY));

        Bundle contentBundle = new Bundle();
        contentBundle.putString(TEST_NAME_KEY, intent.getStringExtra(TEST_NAME_KEY));
        contentBundle.putInt(NUMBER_OF_QUESTIONS_KEY, intent.getIntExtra(NUMBER_OF_QUESTIONS_KEY, 50));
        contentBundle.putString(TEST_MODE_KEY, testMode.getPersistentMemoryString());

        Bundle keyboardBundle = new Bundle();
        keyboardBundle.putString(TEST_MODE_KEY, testMode.getPersistentMemoryString());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment contentFragment = new TestContentFragment();
        contentFragment.setArguments(contentBundle);
        transaction.replace(R.id.test_content_frame, contentFragment);
        Fragment keyboardFragment = new KeyboardFragment();
        keyboardFragment.setArguments(keyboardBundle);
        transaction.replace(R.id.keyboard_frame, keyboardFragment);
        transaction.commit();

    }

    // required method for KeyboardFragment.KeyboardListener
    @Override
    public void onKeyTouched(String keyString) {

        // find the currect fragment and then perform the appropriate action on it
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.test_content_frame);
        if (f instanceof TestContentFragment) {
            // update display
            TestContentFragment testContentFragment = (TestContentFragment) f;
            testContentFragment.onKeyTouched(keyString);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("TAG", "onActivityResult: activity was called" + resultCode);

        if (requestCode == TEST_RESULTS_RESULT) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
            }
        }
        finish();
    }

}