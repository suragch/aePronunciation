package com.aepronunciation.ipa;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

// making this parcelable (rather than serialized) improves speed
class Answer implements Parcelable {
    private String correct;
    private String user;

    // constructor
    Answer() {}

    // constructor when parceled
    private Answer(Parcel in) {
        correct = in.readString();
        user = in.readString();
    }

    // getters
    String getCorrectAnswer() {
        return correct;
    }

    String getUserAnswer() {
        return user;
    }

    // setters
    void setCorrectAnswer(String correct) {
        this.correct = correct;
    }

    void setUserAnswer(String user) {
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

    static String[] parseDouble(String ipaDouble) {
        if (TextUtils.isEmpty(ipaDouble)) {
            return null;
        }
        String[] returnIpa = new String[2];


        // TODO this is error prone if changes happen elsewhere. Check for consonant instead.
        if (ipaDouble.startsWith("aɪ") || ipaDouble.startsWith("aʊ")
                || ipaDouble.startsWith("eɪ") || ipaDouble.startsWith("oʊ")
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

    static String getErrorMessage(Context context, String ipa) {

        // Types of errors:
        //
        // can't begin with ŋ
        // can't end with w j h
        // r was used as the second sound
        // two vowels
        // two consonants

        String errorMessage = "";

        String[] parsedIpa = parseDouble(ipa);
        if (parsedIpa == null || parsedIpa.length != 2) {
            // TODO return a more intelligent error message (but this situation shouldn't ever happen)
            return errorMessage;
        }

        // starts with a consonant
        if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa[0])) {

            // ends with a consonant
            if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa[1])) {

                errorMessage = String.format(context.getString(R.string.error_two_consonants), parsedIpa[0], parsedIpa[1]);

            } else if (parsedIpa[0].equals("ŋ")) {

                // starts with ŋ
                errorMessage = String.format(context.getString(R.string.error_initial_ng), parsedIpa[0], parsedIpa[1]);

            }

        } else { // starts with a vowel


            if (parsedIpa[1].equals("r")) {

                // ends in r
                errorMessage = context.getString(R.string.error_final_r);

            } else if ("iɪɛæɑɔʊuʌəeɪaɪaʊɔɪoʊɝɚɑrɛrɪrɔr".contains(parsedIpa[1])) {

                // ends with vowel
                errorMessage = String.format(context.getString(R.string.error_two_vowels), parsedIpa[0], parsedIpa[1]);

            } else if ("wjh".contains(parsedIpa[1])) {

                // ends with wjh
                errorMessage = String.format(context.getString(R.string.error_final_wjh), parsedIpa[0], parsedIpa[1]);
            }
        }

        return errorMessage;
    }
}


