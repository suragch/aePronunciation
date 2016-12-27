package com.aepronunciation.ipa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class PracticeFragment extends Fragment implements PracticeContentFragment.PracticeScreenListener, KeyboardFragment.KeyboardListener {

    PracticeContentFragment childPracticeFragment;
    KeyboardFragment childKeyboardFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_practice, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        childPracticeFragment = new PracticeContentFragment();
        transaction.replace(R.id.practice_content_frame, childPracticeFragment);
        childKeyboardFragment = new KeyboardFragment();
        transaction.replace(R.id.keyboard_frame, childKeyboardFragment);
        transaction.commit();
    }

    // required method for PracticeContentFragment.PracticeScreenListener
    @Override
    public void updateKeyboardKeysFor(SoundMode mode) {
        // find the currect fragment and then perform the appropriate action on it
        Fragment f = getChildFragmentManager().findFragmentById(R.id.keyboard_frame);
        if (f instanceof KeyboardFragment) {
            // update display
            KeyboardFragment keyboardFragment = (KeyboardFragment) f;
            keyboardFragment.updateKeysFor(mode);
        }
    }

    @Override
    public void updateKeyboardKeySelectionFor(ArrayList<String> selectedSounds) {

        if (childKeyboardFragment != null) {
            childKeyboardFragment.updateKeyAppearanceForSelectedSounds(selectedSounds);
        }
    }

    // required method for KeyboardFragment.KeyboardListener
    @Override
    public void onKeyTouched(String keyString) {

        if (childPracticeFragment != null) {
            childPracticeFragment.onKeyTouched(keyString);
        }
    }

    public void updateUiForSelectedSounds(final SoundMode mode, final ArrayList<String> vowels, final ArrayList<String> consonants) {

        if (childPracticeFragment != null) {
            childPracticeFragment.updateUiForSelectedSounds(mode, vowels, consonants);
        }
    }
}
