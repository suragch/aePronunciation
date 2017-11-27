package com.aepronunciation.ipa;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.aepronunciation.ipa.MainActivity.PRACTICE_MODE_IS_SINGLE_KEY;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;

public class SelectSoundDialogFragment extends DialogFragment {

    public interface SelectSoundDialogListener {
        void onDialogPositiveClick(
                SoundMode numberSounds,
                ArrayList<String> chosenVowels,
                ArrayList<String> chosenConsonants);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public static final String KEY_DIALOG_IS_SINGLE_MODE = "isSingleMode";
    public static final String KEY_DIALOG_VOWEL_LIST = "vowels";
    public static final String KEY_DIALOG_CONSONANT_LIST = "consonants";

    SelectSoundDialogListener mListener;
    RadioButton rbSingle;
    RadioButton rbDouble;
    CheckBox cbVowelsCategory;
    CheckBox cbConsonantsCategory;
    CheckBox[] checkBoxesVowels;
    CheckBox[] checkBoxesConsonants;
    CheckBox cbShwua;
    CheckBox cbUnstressedEr;
    CheckBox cbGlottalStop;
    CheckBox cbFlapT;
    Button positiveButton;
    boolean listenerDisabled = false;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_sound, null);

        rbSingle = (RadioButton) view.findViewById(R.id.radio_single);
        rbDouble = (RadioButton) view.findViewById(R.id.radio_double);
        cbShwua = (CheckBox) view.findViewById(R.id.cb_shwua);
        cbUnstressedEr = (CheckBox) view.findViewById(R.id.cb_er_unstressed);
        cbGlottalStop = (CheckBox) view.findViewById(R.id.cb_glottal_stop);
        cbFlapT = (CheckBox) view.findViewById(R.id.cb_flap_t);

        // get saved practice mode
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSingle = settings.getBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
        if (isSingle) {
            rbSingle.setChecked(true);
        } else {
            rbDouble.setChecked(true);
            cbShwua.setVisibility(View.GONE);
            cbUnstressedEr.setVisibility(View.GONE);
            cbGlottalStop.setVisibility(View.GONE);
            cbFlapT.setVisibility(View.GONE);
        }

        initializeCheckBoxes(view);

        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.select_sounds_radio_group);
        rg.setOnCheckedChangeListener(radioGroupListener);

        // disable the OK button
        view.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = (AlertDialog) getDialog();
                positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            }
        });

        // build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(getString(R.string.select_sounds_title))
                .setPositiveButton(R.string.select_sounds_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SoundMode soundType = SoundMode.Double;
                        //RadioButton single = (RadioButton) rg.findViewById(R.id.radio_single);
                        if (rbSingle.isChecked()) {
                            soundType = SoundMode.Single;
                        }

                        // get all chosen sounds
                        ArrayList<String> chosenVowels = new ArrayList<>();
                        for (CheckBox cb : checkBoxesVowels) {
                            if (cb.isChecked() & cb.getVisibility() == View.VISIBLE) {
                                chosenVowels.add(cb.getText().toString());
                            }
                        }
                        ArrayList<String> chosenConsonants = new ArrayList<>();
                        for (CheckBox cb : checkBoxesConsonants) {
                            if (cb.isChecked() & cb.getVisibility() == View.VISIBLE) {
                                chosenConsonants.add(cb.getText().toString());
                            }
                        }

                        // TODO: save single/double state to user preferences

                        // report back to parent fragment
                        mListener.onDialogPositiveClick(soundType, chosenVowels, chosenConsonants);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SelectSoundDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundMode soundType = SoundMode.Double;
                RadioButton single = (RadioButton) alertDialog.findViewById(R.id.radio_single);
                if (single != null && single.isChecked()) {
                    soundType = SoundMode.Single;
                }

                // get all chosen sounds
                ArrayList<String> chosenVowels = new ArrayList<>();
                for (CheckBox cb : checkBoxesVowels) {
                    if (cb.isChecked() & cb.getVisibility() == View.VISIBLE) {
                        chosenVowels.add(cb.getText().toString());
                    }
                }
                ArrayList<String> chosenConsonants = new ArrayList<>();
                for (CheckBox cb : checkBoxesConsonants) {
                    if (cb.isChecked() & cb.getVisibility() == View.VISIBLE) {
                        chosenConsonants.add(cb.getText().toString());
                    }
                }

                mListener.onDialogPositiveClick(soundType, chosenVowels, chosenConsonants);

                dismiss();
            }
        });
    }


    private void initializeCheckBoxes(View layout) {


        checkBoxesVowels = new CheckBox[]{
                (CheckBox) layout.findViewById(R.id.cb_i),
                (CheckBox) layout.findViewById(R.id.cb_i_short),
                (CheckBox) layout.findViewById(R.id.cb_e_short),
                (CheckBox) layout.findViewById(R.id.cb_ae),
                (CheckBox) layout.findViewById(R.id.cb_a),
                (CheckBox) layout.findViewById(R.id.cb_c_backwards),
                (CheckBox) layout.findViewById(R.id.cb_u_short),
                (CheckBox) layout.findViewById(R.id.cb_u),
                (CheckBox) layout.findViewById(R.id.cb_v_upsidedown),
                cbShwua,
                (CheckBox) layout.findViewById(R.id.cb_ei),
                (CheckBox) layout.findViewById(R.id.cb_ai),
                (CheckBox) layout.findViewById(R.id.cb_au),
                (CheckBox) layout.findViewById(R.id.cb_oi),
                (CheckBox) layout.findViewById(R.id.cb_ou),
                (CheckBox) layout.findViewById(R.id.cb_er_stressed),
                cbUnstressedEr,
                (CheckBox) layout.findViewById(R.id.cb_ar),
                (CheckBox) layout.findViewById(R.id.cb_er),
                (CheckBox) layout.findViewById(R.id.cb_ir),
                (CheckBox) layout.findViewById(R.id.cb_or)
        };
        checkBoxesConsonants = new CheckBox[]{
                (CheckBox) layout.findViewById(R.id.cb_p),
                (CheckBox) layout.findViewById(R.id.cb_b),
                (CheckBox) layout.findViewById(R.id.cb_t),
                (CheckBox) layout.findViewById(R.id.cb_d),
                (CheckBox) layout.findViewById(R.id.cb_k),
                (CheckBox) layout.findViewById(R.id.cb_g),
                (CheckBox) layout.findViewById(R.id.cb_ch),
                (CheckBox) layout.findViewById(R.id.cb_dzh),
                (CheckBox) layout.findViewById(R.id.cb_f),
                (CheckBox) layout.findViewById(R.id.cb_v),
                (CheckBox) layout.findViewById(R.id.cb_th_voiceless),
                (CheckBox) layout.findViewById(R.id.cb_th_voiced),
                (CheckBox) layout.findViewById(R.id.cb_s),
                (CheckBox) layout.findViewById(R.id.cb_z),
                (CheckBox) layout.findViewById(R.id.cb_sh),
                (CheckBox) layout.findViewById(R.id.cb_zh),
                (CheckBox) layout.findViewById(R.id.cb_m),
                (CheckBox) layout.findViewById(R.id.cb_n),
                (CheckBox) layout.findViewById(R.id.cb_ng),
                (CheckBox) layout.findViewById(R.id.cb_l),
                (CheckBox) layout.findViewById(R.id.cb_w),
                (CheckBox) layout.findViewById(R.id.cb_j),
                (CheckBox) layout.findViewById(R.id.cb_h),
                (CheckBox) layout.findViewById(R.id.cb_r),
                cbGlottalStop,
                cbFlapT
        };
        if (checkBoxesConsonants.length != Ipa.NUMBER_OF_CONSONANTS ||
                checkBoxesVowels.length != Ipa.NUMBER_OF_VOWELS) {
            throw new RuntimeException("update number of checkboxes if vowels or consonant number changes");
        }

        cbVowelsCategory = (CheckBox) layout.findViewById(R.id.cbVowels);
        cbConsonantsCategory = (CheckBox) layout.findViewById(R.id.cbConsonants);


        // get saved settings
        Bundle mArgs = getArguments();
        // FIXME use getSerializable to pass SoundMode enum directly
        boolean isSingleMode = mArgs.getBoolean(KEY_DIALOG_IS_SINGLE_MODE);
        SoundMode mode = SoundMode.Double;
        if (isSingleMode) {
            mode = SoundMode.Single;
        }
        ArrayList<String> vowelSounds = mArgs.getStringArrayList(KEY_DIALOG_VOWEL_LIST);
        ArrayList<String> consonantSounds = mArgs.getStringArrayList(KEY_DIALOG_CONSONANT_LIST);
        updateCheckedState(mode, vowelSounds, consonantSounds);


        // set listeners on the IPA checkboxes so that OK button can be disabled
        for (CheckBox cb : checkBoxesVowels) {
            cb.setOnCheckedChangeListener(checkBoxListener);
        }
        for (CheckBox cb : checkBoxesConsonants) {
            cb.setOnCheckedChangeListener(checkBoxListener);
        }
        cbVowelsCategory.setOnCheckedChangeListener(checkBoxListener);
        cbConsonantsCategory.setOnCheckedChangeListener(checkBoxListener);
    }

    // allow calling fragment to set state
    private void updateCheckedState(SoundMode mode, ArrayList<String> vowelSounds, ArrayList<String> consonantSounds) {

        if (mode == null || vowelSounds == null || consonantSounds == null) {
            return;
        }

        // radio group
        if (mode == SoundMode.Single) {
            rbSingle.setChecked(true);
        } else {
            rbDouble.setChecked(true);
        }

        // uncheck the vowel/consonant boxes if some of the small boxes are unchecked
        listenerDisabled = true;
        cbVowelsCategory.setChecked(checkBoxesVowels.length == vowelSounds.size());
        cbConsonantsCategory.setChecked(checkBoxesConsonants.length == consonantSounds.size());
        listenerDisabled = false;

        // check individual boxes
        String currentCbString;
        boolean found;
        for (CheckBox cb : checkBoxesVowels) {
            currentCbString = cb.getText().toString();
            found = false;
            for (String sound : vowelSounds) {
                if (currentCbString.equals(sound)) {
                    found = true;
                }
            }
            cb.setChecked(found);
        }
        for (CheckBox cb : checkBoxesConsonants) {
            currentCbString = cb.getText().toString();
            found = false;
            for (String sound : consonantSounds) {
                if (currentCbString.equals(sound)) {
                    found = true;
                }
            }
            cb.setChecked(found);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof SelectSoundDialogListener) {
            mListener = (SelectSoundDialogListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement SelectSoundDialogListener");
        }
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

            switch (checkedId) {
                case R.id.radio_single:
                    // show optional sounds (unstressed er, shwua, glottal stop and flap t)
                    cbShwua.setVisibility(View.VISIBLE);
                    cbUnstressedEr.setVisibility(View.VISIBLE);
                    cbGlottalStop.setVisibility(View.VISIBLE);
                    cbFlapT.setVisibility(View.VISIBLE);
                    break;
                case R.id.radio_double:
                    // hide optional sounds
                    cbShwua.setVisibility(View.GONE);
                    cbUnstressedEr.setVisibility(View.GONE);
                    cbGlottalStop.setVisibility(View.GONE);
                    cbFlapT.setVisibility(View.GONE);
                    break;

            }
            // disable/enable OK button if needed
            boolean enabledState = getButtonShouldBeEnabledState();
            if (positiveButton.isEnabled() != enabledState) {
                positiveButton.setEnabled(enabledState);
            }
        }
    };


    private CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            if (listenerDisabled) {
                return;
            }

            switch (compoundButton.getId()) {
                case R.id.cbVowels:

                    for (CheckBox cb : checkBoxesVowels) {
                        cb.setChecked(isChecked);
                    }
                    break;
                case R.id.cbConsonants:

                    for (CheckBox cb : checkBoxesConsonants) {
                        cb.setChecked(isChecked);
                    }
                    break;
                default:
                    // all other check boxes are individual sounds
                    // set the enabled state of the OK button
                    boolean enabledState = getButtonShouldBeEnabledState();
                    if (positiveButton.isEnabled() != enabledState) {
                        positiveButton.setEnabled(enabledState);
                    }
                    break;
            }
        }
    };

    private boolean getButtonShouldBeEnabledState() {

        // count the number of checked boxes for conconants and vowels
        int vowelsChecked = 0;
        for (CheckBox cb : checkBoxesVowels) {
            if (cb.isChecked() && cb.getVisibility() == View.VISIBLE) {
                vowelsChecked++;
                if (vowelsChecked > 1) break;
            }
        }
        int consonantsChecked = 0;
        for (CheckBox cb : checkBoxesConsonants) {
            if (cb.isChecked() && cb.getVisibility() == View.VISIBLE) {
                consonantsChecked++;
                if (consonantsChecked > 1) break;
            }
        }

        // There must be at least two sounds for single or one for double
        if (rbSingle.isChecked()) {
            if (vowelsChecked + consonantsChecked <= 1) {
                return false;
            }
        } else { // Double
            if (vowelsChecked + consonantsChecked < 1) {
                return false;
            }
        }
        return true;
    }
}