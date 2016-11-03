package com.aepronunciation.ipa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class SelectSoundActivity extends BaseActivity {
    protected CheckBox cbAllVowels = null;
    protected CheckBox cbAllConsonants = null;

    protected SharedPreferences settings;
    private boolean doubleSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sound);

        cbAllVowels = (CheckBox)findViewById(R.id.cbAllVowels);
        cbAllConsonants = (CheckBox)findViewById(R.id.cbAllConsonants);

        cbAllConsonants.setChecked(false);
        cbAllVowels.setChecked(false);

        doubleSounds = getIntent().getBooleanExtra("doubleSounds", false);

        fragmentManager = getSupportFragmentManager();
        keyboardFragment = (KeyboardFragment) fragmentManager
                .findFragmentByTag(KEYBOARD_FRAGMENT_TAG);
        if (keyboardFragment == null) {
            keyboardFragment = new KeyboardFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.keyboardContainer, keyboardFragment,
                            KEYBOARD_FRAGMENT_TAG).commitAllowingStateLoss();
        }

        final ArrayList<String> allowedSounds = getIntent().getStringArrayListExtra("allowedSounds");
        cbAllVowels.post(new Runnable() {
            public void run() {
                keyboardFragment.setSelectMode(true);
                keyboardFragment.hideFunctionKeys();
                if (allowedSounds != null) {
                    keyboardFragment.setSoundsSelected(allowedSounds, true);
                }
                if (doubleSounds) {
                    keyboardFragment.hideUnstressedVowels();
                }
                adjustCheckboxStates();
            }
        });
    }

    @Override
    public void onKeyTouched(String keyString) {
        adjustCheckboxStates();
    }

    public void vowelsBoxClick(View v) {
        if (doubleSounds) {
            keyboardFragment.setSoundsSelected(PhonemeTable.INSTANCE.getAllVowelsWithoutUnstressed(),
                    cbAllVowels.isChecked());
        } else {
            keyboardFragment.setSoundsSelected(PhonemeTable.INSTANCE.getAllVowels(),
                    cbAllVowels.isChecked());
        }
    }

    public void consonantsBoxClick(View v) {
        keyboardFragment.setSoundsSelected(PhonemeTable.INSTANCE.getAllConsonants(),
                cbAllConsonants.isChecked());
    }

    public void okClick(View v) {
        Intent intent = new Intent(this, PracticeDoubleActivity.class);
        ArrayList<String> selected = keyboardFragment.getSelectedSounds();
        intent.putExtra("selected", selected);

        if (doubleSounds) {
            int vowels = 0;
            int consonants = 0;
            for (String s : selected) {
                if (PhonemeTable.INSTANCE.isConsonant(s)) {
                    consonants++;
                } else {
                    vowels++;
                }
            }

            // You must select at least on vowel and one consonant
            if (consonants == 0 || vowels == 0) {
                showError(getResources().getString(R.string.err_select_at_least_c_and_v));
                return;
            }
        } else {
            if (selected.isEmpty()) {
                showError(getResources().getString(R.string.err_select_at_least_one_sound));
                return;
            }
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelClick(View v) {
        setResult(RESULT_CANCELED, null);
        finish();
    }
    private void showError(String msg) {
        Bundle args = new Bundle();
        args.putString("errorMessage", msg);
        DialogFragment dialog = new ErrorDialogFragment();
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "ErrorDialogFragmentTag");
        return;
    }

    private void adjustCheckboxStates() {
        ArrayList<String> selected = keyboardFragment.getSelectedSounds();
        int vcount = 0;
        int ccount = 0;
        for (String s: selected) {
            if (PhonemeTable.INSTANCE.isConsonant(s)) {
                ccount++;
            } else {
                vcount++;
            }
        }

        int allVowelsCount = PhonemeTable.INSTANCE.getAllVowels().size();
        // No unstressed vowels in double sounds practice
        if (doubleSounds) {
            allVowelsCount = PhonemeTable.INSTANCE.getAllVowelsWithoutUnstressed().size();
        }
        int allConsonantsCount = PhonemeTable.INSTANCE.getAllConsonants().size();
        Log.d("debug", String.format("%s", selected.toString()));
        Log.d("debug", String.format("select %d vowels and %d consonants", vcount, ccount));
        cbAllVowels.setChecked(vcount == allVowelsCount);
        cbAllConsonants.setChecked(ccount == allConsonantsCount);
    }
}
