package com.aepronunciation.ipa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import android.text.TextUtils;
import android.util.Pair;

class DoubleSound {

    // private class variables
    private LinkedHashMap<String, Integer> hashMap;
    private static Random random = new Random();
    private ArrayList<String> doubleSounds;

    // constructor
    DoubleSound() {
        initMap();
    }

    int getSoundCount() {
        if (doubleSounds != null) {
            return doubleSounds.size();
        } else {
            return 0;
        }
    }

    void restrictListToPairsContainingAtLeastOneSoundFrom(ArrayList<String> consonants, ArrayList<String> vowels) {

        // error checking
        if (consonants.isEmpty() && vowels.isEmpty()) {
            return;
        }

        // loop through all pairs and add any for which both consonant and vowel are in allowedSounds
        doubleSounds = new ArrayList<>();
        for (String key : hashMap.keySet()) {
            Pair<String,String> cv = Ipa.splitDoubleSound(key);
            if (stringContainsItemFromList(cv.first, consonants) || stringContainsItemFromList(cv.second, vowels)) {
                doubleSounds.add(key);
            }
        }
    }

    void restrictListToAllPairsContaining(String ipa) {

        // error checking
        if (TextUtils.isEmpty(ipa)) {
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

    void restrictListToPairsContainingBothSoundsFrom(ArrayList<String> consonants, ArrayList<String> vowels) {

        // Since every pair contains both a vowel and a consonant,
        // an exact match requires both to be present.
        if (vowels.isEmpty() || consonants.isEmpty()) {
            return;
        }

        // loop through all pairs and add any for with both consonant and vowel are in allowedSounds
        doubleSounds = new ArrayList<>();
        for (String key : hashMap.keySet()) {
            Pair<String,String> cv = Ipa.splitDoubleSound(key);
            if (stringContainsItemFromList(cv.first, consonants) && stringContainsItemFromList(cv.second, vowels)) {
                doubleSounds.add(key);
            }
        }
    }

    void includeAllSounds() {
        doubleSounds = new ArrayList<>(hashMap.keySet());
    }

    private static boolean stringContainsItemFromList(String inputString, ArrayList<String> items) {
        for(int i =0; i < items.size(); i++) {
            if(inputString.equals(items.get(i))) {
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

        // returns null if no value found
        if (hashMap.containsKey(doubleSoundIpa)){
            return hashMap.get(doubleSoundIpa);
        }else{
            return -1;
        }

    }

    public ArrayList<String> getSounds() {
        if (doubleSounds == null) {
            includeAllSounds();
        }
        return doubleSounds;
    }

    // initialize the hashMap when new object created
    private void initMap() {

        hashMap = new LinkedHashMap<>();

        hashMap.put("pi", R.raw.double_p_i);
        hashMap.put("pɪ", R.raw.double_p_is);
        hashMap.put("peɪ", R.raw.double_p_ei);
        hashMap.put("pɛ", R.raw.double_p_e);
        hashMap.put("pæ", R.raw.double_p_ae);
        hashMap.put("pɑ", R.raw.double_p_a);
        hashMap.put("pɔ", R.raw.double_p_c);
        hashMap.put("poʊ", R.raw.double_p_ou);
        hashMap.put("pʊ", R.raw.double_p_us);
        hashMap.put("pu", R.raw.double_p_u);
        hashMap.put("pʌ", R.raw.double_p_vu);
        hashMap.put("paɪ", R.raw.double_p_ai);
        hashMap.put("paʊ", R.raw.double_p_au);
        hashMap.put("pɔɪ", R.raw.double_p_oi);
        hashMap.put("pɝ", R.raw.double_p_ers);
        hashMap.put("pɑr", R.raw.double_p_ar);
        hashMap.put("pɛr", R.raw.double_p_er);
        hashMap.put("pɪr", R.raw.double_p_ir);
        hashMap.put("pɔr", R.raw.double_p_or);
        hashMap.put("bi", R.raw.double_b_i);
        hashMap.put("bɪ", R.raw.double_b_is);
        hashMap.put("beɪ", R.raw.double_b_ei);
        hashMap.put("bɛ", R.raw.double_b_e);
        hashMap.put("bæ", R.raw.double_b_ae);
        hashMap.put("bɑ", R.raw.double_b_a);
        hashMap.put("bɔ", R.raw.double_b_c);
        hashMap.put("boʊ", R.raw.double_b_ou);
        hashMap.put("bʊ", R.raw.double_b_us);
        hashMap.put("bu", R.raw.double_b_u);
        hashMap.put("bʌ", R.raw.double_b_vu);
        hashMap.put("baɪ", R.raw.double_b_ai);
        hashMap.put("baʊ", R.raw.double_b_au);
        hashMap.put("bɔɪ", R.raw.double_b_oi);
        hashMap.put("bɝ", R.raw.double_b_ers);
        hashMap.put("bɑr", R.raw.double_b_ar);
        hashMap.put("bɛr", R.raw.double_b_er);
        hashMap.put("bɪr", R.raw.double_b_ir);
        hashMap.put("bɔr", R.raw.double_b_or);
        hashMap.put("ti", R.raw.double_t_i);
        hashMap.put("tɪ", R.raw.double_t_is);
        hashMap.put("teɪ", R.raw.double_t_ei);
        hashMap.put("tɛ", R.raw.double_t_e);
        hashMap.put("tæ", R.raw.double_t_ae);
        hashMap.put("tɑ", R.raw.double_t_a);
        hashMap.put("tɔ", R.raw.double_t_c);
        hashMap.put("toʊ", R.raw.double_t_ou);
        hashMap.put("tʊ", R.raw.double_t_us);
        hashMap.put("tu", R.raw.double_t_u);
        hashMap.put("tʌ", R.raw.double_t_vu);
        hashMap.put("taɪ", R.raw.double_t_ai);
        hashMap.put("taʊ", R.raw.double_t_au);
        hashMap.put("tɔɪ", R.raw.double_t_oi);
        hashMap.put("tɝ", R.raw.double_t_ers);
        hashMap.put("tɑr", R.raw.double_t_ar);
        hashMap.put("tɛr", R.raw.double_t_er);
        hashMap.put("tɪr", R.raw.double_t_ir);
        hashMap.put("tɔr", R.raw.double_t_or);
        hashMap.put("di", R.raw.double_d_i);
        hashMap.put("dɪ", R.raw.double_d_is);
        hashMap.put("deɪ", R.raw.double_d_ei);
        hashMap.put("dɛ", R.raw.double_d_e);
        hashMap.put("dæ", R.raw.double_d_ae);
        hashMap.put("dɑ", R.raw.double_d_a);
        hashMap.put("dɔ", R.raw.double_d_c);
        hashMap.put("doʊ", R.raw.double_d_ou);
        hashMap.put("dʊ", R.raw.double_d_us);
        hashMap.put("du", R.raw.double_d_u);
        hashMap.put("dʌ", R.raw.double_d_vu);
        hashMap.put("daɪ", R.raw.double_d_ai);
        hashMap.put("daʊ", R.raw.double_d_au);
        hashMap.put("dɔɪ", R.raw.double_d_oi);
        hashMap.put("dɝ", R.raw.double_d_ers);
        hashMap.put("dɑr", R.raw.double_d_ar);
        hashMap.put("dɛr", R.raw.double_d_er);
        hashMap.put("dɪr", R.raw.double_d_ir);
        hashMap.put("dɔr", R.raw.double_d_or);
        hashMap.put("ki", R.raw.double_k_i);
        hashMap.put("kɪ", R.raw.double_k_is);
        hashMap.put("keɪ", R.raw.double_k_ei);
        hashMap.put("kɛ", R.raw.double_k_e);
        hashMap.put("kæ", R.raw.double_k_ae);
        hashMap.put("kɑ", R.raw.double_k_a);
        hashMap.put("kɔ", R.raw.double_k_c);
        hashMap.put("koʊ", R.raw.double_k_ou);
        hashMap.put("kʊ", R.raw.double_k_us);
        hashMap.put("ku", R.raw.double_k_u);
        hashMap.put("kʌ", R.raw.double_k_vu);
        hashMap.put("kaɪ", R.raw.double_k_ai);
        hashMap.put("kaʊ", R.raw.double_k_au);
        hashMap.put("kɔɪ", R.raw.double_k_oi);
        hashMap.put("kɝ", R.raw.double_k_ers);
        hashMap.put("kɑr", R.raw.double_k_ar);
        hashMap.put("kɛr", R.raw.double_k_er);
        hashMap.put("kɪr", R.raw.double_k_ir);
        hashMap.put("kɔr", R.raw.double_k_or);
        hashMap.put("gi", R.raw.double_g_i);
        hashMap.put("gɪ", R.raw.double_g_is);
        hashMap.put("geɪ", R.raw.double_g_ei);
        hashMap.put("gɛ", R.raw.double_g_e);
        hashMap.put("gæ", R.raw.double_g_ae);
        hashMap.put("gɑ", R.raw.double_g_a);
        hashMap.put("gɔ", R.raw.double_g_c);
        hashMap.put("goʊ", R.raw.double_g_ou);
        hashMap.put("gʊ", R.raw.double_g_us);
        hashMap.put("gu", R.raw.double_g_u);
        hashMap.put("gʌ", R.raw.double_g_vu);
        hashMap.put("gaɪ", R.raw.double_g_ai);
        hashMap.put("gaʊ", R.raw.double_g_au);
        hashMap.put("gɔɪ", R.raw.double_g_oi);
        hashMap.put("gɝ", R.raw.double_g_ers);
        hashMap.put("gɑr", R.raw.double_g_ar);
        hashMap.put("gɛr", R.raw.double_g_er);
        hashMap.put("gɪr", R.raw.double_g_ir);
        hashMap.put("gɔr", R.raw.double_g_or);
        hashMap.put("ʧi", R.raw.double_ch_i);
        hashMap.put("ʧɪ", R.raw.double_ch_is);
        hashMap.put("ʧeɪ", R.raw.double_ch_ei);
        hashMap.put("ʧɛ", R.raw.double_ch_e);
        hashMap.put("ʧæ", R.raw.double_ch_ae);
        hashMap.put("ʧɑ", R.raw.double_ch_a);
        hashMap.put("ʧɔ", R.raw.double_ch_c);
        hashMap.put("ʧoʊ", R.raw.double_ch_ou);
        hashMap.put("ʧʊ", R.raw.double_ch_us);
        hashMap.put("ʧu", R.raw.double_ch_u);
        hashMap.put("ʧʌ", R.raw.double_ch_vu);
        hashMap.put("ʧaɪ", R.raw.double_ch_ai);
        hashMap.put("ʧaʊ", R.raw.double_ch_au);
        hashMap.put("ʧɔɪ", R.raw.double_ch_oi);
        hashMap.put("ʧɝ", R.raw.double_ch_ers);
        hashMap.put("ʧɑr", R.raw.double_ch_ar);
        hashMap.put("ʧɛr", R.raw.double_ch_er);
        hashMap.put("ʧɪr", R.raw.double_ch_ir);
        hashMap.put("ʧɔr", R.raw.double_ch_or);
        hashMap.put("ʤi", R.raw.double_dzh_i);
        hashMap.put("ʤɪ", R.raw.double_dzh_is);
        hashMap.put("ʤeɪ", R.raw.double_dzh_ei);
        hashMap.put("ʤɛ", R.raw.double_dzh_e);
        hashMap.put("ʤæ", R.raw.double_dzh_ae);
        hashMap.put("ʤɑ", R.raw.double_dzh_a);
        hashMap.put("ʤɔ", R.raw.double_dzh_c);
        hashMap.put("ʤoʊ", R.raw.double_dzh_ou);
        hashMap.put("ʤʊ", R.raw.double_dzh_us);
        hashMap.put("ʤu", R.raw.double_dzh_u);
        hashMap.put("ʤʌ", R.raw.double_dzh_vu);
        hashMap.put("ʤaɪ", R.raw.double_dzh_ai);
        hashMap.put("ʤaʊ", R.raw.double_dzh_au);
        hashMap.put("ʤɔɪ", R.raw.double_dzh_oi);
        hashMap.put("ʤɝ", R.raw.double_dzh_ers);
        hashMap.put("ʤɑr", R.raw.double_dzh_ar);
        hashMap.put("ʤɛr", R.raw.double_dzh_er);
        hashMap.put("ʤɪr", R.raw.double_dzh_ir);
        hashMap.put("ʤɔr", R.raw.double_dzh_or);
        hashMap.put("fi", R.raw.double_f_i);
        hashMap.put("fɪ", R.raw.double_f_is);
        hashMap.put("feɪ", R.raw.double_f_ei);
        hashMap.put("fɛ", R.raw.double_f_e);
        hashMap.put("fæ", R.raw.double_f_ae);
        hashMap.put("fɑ", R.raw.double_f_a);
        hashMap.put("fɔ", R.raw.double_f_c);
        hashMap.put("foʊ", R.raw.double_f_ou);
        hashMap.put("fʊ", R.raw.double_f_us);
        hashMap.put("fu", R.raw.double_f_u);
        hashMap.put("fʌ", R.raw.double_f_vu);
        hashMap.put("faɪ", R.raw.double_f_ai);
        hashMap.put("faʊ", R.raw.double_f_au);
        hashMap.put("fɔɪ", R.raw.double_f_oi);
        hashMap.put("fɝ", R.raw.double_f_ers);
        hashMap.put("fɑr", R.raw.double_f_ar);
        hashMap.put("fɛr", R.raw.double_f_er);
        hashMap.put("fɪr", R.raw.double_f_ir);
        hashMap.put("fɔr", R.raw.double_f_or);
        hashMap.put("vi", R.raw.double_v_i);
        hashMap.put("vɪ", R.raw.double_v_is);
        hashMap.put("veɪ", R.raw.double_v_ei);
        hashMap.put("vɛ", R.raw.double_v_e);
        hashMap.put("væ", R.raw.double_v_ae);
        hashMap.put("vɑ", R.raw.double_v_a);
        hashMap.put("vɔ", R.raw.double_v_c);
        hashMap.put("voʊ", R.raw.double_v_ou);
        hashMap.put("vʊ", R.raw.double_v_us);
        hashMap.put("vu", R.raw.double_v_u);
        hashMap.put("vʌ", R.raw.double_v_vu);
        hashMap.put("vaɪ", R.raw.double_v_ai);
        hashMap.put("vaʊ", R.raw.double_v_au);
        hashMap.put("vɔɪ", R.raw.double_v_oi);
        hashMap.put("vɝ", R.raw.double_v_ers);
        hashMap.put("vɑr", R.raw.double_v_ar);
        hashMap.put("vɛr", R.raw.double_v_er);
        hashMap.put("vɪr", R.raw.double_v_ir);
        hashMap.put("vɔr", R.raw.double_v_or);
        hashMap.put("θi", R.raw.double_th_i);
        hashMap.put("θɪ", R.raw.double_th_is);
        hashMap.put("θeɪ", R.raw.double_th_ei);
        hashMap.put("θɛ", R.raw.double_th_e);
        hashMap.put("θæ", R.raw.double_th_ae);
        hashMap.put("θɑ", R.raw.double_th_a);
        hashMap.put("θɔ", R.raw.double_th_c);
        hashMap.put("θoʊ", R.raw.double_th_ou);
        hashMap.put("θʊ", R.raw.double_th_us);
        hashMap.put("θu", R.raw.double_th_u);
        hashMap.put("θʌ", R.raw.double_th_vu);
        hashMap.put("θaɪ", R.raw.double_th_ai);
        hashMap.put("θaʊ", R.raw.double_th_au);
        hashMap.put("θɔɪ", R.raw.double_th_oi);
        hashMap.put("θɝ", R.raw.double_th_ers);
        hashMap.put("θɑr", R.raw.double_th_ar);
        hashMap.put("θɛr", R.raw.double_th_er);
        hashMap.put("θɪr", R.raw.double_th_ir);
        hashMap.put("θɔr", R.raw.double_th_or);
        hashMap.put("ði", R.raw.double_thv_i);
        hashMap.put("ðɪ", R.raw.double_thv_is);
        hashMap.put("ðeɪ", R.raw.double_thv_ei);
        hashMap.put("ðɛ", R.raw.double_thv_e);
        hashMap.put("ðæ", R.raw.double_thv_ae);
        hashMap.put("ðɑ", R.raw.double_thv_a);
        hashMap.put("ðɔ", R.raw.double_thv_c);
        hashMap.put("ðoʊ", R.raw.double_thv_ou);
        hashMap.put("ðʊ", R.raw.double_thv_us);
        hashMap.put("ðu", R.raw.double_thv_u);
        hashMap.put("ðʌ", R.raw.double_thv_vu);
        hashMap.put("ðaɪ", R.raw.double_thv_ai);
        hashMap.put("ðaʊ", R.raw.double_thv_au);
        hashMap.put("ðɔɪ", R.raw.double_thv_oi);
        hashMap.put("ðɝ", R.raw.double_thv_ers);
        hashMap.put("ðɑr", R.raw.double_thv_ar);
        hashMap.put("ðɛr", R.raw.double_thv_er);
        hashMap.put("ðɪr", R.raw.double_thv_ir);
        hashMap.put("ðɔr", R.raw.double_thv_or);
        hashMap.put("si", R.raw.double_s_i);
        hashMap.put("sɪ", R.raw.double_s_is);
        hashMap.put("seɪ", R.raw.double_s_ei);
        hashMap.put("sɛ", R.raw.double_s_e);
        hashMap.put("sæ", R.raw.double_s_ae);
        hashMap.put("sɑ", R.raw.double_s_a);
        hashMap.put("sɔ", R.raw.double_s_c);
        hashMap.put("soʊ", R.raw.double_s_ou);
        hashMap.put("sʊ", R.raw.double_s_us);
        hashMap.put("su", R.raw.double_s_u);
        hashMap.put("sʌ", R.raw.double_s_vu);
        hashMap.put("saɪ", R.raw.double_s_ai);
        hashMap.put("saʊ", R.raw.double_s_au);
        hashMap.put("sɔɪ", R.raw.double_s_oi);
        hashMap.put("sɝ", R.raw.double_s_ers);
        hashMap.put("sɑr", R.raw.double_s_ar);
        hashMap.put("sɛr", R.raw.double_s_er);
        hashMap.put("sɪr", R.raw.double_s_ir);
        hashMap.put("sɔr", R.raw.double_s_or);
        hashMap.put("zi", R.raw.double_z_i);
        hashMap.put("zɪ", R.raw.double_z_is);
        hashMap.put("zeɪ", R.raw.double_z_ei);
        hashMap.put("zɛ", R.raw.double_z_e);
        hashMap.put("zæ", R.raw.double_z_ae);
        hashMap.put("zɑ", R.raw.double_z_a);
        hashMap.put("zɔ", R.raw.double_z_c);
        hashMap.put("zoʊ", R.raw.double_z_ou);
        hashMap.put("zʊ", R.raw.double_z_us);
        hashMap.put("zu", R.raw.double_z_u);
        hashMap.put("zʌ", R.raw.double_z_vu);
        hashMap.put("zaɪ", R.raw.double_z_ai);
        hashMap.put("zaʊ", R.raw.double_z_au);
        hashMap.put("zɔɪ", R.raw.double_z_oi);
        hashMap.put("zɝ", R.raw.double_z_ers);
        hashMap.put("zɑr", R.raw.double_z_ar);
        hashMap.put("zɛr", R.raw.double_z_er);
        hashMap.put("zɪr", R.raw.double_z_ir);
        hashMap.put("zɔr", R.raw.double_z_or);
        hashMap.put("ʃi", R.raw.double_sh_i);
        hashMap.put("ʃɪ", R.raw.double_sh_is);
        hashMap.put("ʃeɪ", R.raw.double_sh_ei);
        hashMap.put("ʃɛ", R.raw.double_sh_e);
        hashMap.put("ʃæ", R.raw.double_sh_ae);
        hashMap.put("ʃɑ", R.raw.double_sh_a);
        hashMap.put("ʃɔ", R.raw.double_sh_c);
        hashMap.put("ʃoʊ", R.raw.double_sh_ou);
        hashMap.put("ʃʊ", R.raw.double_sh_us);
        hashMap.put("ʃu", R.raw.double_sh_u);
        hashMap.put("ʃʌ", R.raw.double_sh_vu);
        hashMap.put("ʃaɪ", R.raw.double_sh_ai);
        hashMap.put("ʃaʊ", R.raw.double_sh_au);
        hashMap.put("ʃɔɪ", R.raw.double_sh_oi);
        hashMap.put("ʃɝ", R.raw.double_sh_ers);
        hashMap.put("ʃɑr", R.raw.double_sh_ar);
        hashMap.put("ʃɛr", R.raw.double_sh_er);
        hashMap.put("ʃɪr", R.raw.double_sh_ir);
        hashMap.put("ʃɔr", R.raw.double_sh_or);
        hashMap.put("ʒi", R.raw.double_zh_i);
        hashMap.put("ʒɪ", R.raw.double_zh_is);
        hashMap.put("ʒeɪ", R.raw.double_zh_ei);
        hashMap.put("ʒɛ", R.raw.double_zh_e);
        hashMap.put("ʒæ", R.raw.double_zh_ae);
        hashMap.put("ʒɑ", R.raw.double_zh_a);
        hashMap.put("ʒɔ", R.raw.double_zh_c);
        hashMap.put("ʒoʊ", R.raw.double_zh_ou);
        hashMap.put("ʒʊ", R.raw.double_zh_us);
        hashMap.put("ʒu", R.raw.double_zh_u);
        hashMap.put("ʒʌ", R.raw.double_zh_vu);
        hashMap.put("ʒaɪ", R.raw.double_zh_ai);
        hashMap.put("ʒaʊ", R.raw.double_zh_au);
        hashMap.put("ʒɔɪ", R.raw.double_zh_oi);
        hashMap.put("ʒɝ", R.raw.double_zh_ers);
        hashMap.put("ʒɑr", R.raw.double_zh_ar);
        hashMap.put("ʒɛr", R.raw.double_zh_er);
        hashMap.put("ʒɪr", R.raw.double_zh_ir);
        hashMap.put("ʒɔr", R.raw.double_zh_or);
        hashMap.put("mi", R.raw.double_m_i);
        hashMap.put("mɪ", R.raw.double_m_is);
        hashMap.put("meɪ", R.raw.double_m_ei);
        hashMap.put("mɛ", R.raw.double_m_e);
        hashMap.put("mæ", R.raw.double_m_ae);
        hashMap.put("mɑ", R.raw.double_m_a);
        hashMap.put("mɔ", R.raw.double_m_c);
        hashMap.put("moʊ", R.raw.double_m_ou);
        hashMap.put("mʊ", R.raw.double_m_us);
        hashMap.put("mu", R.raw.double_m_u);
        hashMap.put("mʌ", R.raw.double_m_vu);
        hashMap.put("maɪ", R.raw.double_m_ai);
        hashMap.put("maʊ", R.raw.double_m_au);
        hashMap.put("mɔɪ", R.raw.double_m_oi);
        hashMap.put("mɝ", R.raw.double_m_ers);
        hashMap.put("mɑr", R.raw.double_m_ar);
        hashMap.put("mɛr", R.raw.double_m_er);
        hashMap.put("mɪr", R.raw.double_m_ir);
        hashMap.put("mɔr", R.raw.double_m_or);
        hashMap.put("ni", R.raw.double_n_i);
        hashMap.put("nɪ", R.raw.double_n_is);
        hashMap.put("neɪ", R.raw.double_n_ei);
        hashMap.put("nɛ", R.raw.double_n_e);
        hashMap.put("næ", R.raw.double_n_ae);
        hashMap.put("nɑ", R.raw.double_n_a);
        hashMap.put("nɔ", R.raw.double_n_c);
        hashMap.put("noʊ", R.raw.double_n_ou);
        hashMap.put("nʊ", R.raw.double_n_us);
        hashMap.put("nu", R.raw.double_n_u);
        hashMap.put("nʌ", R.raw.double_n_vu);
        hashMap.put("naɪ", R.raw.double_n_ai);
        hashMap.put("naʊ", R.raw.double_n_au);
        hashMap.put("nɔɪ", R.raw.double_n_oi);
        hashMap.put("nɝ", R.raw.double_n_ers);
        hashMap.put("nɑr", R.raw.double_n_ar);
        hashMap.put("nɛr", R.raw.double_n_er);
        hashMap.put("nɪr", R.raw.double_n_ir);
        hashMap.put("nɔr", R.raw.double_n_or);
        hashMap.put("li", R.raw.double_l_i);
        hashMap.put("lɪ", R.raw.double_l_is);
        hashMap.put("leɪ", R.raw.double_l_ei);
        hashMap.put("lɛ", R.raw.double_l_e);
        hashMap.put("læ", R.raw.double_l_ae);
        hashMap.put("lɑ", R.raw.double_l_a);
        hashMap.put("lɔ", R.raw.double_l_c);
        hashMap.put("loʊ", R.raw.double_l_ou);
        hashMap.put("lʊ", R.raw.double_l_us);
        hashMap.put("lu", R.raw.double_l_u);
        hashMap.put("lʌ", R.raw.double_l_vu);
        hashMap.put("laɪ", R.raw.double_l_ai);
        hashMap.put("laʊ", R.raw.double_l_au);
        hashMap.put("lɔɪ", R.raw.double_l_oi);
        hashMap.put("lɝ", R.raw.double_l_ers);
        hashMap.put("lɑr", R.raw.double_l_ar);
        hashMap.put("lɛr", R.raw.double_l_er);
        hashMap.put("lɪr", R.raw.double_l_ir);
        hashMap.put("lɔr", R.raw.double_l_or);
        hashMap.put("wi", R.raw.double_w_i);
        hashMap.put("wɪ", R.raw.double_w_is);
        hashMap.put("weɪ", R.raw.double_w_ei);
        hashMap.put("wɛ", R.raw.double_w_e);
        hashMap.put("wæ", R.raw.double_w_ae);
        hashMap.put("wɑ", R.raw.double_w_a);
        hashMap.put("wɔ", R.raw.double_w_c);
        hashMap.put("woʊ", R.raw.double_w_ou);
        hashMap.put("wʊ", R.raw.double_w_us);
        hashMap.put("wu", R.raw.double_w_u);
        hashMap.put("wʌ", R.raw.double_w_vu);
        hashMap.put("waɪ", R.raw.double_w_ai);
        hashMap.put("waʊ", R.raw.double_w_au);
        hashMap.put("wɔɪ", R.raw.double_w_oi);
        hashMap.put("wɝ", R.raw.double_w_ers);
        hashMap.put("wɑr", R.raw.double_w_ar);
        hashMap.put("wɛr", R.raw.double_w_er);
        hashMap.put("wɪr", R.raw.double_w_ir);
        hashMap.put("wɔr", R.raw.double_w_or);
        hashMap.put("ji", R.raw.double_j_i);
        hashMap.put("jɪ", R.raw.double_j_is);
        hashMap.put("jeɪ", R.raw.double_j_ei);
        hashMap.put("jɛ", R.raw.double_j_e);
        hashMap.put("jæ", R.raw.double_j_ae);
        hashMap.put("jɑ", R.raw.double_j_a);
        hashMap.put("jɔ", R.raw.double_j_c);
        hashMap.put("joʊ", R.raw.double_j_ou);
        hashMap.put("jʊ", R.raw.double_j_us);
        hashMap.put("ju", R.raw.double_j_u);
        hashMap.put("jʌ", R.raw.double_j_vu);
        hashMap.put("jaɪ", R.raw.double_j_ai);
        hashMap.put("jaʊ", R.raw.double_j_au);
        hashMap.put("jɔɪ", R.raw.double_j_oi);
        hashMap.put("jɝ", R.raw.double_j_ers);
        hashMap.put("jɑr", R.raw.double_j_ar);
        hashMap.put("jɛr", R.raw.double_j_er);
        hashMap.put("jɪr", R.raw.double_j_ir);
        hashMap.put("jɔr", R.raw.double_j_or);
        hashMap.put("hi", R.raw.double_h_i);
        hashMap.put("hɪ", R.raw.double_h_is);
        hashMap.put("heɪ", R.raw.double_h_ei);
        hashMap.put("hɛ", R.raw.double_h_e);
        hashMap.put("hæ", R.raw.double_h_ae);
        hashMap.put("hɑ", R.raw.double_h_a);
        hashMap.put("hɔ", R.raw.double_h_c);
        hashMap.put("hoʊ", R.raw.double_h_ou);
        hashMap.put("hʊ", R.raw.double_h_us);
        hashMap.put("hu", R.raw.double_h_u);
        hashMap.put("hʌ", R.raw.double_h_vu);
        hashMap.put("haɪ", R.raw.double_h_ai);
        hashMap.put("haʊ", R.raw.double_h_au);
        hashMap.put("hɔɪ", R.raw.double_h_oi);
        hashMap.put("hɝ", R.raw.double_h_ers);
        hashMap.put("hɑr", R.raw.double_h_ar);
        hashMap.put("hɛr", R.raw.double_h_er);
        hashMap.put("hɪr", R.raw.double_h_ir);
        hashMap.put("hɔr", R.raw.double_h_or);
        hashMap.put("ri", R.raw.double_r_i);
        hashMap.put("rɪ", R.raw.double_r_is);
        hashMap.put("reɪ", R.raw.double_r_ei);
        hashMap.put("rɛ", R.raw.double_r_e);
        hashMap.put("ræ", R.raw.double_r_ae);
        hashMap.put("rɑ", R.raw.double_r_a);
        hashMap.put("rɔ", R.raw.double_r_c);
        hashMap.put("roʊ", R.raw.double_r_ou);
        hashMap.put("rʊ", R.raw.double_r_us);
        hashMap.put("ru", R.raw.double_r_u);
        hashMap.put("rʌ", R.raw.double_r_vu);
        hashMap.put("raɪ", R.raw.double_r_ai);
        hashMap.put("raʊ", R.raw.double_r_au);
        hashMap.put("rɔɪ", R.raw.double_r_oi);
        hashMap.put("rɝ", R.raw.double_r_ers);
        hashMap.put("rɑr", R.raw.double_r_ar);
        hashMap.put("rɛr", R.raw.double_r_er);
        hashMap.put("rɪr", R.raw.double_r_ir);
        hashMap.put("rɔr", R.raw.double_r_or);
        hashMap.put("ip", R.raw.double_i_p);
        hashMap.put("ɪp", R.raw.double_is_p);
        hashMap.put("eɪp", R.raw.double_ei_p);
        hashMap.put("ɛp", R.raw.double_e_p);
        hashMap.put("æp", R.raw.double_ae_p);
        hashMap.put("ɑp", R.raw.double_a_p);
        hashMap.put("ɔp", R.raw.double_c_p);
        hashMap.put("oʊp", R.raw.double_ou_p);
        hashMap.put("ʊp", R.raw.double_us_p);
        hashMap.put("up", R.raw.double_u_p);
        hashMap.put("ʌp", R.raw.double_vu_p);
        hashMap.put("aɪp", R.raw.double_ai_p);
        hashMap.put("aʊp", R.raw.double_au_p);
        hashMap.put("ɔɪp", R.raw.double_oi_p);
        hashMap.put("ɝp", R.raw.double_ers_p);
        hashMap.put("ɑrp", R.raw.double_ar_p);
        hashMap.put("ɛrp", R.raw.double_er_p);
        hashMap.put("ɪrp", R.raw.double_ir_p);
        hashMap.put("ɔrp", R.raw.double_or_p);
        hashMap.put("ib", R.raw.double_i_b);
        hashMap.put("ɪb", R.raw.double_is_b);
        hashMap.put("eɪb", R.raw.double_ei_b);
        hashMap.put("ɛb", R.raw.double_e_b);
        hashMap.put("æb", R.raw.double_ae_b);
        hashMap.put("ɑb", R.raw.double_a_b);
        hashMap.put("ɔb", R.raw.double_c_b);
        hashMap.put("oʊb", R.raw.double_ou_b);
        hashMap.put("ʊb", R.raw.double_us_b);
        hashMap.put("ub", R.raw.double_u_b);
        hashMap.put("ʌb", R.raw.double_vu_b);
        hashMap.put("aɪb", R.raw.double_ai_b);
        hashMap.put("aʊb", R.raw.double_au_b);
        hashMap.put("ɔɪb", R.raw.double_oi_b);
        hashMap.put("ɝb", R.raw.double_ers_b);
        hashMap.put("ɑrb", R.raw.double_ar_b);
        hashMap.put("ɛrb", R.raw.double_er_b);
        hashMap.put("ɪrb", R.raw.double_ir_b);
        hashMap.put("ɔrb", R.raw.double_or_b);
        hashMap.put("it", R.raw.double_i_t);
        hashMap.put("ɪt", R.raw.double_is_t);
        hashMap.put("eɪt", R.raw.double_ei_t);
        hashMap.put("ɛt", R.raw.double_e_t);
        hashMap.put("æt", R.raw.double_ae_t);
        hashMap.put("ɑt", R.raw.double_a_t);
        hashMap.put("ɔt", R.raw.double_c_t);
        hashMap.put("oʊt", R.raw.double_ou_t);
        hashMap.put("ʊt", R.raw.double_us_t);
        hashMap.put("ut", R.raw.double_u_t);
        hashMap.put("ʌt", R.raw.double_vu_t);
        hashMap.put("aɪt", R.raw.double_ai_t);
        hashMap.put("aʊt", R.raw.double_au_t);
        hashMap.put("ɔɪt", R.raw.double_oi_t);
        hashMap.put("ɝt", R.raw.double_ers_t);
        hashMap.put("ɑrt", R.raw.double_ar_t);
        hashMap.put("ɛrt", R.raw.double_er_t);
        hashMap.put("ɪrt", R.raw.double_ir_t);
        hashMap.put("ɔrt", R.raw.double_or_t);
        hashMap.put("id", R.raw.double_i_d);
        hashMap.put("ɪd", R.raw.double_is_d);
        hashMap.put("eɪd", R.raw.double_ei_d);
        hashMap.put("ɛd", R.raw.double_e_d);
        hashMap.put("æd", R.raw.double_ae_d);
        hashMap.put("ɑd", R.raw.double_a_d);
        hashMap.put("ɔd", R.raw.double_c_d);
        hashMap.put("oʊd", R.raw.double_ou_d);
        hashMap.put("ʊd", R.raw.double_us_d);
        hashMap.put("ud", R.raw.double_u_d);
        hashMap.put("ʌd", R.raw.double_vu_d);
        hashMap.put("aɪd", R.raw.double_ai_d);
        hashMap.put("aʊd", R.raw.double_au_d);
        hashMap.put("ɔɪd", R.raw.double_oi_d);
        hashMap.put("ɝd", R.raw.double_ers_d);
        hashMap.put("ɑrd", R.raw.double_ar_d);
        hashMap.put("ɛrd", R.raw.double_er_d);
        hashMap.put("ɪrd", R.raw.double_ir_d);
        hashMap.put("ɔrd", R.raw.double_or_d);
        hashMap.put("ik", R.raw.double_i_k);
        hashMap.put("ɪk", R.raw.double_is_k);
        hashMap.put("eɪk", R.raw.double_ei_k);
        hashMap.put("ɛk", R.raw.double_e_k);
        hashMap.put("æk", R.raw.double_ae_k);
        hashMap.put("ɑk", R.raw.double_a_k);
        hashMap.put("ɔk", R.raw.double_c_k);
        hashMap.put("oʊk", R.raw.double_ou_k);
        hashMap.put("ʊk", R.raw.double_us_k);
        hashMap.put("uk", R.raw.double_u_k);
        hashMap.put("ʌk", R.raw.double_vu_k);
        hashMap.put("aɪk", R.raw.double_ai_k);
        hashMap.put("aʊk", R.raw.double_au_k);
        hashMap.put("ɔɪk", R.raw.double_oi_k);
        hashMap.put("ɝk", R.raw.double_ers_k);
        hashMap.put("ɑrk", R.raw.double_ar_k);
        hashMap.put("ɛrk", R.raw.double_er_k);
        hashMap.put("ɪrk", R.raw.double_ir_k);
        hashMap.put("ɔrk", R.raw.double_or_k);
        hashMap.put("ig", R.raw.double_i_g);
        hashMap.put("ɪg", R.raw.double_is_g);
        hashMap.put("eɪg", R.raw.double_ei_g);
        hashMap.put("ɛg", R.raw.double_e_g);
        hashMap.put("æg", R.raw.double_ae_g);
        hashMap.put("ɑg", R.raw.double_a_g);
        hashMap.put("ɔg", R.raw.double_c_g);
        hashMap.put("oʊg", R.raw.double_ou_g);
        hashMap.put("ʊg", R.raw.double_us_g);
        hashMap.put("ug", R.raw.double_u_g);
        hashMap.put("ʌg", R.raw.double_vu_g);
        hashMap.put("aɪg", R.raw.double_ai_g);
        hashMap.put("aʊg", R.raw.double_au_g);
        hashMap.put("ɔɪg", R.raw.double_oi_g);
        hashMap.put("ɝg", R.raw.double_ers_g);
        hashMap.put("ɑrg", R.raw.double_ar_g);
        hashMap.put("ɛrg", R.raw.double_er_g);
        hashMap.put("ɪrg", R.raw.double_ir_g);
        hashMap.put("ɔrg", R.raw.double_or_g);
        hashMap.put("iʧ", R.raw.double_i_ch);
        hashMap.put("ɪʧ", R.raw.double_is_ch);
        hashMap.put("eɪʧ", R.raw.double_ei_ch);
        hashMap.put("ɛʧ", R.raw.double_e_ch);
        hashMap.put("æʧ", R.raw.double_ae_ch);
        hashMap.put("ɑʧ", R.raw.double_a_ch);
        hashMap.put("ɔʧ", R.raw.double_c_ch);
        hashMap.put("oʊʧ", R.raw.double_ou_ch);
        hashMap.put("ʊʧ", R.raw.double_us_ch);
        hashMap.put("uʧ", R.raw.double_u_ch);
        hashMap.put("ʌʧ", R.raw.double_vu_ch);
        hashMap.put("aɪʧ", R.raw.double_ai_ch);
        hashMap.put("aʊʧ", R.raw.double_au_ch);
        hashMap.put("ɔɪʧ", R.raw.double_oi_ch);
        hashMap.put("ɝʧ", R.raw.double_ers_ch);
        hashMap.put("ɑrʧ", R.raw.double_ar_ch);
        hashMap.put("ɛrʧ", R.raw.double_er_ch);
        hashMap.put("ɪrʧ", R.raw.double_ir_ch);
        hashMap.put("ɔrʧ", R.raw.double_or_ch);
        hashMap.put("iʤ", R.raw.double_i_dzh);
        hashMap.put("ɪʤ", R.raw.double_is_dzh);
        hashMap.put("eɪʤ", R.raw.double_ei_dzh);
        hashMap.put("ɛʤ", R.raw.double_e_dzh);
        hashMap.put("æʤ", R.raw.double_ae_dzh);
        hashMap.put("ɑʤ", R.raw.double_a_dzh);
        hashMap.put("ɔʤ", R.raw.double_c_dzh);
        hashMap.put("oʊʤ", R.raw.double_ou_dzh);
        hashMap.put("ʊʤ", R.raw.double_us_dzh);
        hashMap.put("uʤ", R.raw.double_u_dzh);
        hashMap.put("ʌʤ", R.raw.double_vu_dzh);
        hashMap.put("aɪʤ", R.raw.double_ai_dzh);
        hashMap.put("aʊʤ", R.raw.double_au_dzh);
        hashMap.put("ɔɪʤ", R.raw.double_oi_dzh);
        hashMap.put("ɝʤ", R.raw.double_ers_dzh);
        hashMap.put("ɑrʤ", R.raw.double_ar_dzh);
        hashMap.put("ɛrʤ", R.raw.double_er_dzh);
        hashMap.put("ɪrʤ", R.raw.double_ir_dzh);
        hashMap.put("ɔrʤ", R.raw.double_or_dzh);
        hashMap.put("if", R.raw.double_i_f);
        hashMap.put("ɪf", R.raw.double_is_f);
        hashMap.put("eɪf", R.raw.double_ei_f);
        hashMap.put("ɛf", R.raw.double_e_f);
        hashMap.put("æf", R.raw.double_ae_f);
        hashMap.put("ɑf", R.raw.double_a_f);
        hashMap.put("ɔf", R.raw.double_c_f);
        hashMap.put("oʊf", R.raw.double_ou_f);
        hashMap.put("ʊf", R.raw.double_us_f);
        hashMap.put("uf", R.raw.double_u_f);
        hashMap.put("ʌf", R.raw.double_vu_f);
        hashMap.put("aɪf", R.raw.double_ai_f);
        hashMap.put("aʊf", R.raw.double_au_f);
        hashMap.put("ɔɪf", R.raw.double_oi_f);
        hashMap.put("ɝf", R.raw.double_ers_f);
        hashMap.put("ɑrf", R.raw.double_ar_f);
        hashMap.put("ɛrf", R.raw.double_er_f);
        hashMap.put("ɪrf", R.raw.double_ir_f);
        hashMap.put("ɔrf", R.raw.double_or_f);
        hashMap.put("iv", R.raw.double_i_v);
        hashMap.put("ɪv", R.raw.double_is_v);
        hashMap.put("eɪv", R.raw.double_ei_v);
        hashMap.put("ɛv", R.raw.double_e_v);
        hashMap.put("æv", R.raw.double_ae_v);
        hashMap.put("ɑv", R.raw.double_a_v);
        hashMap.put("ɔv", R.raw.double_c_v);
        hashMap.put("oʊv", R.raw.double_ou_v);
        hashMap.put("ʊv", R.raw.double_us_v);
        hashMap.put("uv", R.raw.double_u_v);
        hashMap.put("ʌv", R.raw.double_vu_v);
        hashMap.put("aɪv", R.raw.double_ai_v);
        hashMap.put("aʊv", R.raw.double_au_v);
        hashMap.put("ɔɪv", R.raw.double_oi_v);
        hashMap.put("ɝv", R.raw.double_ers_v);
        hashMap.put("ɑrv", R.raw.double_ar_v);
        hashMap.put("ɛrv", R.raw.double_er_v);
        hashMap.put("ɪrv", R.raw.double_ir_v);
        hashMap.put("ɔrv", R.raw.double_or_v);
        hashMap.put("iθ", R.raw.double_i_th);
        hashMap.put("ɪθ", R.raw.double_is_th);
        hashMap.put("eɪθ", R.raw.double_ei_th);
        hashMap.put("ɛθ", R.raw.double_e_th);
        hashMap.put("æθ", R.raw.double_ae_th);
        hashMap.put("ɑθ", R.raw.double_a_th);
        hashMap.put("ɔθ", R.raw.double_c_th);
        hashMap.put("oʊθ", R.raw.double_ou_th);
        hashMap.put("ʊθ", R.raw.double_us_th);
        hashMap.put("uθ", R.raw.double_u_th);
        hashMap.put("ʌθ", R.raw.double_vu_th);
        hashMap.put("aɪθ", R.raw.double_ai_th);
        hashMap.put("aʊθ", R.raw.double_au_th);
        hashMap.put("ɔɪθ", R.raw.double_oi_th);
        hashMap.put("ɝθ", R.raw.double_ers_th);
        hashMap.put("ɑrθ", R.raw.double_ar_th);
        hashMap.put("ɛrθ", R.raw.double_er_th);
        hashMap.put("ɪrθ", R.raw.double_ir_th);
        hashMap.put("ɔrθ", R.raw.double_or_th);
        hashMap.put("ið", R.raw.double_i_thv);
        hashMap.put("ɪð", R.raw.double_is_thv);
        hashMap.put("eɪð", R.raw.double_ei_thv);
        hashMap.put("ɛð", R.raw.double_e_thv);
        hashMap.put("æð", R.raw.double_ae_thv);
        hashMap.put("ɑð", R.raw.double_a_thv);
        hashMap.put("ɔð", R.raw.double_c_thv);
        hashMap.put("oʊð", R.raw.double_ou_thv);
        hashMap.put("ʊð", R.raw.double_us_thv);
        hashMap.put("uð", R.raw.double_u_thv);
        hashMap.put("ʌð", R.raw.double_vu_thv);
        hashMap.put("aɪð", R.raw.double_ai_thv);
        hashMap.put("aʊð", R.raw.double_au_thv);
        hashMap.put("ɔɪð", R.raw.double_oi_thv);
        hashMap.put("ɝð", R.raw.double_ers_thv);
        hashMap.put("ɑrð", R.raw.double_ar_thv);
        hashMap.put("ɛrð", R.raw.double_er_thv);
        hashMap.put("ɪrð", R.raw.double_ir_thv);
        hashMap.put("ɔrð", R.raw.double_or_thv);
        hashMap.put("is", R.raw.double_i_s);
        hashMap.put("ɪs", R.raw.double_is_s);
        hashMap.put("eɪs", R.raw.double_ei_s);
        hashMap.put("ɛs", R.raw.double_e_s);
        hashMap.put("æs", R.raw.double_ae_s);
        hashMap.put("ɑs", R.raw.double_a_s);
        hashMap.put("ɔs", R.raw.double_c_s);
        hashMap.put("oʊs", R.raw.double_ou_s);
        hashMap.put("ʊs", R.raw.double_us_s);
        hashMap.put("us", R.raw.double_u_s);
        hashMap.put("ʌs", R.raw.double_vu_s);
        hashMap.put("aɪs", R.raw.double_ai_s);
        hashMap.put("aʊs", R.raw.double_au_s);
        hashMap.put("ɔɪs", R.raw.double_oi_s);
        hashMap.put("ɝs", R.raw.double_ers_s);
        hashMap.put("ɑrs", R.raw.double_ar_s);
        hashMap.put("ɛrs", R.raw.double_er_s);
        hashMap.put("ɪrs", R.raw.double_ir_s);
        hashMap.put("ɔrs", R.raw.double_or_s);
        hashMap.put("iz", R.raw.double_i_z);
        hashMap.put("ɪz", R.raw.double_is_z);
        hashMap.put("eɪz", R.raw.double_ei_z);
        hashMap.put("ɛz", R.raw.double_e_z);
        hashMap.put("æz", R.raw.double_ae_z);
        hashMap.put("ɑz", R.raw.double_a_z);
        hashMap.put("ɔz", R.raw.double_c_z);
        hashMap.put("oʊz", R.raw.double_ou_z);
        hashMap.put("ʊz", R.raw.double_us_z);
        hashMap.put("uz", R.raw.double_u_z);
        hashMap.put("ʌz", R.raw.double_vu_z);
        hashMap.put("aɪz", R.raw.double_ai_z);
        hashMap.put("aʊz", R.raw.double_au_z);
        hashMap.put("ɔɪz", R.raw.double_oi_z);
        hashMap.put("ɝz", R.raw.double_ers_z);
        hashMap.put("ɑrz", R.raw.double_ar_z);
        hashMap.put("ɛrz", R.raw.double_er_z);
        hashMap.put("ɪrz", R.raw.double_ir_z);
        hashMap.put("ɔrz", R.raw.double_or_z);
        hashMap.put("iʃ", R.raw.double_i_sh);
        hashMap.put("ɪʃ", R.raw.double_is_sh);
        hashMap.put("eɪʃ", R.raw.double_ei_sh);
        hashMap.put("ɛʃ", R.raw.double_e_sh);
        hashMap.put("æʃ", R.raw.double_ae_sh);
        hashMap.put("ɑʃ", R.raw.double_a_sh);
        hashMap.put("ɔʃ", R.raw.double_c_sh);
        hashMap.put("oʊʃ", R.raw.double_ou_sh);
        hashMap.put("ʊʃ", R.raw.double_us_sh);
        hashMap.put("uʃ", R.raw.double_u_sh);
        hashMap.put("ʌʃ", R.raw.double_vu_sh);
        hashMap.put("aɪʃ", R.raw.double_ai_sh);
        hashMap.put("aʊʃ", R.raw.double_au_sh);
        hashMap.put("ɔɪʃ", R.raw.double_oi_sh);
        hashMap.put("ɝʃ", R.raw.double_ers_sh);
        hashMap.put("ɑrʃ", R.raw.double_ar_sh);
        hashMap.put("ɛrʃ", R.raw.double_er_sh);
        hashMap.put("ɪrʃ", R.raw.double_ir_sh);
        hashMap.put("ɔrʃ", R.raw.double_or_sh);
        hashMap.put("iʒ", R.raw.double_i_zh);
        hashMap.put("ɪʒ", R.raw.double_is_zh);
        hashMap.put("eɪʒ", R.raw.double_ei_zh);
        hashMap.put("ɛʒ", R.raw.double_e_zh);
        hashMap.put("æʒ", R.raw.double_ae_zh);
        hashMap.put("ɑʒ", R.raw.double_a_zh);
        hashMap.put("ɔʒ", R.raw.double_c_zh);
        hashMap.put("oʊʒ", R.raw.double_ou_zh);
        hashMap.put("ʊʒ", R.raw.double_us_zh);
        hashMap.put("uʒ", R.raw.double_u_zh);
        hashMap.put("ʌʒ", R.raw.double_vu_zh);
        hashMap.put("aɪʒ", R.raw.double_ai_zh);
        hashMap.put("aʊʒ", R.raw.double_au_zh);
        hashMap.put("ɔɪʒ", R.raw.double_oi_zh);
        hashMap.put("ɝʒ", R.raw.double_ers_zh);
        hashMap.put("ɑrʒ", R.raw.double_ar_zh);
        hashMap.put("ɛrʒ", R.raw.double_er_zh);
        hashMap.put("ɪrʒ", R.raw.double_ir_zh);
        hashMap.put("ɔrʒ", R.raw.double_or_zh);
        hashMap.put("im", R.raw.double_i_m);
        hashMap.put("ɪm", R.raw.double_is_m);
        hashMap.put("eɪm", R.raw.double_ei_m);
        hashMap.put("ɛm", R.raw.double_e_m);
        hashMap.put("æm", R.raw.double_ae_m);
        hashMap.put("ɑm", R.raw.double_a_m);
        hashMap.put("ɔm", R.raw.double_c_m);
        hashMap.put("oʊm", R.raw.double_ou_m);
        hashMap.put("ʊm", R.raw.double_us_m);
        hashMap.put("um", R.raw.double_u_m);
        hashMap.put("ʌm", R.raw.double_vu_m);
        hashMap.put("aɪm", R.raw.double_ai_m);
        hashMap.put("aʊm", R.raw.double_au_m);
        hashMap.put("ɔɪm", R.raw.double_oi_m);
        hashMap.put("ɝm", R.raw.double_ers_m);
        hashMap.put("ɑrm", R.raw.double_ar_m);
        hashMap.put("ɛrm", R.raw.double_er_m);
        hashMap.put("ɪrm", R.raw.double_ir_m);
        hashMap.put("ɔrm", R.raw.double_or_m);
        hashMap.put("in", R.raw.double_i_n);
        hashMap.put("ɪn", R.raw.double_is_n);
        hashMap.put("eɪn", R.raw.double_ei_n);
        hashMap.put("ɛn", R.raw.double_e_n);
        hashMap.put("æn", R.raw.double_ae_n);
        hashMap.put("ɑn", R.raw.double_a_n);
        hashMap.put("ɔn", R.raw.double_c_n);
        hashMap.put("oʊn", R.raw.double_ou_n);
        hashMap.put("ʊn", R.raw.double_us_n);
        hashMap.put("un", R.raw.double_u_n);
        hashMap.put("ʌn", R.raw.double_vu_n);
        hashMap.put("aɪn", R.raw.double_ai_n);
        hashMap.put("aʊn", R.raw.double_au_n);
        hashMap.put("ɔɪn", R.raw.double_oi_n);
        hashMap.put("ɝn", R.raw.double_ers_n);
        hashMap.put("ɑrn", R.raw.double_ar_n);
        hashMap.put("ɛrn", R.raw.double_er_n);
        hashMap.put("ɪrn", R.raw.double_ir_n);
        hashMap.put("ɔrn", R.raw.double_or_n);
        hashMap.put("iŋ", R.raw.double_i_ng);
        hashMap.put("ɪŋ", R.raw.double_is_ng);
        hashMap.put("eɪŋ", R.raw.double_ei_ng);
        hashMap.put("ɛŋ", R.raw.double_e_ng);
        hashMap.put("æŋ", R.raw.double_ae_ng);
        hashMap.put("ɑŋ", R.raw.double_a_ng);
        hashMap.put("ɔŋ", R.raw.double_c_ng);
        hashMap.put("oʊŋ", R.raw.double_ou_ng);
        hashMap.put("ʊŋ", R.raw.double_us_ng);
        hashMap.put("uŋ", R.raw.double_u_ng);
        hashMap.put("ʌŋ", R.raw.double_vu_ng);
        hashMap.put("aɪŋ", R.raw.double_ai_ng);
        hashMap.put("aʊŋ", R.raw.double_au_ng);
        hashMap.put("ɔɪŋ", R.raw.double_oi_ng);
        hashMap.put("ɝŋ", R.raw.double_ers_ng);
        hashMap.put("ɑrŋ", R.raw.double_ar_ng);
        hashMap.put("ɛrŋ", R.raw.double_er_ng);
        hashMap.put("ɪrŋ", R.raw.double_ir_ng);
        hashMap.put("ɔrŋ", R.raw.double_or_ng);
        hashMap.put("il", R.raw.double_i_l);
        hashMap.put("ɪl", R.raw.double_is_l);
        hashMap.put("eɪl", R.raw.double_ei_l);
        hashMap.put("ɛl", R.raw.double_e_l);
        hashMap.put("æl", R.raw.double_ae_l);
        hashMap.put("ɑl", R.raw.double_a_l);
        hashMap.put("ɔl", R.raw.double_c_l);
        hashMap.put("oʊl", R.raw.double_ou_l);
        hashMap.put("ʊl", R.raw.double_us_l);
        hashMap.put("ul", R.raw.double_u_l);
        hashMap.put("ʌl", R.raw.double_vu_l);
        hashMap.put("aɪl", R.raw.double_ai_l);
        hashMap.put("aʊl", R.raw.double_au_l);
        hashMap.put("ɔɪl", R.raw.double_oi_l);
        hashMap.put("ɝl", R.raw.double_ers_l);
        hashMap.put("ɑrl", R.raw.double_ar_l);
        hashMap.put("ɛrl", R.raw.double_er_l);
        hashMap.put("ɪrl", R.raw.double_ir_l);
        hashMap.put("ɔrl", R.raw.double_or_l);
    }

}