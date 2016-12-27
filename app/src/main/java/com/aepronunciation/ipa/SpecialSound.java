package com.aepronunciation.ipa;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.text.TextUtils;

class SpecialSound {

    // These are double sounds that are used in the learning tab but not in
    // the practice or tests.

    // private class variables
    private LinkedHashMap<String, Integer> glottalHashMap;
    private LinkedHashMap<String, Integer> flapHashMap;
    private LinkedHashMap<String, Integer> shwuaHashMap;
    private LinkedHashMap<String, Integer> erUnstressedHashMap;
    private ArrayList<String> doubleSounds;

    private static final String FLAP_T = "ɾ";
    private static final String GLOTTAL_STOP = "ʔ";
    private static final String SHWUA = "ə";
    private static final String ER_UNSTRESSED = "ɚ";

    // constructor
    SpecialSound() {
        initTreeMap();
    }


    void loadSoundsFor(String ipa) {

        // error checking
        if (TextUtils.isEmpty(ipa)) {
            return;
        }

        if (ipa.equals(GLOTTAL_STOP)) {
            doubleSounds = new ArrayList<>(glottalHashMap.keySet());
        } else if (ipa.equals(FLAP_T)) {
            doubleSounds = new ArrayList<>(flapHashMap.keySet());
        } else if (ipa.equals(SHWUA)) {
            doubleSounds = new ArrayList<>(shwuaHashMap.keySet());
        } else if (ipa.equals(ER_UNSTRESSED)) {
            doubleSounds = new ArrayList<>(erUnstressedHashMap.keySet());
        }

    }


    int getSoundResourceId(String doubleSoundIpa) {

        // returns null if no value found
        if (glottalHashMap.containsKey(doubleSoundIpa)) {
            return glottalHashMap.get(doubleSoundIpa);
        } else if (flapHashMap.containsKey(doubleSoundIpa)) {
            return flapHashMap.get(doubleSoundIpa);
        } else if (shwuaHashMap.containsKey(doubleSoundIpa)) {
            return shwuaHashMap.get(doubleSoundIpa);
        } else if (erUnstressedHashMap.containsKey(doubleSoundIpa)) {
            return erUnstressedHashMap.get(doubleSoundIpa);
        }else{
            return -1;
        }

    }

    public ArrayList<String> getSounds() {
        if (doubleSounds == null) {
            doubleSounds = new ArrayList<>();
        }
        return doubleSounds;
    }

    // initialize the hashMap when new object created
    private void initTreeMap() {

        glottalHashMap = new LinkedHashMap<>();

        glottalHashMap.put("iʔ", R.raw.double_i_glottal);
        glottalHashMap.put("ɪʔ", R.raw.double_is_glottal);
        glottalHashMap.put("eɪʔ", R.raw.double_ei_glottal);
        glottalHashMap.put("ɛʔ", R.raw.double_e_glottal);
        glottalHashMap.put("æʔ", R.raw.double_ae_glottal);
        glottalHashMap.put("ɑʔ", R.raw.double_a_glottal);
        glottalHashMap.put("ɔʔ", R.raw.double_c_glottal);
        glottalHashMap.put("oʊʔ", R.raw.double_ou_glottal);
        glottalHashMap.put("ʊʔ", R.raw.double_us_glottal);
        glottalHashMap.put("uʔ", R.raw.double_u_glottal);
        glottalHashMap.put("ʌʔ", R.raw.double_vu_glottal);
        glottalHashMap.put("aɪʔ", R.raw.double_ai_glottal);
        glottalHashMap.put("aʊʔ", R.raw.double_au_glottal);
        glottalHashMap.put("ɔɪʔ", R.raw.double_oi_glottal);
        glottalHashMap.put("ɝʔ", R.raw.double_ers_glottal);
        glottalHashMap.put("ɑrʔ", R.raw.double_ar_glottal);
        glottalHashMap.put("ɛrʔ", R.raw.double_er_glottal);
        glottalHashMap.put("ɪrʔ", R.raw.double_ir_glottal);
        glottalHashMap.put("ɔrʔ", R.raw.double_or_glottal);


        flapHashMap = new LinkedHashMap<>();

        flapHashMap.put("ˈiɾə", R.raw.double_i_flap_shwua);
        flapHashMap.put("ˈɪɾə", R.raw.double_is_flap_shwua);
        flapHashMap.put("ˈeɪɾə", R.raw.double_ei_flap_shwua);
        flapHashMap.put("ˈɛɾə", R.raw.double_e_flap_shwua);
        flapHashMap.put("ˈæɾə", R.raw.double_ae_flap_shwua);
        flapHashMap.put("ˈɑɾə", R.raw.double_a_flap_shwua);
        flapHashMap.put("ˈɔɾə", R.raw.double_c_flap_shwua);
        flapHashMap.put("ˈoʊɾə", R.raw.double_ou_flap_shwua);
        flapHashMap.put("ˈʊɾə", R.raw.double_us_flap_shwua);
        flapHashMap.put("ˈuɾə", R.raw.double_u_flap_shwua);
        flapHashMap.put("ˈʌɾə", R.raw.double_vu_flap_shwua);
        flapHashMap.put("ˈaɪɾə", R.raw.double_ai_flap_shwua);
        flapHashMap.put("ˈaʊɾə", R.raw.double_au_flap_shwua);
        flapHashMap.put("ˈɔɪɾə", R.raw.double_oi_flap_shwua);
        flapHashMap.put("ˈɝɾə", R.raw.double_ers_flap_shwua);
        flapHashMap.put("ˈɑrɾə", R.raw.double_ar_flap_shwua);
        flapHashMap.put("ˈɛrɾə", R.raw.double_er_flap_shwua);
        flapHashMap.put("ˈɪrɾə", R.raw.double_ir_flap_shwua);
        flapHashMap.put("ˈɔrɾə", R.raw.double_or_flap_shwua);

        shwuaHashMap = new LinkedHashMap<>();

        shwuaHashMap.put("pə", R.raw.double_p_shwua);
        shwuaHashMap.put("tə", R.raw.double_t_shwua);
        shwuaHashMap.put("kə", R.raw.double_k_shwua);
        shwuaHashMap.put("ʧə", R.raw.double_ch_shwua);
        shwuaHashMap.put("fə", R.raw.double_f_shwua);
        shwuaHashMap.put("θə", R.raw.double_th_shwua);
        shwuaHashMap.put("sə", R.raw.double_s_shwua);
        shwuaHashMap.put("ʃə", R.raw.double_sh_shwua);
        shwuaHashMap.put("bə", R.raw.double_b_shwua);
        shwuaHashMap.put("də", R.raw.double_d_shwua);
        shwuaHashMap.put("gə", R.raw.double_g_shwua);
        shwuaHashMap.put("ʤə", R.raw.double_dzh_shwua);
        shwuaHashMap.put("və", R.raw.double_v_shwua);
        shwuaHashMap.put("ðə", R.raw.double_thv_shwua);
        shwuaHashMap.put("zə", R.raw.double_z_shwua);
        shwuaHashMap.put("ʒə", R.raw.double_zh_shwua);
        shwuaHashMap.put("mə", R.raw.double_m_shwua);
        shwuaHashMap.put("nə", R.raw.double_n_shwua);
        shwuaHashMap.put("lə", R.raw.double_l_shwua);
        shwuaHashMap.put("wə", R.raw.double_w_shwua);
        shwuaHashMap.put("jə", R.raw.double_j_shwua);
        shwuaHashMap.put("hə", R.raw.double_h_shwua);
        shwuaHashMap.put("rə", R.raw.double_r_shwua);
        shwuaHashMap.put("əp", R.raw.double_shwua_p);
        shwuaHashMap.put("ət", R.raw.double_shwua_t);
        shwuaHashMap.put("ək", R.raw.double_shwua_k);
        shwuaHashMap.put("əʧ", R.raw.double_shwua_ch);
        shwuaHashMap.put("əf", R.raw.double_shwua_f);
        shwuaHashMap.put("əθ", R.raw.double_shwua_th);
        shwuaHashMap.put("əs", R.raw.double_shwua_s);
        shwuaHashMap.put("əʃ", R.raw.double_shwua_sh);
        shwuaHashMap.put("əb", R.raw.double_shwua_b);
        shwuaHashMap.put("əd", R.raw.double_shwua_d);
        shwuaHashMap.put("əg", R.raw.double_shwua_g);
        shwuaHashMap.put("əʤ", R.raw.double_shwua_dzh);
        shwuaHashMap.put("əv", R.raw.double_shwua_v);
        shwuaHashMap.put("əð", R.raw.double_shwua_thv);
        shwuaHashMap.put("əz", R.raw.double_shwua_z);
        shwuaHashMap.put("əʒ", R.raw.double_shwua_zh);
        shwuaHashMap.put("əm", R.raw.double_shwua_m);
        shwuaHashMap.put("ən", R.raw.double_shwua_n);
        shwuaHashMap.put("əŋ", R.raw.double_shwua_ng);
        shwuaHashMap.put("əl", R.raw.double_shwua_l);

        erUnstressedHashMap = new LinkedHashMap<>();

        erUnstressedHashMap.put("pɚ", R.raw.double_p_eru);
        erUnstressedHashMap.put("tɚ", R.raw.double_t_eru);
        erUnstressedHashMap.put("kɚ", R.raw.double_k_eru);
        erUnstressedHashMap.put("ʧɚ", R.raw.double_ch_eru);
        erUnstressedHashMap.put("fɚ", R.raw.double_f_eru);
        erUnstressedHashMap.put("θɚ", R.raw.double_th_eru);
        erUnstressedHashMap.put("sɚ", R.raw.double_s_eru);
        erUnstressedHashMap.put("ʃɚ", R.raw.double_sh_eru);
        erUnstressedHashMap.put("bɚ", R.raw.double_b_eru);
        erUnstressedHashMap.put("dɚ", R.raw.double_d_eru);
        erUnstressedHashMap.put("gɚ", R.raw.double_g_eru);
        erUnstressedHashMap.put("ʤɚ", R.raw.double_dzh_eru);
        erUnstressedHashMap.put("vɚ", R.raw.double_v_eru);
        erUnstressedHashMap.put("ðɚ", R.raw.double_thv_eru);
        erUnstressedHashMap.put("zɚ", R.raw.double_z_eru);
        erUnstressedHashMap.put("ʒɚ", R.raw.double_zh_eru);
        erUnstressedHashMap.put("mɚ", R.raw.double_m_eru);
        erUnstressedHashMap.put("nɚ", R.raw.double_n_eru);
        erUnstressedHashMap.put("lɚ", R.raw.double_l_eru);
        erUnstressedHashMap.put("wɚ", R.raw.double_w_eru);
        erUnstressedHashMap.put("jɚ", R.raw.double_j_eru);
        erUnstressedHashMap.put("hɚ", R.raw.double_h_eru);
        erUnstressedHashMap.put("rɚ", R.raw.double_r_eru);
        erUnstressedHashMap.put("ɚp", R.raw.double_eru_p);
        erUnstressedHashMap.put("ɚt", R.raw.double_eru_t);
        erUnstressedHashMap.put("ɚk", R.raw.double_eru_k);
        erUnstressedHashMap.put("ɚʧ", R.raw.double_eru_ch);
        erUnstressedHashMap.put("ɚf", R.raw.double_eru_f);
        erUnstressedHashMap.put("ɚθ", R.raw.double_eru_th);
        erUnstressedHashMap.put("ɚs", R.raw.double_eru_s);
        erUnstressedHashMap.put("ɚʃ", R.raw.double_eru_sh);
        erUnstressedHashMap.put("ɚb", R.raw.double_eru_b);
        erUnstressedHashMap.put("ɚd", R.raw.double_eru_d);
        erUnstressedHashMap.put("ɚg", R.raw.double_eru_g);
        erUnstressedHashMap.put("ɚʤ", R.raw.double_eru_dzh);
        erUnstressedHashMap.put("ɚv", R.raw.double_eru_v);
        erUnstressedHashMap.put("ɚð", R.raw.double_eru_thv);
        erUnstressedHashMap.put("ɚz", R.raw.double_eru_z);
        erUnstressedHashMap.put("ɚʒ", R.raw.double_eru_zh);
        erUnstressedHashMap.put("ɚm", R.raw.double_eru_m);
        erUnstressedHashMap.put("ɚn", R.raw.double_eru_n);
        erUnstressedHashMap.put("ɚŋ", R.raw.double_eru_ng);
        erUnstressedHashMap.put("ɚl", R.raw.double_eru_l);

    }

}