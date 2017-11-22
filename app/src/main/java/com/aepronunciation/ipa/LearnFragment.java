package com.aepronunciation.ipa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LearnFragment extends Fragment implements KeyboardFragment.KeyboardListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_learn, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment childLearnSingleFragment = new LearnSingleFragment();
        transaction.replace(R.id.learn_content_frame, childLearnSingleFragment);
        Fragment childKeyboardFragment = new KeyboardFragment();
        transaction.replace(R.id.keyboard_frame, childKeyboardFragment);
        transaction.commit();
    }

    @Override
    public void onKeyTouched(String keyString) {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.learn_content_frame);
        if (f instanceof LearnSingleFragment) {
            // update display
            LearnSingleFragment singleFragment = (LearnSingleFragment) f;
            singleFragment.onNewSelection(keyString);
        }
    }
}
