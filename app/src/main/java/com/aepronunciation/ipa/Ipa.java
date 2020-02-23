package com.aepronunciation.ipa;


import android.util.Pair;

import java.util.ArrayList;

class Ipa {

    static final int NUMBER_OF_VOWELS = 21;
    static final int NUMBER_OF_VOWELS_FOR_DOUBLES = 19; // not ə, ɚ
    static final int NUMBER_OF_CONSONANTS = 26;
    static final int NUMBER_OF_CONSONANTS_FOR_DOUBLES = 24; // not ʔ. ɾ

    // return <C, V>
    static Pair<String, String> splitDoubleSound(String s) {
        if (isConsonant(s.substring(0, 1))) {
            return Pair.create(s.substring(0, 1), s.substring(1));
        }
        return Pair.create(s.substring(s.length() - 1), s.substring(0, s.length() - 1));
    }

    static boolean isConsonant(String ipa) {
        return "ptkʧfθsʃbdgʤvðzʒmnŋlwjhrʔɾ".contains(ipa);
    }

    static boolean isSpecial(String ipa) {
        return "ʔɾəɚ".contains(ipa);
    }

    static boolean hasTwoPronunciations(String ipa) {
        return "fvθðmnl".contains(ipa);
    }

    static ArrayList<String> getAllVowels() {
        ArrayList<String> vowels = new ArrayList<>();
        vowels.add(i);
        vowels.add(i_short);
        vowels.add(e_short);
        vowels.add(ae);
        vowels.add(a);
        vowels.add(c_backwards);
        vowels.add(u_short);
        vowels.add(u);
        vowels.add(v_upsidedown);
        vowels.add(schwa);
        vowels.add(ei);
        vowels.add(ai);
        vowels.add(au);
        vowels.add(oi);
        vowels.add(ou);
        vowels.add(er_stressed);
        vowels.add(er_unstressed);
        vowels.add(ar);
        vowels.add(er);
        vowels.add(ir);
        vowels.add(or);
        return vowels;
    }


    static ArrayList<String> getAllConsonants() {
        ArrayList<String> consonants = new ArrayList<>();
        consonants.add(p);
        consonants.add(t);
        consonants.add(k);
        consonants.add(ch);
        consonants.add(f);
        consonants.add(th_voiceless);
        consonants.add(s);
        consonants.add(sh);
        consonants.add(b);
        consonants.add(d);
        consonants.add(g);
        consonants.add(dzh);
        consonants.add(v);
        consonants.add(th_voiced);
        consonants.add(z);
        consonants.add(zh);
        consonants.add(m);
        consonants.add(n);
        consonants.add(ng);
        consonants.add(l);
        consonants.add(w);
        consonants.add(j);
        consonants.add(h);
        consonants.add(r);
        consonants.add(glottal_stop);
        consonants.add(flap_t);

        return consonants;
    }
    private static final String p = "p";
    private static final String t = "t";
    private static final String k = "k";
    private static final String ch = "ʧ";
    private static final String f = "f";
    private static final String th_voiceless = "θ";
    private static final String s = "s";
    private static final String sh = "ʃ";
    private static final String b = "b";
    private static final String d = "d";
    private static final String g = "g";
    private static final String dzh = "ʤ";
    private static final String v = "v";
    private static final String th_voiced = "ð";
    private static final String z = "z";
    private static final String zh = "ʒ";
    private static final String m = "m";
    private static final String n = "n";
    private static final String ng = "ŋ";
    private static final String l = "l";
    private static final String w = "w";
    private static final String j = "j";
    private static final String h = "h";
    private static final String r = "r";
    private static final String i = "i";
    private static final String i_short = "ɪ";
    private static final String e_short = "ɛ";
    private static final String ae = "æ";
    private static final String a = "ɑ";
    private static final String c_backwards = "ɔ";
    private static final String u_short = "ʊ";
    private static final String u = "u";
    private static final String v_upsidedown = "ʌ";
    static final String schwa = "ə";
    static final String ei = "eɪ";
    static final String ai = "aɪ";
    static final String au = "aʊ";
    static final String oi = "ɔɪ";
    static final String ou = "oʊ";
    static final String flap_t = "ɾ";
    private static final String er_stressed = "ɝ";
    private static final String er_unstressed = "ɚ";
    static final String ar = "ɑr";
    static final String er = "ɛr";
    static final String ir = "ɪr";
    static final String or = "ɔr";
    private static final String glottal_stop = "ʔ";
    //private static final String left_bracket = "[";
    //private static final String right_bracket = "]";
    //private static final String slash = "/";
    //private static final String undertie = "‿";
    //private static final String space = "\u0020";
    //private static final String primary_stress = "ˈ";
    //private static final String secondary_stress = "ˌ";
    //private static final String long_vowel = "ː";
    //private static final String return = "\n";
    //private static final String alt_e_short = "e";
    //private static final String alt_c_backwards = "ɒ";
    //private static final String alt_ou = "əʊ";
    //private static final String alt_er_stressed = "ɜː";
    //private static final String alt_er_unstressed = "ə";
    //private static final String alt_ar = "ɑː";
    //private static final String alt_er = "eə";
    //private static final String alt_ir = "ɪə";
    //private static final String alt_or = "ɔː";
    //private static final String alt_l = "ɫ";
    //private static final String alt_w = "ʍ";
    //private static final String alt_h = "ʰ";

    //private static final String alt_r = "ɹ";
}
