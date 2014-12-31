package com.aepronunciation.ipa;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

// making this parcelable (rather than serialized) improves speed
public class Answer implements Parcelable {
	private String correct;
	private String user;

	// constructor
	public Answer() {
		;
	}

	// constructor when parceled
	private Answer(Parcel in) {
		correct = in.readString();
		user = in.readString();
	}

	// getters
	public String getCorrectAnswer() {
		return correct;
	}

	public String getUserAnswer() {
		return user;
	}

	// setters
	public void setCorrectAnswer(String correct) {
		this.correct = correct;
	}

	public void setUserAnswer(String user) {
		this.user = user;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeString(correct);
		out.writeString(user);

	}

	// used so that can use a normal constructor ???
	public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
		public Answer createFromParcel(Parcel in) {
			return new Answer(in);
		}

		public Answer[] newArray(int size) {
			return new Answer[size];
		}
	};

	public static String[] parseDouble(String ipaDouble) {
		if (TextUtils.isEmpty(ipaDouble)) {
			return null;
		}
		String[] returnIpa = new String[2];
		if (ipaDouble.startsWith("aɪ") || ipaDouble.startsWith("aʊ")
				|| ipaDouble.startsWith("ɔɪ") || ipaDouble.startsWith("ɑr")
				|| ipaDouble.startsWith("ɛr") || ipaDouble.startsWith("ɪr")
				|| ipaDouble.startsWith("ɔr")) {

			returnIpa[0] = ipaDouble.substring(0, 2);
			if (ipaDouble.length() == 2) {
				//Log.e("app", "String was too short");
				returnIpa[0] = ipaDouble.substring(0, 1);
				returnIpa[1] = ipaDouble.substring(1);
			} else {
				returnIpa[1] = ipaDouble.substring(2);
			}
		} else {
			returnIpa[0] = ipaDouble.substring(0, 1);
			if (ipaDouble.length() < 2) {
				//Log.e("app", "String was too short");
				returnIpa[1] = "";
			} else {
				returnIpa[1] = ipaDouble.substring(1);
			}
		}

		return returnIpa;
	}

	public static String getErrorMessage(String ipa) {

		// Types of errors:
		//
		// can't begin with ŋ
		// can't end with w j h
		// r was used as the second sound
		// two vowels
		// two consonants

		String errorMessage = "";

		String[] parsedIpa = parseDouble(ipa);

		// starts with a consonant
		if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa[0])) {

			// ends with a consonant
			if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa[1])) {

				errorMessage = "You wrote "
						+ parsedIpa[0]
						+ " + "
						+ parsedIpa[1]
						+ " (two consonants). You should choose one consonant and one vowel.";

			} else if (parsedIpa[0].equals("ŋ")) {

				// starts with ŋ
				errorMessage = "You wrote " + parsedIpa[0] + parsedIpa[1]
						+ ", but /ŋ/ never begins a sylable in English.";
			}

		} else { // starts with a vowel

			// ends with vowel
			if (parsedIpa[1].equals("r")) {
				
				// ends in r
				errorMessage = "You should use the keys for one of the R vowels (ɝ, ɑr, ɛr, ɪr, ɔr) if you want to end a sound with /r/. There should also be another consonant.";

			} else if ("iɪɛæɑɔʊuʌəeaɪaʊɔɪoɝɚɑrɛrɪrɔr".contains(parsedIpa[1])) {
				
				errorMessage = "You wrote "
						+ parsedIpa[0]
						+ " + "
						+ parsedIpa[1]
						+ " (two vowels). You should choose one consonant and one vowel.";

			} else if ("wjh".contains(parsedIpa[1])) {

				// ends with wjh
				errorMessage = "You wrote " + parsedIpa[0] + parsedIpa[1]
						+ ", but a syllable never ends with the /"
						+ parsedIpa[1] + "/ sound in English.";
			}
		}

		return errorMessage;
	}
}