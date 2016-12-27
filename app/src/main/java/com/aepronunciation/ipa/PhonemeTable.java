package com.aepronunciation.ipa;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PhonemeTable {

    private static final int P_VOWEL = 1;
    private static final int P_CONSONANT = 2;
    private static final int P_SPECIAL = 4;
    private static final int P_UNSTRESSED = 8;
    private static final int P_INITIAL_FINAL = 16;

    static final int NUMBER_OF_VOWELS = 21;
    static final int NUMBER_OF_VOWELS_FOR_DOUBLES = 19; // not ə, ɚ
    static final int NUMBER_OF_CONSONANTS = 26;
    static final int NUMBER_OF_CONSONANTS_FOR_DOUBLES = 24; // not ʔ. ɾ

    static final PhonemeTable INSTANCE = new PhonemeTable();

    private HashMap<String, Integer> soundFlagsMap;

    private PhonemeTable() {
        init();
    }

    boolean isVowel(String s) {
        return (getFlags(s) & P_VOWEL) != 0;
    }

    boolean isConsonant(String s) {
        return (getFlags(s) & P_CONSONANT) != 0;
    }

    boolean isSpecial(String s) {
        return (getFlags(s) & P_SPECIAL) != 0;
    }

    boolean hasTwoPronunciations(String s) {
        return (getFlags(s) & P_INITIAL_FINAL) != 0;
    }


    private int getFlags(String s) {
        if (soundFlagsMap.containsKey(s)) {
            return soundFlagsMap.get(s);
        }
        return 0;
    }

    ArrayList<String> getAllVowels() {
        ArrayList<String> vowels = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: soundFlagsMap.entrySet()) {
            if ((entry.getValue() & P_VOWEL) != 0) {
                vowels.add(entry.getKey());
            }
        }
        return vowels;
    }

    ArrayList<String> getAllConsonants() {
        ArrayList<String> vowels = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: soundFlagsMap.entrySet()) {
            if ((entry.getValue() & P_CONSONANT) != 0) {
                vowels.add(entry.getKey());
            }
        }
        return vowels;
    }

    private void init() {
        soundFlagsMap = new HashMap<>();

        soundFlagsMap.put("p", P_CONSONANT);
        soundFlagsMap.put("t", P_CONSONANT);
        soundFlagsMap.put("k", P_CONSONANT);
        soundFlagsMap.put("ʧ", P_CONSONANT);
        soundFlagsMap.put("f", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("θ", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("s", P_CONSONANT);
        soundFlagsMap.put("ʃ", P_CONSONANT);
        soundFlagsMap.put("b", P_CONSONANT);
        soundFlagsMap.put("d", P_CONSONANT);
        soundFlagsMap.put("g", P_CONSONANT);
        soundFlagsMap.put("ʤ", P_CONSONANT);
        soundFlagsMap.put("v", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("ð", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("z", P_CONSONANT);
        soundFlagsMap.put("ʒ", P_CONSONANT);
        soundFlagsMap.put("m", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("n", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("ŋ", P_CONSONANT);
        soundFlagsMap.put("l", P_CONSONANT | P_INITIAL_FINAL);
        soundFlagsMap.put("w", P_CONSONANT);
        soundFlagsMap.put("j", P_CONSONANT);
        soundFlagsMap.put("h", P_CONSONANT);
        soundFlagsMap.put("r", P_CONSONANT);
        soundFlagsMap.put("ʔ", P_CONSONANT | P_SPECIAL);
        soundFlagsMap.put("ɾ", P_CONSONANT | P_SPECIAL);
        soundFlagsMap.put("i", P_VOWEL);
        soundFlagsMap.put("ɪ", P_VOWEL);
        soundFlagsMap.put("ɛ", P_VOWEL);
        soundFlagsMap.put("æ", P_VOWEL);
        soundFlagsMap.put("ɑ", P_VOWEL);
        soundFlagsMap.put("ɔ", P_VOWEL);
        soundFlagsMap.put("ʊ", P_VOWEL);
        soundFlagsMap.put("u", P_VOWEL);
        soundFlagsMap.put("ʌ", P_VOWEL);
        soundFlagsMap.put("ə", P_VOWEL | P_UNSTRESSED | P_SPECIAL);
        soundFlagsMap.put("eɪ", P_VOWEL);
        soundFlagsMap.put("aɪ", P_VOWEL);
        soundFlagsMap.put("aʊ", P_VOWEL);
        soundFlagsMap.put("ɔɪ", P_VOWEL);
        soundFlagsMap.put("oʊ", P_VOWEL);
        soundFlagsMap.put("ɝ", P_VOWEL);
        soundFlagsMap.put("ɚ", P_VOWEL | P_UNSTRESSED | P_SPECIAL);
        soundFlagsMap.put("ɑr", P_VOWEL);
        soundFlagsMap.put("ɛr", P_VOWEL);
        soundFlagsMap.put("ɪr", P_VOWEL);
        soundFlagsMap.put("ɔr", P_VOWEL);
    }

    // return <C, V>
    Pair<String, String> splitDoubleSound(String s) {
        if (isConsonant(s.substring(0, 1))) {
            return Pair.create(s.substring(0,1), s.substring(1));
        } else {
            return Pair.create(s.substring(s.length()-1), s.substring(0, s.length()-1));
        }
    }
}