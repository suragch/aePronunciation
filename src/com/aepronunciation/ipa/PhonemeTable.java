package com.aepronunciation.ipa;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhonemeTable {
    static final int P_VOWEL = 1;
    static final int P_CONSONANT = 2;
    static final int P_SPECIAL = 4;
    static final int P_UNSTRESSED = 8;

    static final PhonemeTable INSTANCE = new PhonemeTable();

    private HashMap<String, Integer> soundFlagsMap;

    public PhonemeTable() {
        init();
    }

    public boolean isVowel(String s) {
        return (getFlags(s) & P_VOWEL) != 0;
    }

    public boolean isConsonant(String s) {
        return (getFlags(s) & P_CONSONANT) != 0;
    }

    public boolean isStress(String s) {
        return (getFlags(s) & P_UNSTRESSED) != 0;
    }

    public int getFlags(String s) {
       if (soundFlagsMap.containsKey(s)) {
           return soundFlagsMap.get(s);
       }
       return 0;
    }

    public ArrayList<String> getAllVowels() {
        ArrayList<String> vowels = new ArrayList<String>();
       for (Map.Entry<String, Integer> entry: soundFlagsMap.entrySet()) {
           if ((entry.getValue() & P_VOWEL) != 0) {
              vowels.add(entry.getKey());
           }
       }
        return vowels;
    }

    public ArrayList<String> getAllVowelsWithoutUnstressed() {
        ArrayList<String> vowels = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry: soundFlagsMap.entrySet()) {
            if (entry.getValue() == P_VOWEL) {
                vowels.add(entry.getKey());
            }
        }
        return vowels;
    }

    public ArrayList<String> getAllConsonants() {
        ArrayList<String> vowels = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry: soundFlagsMap.entrySet()) {
            if ((entry.getValue() & P_CONSONANT) != 0) {
                vowels.add(entry.getKey());
            }
        }
        return vowels;
    }

    private void init() {
        soundFlagsMap = new HashMap<String, Integer>();

        soundFlagsMap.put("p", P_CONSONANT);
        soundFlagsMap.put("t", P_CONSONANT);
        soundFlagsMap.put("k", P_CONSONANT);
        soundFlagsMap.put("ʧ", P_CONSONANT);
        soundFlagsMap.put("f", P_CONSONANT);
        soundFlagsMap.put("θ", P_CONSONANT);
        soundFlagsMap.put("s", P_CONSONANT);
        soundFlagsMap.put("ʃ", P_CONSONANT);
        soundFlagsMap.put("b", P_CONSONANT);
        soundFlagsMap.put("d", P_CONSONANT);
        soundFlagsMap.put("g", P_CONSONANT);
        soundFlagsMap.put("ʤ", P_CONSONANT);
        soundFlagsMap.put("v", P_CONSONANT);
        soundFlagsMap.put("ð", P_CONSONANT);
        soundFlagsMap.put("z", P_CONSONANT);
        soundFlagsMap.put("ʒ", P_CONSONANT);
        soundFlagsMap.put("m", P_CONSONANT);
        soundFlagsMap.put("n", P_CONSONANT);
        soundFlagsMap.put("ŋ", P_CONSONANT);
        soundFlagsMap.put("l", P_CONSONANT);
        soundFlagsMap.put("w", P_CONSONANT);
        soundFlagsMap.put("j", P_CONSONANT);
        soundFlagsMap.put("h", P_CONSONANT);
        soundFlagsMap.put("r", P_CONSONANT);
        soundFlagsMap.put("i", P_VOWEL);
        soundFlagsMap.put("ɪ", P_VOWEL);
        soundFlagsMap.put("ɛ", P_VOWEL);
        soundFlagsMap.put("æ", P_VOWEL);
        soundFlagsMap.put("ɑ", P_VOWEL);
        soundFlagsMap.put("ɔ", P_VOWEL);
        soundFlagsMap.put("ʊ", P_VOWEL);
        soundFlagsMap.put("u", P_VOWEL);
        soundFlagsMap.put("ʌ", P_VOWEL);
        soundFlagsMap.put("ə", P_VOWEL | P_UNSTRESSED);
        soundFlagsMap.put("e", P_VOWEL);
        soundFlagsMap.put("aɪ", P_VOWEL);
        soundFlagsMap.put("aʊ", P_VOWEL);
        soundFlagsMap.put("ɔɪ", P_VOWEL);
        soundFlagsMap.put("o", P_VOWEL);
        soundFlagsMap.put("ɝ", P_VOWEL);
        soundFlagsMap.put("ɚ", P_VOWEL | P_UNSTRESSED);
        soundFlagsMap.put("ɑr", P_VOWEL);
        soundFlagsMap.put("ɛr", P_VOWEL);
        soundFlagsMap.put("ɪr", P_VOWEL);
        soundFlagsMap.put("ɔr", P_VOWEL);
        soundFlagsMap.put("ʔ", P_SPECIAL);
        soundFlagsMap.put("ɾ", P_SPECIAL);
    }

    // return <C, V>
    public Pair<String, String> SplitDoubleSound(String s) {
        if (isConsonant(s.substring(0, 1))) {
            return Pair.create(s.substring(0,1), s.substring(1));
        } else {
            return Pair.create(s.substring(s.length()-1), s.substring(0, s.length()-1));
        }
    }

}
