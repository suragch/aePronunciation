package com.aepronunciation.ipa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import static android.content.Context.MODE_PRIVATE;
import static com.aepronunciation.ipa.MainActivity.NUMBER_OF_QUESTIONS_KEY;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TEST_NAME_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;
import static com.aepronunciation.ipa.MainActivity.TEST_RESULTS_RESULT;


public class TestFragment extends Fragment implements View.OnClickListener {

    protected static final String DEFAULT_NAME = "Me";
    protected static final String DEFAULT_QUESTIONS = "50";
    //protected static final String DOUBLE_MODE = "double";
    //protected static final String SINGLE_MODE = "single";
    //protected static final String DEFAULT_MODE = SINGLE_MODE;

    private EditText etName;
    private Spinner spinner;
    RadioButton rbSingle;
    //RadioButton rbDouble;
    SharedPreferences settings;
    Button rlBeginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab_fragment_test, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // setup name
        String name = settings.getString(TEST_NAME_KEY, DEFAULT_NAME);
        etName = (EditText)  view.findViewById(R.id.etName);
        if (!name.equals(DEFAULT_NAME)) {
            etName.setText(name);
            etName.clearFocus();
        }

        // set up test mode
        String testModeString = settings.getString(TEST_MODE_KEY, SoundMode.Single.getPersistentMemoryString());
        rbSingle = (RadioButton)  view.findViewById(R.id.rbTestSetupSingle);
        RadioButton rbDouble = (RadioButton)  view.findViewById(R.id.rbTestSetupDouble);
        if (testModeString.equals(SoundMode.Double.getPersistentMemoryString())) {
            rbDouble.setChecked(true);
        } else { // default
            rbSingle.setChecked(true);
        }



        // Setup spinner
        spinner = (Spinner)  view.findViewById(R.id.spinnerQuestions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.listvalues,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String numQuestions = settings.getString(NUMBER_OF_QUESTIONS_KEY,
                DEFAULT_QUESTIONS);
        int spinnerPosition = adapter.getPosition(numQuestions);
        spinner.setSelection(spinnerPosition);

        // begin button
        RelativeLayout beginButton = (RelativeLayout) view.findViewById(R.id.beginButtonLayout);
        beginButton.setOnClickListener(this);

        return view;
    }

    public void beginClick() {

        // Name
        String name = etName.getText().toString();
        name = name.trim(); // remove any extra spaces
        if (TextUtils.isEmpty(name)) {
            name = DEFAULT_NAME;
        }

        // Number of questions
        String questions = spinner.getSelectedItem().toString();

        // Test mode
        String testModeString;
        if (rbSingle.isChecked()) {
            testModeString = SoundMode.Single.getPersistentMemoryString();
        } else {
            testModeString = SoundMode.Double.getPersistentMemoryString();
        }

        // Save to preferences
        settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NUMBER_OF_QUESTIONS_KEY, questions);
        editor.putString(TEST_MODE_KEY, testModeString);
        editor.putString(TEST_NAME_KEY, name);
        editor.commit();

        // Start test activity
        Intent intent = new Intent(getActivity(), TestActivity.class);
        intent.putExtra(TEST_NAME_KEY, name);
        intent.putExtra(TEST_MODE_KEY, testModeString);
        intent.putExtra(NUMBER_OF_QUESTIONS_KEY, Integer.parseInt(questions));
        getActivity().startActivityForResult(intent, TEST_RESULTS_RESULT);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.beginButtonLayout) {
            beginClick();
        }
    }
}
