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
    static final String p = "p";
    static final String t = "t";
    static final String k = "k";
    static final String ch = "ʧ";
    static final String f = "f";
    static final String th_voiceless = "θ";
    static final String s = "s";
    static final String sh = "ʃ";
    static final String b = "b";
    static final String d = "d";
    static final String g = "g";
    static final String dzh = "ʤ";
    static final String v = "v";
    static final String th_voiced = "ð";
    static final String z = "z";
    static final String zh = "ʒ";
    static final String m = "m";
    static final String n = "n";
    static final String ng = "ŋ";
    static final String l = "l";
    static final String w = "w";
    static final String j = "j";
    static final String h = "h";
    static final String r = "r";
    static final String i = "i";
    static final String i_short = "ɪ";
    static final String e_short = "ɛ";
    static final String ae = "æ";
    static final String a = "ɑ";
    static final String c_backwards = "ɔ";
    static final String u_short = "ʊ";
    static final String u = "u";
    static final String v_upsidedown = "ʌ";
    static final String schwa = "ə";
    static final String ei = "eɪ";
    static final String ai = "aɪ";
    static final String au = "aʊ";
    static final String oi = "ɔɪ";
    static final String ou = "oʊ";
    static final String flap_t = "ɾ";
    static final String er_stressed = "ɝ";
    static final String er_unstressed = "ɚ";
    static final String ar = "ɑr";
    static final String er = "ɛr";
    static final String ir = "ɪr";
    static final String or = "ɔr";
    static final String glottal_stop = "ʔ";
    static final String left_bracket = "[";
    static final String right_bracket = "]";
    static final String slash = "/";
    static final String undertie = "‿";
    //static final String space = "\u0020";
    static final String primary_stress = "ˈ";
    static final String secondary_stress = "ˌ";
    static final String long_vowel = "ː";
    //static final String return = "\n";
    static final String alt_e_short = "e";
    static final String alt_c_backwards = "ɒ";
    static final String alt_ou = "əʊ";
    static final String alt_er_stressed = "ɜː";
    static final String alt_er_unstressed = "ə";
    static final String alt_ar = "ɑː";
    static final String alt_er = "eə";
    static final String alt_ir = "ɪə";
    static final String alt_or = "ɔː";
    static final String alt_l = "ɫ";
    static final String alt_w = "ʍ";
    static final String alt_h = "ʰ";

    static final String alt_r = "ɹ";
}
