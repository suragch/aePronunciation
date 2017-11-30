package com.aepronunciation.ipa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import android.text.TextUtils;
import android.util.Pair;

class DoubleSound {

    // private class variables
    private static final Map<String, Integer> soundMap = createSoundMap();
    private static final Map<String, Integer> specialSoundMap = createSpecialSoundMap();
    private static Random random = new Random();
    private ArrayList<String> doubleSounds;

    int getSoundCount() {
        if (doubleSounds == null) return 0;
        return doubleSounds.size();
    }

    void restrictListToPairsContainingAtLeastOneSoundFrom(
            ArrayList<String> consonants, ArrayList<String> vowels) {

        // error checking
        if (consonants.isEmpty() && vowels.isEmpty()) return;

        // loop through all pairs and add any for which both consonant and vowel
        // are in allowedSounds
        doubleSounds = new ArrayList<>();
        for (String key : soundMap.keySet()) {
            Pair<String, String> cv = Ipa.splitDoubleSound(key);
            if (stringContainsItemFromList(cv.first, consonants) ||
                    stringContainsItemFromList(cv.second, vowels)) {
                doubleSounds.add(key);
            }
        }
    }

    void restrictListToAllPairsContaining(String ipa) {
        if (TextUtils.isEmpty(ipa)) return;

        if (Ipa.isSpecial(ipa)) {
            restrictListToSpecialSoundsContaining(ipa);
            return;
        }

        ArrayList<String> singleItemArray = new ArrayList<>();
        singleItemArray.add(ipa);

        if (Ipa.isConsonant(ipa)) {
            restrictListToPairsContainingAtLeastOneSoundFrom(singleItemArray, new ArrayList<String>());
        } else { // vowel
            restrictListToPairsContainingAtLeastOneSoundFrom(new ArrayList<String>(), singleItemArray);
        }
    }

    void restrictListToPairsContainingBothSoundsFrom(
            ArrayList<String> consonants, ArrayList<String> vowels) {

        // Since every pair contains both a vowel and a consonant,
        // an exact match requires both to be present.
        if (vowels.isEmpty() || consonants.isEmpty()) return;

        // loop through all pairs and add any for with both consonant and vowel
        // are in allowedSounds
        doubleSounds = new ArrayList<>();
        for (String key : soundMap.keySet()) {
            Pair<String, String> cv = Ipa.splitDoubleSound(key);
            if (stringContainsItemFromList(cv.first, consonants) &&
                    stringContainsItemFromList(cv.second, vowels)) {
                doubleSounds.add(key);
            }
        }
    }

    private void restrictListToSpecialSoundsContaining(String ipa) {
        doubleSounds = new ArrayList<>();
        for (String key : specialSoundMap.keySet()) {
            if (shouldIncludeSpecialSoundForIpa(key, ipa)) {
                doubleSounds.add(key);
            }
        }
    }

    private boolean shouldIncludeSpecialSoundForIpa(String specialSound, String ipa) {
        if (!specialSound.contains(ipa)) return false;
        if (specialSound.contains(Ipa.flap_t) && ipa.equals(Ipa.schwa)) return false;
        return true;
    }

    void includeAllSounds() {
        doubleSounds = new ArrayList<>(soundMap.keySet());
    }

    private static boolean stringContainsItemFromList(String inputString, ArrayList<String> items) {
        for (int i = 0; i < items.size(); i++) {
            if (inputString.equals(items.get(i))) {
                return true;
            }
        }
        return false;
    }

    String getRandomIpa() {

        // get double sounds string array
        // this does not include schwa and unstressed er
        if (doubleSounds == null || doubleSounds.size() == 0) {
            includeAllSounds();
        }

        // get random integer (0 <= x < numberOfSounds)
        int soundIndex = random.nextInt(doubleSounds.size());

        // return ipa string
        return doubleSounds.get(soundIndex);
    }

    int getSoundResourceId(String doubleSoundIpa) {

        if (specialSoundMap.containsKey(doubleSoundIpa)) {
            return specialSoundMap.get(doubleSoundIpa);
        }

        if (soundMap.containsKey(doubleSoundIpa)) {
            return soundMap.get(doubleSoundIpa);
        }

        return -1;
    }

    public ArrayList<String> getSounds() {
        if (doubleSounds == null) {
            includeAllSounds();
        }
        return doubleSounds;
    }

    static Pair<String, String> parse(String ipaDouble) {
        if (ipaDouble == null || ipaDouble.length() < 2) return null;

        String first;
        String second;

        if (!startsWithTwoCharVowel(ipaDouble)) {
            first = ipaDouble.substring(0, 1);
            second = ipaDouble.substring(1);
            return new Pair<>(first, second);
        }

        if (ipaDouble.length() == 2) {
            // this is technically an error (shouldn't separate a 2-char vowel)
            first = ipaDouble.substring(0, 1);
            second = ipaDouble.substring(1);
            return new Pair<>(first, second);
        }

        first = ipaDouble.substring(0, 2);
        second = ipaDouble.substring(2);
        return new Pair<>(first, second);
    }

    private static boolean startsWithTwoCharVowel(String ipaDouble) {
        return ipaDouble.startsWith(Ipa.ai) || ipaDouble.startsWith(Ipa.au)
                || ipaDouble.startsWith(Ipa.ei) || ipaDouble.startsWith(Ipa.ou)
                || ipaDouble.startsWith(Ipa.oi) || ipaDouble.startsWith(Ipa.ar)
                || ipaDouble.startsWith(Ipa.er) || ipaDouble.startsWith(Ipa.ir)
                || ipaDouble.startsWith(Ipa.or);
    }

    // initialize the result when new object created
    private static Map<String, Integer> createSoundMap() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("pi", R.raw.double_p_i);
        result.put("pɪ", R.raw.double_p_is);
        result.put("peɪ", R.raw.double_p_ei);
        result.put("pɛ", R.raw.double_p_e);
        result.put("pæ", R.raw.double_p_ae);
        result.put("pɑ", R.raw.double_p_a);
        result.put("pɔ", R.raw.double_p_c);
        result.put("poʊ", R.raw.double_p_ou);
        result.put("pʊ", R.raw.double_p_us);
        result.put("pu", R.raw.double_p_u);
        result.put("pʌ", R.raw.double_p_vu);
        result.put("paɪ", R.raw.double_p_ai);
        result.put("paʊ", R.raw.double_p_au);
        result.put("pɔɪ", R.raw.double_p_oi);
        result.put("pɝ", R.raw.double_p_ers);
        result.put("pɑr", R.raw.double_p_ar);
        result.put("pɛr", R.raw.double_p_er);
        result.put("pɪr", R.raw.double_p_ir);
        result.put("pɔr", R.raw.double_p_or);
        result.put("bi", R.raw.double_b_i);
        result.put("bɪ", R.raw.double_b_is);
        result.put("beɪ", R.raw.double_b_ei);
        result.put("bɛ", R.raw.double_b_e);
        result.put("bæ", R.raw.double_b_ae);
        result.put("bɑ", R.raw.double_b_a);
        result.put("bɔ", R.raw.double_b_c);
        result.put("boʊ", R.raw.double_b_ou);
        result.put("bʊ", R.raw.double_b_us);
        result.put("bu", R.raw.double_b_u);
        result.put("bʌ", R.raw.double_b_vu);
        result.put("baɪ", R.raw.double_b_ai);
        result.put("baʊ", R.raw.double_b_au);
        result.put("bɔɪ", R.raw.double_b_oi);
        result.put("bɝ", R.raw.double_b_ers);
        result.put("bɑr", R.raw.double_b_ar);
        result.put("bɛr", R.raw.double_b_er);
        result.put("bɪr", R.raw.double_b_ir);
        result.put("bɔr", R.raw.double_b_or);
        result.put("ti", R.raw.double_t_i);
        result.put("tɪ", R.raw.double_t_is);
        result.put("teɪ", R.raw.double_t_ei);
        result.put("tɛ", R.raw.double_t_e);
        result.put("tæ", R.raw.double_t_ae);
        result.put("tɑ", R.raw.double_t_a);
        result.put("tɔ", R.raw.double_t_c);
        result.put("toʊ", R.raw.double_t_ou);
        result.put("tʊ", R.raw.double_t_us);
        result.put("tu", R.raw.double_t_u);
        result.put("tʌ", R.raw.double_t_vu);
        result.put("taɪ", R.raw.double_t_ai);
        result.put("taʊ", R.raw.double_t_au);
        result.put("tɔɪ", R.raw.double_t_oi);
        result.put("tɝ", R.raw.double_t_ers);
        result.put("tɑr", R.raw.double_t_ar);
        result.put("tɛr", R.raw.double_t_er);
        result.put("tɪr", R.raw.double_t_ir);
        result.put("tɔr", R.raw.double_t_or);
        result.put("di", R.raw.double_d_i);
        result.put("dɪ", R.raw.double_d_is);
        result.put("deɪ", R.raw.double_d_ei);
        result.put("dɛ", R.raw.double_d_e);
        result.put("dæ", R.raw.double_d_ae);
        result.put("dɑ", R.raw.double_d_a);
        result.put("dɔ", R.raw.double_d_c);
        result.put("doʊ", R.raw.double_d_ou);
        result.put("dʊ", R.raw.double_d_us);
        result.put("du", R.raw.double_d_u);
        result.put("dʌ", R.raw.double_d_vu);
        result.put("daɪ", R.raw.double_d_ai);
        result.put("daʊ", R.raw.double_d_au);
        result.put("dɔɪ", R.raw.double_d_oi);
        result.put("dɝ", R.raw.double_d_ers);
        result.put("dɑr", R.raw.double_d_ar);
        result.put("dɛr", R.raw.double_d_er);
        result.put("dɪr", R.raw.double_d_ir);
        result.put("dɔr", R.raw.double_d_or);
        result.put("ki", R.raw.double_k_i);
        result.put("kɪ", R.raw.double_k_is);
        result.put("keɪ", R.raw.double_k_ei);
        result.put("kɛ", R.raw.double_k_e);
        result.put("kæ", R.raw.double_k_ae);
        result.put("kɑ", R.raw.double_k_a);
        result.put("kɔ", R.raw.double_k_c);
        result.put("koʊ", R.raw.double_k_ou);
        result.put("kʊ", R.raw.double_k_us);
        result.put("ku", R.raw.double_k_u);
        result.put("kʌ", R.raw.double_k_vu);
        result.put("kaɪ", R.raw.double_k_ai);
        result.put("kaʊ", R.raw.double_k_au);
        result.put("kɔɪ", R.raw.double_k_oi);
        result.put("kɝ", R.raw.double_k_ers);
        result.put("kɑr", R.raw.double_k_ar);
        result.put("kɛr", R.raw.double_k_er);
        result.put("kɪr", R.raw.double_k_ir);
        result.put("kɔr", R.raw.double_k_or);
        result.put("gi", R.raw.double_g_i);
        result.put("gɪ", R.raw.double_g_is);
        result.put("geɪ", R.raw.double_g_ei);
        result.put("gɛ", R.raw.double_g_e);
        result.put("gæ", R.raw.double_g_ae);
        result.put("gɑ", R.raw.double_g_a);
        result.put("gɔ", R.raw.double_g_c);
        result.put("goʊ", R.raw.double_g_ou);
        result.put("gʊ", R.raw.double_g_us);
        result.put("gu", R.raw.double_g_u);
        result.put("gʌ", R.raw.double_g_vu);
        result.put("gaɪ", R.raw.double_g_ai);
        result.put("gaʊ", R.raw.double_g_au);
        result.put("gɔɪ", R.raw.double_g_oi);
        result.put("gɝ", R.raw.double_g_ers);
        result.put("gɑr", R.raw.double_g_ar);
        result.put("gɛr", R.raw.double_g_er);
        result.put("gɪr", R.raw.double_g_ir);
        result.put("gɔr", R.raw.double_g_or);
        result.put("ʧi", R.raw.double_ch_i);
        result.put("ʧɪ", R.raw.double_ch_is);
        result.put("ʧeɪ", R.raw.double_ch_ei);
        result.put("ʧɛ", R.raw.double_ch_e);
        result.put("ʧæ", R.raw.double_ch_ae);
        result.put("ʧɑ", R.raw.double_ch_a);
        result.put("ʧɔ", R.raw.double_ch_c);
        result.put("ʧoʊ", R.raw.double_ch_ou);
        result.put("ʧʊ", R.raw.double_ch_us);
        result.put("ʧu", R.raw.double_ch_u);
        result.put("ʧʌ", R.raw.double_ch_vu);
        result.put("ʧaɪ", R.raw.double_ch_ai);
        result.put("ʧaʊ", R.raw.double_ch_au);
        result.put("ʧɔɪ", R.raw.double_ch_oi);
        result.put("ʧɝ", R.raw.double_ch_ers);
        result.put("ʧɑr", R.raw.double_ch_ar);
        result.put("ʧɛr", R.raw.double_ch_er);
        result.put("ʧɪr", R.raw.double_ch_ir);
        result.put("ʧɔr", R.raw.double_ch_or);
        result.put("ʤi", R.raw.double_dzh_i);
        result.put("ʤɪ", R.raw.double_dzh_is);
        result.put("ʤeɪ", R.raw.double_dzh_ei);
        result.put("ʤɛ", R.raw.double_dzh_e);
        result.put("ʤæ", R.raw.double_dzh_ae);
        result.put("ʤɑ", R.raw.double_dzh_a);
        result.put("ʤɔ", R.raw.double_dzh_c);
        result.put("ʤoʊ", R.raw.double_dzh_ou);
        result.put("ʤʊ", R.raw.double_dzh_us);
        result.put("ʤu", R.raw.double_dzh_u);
        result.put("ʤʌ", R.raw.double_dzh_vu);
        result.put("ʤaɪ", R.raw.double_dzh_ai);
        result.put("ʤaʊ", R.raw.double_dzh_au);
        result.put("ʤɔɪ", R.raw.double_dzh_oi);
        result.put("ʤɝ", R.raw.double_dzh_ers);
        result.put("ʤɑr", R.raw.double_dzh_ar);
        result.put("ʤɛr", R.raw.double_dzh_er);
        result.put("ʤɪr", R.raw.double_dzh_ir);
        result.put("ʤɔr", R.raw.double_dzh_or);
        result.put("fi", R.raw.double_f_i);
        result.put("fɪ", R.raw.double_f_is);
        result.put("feɪ", R.raw.double_f_ei);
        result.put("fɛ", R.raw.double_f_e);
        result.put("fæ", R.raw.double_f_ae);
        result.put("fɑ", R.raw.double_f_a);
        result.put("fɔ", R.raw.double_f_c);
        result.put("foʊ", R.raw.double_f_ou);
        result.put("fʊ", R.raw.double_f_us);
        result.put("fu", R.raw.double_f_u);
        result.put("fʌ", R.raw.double_f_vu);
        result.put("faɪ", R.raw.double_f_ai);
        result.put("faʊ", R.raw.double_f_au);
        result.put("fɔɪ", R.raw.double_f_oi);
        result.put("fɝ", R.raw.double_f_ers);
        result.put("fɑr", R.raw.double_f_ar);
        result.put("fɛr", R.raw.double_f_er);
        result.put("fɪr", R.raw.double_f_ir);
        result.put("fɔr", R.raw.double_f_or);
        result.put("vi", R.raw.double_v_i);
        result.put("vɪ", R.raw.double_v_is);
        result.put("veɪ", R.raw.double_v_ei);
        result.put("vɛ", R.raw.double_v_e);
        result.put("væ", R.raw.double_v_ae);
        result.put("vɑ", R.raw.double_v_a);
        result.put("vɔ", R.raw.double_v_c);
        result.put("voʊ", R.raw.double_v_ou);
        result.put("vʊ", R.raw.double_v_us);
        result.put("vu", R.raw.double_v_u);
        result.put("vʌ", R.raw.double_v_vu);
        result.put("vaɪ", R.raw.double_v_ai);
        result.put("vaʊ", R.raw.double_v_au);
        result.put("vɔɪ", R.raw.double_v_oi);
        result.put("vɝ", R.raw.double_v_ers);
        result.put("vɑr", R.raw.double_v_ar);
        result.put("vɛr", R.raw.double_v_er);
        result.put("vɪr", R.raw.double_v_ir);
        result.put("vɔr", R.raw.double_v_or);
        result.put("θi", R.raw.double_th_i);
        result.put("θɪ", R.raw.double_th_is);
        result.put("θeɪ", R.raw.double_th_ei);
        result.put("θɛ", R.raw.double_th_e);
        result.put("θæ", R.raw.double_th_ae);
        result.put("θɑ", R.raw.double_th_a);
        result.put("θɔ", R.raw.double_th_c);
        result.put("θoʊ", R.raw.double_th_ou);
        result.put("θʊ", R.raw.double_th_us);
        result.put("θu", R.raw.double_th_u);
        result.put("θʌ", R.raw.double_th_vu);
        result.put("θaɪ", R.raw.double_th_ai);
        result.put("θaʊ", R.raw.double_th_au);
        result.put("θɔɪ", R.raw.double_th_oi);
        result.put("θɝ", R.raw.double_th_ers);
        result.put("θɑr", R.raw.double_th_ar);
        result.put("θɛr", R.raw.double_th_er);
        result.put("θɪr", R.raw.double_th_ir);
        result.put("θɔr", R.raw.double_th_or);
        result.put("ði", R.raw.double_thv_i);
        result.put("ðɪ", R.raw.double_thv_is);
        result.put("ðeɪ", R.raw.double_thv_ei);
        result.put("ðɛ", R.raw.double_thv_e);
        result.put("ðæ", R.raw.double_thv_ae);
        result.put("ðɑ", R.raw.double_thv_a);
        result.put("ðɔ", R.raw.double_thv_c);
        result.put("ðoʊ", R.raw.double_thv_ou);
        result.put("ðʊ", R.raw.double_thv_us);
        result.put("ðu", R.raw.double_thv_u);
        result.put("ðʌ", R.raw.double_thv_vu);
        result.put("ðaɪ", R.raw.double_thv_ai);
        result.put("ðaʊ", R.raw.double_thv_au);
        result.put("ðɔɪ", R.raw.double_thv_oi);
        result.put("ðɝ", R.raw.double_thv_ers);
        result.put("ðɑr", R.raw.double_thv_ar);
        result.put("ðɛr", R.raw.double_thv_er);
        result.put("ðɪr", R.raw.double_thv_ir);
        result.put("ðɔr", R.raw.double_thv_or);
        result.put("si", R.raw.double_s_i);
        result.put("sɪ", R.raw.double_s_is);
        result.put("seɪ", R.raw.double_s_ei);
        result.put("sɛ", R.raw.double_s_e);
        result.put("sæ", R.raw.double_s_ae);
        result.put("sɑ", R.raw.double_s_a);
        result.put("sɔ", R.raw.double_s_c);
        result.put("soʊ", R.raw.double_s_ou);
        result.put("sʊ", R.raw.double_s_us);
        result.put("su", R.raw.double_s_u);
        result.put("sʌ", R.raw.double_s_vu);
        result.put("saɪ", R.raw.double_s_ai);
        result.put("saʊ", R.raw.double_s_au);
        result.put("sɔɪ", R.raw.double_s_oi);
        result.put("sɝ", R.raw.double_s_ers);
        result.put("sɑr", R.raw.double_s_ar);
        result.put("sɛr", R.raw.double_s_er);
        result.put("sɪr", R.raw.double_s_ir);
        result.put("sɔr", R.raw.double_s_or);
        result.put("zi", R.raw.double_z_i);
        result.put("zɪ", R.raw.double_z_is);
        result.put("zeɪ", R.raw.double_z_ei);
        result.put("zɛ", R.raw.double_z_e);
        result.put("zæ", R.raw.double_z_ae);
        result.put("zɑ", R.raw.double_z_a);
        result.put("zɔ", R.raw.double_z_c);
        result.put("zoʊ", R.raw.double_z_ou);
        result.put("zʊ", R.raw.double_z_us);
        result.put("zu", R.raw.double_z_u);
        result.put("zʌ", R.raw.double_z_vu);
        result.put("zaɪ", R.raw.double_z_ai);
        result.put("zaʊ", R.raw.double_z_au);
        result.put("zɔɪ", R.raw.double_z_oi);
        result.put("zɝ", R.raw.double_z_ers);
        result.put("zɑr", R.raw.double_z_ar);
        result.put("zɛr", R.raw.double_z_er);
        result.put("zɪr", R.raw.double_z_ir);
        result.put("zɔr", R.raw.double_z_or);
        result.put("ʃi", R.raw.double_sh_i);
        result.put("ʃɪ", R.raw.double_sh_is);
        result.put("ʃeɪ", R.raw.double_sh_ei);
        result.put("ʃɛ", R.raw.double_sh_e);
        result.put("ʃæ", R.raw.double_sh_ae);
        result.put("ʃɑ", R.raw.double_sh_a);
        result.put("ʃɔ", R.raw.double_sh_c);
        result.put("ʃoʊ", R.raw.double_sh_ou);
        result.put("ʃʊ", R.raw.double_sh_us);
        result.put("ʃu", R.raw.double_sh_u);
        result.put("ʃʌ", R.raw.double_sh_vu);
        result.put("ʃaɪ", R.raw.double_sh_ai);
        result.put("ʃaʊ", R.raw.double_sh_au);
        result.put("ʃɔɪ", R.raw.double_sh_oi);
        result.put("ʃɝ", R.raw.double_sh_ers);
        result.put("ʃɑr", R.raw.double_sh_ar);
        result.put("ʃɛr", R.raw.double_sh_er);
        result.put("ʃɪr", R.raw.double_sh_ir);
        result.put("ʃɔr", R.raw.double_sh_or);
        result.put("ʒi", R.raw.double_zh_i);
        result.put("ʒɪ", R.raw.double_zh_is);
        result.put("ʒeɪ", R.raw.double_zh_ei);
        result.put("ʒɛ", R.raw.double_zh_e);
        result.put("ʒæ", R.raw.double_zh_ae);
        result.put("ʒɑ", R.raw.double_zh_a);
        result.put("ʒɔ", R.raw.double_zh_c);
        result.put("ʒoʊ", R.raw.double_zh_ou);
        result.put("ʒʊ", R.raw.double_zh_us);
        result.put("ʒu", R.raw.double_zh_u);
        result.put("ʒʌ", R.raw.double_zh_vu);
        result.put("ʒaɪ", R.raw.double_zh_ai);
        result.put("ʒaʊ", R.raw.double_zh_au);
        result.put("ʒɔɪ", R.raw.double_zh_oi);
        result.put("ʒɝ", R.raw.double_zh_ers);
        result.put("ʒɑr", R.raw.double_zh_ar);
        result.put("ʒɛr", R.raw.double_zh_er);
        result.put("ʒɪr", R.raw.double_zh_ir);
        result.put("ʒɔr", R.raw.double_zh_or);
        result.put("mi", R.raw.double_m_i);
        result.put("mɪ", R.raw.double_m_is);
        result.put("meɪ", R.raw.double_m_ei);
        result.put("mɛ", R.raw.double_m_e);
        result.put("mæ", R.raw.double_m_ae);
        result.put("mɑ", R.raw.double_m_a);
        result.put("mɔ", R.raw.double_m_c);
        result.put("moʊ", R.raw.double_m_ou);
        result.put("mʊ", R.raw.double_m_us);
        result.put("mu", R.raw.double_m_u);
        result.put("mʌ", R.raw.double_m_vu);
        result.put("maɪ", R.raw.double_m_ai);
        result.put("maʊ", R.raw.double_m_au);
        result.put("mɔɪ", R.raw.double_m_oi);
        result.put("mɝ", R.raw.double_m_ers);
        result.put("mɑr", R.raw.double_m_ar);
        result.put("mɛr", R.raw.double_m_er);
        result.put("mɪr", R.raw.double_m_ir);
        result.put("mɔr", R.raw.double_m_or);
        result.put("ni", R.raw.double_n_i);
        result.put("nɪ", R.raw.double_n_is);
        result.put("neɪ", R.raw.double_n_ei);
        result.put("nɛ", R.raw.double_n_e);
        result.put("næ", R.raw.double_n_ae);
        result.put("nɑ", R.raw.double_n_a);
        result.put("nɔ", R.raw.double_n_c);
        result.put("noʊ", R.raw.double_n_ou);
        result.put("nʊ", R.raw.double_n_us);
        result.put("nu", R.raw.double_n_u);
        result.put("nʌ", R.raw.double_n_vu);
        result.put("naɪ", R.raw.double_n_ai);
        result.put("naʊ", R.raw.double_n_au);
        result.put("nɔɪ", R.raw.double_n_oi);
        result.put("nɝ", R.raw.double_n_ers);
        result.put("nɑr", R.raw.double_n_ar);
        result.put("nɛr", R.raw.double_n_er);
        result.put("nɪr", R.raw.double_n_ir);
        result.put("nɔr", R.raw.double_n_or);
        result.put("li", R.raw.double_l_i);
        result.put("lɪ", R.raw.double_l_is);
        result.put("leɪ", R.raw.double_l_ei);
        result.put("lɛ", R.raw.double_l_e);
        result.put("læ", R.raw.double_l_ae);
        result.put("lɑ", R.raw.double_l_a);
        result.put("lɔ", R.raw.double_l_c);
        result.put("loʊ", R.raw.double_l_ou);
        result.put("lʊ", R.raw.double_l_us);
        result.put("lu", R.raw.double_l_u);
        result.put("lʌ", R.raw.double_l_vu);
        result.put("laɪ", R.raw.double_l_ai);
        result.put("laʊ", R.raw.double_l_au);
        result.put("lɔɪ", R.raw.double_l_oi);
        result.put("lɝ", R.raw.double_l_ers);
        result.put("lɑr", R.raw.double_l_ar);
        result.put("lɛr", R.raw.double_l_er);
        result.put("lɪr", R.raw.double_l_ir);
        result.put("lɔr", R.raw.double_l_or);
        result.put("wi", R.raw.double_w_i);
        result.put("wɪ", R.raw.double_w_is);
        result.put("weɪ", R.raw.double_w_ei);
        result.put("wɛ", R.raw.double_w_e);
        result.put("wæ", R.raw.double_w_ae);
        result.put("wɑ", R.raw.double_w_a);
        result.put("wɔ", R.raw.double_w_c);
        result.put("woʊ", R.raw.double_w_ou);
        result.put("wʊ", R.raw.double_w_us);
        result.put("wu", R.raw.double_w_u);
        result.put("wʌ", R.raw.double_w_vu);
        result.put("waɪ", R.raw.double_w_ai);
        result.put("waʊ", R.raw.double_w_au);
        result.put("wɔɪ", R.raw.double_w_oi);
        result.put("wɝ", R.raw.double_w_ers);
        result.put("wɑr", R.raw.double_w_ar);
        result.put("wɛr", R.raw.double_w_er);
        result.put("wɪr", R.raw.double_w_ir);
        result.put("wɔr", R.raw.double_w_or);
        result.put("ji", R.raw.double_j_i);
        result.put("jɪ", R.raw.double_j_is);
        result.put("jeɪ", R.raw.double_j_ei);
        result.put("jɛ", R.raw.double_j_e);
        result.put("jæ", R.raw.double_j_ae);
        result.put("jɑ", R.raw.double_j_a);
        result.put("jɔ", R.raw.double_j_c);
        result.put("joʊ", R.raw.double_j_ou);
        result.put("jʊ", R.raw.double_j_us);
        result.put("ju", R.raw.double_j_u);
        result.put("jʌ", R.raw.double_j_vu);
        result.put("jaɪ", R.raw.double_j_ai);
        result.put("jaʊ", R.raw.double_j_au);
        result.put("jɔɪ", R.raw.double_j_oi);
        result.put("jɝ", R.raw.double_j_ers);
        result.put("jɑr", R.raw.double_j_ar);
        result.put("jɛr", R.raw.double_j_er);
        result.put("jɪr", R.raw.double_j_ir);
        result.put("jɔr", R.raw.double_j_or);
        result.put("hi", R.raw.double_h_i);
        result.put("hɪ", R.raw.double_h_is);
        result.put("heɪ", R.raw.double_h_ei);
        result.put("hɛ", R.raw.double_h_e);
        result.put("hæ", R.raw.double_h_ae);
        result.put("hɑ", R.raw.double_h_a);
        result.put("hɔ", R.raw.double_h_c);
        result.put("hoʊ", R.raw.double_h_ou);
        result.put("hʊ", R.raw.double_h_us);
        result.put("hu", R.raw.double_h_u);
        result.put("hʌ", R.raw.double_h_vu);
        result.put("haɪ", R.raw.double_h_ai);
        result.put("haʊ", R.raw.double_h_au);
        result.put("hɔɪ", R.raw.double_h_oi);
        result.put("hɝ", R.raw.double_h_ers);
        result.put("hɑr", R.raw.double_h_ar);
        result.put("hɛr", R.raw.double_h_er);
        result.put("hɪr", R.raw.double_h_ir);
        result.put("hɔr", R.raw.double_h_or);
        result.put("ri", R.raw.double_r_i);
        result.put("rɪ", R.raw.double_r_is);
        result.put("reɪ", R.raw.double_r_ei);
        result.put("rɛ", R.raw.double_r_e);
        result.put("ræ", R.raw.double_r_ae);
        result.put("rɑ", R.raw.double_r_a);
        result.put("rɔ", R.raw.double_r_c);
        result.put("roʊ", R.raw.double_r_ou);
        result.put("rʊ", R.raw.double_r_us);
        result.put("ru", R.raw.double_r_u);
        result.put("rʌ", R.raw.double_r_vu);
        result.put("raɪ", R.raw.double_r_ai);
        result.put("raʊ", R.raw.double_r_au);
        result.put("rɔɪ", R.raw.double_r_oi);
        result.put("rɝ", R.raw.double_r_ers);
        result.put("rɑr", R.raw.double_r_ar);
        result.put("rɛr", R.raw.double_r_er);
        result.put("rɪr", R.raw.double_r_ir);
        result.put("rɔr", R.raw.double_r_or);
        result.put("ip", R.raw.double_i_p);
        result.put("ɪp", R.raw.double_is_p);
        result.put("eɪp", R.raw.double_ei_p);
        result.put("ɛp", R.raw.double_e_p);
        result.put("æp", R.raw.double_ae_p);
        result.put("ɑp", R.raw.double_a_p);
        result.put("ɔp", R.raw.double_c_p);
        result.put("oʊp", R.raw.double_ou_p);
        result.put("ʊp", R.raw.double_us_p);
        result.put("up", R.raw.double_u_p);
        result.put("ʌp", R.raw.double_vu_p);
        result.put("aɪp", R.raw.double_ai_p);
        result.put("aʊp", R.raw.double_au_p);
        result.put("ɔɪp", R.raw.double_oi_p);
        result.put("ɝp", R.raw.double_ers_p);
        result.put("ɑrp", R.raw.double_ar_p);
        result.put("ɛrp", R.raw.double_er_p);
        result.put("ɪrp", R.raw.double_ir_p);
        result.put("ɔrp", R.raw.double_or_p);
        result.put("ib", R.raw.double_i_b);
        result.put("ɪb", R.raw.double_is_b);
        result.put("eɪb", R.raw.double_ei_b);
        result.put("ɛb", R.raw.double_e_b);
        result.put("æb", R.raw.double_ae_b);
        result.put("ɑb", R.raw.double_a_b);
        result.put("ɔb", R.raw.double_c_b);
        result.put("oʊb", R.raw.double_ou_b);
        result.put("ʊb", R.raw.double_us_b);
        result.put("ub", R.raw.double_u_b);
        result.put("ʌb", R.raw.double_vu_b);
        result.put("aɪb", R.raw.double_ai_b);
        result.put("aʊb", R.raw.double_au_b);
        result.put("ɔɪb", R.raw.double_oi_b);
        result.put("ɝb", R.raw.double_ers_b);
        result.put("ɑrb", R.raw.double_ar_b);
        result.put("ɛrb", R.raw.double_er_b);
        result.put("ɪrb", R.raw.double_ir_b);
        result.put("ɔrb", R.raw.double_or_b);
        result.put("it", R.raw.double_i_t);
        result.put("ɪt", R.raw.double_is_t);
        result.put("eɪt", R.raw.double_ei_t);
        result.put("ɛt", R.raw.double_e_t);
        result.put("æt", R.raw.double_ae_t);
        result.put("ɑt", R.raw.double_a_t);
        result.put("ɔt", R.raw.double_c_t);
        result.put("oʊt", R.raw.double_ou_t);
        result.put("ʊt", R.raw.double_us_t);
        result.put("ut", R.raw.double_u_t);
        result.put("ʌt", R.raw.double_vu_t);
        result.put("aɪt", R.raw.double_ai_t);
        result.put("aʊt", R.raw.double_au_t);
        result.put("ɔɪt", R.raw.double_oi_t);
        result.put("ɝt", R.raw.double_ers_t);
        result.put("ɑrt", R.raw.double_ar_t);
        result.put("ɛrt", R.raw.double_er_t);
        result.put("ɪrt", R.raw.double_ir_t);
        result.put("ɔrt", R.raw.double_or_t);
        result.put("id", R.raw.double_i_d);
        result.put("ɪd", R.raw.double_is_d);
        result.put("eɪd", R.raw.double_ei_d);
        result.put("ɛd", R.raw.double_e_d);
        result.put("æd", R.raw.double_ae_d);
        result.put("ɑd", R.raw.double_a_d);
        result.put("ɔd", R.raw.double_c_d);
        result.put("oʊd", R.raw.double_ou_d);
        result.put("ʊd", R.raw.double_us_d);
        result.put("ud", R.raw.double_u_d);
        result.put("ʌd", R.raw.double_vu_d);
        result.put("aɪd", R.raw.double_ai_d);
        result.put("aʊd", R.raw.double_au_d);
        result.put("ɔɪd", R.raw.double_oi_d);
        result.put("ɝd", R.raw.double_ers_d);
        result.put("ɑrd", R.raw.double_ar_d);
        result.put("ɛrd", R.raw.double_er_d);
        result.put("ɪrd", R.raw.double_ir_d);
        result.put("ɔrd", R.raw.double_or_d);
        result.put("ik", R.raw.double_i_k);
        result.put("ɪk", R.raw.double_is_k);
        result.put("eɪk", R.raw.double_ei_k);
        result.put("ɛk", R.raw.double_e_k);
        result.put("æk", R.raw.double_ae_k);
        result.put("ɑk", R.raw.double_a_k);
        result.put("ɔk", R.raw.double_c_k);
        result.put("oʊk", R.raw.double_ou_k);
        result.put("ʊk", R.raw.double_us_k);
        result.put("uk", R.raw.double_u_k);
        result.put("ʌk", R.raw.double_vu_k);
        result.put("aɪk", R.raw.double_ai_k);
        result.put("aʊk", R.raw.double_au_k);
        result.put("ɔɪk", R.raw.double_oi_k);
        result.put("ɝk", R.raw.double_ers_k);
        result.put("ɑrk", R.raw.double_ar_k);
        result.put("ɛrk", R.raw.double_er_k);
        result.put("ɪrk", R.raw.double_ir_k);
        result.put("ɔrk", R.raw.double_or_k);
        result.put("ig", R.raw.double_i_g);
        result.put("ɪg", R.raw.double_is_g);
        result.put("eɪg", R.raw.double_ei_g);
        result.put("ɛg", R.raw.double_e_g);
        result.put("æg", R.raw.double_ae_g);
        result.put("ɑg", R.raw.double_a_g);
        result.put("ɔg", R.raw.double_c_g);
        result.put("oʊg", R.raw.double_ou_g);
        result.put("ʊg", R.raw.double_us_g);
        result.put("ug", R.raw.double_u_g);
        result.put("ʌg", R.raw.double_vu_g);
        result.put("aɪg", R.raw.double_ai_g);
        result.put("aʊg", R.raw.double_au_g);
        result.put("ɔɪg", R.raw.double_oi_g);
        result.put("ɝg", R.raw.double_ers_g);
        result.put("ɑrg", R.raw.double_ar_g);
        result.put("ɛrg", R.raw.double_er_g);
        result.put("ɪrg", R.raw.double_ir_g);
        result.put("ɔrg", R.raw.double_or_g);
        result.put("iʧ", R.raw.double_i_ch);
        result.put("ɪʧ", R.raw.double_is_ch);
        result.put("eɪʧ", R.raw.double_ei_ch);
        result.put("ɛʧ", R.raw.double_e_ch);
        result.put("æʧ", R.raw.double_ae_ch);
        result.put("ɑʧ", R.raw.double_a_ch);
        result.put("ɔʧ", R.raw.double_c_ch);
        result.put("oʊʧ", R.raw.double_ou_ch);
        result.put("ʊʧ", R.raw.double_us_ch);
        result.put("uʧ", R.raw.double_u_ch);
        result.put("ʌʧ", R.raw.double_vu_ch);
        result.put("aɪʧ", R.raw.double_ai_ch);
        result.put("aʊʧ", R.raw.double_au_ch);
        result.put("ɔɪʧ", R.raw.double_oi_ch);
        result.put("ɝʧ", R.raw.double_ers_ch);
        result.put("ɑrʧ", R.raw.double_ar_ch);
        result.put("ɛrʧ", R.raw.double_er_ch);
        result.put("ɪrʧ", R.raw.double_ir_ch);
        result.put("ɔrʧ", R.raw.double_or_ch);
        result.put("iʤ", R.raw.double_i_dzh);
        result.put("ɪʤ", R.raw.double_is_dzh);
        result.put("eɪʤ", R.raw.double_ei_dzh);
        result.put("ɛʤ", R.raw.double_e_dzh);
        result.put("æʤ", R.raw.double_ae_dzh);
        result.put("ɑʤ", R.raw.double_a_dzh);
        result.put("ɔʤ", R.raw.double_c_dzh);
        result.put("oʊʤ", R.raw.double_ou_dzh);
        result.put("ʊʤ", R.raw.double_us_dzh);
        result.put("uʤ", R.raw.double_u_dzh);
        result.put("ʌʤ", R.raw.double_vu_dzh);
        result.put("aɪʤ", R.raw.double_ai_dzh);
        result.put("aʊʤ", R.raw.double_au_dzh);
        result.put("ɔɪʤ", R.raw.double_oi_dzh);
        result.put("ɝʤ", R.raw.double_ers_dzh);
        result.put("ɑrʤ", R.raw.double_ar_dzh);
        result.put("ɛrʤ", R.raw.double_er_dzh);
        result.put("ɪrʤ", R.raw.double_ir_dzh);
        result.put("ɔrʤ", R.raw.double_or_dzh);
        result.put("if", R.raw.double_i_f);
        result.put("ɪf", R.raw.double_is_f);
        result.put("eɪf", R.raw.double_ei_f);
        result.put("ɛf", R.raw.double_e_f);
        result.put("æf", R.raw.double_ae_f);
        result.put("ɑf", R.raw.double_a_f);
        result.put("ɔf", R.raw.double_c_f);
        result.put("oʊf", R.raw.double_ou_f);
        result.put("ʊf", R.raw.double_us_f);
        result.put("uf", R.raw.double_u_f);
        result.put("ʌf", R.raw.double_vu_f);
        result.put("aɪf", R.raw.double_ai_f);
        result.put("aʊf", R.raw.double_au_f);
        result.put("ɔɪf", R.raw.double_oi_f);
        result.put("ɝf", R.raw.double_ers_f);
        result.put("ɑrf", R.raw.double_ar_f);
        result.put("ɛrf", R.raw.double_er_f);
        result.put("ɪrf", R.raw.double_ir_f);
        result.put("ɔrf", R.raw.double_or_f);
        result.put("iv", R.raw.double_i_v);
        result.put("ɪv", R.raw.double_is_v);
        result.put("eɪv", R.raw.double_ei_v);
        result.put("ɛv", R.raw.double_e_v);
        result.put("æv", R.raw.double_ae_v);
        result.put("ɑv", R.raw.double_a_v);
        result.put("ɔv", R.raw.double_c_v);
        result.put("oʊv", R.raw.double_ou_v);
        result.put("ʊv", R.raw.double_us_v);
        result.put("uv", R.raw.double_u_v);
        result.put("ʌv", R.raw.double_vu_v);
        result.put("aɪv", R.raw.double_ai_v);
        result.put("aʊv", R.raw.double_au_v);
        result.put("ɔɪv", R.raw.double_oi_v);
        result.put("ɝv", R.raw.double_ers_v);
        result.put("ɑrv", R.raw.double_ar_v);
        result.put("ɛrv", R.raw.double_er_v);
        result.put("ɪrv", R.raw.double_ir_v);
        result.put("ɔrv", R.raw.double_or_v);
        result.put("iθ", R.raw.double_i_th);
        result.put("ɪθ", R.raw.double_is_th);
        result.put("eɪθ", R.raw.double_ei_th);
        result.put("ɛθ", R.raw.double_e_th);
        result.put("æθ", R.raw.double_ae_th);
        result.put("ɑθ", R.raw.double_a_th);
        result.put("ɔθ", R.raw.double_c_th);
        result.put("oʊθ", R.raw.double_ou_th);
        result.put("ʊθ", R.raw.double_us_th);
        result.put("uθ", R.raw.double_u_th);
        result.put("ʌθ", R.raw.double_vu_th);
        result.put("aɪθ", R.raw.double_ai_th);
        result.put("aʊθ", R.raw.double_au_th);
        result.put("ɔɪθ", R.raw.double_oi_th);
        result.put("ɝθ", R.raw.double_ers_th);
        result.put("ɑrθ", R.raw.double_ar_th);
        result.put("ɛrθ", R.raw.double_er_th);
        result.put("ɪrθ", R.raw.double_ir_th);
        result.put("ɔrθ", R.raw.double_or_th);
        result.put("ið", R.raw.double_i_thv);
        result.put("ɪð", R.raw.double_is_thv);
        result.put("eɪð", R.raw.double_ei_thv);
        result.put("ɛð", R.raw.double_e_thv);
        result.put("æð", R.raw.double_ae_thv);
        result.put("ɑð", R.raw.double_a_thv);
        result.put("ɔð", R.raw.double_c_thv);
        result.put("oʊð", R.raw.double_ou_thv);
        result.put("ʊð", R.raw.double_us_thv);
        result.put("uð", R.raw.double_u_thv);
        result.put("ʌð", R.raw.double_vu_thv);
        result.put("aɪð", R.raw.double_ai_thv);
        result.put("aʊð", R.raw.double_au_thv);
        result.put("ɔɪð", R.raw.double_oi_thv);
        result.put("ɝð", R.raw.double_ers_thv);
        result.put("ɑrð", R.raw.double_ar_thv);
        result.put("ɛrð", R.raw.double_er_thv);
        result.put("ɪrð", R.raw.double_ir_thv);
        result.put("ɔrð", R.raw.double_or_thv);
        result.put("is", R.raw.double_i_s);
        result.put("ɪs", R.raw.double_is_s);
        result.put("eɪs", R.raw.double_ei_s);
        result.put("ɛs", R.raw.double_e_s);
        result.put("æs", R.raw.double_ae_s);
        result.put("ɑs", R.raw.double_a_s);
        result.put("ɔs", R.raw.double_c_s);
        result.put("oʊs", R.raw.double_ou_s);
        result.put("ʊs", R.raw.double_us_s);
        result.put("us", R.raw.double_u_s);
        result.put("ʌs", R.raw.double_vu_s);
        result.put("aɪs", R.raw.double_ai_s);
        result.put("aʊs", R.raw.double_au_s);
        result.put("ɔɪs", R.raw.double_oi_s);
        result.put("ɝs", R.raw.double_ers_s);
        result.put("ɑrs", R.raw.double_ar_s);
        result.put("ɛrs", R.raw.double_er_s);
        result.put("ɪrs", R.raw.double_ir_s);
        result.put("ɔrs", R.raw.double_or_s);
        result.put("iz", R.raw.double_i_z);
        result.put("ɪz", R.raw.double_is_z);
        result.put("eɪz", R.raw.double_ei_z);
        result.put("ɛz", R.raw.double_e_z);
        result.put("æz", R.raw.double_ae_z);
        result.put("ɑz", R.raw.double_a_z);
        result.put("ɔz", R.raw.double_c_z);
        result.put("oʊz", R.raw.double_ou_z);
        result.put("ʊz", R.raw.double_us_z);
        result.put("uz", R.raw.double_u_z);
        result.put("ʌz", R.raw.double_vu_z);
        result.put("aɪz", R.raw.double_ai_z);
        result.put("aʊz", R.raw.double_au_z);
        result.put("ɔɪz", R.raw.double_oi_z);
        result.put("ɝz", R.raw.double_ers_z);
        result.put("ɑrz", R.raw.double_ar_z);
        result.put("ɛrz", R.raw.double_er_z);
        result.put("ɪrz", R.raw.double_ir_z);
        result.put("ɔrz", R.raw.double_or_z);
        result.put("iʃ", R.raw.double_i_sh);
        result.put("ɪʃ", R.raw.double_is_sh);
        result.put("eɪʃ", R.raw.double_ei_sh);
        result.put("ɛʃ", R.raw.double_e_sh);
        result.put("æʃ", R.raw.double_ae_sh);
        result.put("ɑʃ", R.raw.double_a_sh);
        result.put("ɔʃ", R.raw.double_c_sh);
        result.put("oʊʃ", R.raw.double_ou_sh);
        result.put("ʊʃ", R.raw.double_us_sh);
        result.put("uʃ", R.raw.double_u_sh);
        result.put("ʌʃ", R.raw.double_vu_sh);
        result.put("aɪʃ", R.raw.double_ai_sh);
        result.put("aʊʃ", R.raw.double_au_sh);
        result.put("ɔɪʃ", R.raw.double_oi_sh);
        result.put("ɝʃ", R.raw.double_ers_sh);
        result.put("ɑrʃ", R.raw.double_ar_sh);
        result.put("ɛrʃ", R.raw.double_er_sh);
        result.put("ɪrʃ", R.raw.double_ir_sh);
        result.put("ɔrʃ", R.raw.double_or_sh);
        result.put("iʒ", R.raw.double_i_zh);
        result.put("ɪʒ", R.raw.double_is_zh);
        result.put("eɪʒ", R.raw.double_ei_zh);
        result.put("ɛʒ", R.raw.double_e_zh);
        result.put("æʒ", R.raw.double_ae_zh);
        result.put("ɑʒ", R.raw.double_a_zh);
        result.put("ɔʒ", R.raw.double_c_zh);
        result.put("oʊʒ", R.raw.double_ou_zh);
        result.put("ʊʒ", R.raw.double_us_zh);
        result.put("uʒ", R.raw.double_u_zh);
        result.put("ʌʒ", R.raw.double_vu_zh);
        result.put("aɪʒ", R.raw.double_ai_zh);
        result.put("aʊʒ", R.raw.double_au_zh);
        result.put("ɔɪʒ", R.raw.double_oi_zh);
        result.put("ɝʒ", R.raw.double_ers_zh);
        result.put("ɑrʒ", R.raw.double_ar_zh);
        result.put("ɛrʒ", R.raw.double_er_zh);
        result.put("ɪrʒ", R.raw.double_ir_zh);
        result.put("ɔrʒ", R.raw.double_or_zh);
        result.put("im", R.raw.double_i_m);
        result.put("ɪm", R.raw.double_is_m);
        result.put("eɪm", R.raw.double_ei_m);
        result.put("ɛm", R.raw.double_e_m);
        result.put("æm", R.raw.double_ae_m);
        result.put("ɑm", R.raw.double_a_m);
        result.put("ɔm", R.raw.double_c_m);
        result.put("oʊm", R.raw.double_ou_m);
        result.put("ʊm", R.raw.double_us_m);
        result.put("um", R.raw.double_u_m);
        result.put("ʌm", R.raw.double_vu_m);
        result.put("aɪm", R.raw.double_ai_m);
        result.put("aʊm", R.raw.double_au_m);
        result.put("ɔɪm", R.raw.double_oi_m);
        result.put("ɝm", R.raw.double_ers_m);
        result.put("ɑrm", R.raw.double_ar_m);
        result.put("ɛrm", R.raw.double_er_m);
        result.put("ɪrm", R.raw.double_ir_m);
        result.put("ɔrm", R.raw.double_or_m);
        result.put("in", R.raw.double_i_n);
        result.put("ɪn", R.raw.double_is_n);
        result.put("eɪn", R.raw.double_ei_n);
        result.put("ɛn", R.raw.double_e_n);
        result.put("æn", R.raw.double_ae_n);
        result.put("ɑn", R.raw.double_a_n);
        result.put("ɔn", R.raw.double_c_n);
        result.put("oʊn", R.raw.double_ou_n);
        result.put("ʊn", R.raw.double_us_n);
        result.put("un", R.raw.double_u_n);
        result.put("ʌn", R.raw.double_vu_n);
        result.put("aɪn", R.raw.double_ai_n);
        result.put("aʊn", R.raw.double_au_n);
        result.put("ɔɪn", R.raw.double_oi_n);
        result.put("ɝn", R.raw.double_ers_n);
        result.put("ɑrn", R.raw.double_ar_n);
        result.put("ɛrn", R.raw.double_er_n);
        result.put("ɪrn", R.raw.double_ir_n);
        result.put("ɔrn", R.raw.double_or_n);
        result.put("iŋ", R.raw.double_i_ng);
        result.put("ɪŋ", R.raw.double_is_ng);
        result.put("eɪŋ", R.raw.double_ei_ng);
        result.put("ɛŋ", R.raw.double_e_ng);
        result.put("æŋ", R.raw.double_ae_ng);
        result.put("ɑŋ", R.raw.double_a_ng);
        result.put("ɔŋ", R.raw.double_c_ng);
        result.put("oʊŋ", R.raw.double_ou_ng);
        result.put("ʊŋ", R.raw.double_us_ng);
        result.put("uŋ", R.raw.double_u_ng);
        result.put("ʌŋ", R.raw.double_vu_ng);
        result.put("aɪŋ", R.raw.double_ai_ng);
        result.put("aʊŋ", R.raw.double_au_ng);
        result.put("ɔɪŋ", R.raw.double_oi_ng);
        result.put("ɝŋ", R.raw.double_ers_ng);
        result.put("ɑrŋ", R.raw.double_ar_ng);
        result.put("ɛrŋ", R.raw.double_er_ng);
        result.put("ɪrŋ", R.raw.double_ir_ng);
        result.put("ɔrŋ", R.raw.double_or_ng);
        result.put("il", R.raw.double_i_l);
        result.put("ɪl", R.raw.double_is_l);
        result.put("eɪl", R.raw.double_ei_l);
        result.put("ɛl", R.raw.double_e_l);
        result.put("æl", R.raw.double_ae_l);
        result.put("ɑl", R.raw.double_a_l);
        result.put("ɔl", R.raw.double_c_l);
        result.put("oʊl", R.raw.double_ou_l);
        result.put("ʊl", R.raw.double_us_l);
        result.put("ul", R.raw.double_u_l);
        result.put("ʌl", R.raw.double_vu_l);
        result.put("aɪl", R.raw.double_ai_l);
        result.put("aʊl", R.raw.double_au_l);
        result.put("ɔɪl", R.raw.double_oi_l);
        result.put("ɝl", R.raw.double_ers_l);
        result.put("ɑrl", R.raw.double_ar_l);
        result.put("ɛrl", R.raw.double_er_l);
        result.put("ɪrl", R.raw.double_ir_l);
        result.put("ɔrl", R.raw.double_or_l);

        return result;
    }

    // initialize the result when new object created
    private static Map<String, Integer> createSpecialSoundMap() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("iʔ", R.raw.double_i_glottal);
        result.put("ɪʔ", R.raw.double_is_glottal);
        result.put("eɪʔ", R.raw.double_ei_glottal);
        result.put("ɛʔ", R.raw.double_e_glottal);
        result.put("æʔ", R.raw.double_ae_glottal);
        result.put("ɑʔ", R.raw.double_a_glottal);
        result.put("ɔʔ", R.raw.double_c_glottal);
        result.put("oʊʔ", R.raw.double_ou_glottal);
        result.put("ʊʔ", R.raw.double_us_glottal);
        result.put("uʔ", R.raw.double_u_glottal);
        result.put("ʌʔ", R.raw.double_vu_glottal);
        result.put("aɪʔ", R.raw.double_ai_glottal);
        result.put("aʊʔ", R.raw.double_au_glottal);
        result.put("ɔɪʔ", R.raw.double_oi_glottal);
        result.put("ɝʔ", R.raw.double_ers_glottal);
        result.put("ɑrʔ", R.raw.double_ar_glottal);
        result.put("ɛrʔ", R.raw.double_er_glottal);
        result.put("ɪrʔ", R.raw.double_ir_glottal);
        result.put("ɔrʔ", R.raw.double_or_glottal);
        result.put("ˈiɾə", R.raw.double_i_flap_shwua);
        result.put("ˈɪɾə", R.raw.double_is_flap_shwua);
        result.put("ˈeɪɾə", R.raw.double_ei_flap_shwua);
        result.put("ˈɛɾə", R.raw.double_e_flap_shwua);
        result.put("ˈæɾə", R.raw.double_ae_flap_shwua);
        result.put("ˈɑɾə", R.raw.double_a_flap_shwua);
        result.put("ˈɔɾə", R.raw.double_c_flap_shwua);
        result.put("ˈoʊɾə", R.raw.double_ou_flap_shwua);
        result.put("ˈʊɾə", R.raw.double_us_flap_shwua);
        result.put("ˈuɾə", R.raw.double_u_flap_shwua);
        result.put("ˈʌɾə", R.raw.double_vu_flap_shwua);
        result.put("ˈaɪɾə", R.raw.double_ai_flap_shwua);
        result.put("ˈaʊɾə", R.raw.double_au_flap_shwua);
        result.put("ˈɔɪɾə", R.raw.double_oi_flap_shwua);
        result.put("ˈɝɾə", R.raw.double_ers_flap_shwua);
        result.put("ˈɑrɾə", R.raw.double_ar_flap_shwua);
        result.put("ˈɛrɾə", R.raw.double_er_flap_shwua);
        result.put("ˈɪrɾə", R.raw.double_ir_flap_shwua);
        result.put("ˈɔrɾə", R.raw.double_or_flap_shwua);
        result.put("pə", R.raw.double_p_shwua);
        result.put("tə", R.raw.double_t_shwua);
        result.put("kə", R.raw.double_k_shwua);
        result.put("ʧə", R.raw.double_ch_shwua);
        result.put("fə", R.raw.double_f_shwua);
        result.put("θə", R.raw.double_th_shwua);
        result.put("sə", R.raw.double_s_shwua);
        result.put("ʃə", R.raw.double_sh_shwua);
        result.put("bə", R.raw.double_b_shwua);
        result.put("də", R.raw.double_d_shwua);
        result.put("gə", R.raw.double_g_shwua);
        result.put("ʤə", R.raw.double_dzh_shwua);
        result.put("və", R.raw.double_v_shwua);
        result.put("ðə", R.raw.double_thv_shwua);
        result.put("zə", R.raw.double_z_shwua);
        result.put("ʒə", R.raw.double_zh_shwua);
        result.put("mə", R.raw.double_m_shwua);
        result.put("nə", R.raw.double_n_shwua);
        result.put("lə", R.raw.double_l_shwua);
        result.put("wə", R.raw.double_w_shwua);
        result.put("jə", R.raw.double_j_shwua);
        result.put("hə", R.raw.double_h_shwua);
        result.put("rə", R.raw.double_r_shwua);
        result.put("əp", R.raw.double_shwua_p);
        result.put("ət", R.raw.double_shwua_t);
        result.put("ək", R.raw.double_shwua_k);
        result.put("əʧ", R.raw.double_shwua_ch);
        result.put("əf", R.raw.double_shwua_f);
        result.put("əθ", R.raw.double_shwua_th);
        result.put("əs", R.raw.double_shwua_s);
        result.put("əʃ", R.raw.double_shwua_sh);
        result.put("əb", R.raw.double_shwua_b);
        result.put("əd", R.raw.double_shwua_d);
        result.put("əg", R.raw.double_shwua_g);
        result.put("əʤ", R.raw.double_shwua_dzh);
        result.put("əv", R.raw.double_shwua_v);
        result.put("əð", R.raw.double_shwua_thv);
        result.put("əz", R.raw.double_shwua_z);
        result.put("əʒ", R.raw.double_shwua_zh);
        result.put("əm", R.raw.double_shwua_m);
        result.put("ən", R.raw.double_shwua_n);
        result.put("əŋ", R.raw.double_shwua_ng);
        result.put("əl", R.raw.double_shwua_l);
        result.put("pɚ", R.raw.double_p_eru);
        result.put("tɚ", R.raw.double_t_eru);
        result.put("kɚ", R.raw.double_k_eru);
        result.put("ʧɚ", R.raw.double_ch_eru);
        result.put("fɚ", R.raw.double_f_eru);
        result.put("θɚ", R.raw.double_th_eru);
        result.put("sɚ", R.raw.double_s_eru);
        result.put("ʃɚ", R.raw.double_sh_eru);
        result.put("bɚ", R.raw.double_b_eru);
        result.put("dɚ", R.raw.double_d_eru);
        result.put("gɚ", R.raw.double_g_eru);
        result.put("ʤɚ", R.raw.double_dzh_eru);
        result.put("vɚ", R.raw.double_v_eru);
        result.put("ðɚ", R.raw.double_thv_eru);
        result.put("zɚ", R.raw.double_z_eru);
        result.put("ʒɚ", R.raw.double_zh_eru);
        result.put("mɚ", R.raw.double_m_eru);
        result.put("nɚ", R.raw.double_n_eru);
        result.put("lɚ", R.raw.double_l_eru);
        result.put("wɚ", R.raw.double_w_eru);
        result.put("jɚ", R.raw.double_j_eru);
        result.put("hɚ", R.raw.double_h_eru);
        result.put("rɚ", R.raw.double_r_eru);
        result.put("ɚp", R.raw.double_eru_p);
        result.put("ɚt", R.raw.double_eru_t);
        result.put("ɚk", R.raw.double_eru_k);
        result.put("ɚʧ", R.raw.double_eru_ch);
        result.put("ɚf", R.raw.double_eru_f);
        result.put("ɚθ", R.raw.double_eru_th);
        result.put("ɚs", R.raw.double_eru_s);
        result.put("ɚʃ", R.raw.double_eru_sh);
        result.put("ɚb", R.raw.double_eru_b);
        result.put("ɚd", R.raw.double_eru_d);
        result.put("ɚg", R.raw.double_eru_g);
        result.put("ɚʤ", R.raw.double_eru_dzh);
        result.put("ɚv", R.raw.double_eru_v);
        result.put("ɚð", R.raw.double_eru_thv);
        result.put("ɚz", R.raw.double_eru_z);
        result.put("ɚʒ", R.raw.double_eru_zh);
        result.put("ɚm", R.raw.double_eru_m);
        result.put("ɚn", R.raw.double_eru_n);
        result.put("ɚŋ", R.raw.double_eru_ng);
        result.put("ɚl", R.raw.double_eru_l);

        return result;
    }
}