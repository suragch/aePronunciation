package com.aepronunciation.ipa;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class PracticeFragment extends Fragment implements PracticeContentFragment.PracticeScreenListener, KeyboardFragment.KeyboardListener {

    private PracticeContentFragment childPracticeFragment;
    private KeyboardFragment childKeyboardFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_practice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

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
        // find the correct fragment and then perform the appropriate action on it
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

    void updateUiForSelectedSounds(final SoundMode mode, final ArrayList<String> vowels, final ArrayList<String> consonants) {

        if (childPracticeFragment != null) {
            childPracticeFragment.updateUiForSelectedSounds(mode, vowels, consonants);
        }
    }
}
