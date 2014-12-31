package com.aepronunciation.ipa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

//import android.widget.Toast;

public class TestSetupActivity extends BaseActivity {

	protected static final String DEFAULT_NAME = "Me";
	protected static final String DEFAULT_QUESTIONS = "50";
	protected static final String DOUBLE_MODE = "double";
	protected static final String SINGLE_BOTH_MODE = "single_both";
	protected static final String SINGLE_CONSONANTS_MODE = "single_consonants";
	protected static final String SINGLE_VOWELS_MODE = "single_vowels";
	protected static final String DEFAULT_MODE = DOUBLE_MODE;

	private CheckBox cbConsonants;
	private CheckBox cbVowels;
	private EditText etName;
	private Spinner spinner;
	RadioButton rbSingle;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_setup);

		// create objects
		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		etName = (EditText) findViewById(R.id.etName);
		cbConsonants = (CheckBox) findViewById(R.id.cbConsonants);
		cbVowels = (CheckBox) findViewById(R.id.cbVowels);
		spinner = (Spinner) findViewById(R.id.spinner);
		rbSingle = (RadioButton) findViewById(R.id.rbSingle);
		final LinearLayout cbGroup = (LinearLayout) findViewById(R.id.checkBoxGroup);

		// setup name
		String name = settings.getString(TEST_NAME_KEY, DEFAULT_NAME);
		if (!name.equals(DEFAULT_NAME)) {
			etName.setText(name);
			etName.clearFocus();
			//etName.setHint(name);
			//etName.setSelectAllOnFocus(true);
		}

		// set up test mode
		String testMode = settings.getString(TEST_MODE_KEY, DEFAULT_MODE);
		if (testMode.equals(DOUBLE_MODE)) {
			rbSingle.setChecked(false);
		} else if (testMode.equals(SINGLE_BOTH_MODE)) {
			rbSingle.setChecked(true);
			cbConsonants.setChecked(true);
			cbVowels.setChecked(true);
		} else if (testMode.equals(SINGLE_CONSONANTS_MODE)) {
			rbSingle.setChecked(true);
			cbConsonants.setChecked(true);
			cbVowels.setChecked(false);
		} else {
			rbSingle.setChecked(true);
			cbConsonants.setChecked(false);
			cbVowels.setChecked(true);
		}
		if (rbSingle.isChecked()) {
			cbGroup.setVisibility(View.VISIBLE);
		} else {
			cbGroup.setVisibility(View.INVISIBLE);
		}
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rbSingle.isChecked()) {
					cbGroup.setVisibility(View.VISIBLE);
				} else {
					cbGroup.setVisibility(View.INVISIBLE);
				}
			}
		});

		// Setup spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.listentries,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		String numQuestions = settings.getString(NUMBER_OF_QUESTIONS_KEY,
				DEFAULT_QUESTIONS);
		int spinnerPosition = adapter.getPosition(numQuestions);
		spinner.setSelection(spinnerPosition);

	}

	public void consonantsBoxClick(View v) {

		// called right after a change
		if (!cbConsonants.isChecked() && !cbVowels.isChecked()) {
			cbVowels.setChecked(true);
		}
	}

	public void vowelsBoxClick(View v) {

		if (!cbVowels.isChecked() && !cbConsonants.isChecked()) {
			cbConsonants.setChecked(true);
		}
	}

	@Override
	public void onKeyTouched(String keyString) {
	}

	public void beginClick(View v) {

		// Name
		String name = etName.getText().toString();
		name = name.trim(); // remove any extra spaces
		if (TextUtils.isEmpty(name)) {
			name = DEFAULT_NAME;
		}

		// Number of questions
		String questions = spinner.getSelectedItem().toString();

		// Test mode
		String testMode = DEFAULT_MODE; // default
		if (rbSingle.isChecked()) {

			if (cbConsonants.isChecked() && cbVowels.isChecked()) {
				testMode = SINGLE_BOTH_MODE;
			} else if (cbConsonants.isChecked() && !cbVowels.isChecked()) {
				testMode = SINGLE_CONSONANTS_MODE;
			} else {
				testMode = SINGLE_VOWELS_MODE;
			}

		}

		// Save to preferences
		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(NUMBER_OF_QUESTIONS_KEY, questions);
		editor.putString(TEST_MODE_KEY, testMode);
		editor.putString(TEST_NAME_KEY, name);
		editor.commit();

		// Start test activity
		Intent intent = new Intent(this, TestActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("test_mode", testMode);
		intent.putExtra("number_of_questions", Integer.parseInt(questions));
		startActivity(intent);
	}

}
