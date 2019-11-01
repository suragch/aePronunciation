package com.aepronunciation.ipa;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LearnFragment extends Fragment implements KeyboardFragment.KeyboardListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_learn, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
            LearnSingleFragment singleFragment = (LearnSingleFragment) f;
            singleFragment.onNewSelection(keyString);
        }
    }
}
