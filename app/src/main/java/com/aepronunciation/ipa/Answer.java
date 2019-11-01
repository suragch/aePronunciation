package com.aepronunciation.ipa;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

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

    static String getErrorMessage(Context context, String ipa) {

        // Types of errors:
        //
        // can't begin with ŋ
        // can't end with w j h
        // r was used as the second sound
        // two vowels
        // two consonants

        String errorMessage = "";

        Pair<String, String> parsedIpa = DoubleSound.parse(ipa);
        if (parsedIpa == null) {
            // TODO return a more intelligent error message
            // (but this situation shouldn't ever happen)
            return errorMessage;
        }

        // starts with a consonant
        if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa.first)) {

            // ends with a consonant
            if ("ptkʧfθsʃbdgʤvðzʒmnŋlwjhr".contains(parsedIpa.second)) {

                errorMessage = String.format(context.getString(R.string.error_two_consonants),
                        parsedIpa.first, parsedIpa.second);

            } else if (parsedIpa.first.equals("ŋ")) {

                // starts with ŋ
                errorMessage = String.format(context.getString(R.string.error_initial_ng),
                        parsedIpa.first, parsedIpa.second);

            }

        } else { // starts with a vowel


            if (parsedIpa.second.equals("r")) {

                // ends in r
                errorMessage = context.getString(R.string.error_final_r);

            } else if ("iɪɛæɑɔʊuʌəeɪaɪaʊɔɪoʊɝɚɑrɛrɪrɔr".contains(parsedIpa.second)) {

                // ends with vowel
                errorMessage = String.format(context.getString(R.string.error_two_vowels),
                        parsedIpa.first, parsedIpa.second);

            } else if ("wjh".contains(parsedIpa.second)) {

                // ends with wjh
                errorMessage = String.format(context.getString(R.string.error_final_wjh),
                        parsedIpa.first, parsedIpa.second);
            }
        }

        return errorMessage;
    }
}


