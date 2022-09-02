package com.aepronunciation.ipa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class SingleSound {

    // private class variables
    private HashMap<String, Integer> soundMap;
    private HashMap<String, Integer> exampleOneMap;
    private HashMap<String, Integer> exampleTwoMap;
    private HashMap<String, Integer> exampleThreeMap;
    private static final Random random = new Random();
    private ArrayList<String> singleSounds;

    // constructor
    SingleSound() {
        initSound();
    }


    int getSoundCount() {
        if (singleSounds != null) {
            return singleSounds.size();
        } else {
            return 0;
        }
    }

    String getRandomIpa() {

        // get single sounds string array
        if (singleSounds == null) {
            includeAllSounds();
        }

        // get random integer (0 <= x < numberOfSounds)
        int soundIndex = random.nextInt(singleSounds.size());

        // return ipa string
        return singleSounds.get(soundIndex);
    }

    private void includeAllSounds() {
        singleSounds = new ArrayList<>(soundMap.keySet());
    }

    void restrictListTo(ArrayList<String> consonants, ArrayList<String> vowels) {
        if (vowels.isEmpty() && consonants.isEmpty()) {
            return;
        }

        singleSounds = new ArrayList<>();
        singleSounds.addAll(consonants);
        singleSounds.addAll(vowels);
    }

    int getSoundResourceId(String singleSoundIpa) {

        if (soundMap == null) {
            initSound();
        }

        // returns null if no value found
        return soundMap.get(singleSoundIpa);
    }

    int getExampleOneResourceId(String singleSoundIpa) {

        if (exampleOneMap == null) {
            initExampleOne();
        }

        // returns null if no value found
        return exampleOneMap.get(singleSoundIpa);
    }

    int getExampleTwoResourceId(String singleSoundIpa) {

        if (exampleTwoMap == null) {
            initExampleTwo();
        }

        // returns null if no value found
        return exampleTwoMap.get(singleSoundIpa);
    }

    int getExampleThreeResourceId(String singleSoundIpa) {

        if (exampleThreeMap == null) {
            initExampleThree();
        }

        // returns null if no value found
        return exampleThreeMap.get(singleSoundIpa);
    }

    // initialize the hashmap when new object created
    private void initSound() {

        soundMap = new HashMap<>();

        soundMap.put("p", R.raw.single_p);
        soundMap.put("t", R.raw.single_t);
        soundMap.put("k", R.raw.single_k);
        soundMap.put("ʧ", R.raw.single_ch);
        soundMap.put("f", R.raw.single_f);
        soundMap.put("θ", R.raw.single_th_voiceless);
        soundMap.put("s", R.raw.single_s);
        soundMap.put("ʃ", R.raw.single_sh);
        soundMap.put("b", R.raw.single_b);
        soundMap.put("d", R.raw.single_d);
        soundMap.put("g", R.raw.single_g);
        soundMap.put("ʤ", R.raw.single_dzh);
        soundMap.put("v", R.raw.single_v);
        soundMap.put("ð", R.raw.single_th_voiced);
        soundMap.put("z", R.raw.single_z);
        soundMap.put("ʒ", R.raw.single_zh);
        soundMap.put("m", R.raw.single_m);
        soundMap.put("n", R.raw.single_n);
        soundMap.put("ŋ", R.raw.single_ng);
        soundMap.put("l", R.raw.single_l);
        soundMap.put("w", R.raw.single_w);
        soundMap.put("j", R.raw.single_j);
        soundMap.put("h", R.raw.single_h);
        soundMap.put("r", R.raw.single_r);
        soundMap.put("i", R.raw.single_i);
        soundMap.put("ɪ", R.raw.single_i_short);
        soundMap.put("ɛ", R.raw.single_e);
        soundMap.put("æ", R.raw.single_ae);
        soundMap.put("ɑ", R.raw.single_a);
        soundMap.put("ɔ", R.raw.single_c_backwards);
        soundMap.put("ʊ", R.raw.single_u_short);
        soundMap.put("u", R.raw.single_u);
        soundMap.put("ʌ", R.raw.single_v_upsidedown);
        soundMap.put("ə", R.raw.single_shwua);
        soundMap.put("eɪ", R.raw.single_ei);
        soundMap.put("aɪ", R.raw.single_ai);
        soundMap.put("aʊ", R.raw.single_au);
        soundMap.put("ɔɪ", R.raw.single_oi);
        soundMap.put("oʊ", R.raw.single_ou);
        soundMap.put("ɝ", R.raw.single_er_stressed);
        soundMap.put("ɚ", R.raw.single_er_unstressed);
        soundMap.put("ɑr", R.raw.single_ar);
        soundMap.put("ɛr", R.raw.single_er);
        soundMap.put("ɪr", R.raw.single_ir);
        soundMap.put("ɔr", R.raw.single_or);
        soundMap.put("ʔ", R.raw.single_glottal_stop);
        soundMap.put("ɾ", R.raw.single_flap_t);

    }

    private void initExampleOne() {

        exampleOneMap = new HashMap<>();

        exampleOneMap.put("p", R.raw.word_pass);
        exampleOneMap.put("t", R.raw.word_toad);
        exampleOneMap.put("k", R.raw.word_cool);
        exampleOneMap.put("ʧ", R.raw.word_chairs);
        exampleOneMap.put("f", R.raw.word_father);
        exampleOneMap.put("θ", R.raw.word_think);
        exampleOneMap.put("s", R.raw.word_set);
        exampleOneMap.put("ʃ", R.raw.word_shoot);
        exampleOneMap.put("b", R.raw.word_bill);
        exampleOneMap.put("d", R.raw.word_delicious);
        exampleOneMap.put("g", R.raw.word_good);
        exampleOneMap.put("ʤ", R.raw.word_jump);
        exampleOneMap.put("v", R.raw.word_very);
        exampleOneMap.put("ð", R.raw.word_there);
        exampleOneMap.put("z", R.raw.word_zoo);
        exampleOneMap.put("ʒ", R.raw.word_usually);
        exampleOneMap.put("m", R.raw.word_me);
        exampleOneMap.put("n", R.raw.word_new);
        exampleOneMap.put("ŋ", R.raw.word_ringing);
        exampleOneMap.put("l", R.raw.word_loud);
        exampleOneMap.put("w", R.raw.word_wary);
        exampleOneMap.put("j", R.raw.word_year);
        exampleOneMap.put("h", R.raw.word_her);
        exampleOneMap.put("r", R.raw.word_real);
        exampleOneMap.put("i", R.raw.word_each);
        exampleOneMap.put("ɪ", R.raw.word_it);
        exampleOneMap.put("ɛ", R.raw.word_edge);
        exampleOneMap.put("æ", R.raw.word_ash);
        exampleOneMap.put("ɑ", R.raw.word_on);
        exampleOneMap.put("ɔ", R.raw.word_ought);
        exampleOneMap.put("ʊ", R.raw.word_put);
        exampleOneMap.put("u", R.raw.word_ooze);
        exampleOneMap.put("ʌ", R.raw.word_up);
        exampleOneMap.put("ə", R.raw.word_above);
        exampleOneMap.put("eɪ", R.raw.word_age);
        exampleOneMap.put("aɪ", R.raw.word_im);
        exampleOneMap.put("aʊ", R.raw.word_out);
        exampleOneMap.put("ɔɪ", R.raw.word_oil);
        exampleOneMap.put("oʊ", R.raw.word_oath);
        exampleOneMap.put("ɝ", R.raw.word_earth);
        exampleOneMap.put("ɚ", R.raw.word_mother);
        exampleOneMap.put("ɑr", R.raw.word_arm);
        exampleOneMap.put("ɛr", R.raw.word_aired);
        exampleOneMap.put("ɪr", R.raw.word_ear);
        exampleOneMap.put("ɔr", R.raw.word_oars);
        exampleOneMap.put("ʔ", R.raw.word_put_glottal);
        exampleOneMap.put("ɾ", R.raw.word_water);

    }

    private void initExampleTwo() {

        exampleTwoMap = new HashMap<>();

        exampleTwoMap.put("p", R.raw.word_speak);
        exampleTwoMap.put("t", R.raw.word_sting);
        exampleTwoMap.put("k", R.raw.word_skill);
        exampleTwoMap.put("ʧ", R.raw.word_eachof);
        exampleTwoMap.put("f", R.raw.word_gift);
        exampleTwoMap.put("θ", R.raw.word_jonathan);
        exampleTwoMap.put("s", R.raw.word_raced);
        exampleTwoMap.put("ʃ", R.raw.word_wished);
        exampleTwoMap.put("b", R.raw.word_above);
        exampleTwoMap.put("d", R.raw.word_cards);
        exampleTwoMap.put("g", R.raw.word_again);
        exampleTwoMap.put("ʤ", R.raw.word_agile);
        exampleTwoMap.put("v", R.raw.word_every);
        exampleTwoMap.put("ð", R.raw.word_father);
        exampleTwoMap.put("z", R.raw.word_raised);
        exampleTwoMap.put("ʒ", R.raw.word_pleasure);
        exampleTwoMap.put("m", R.raw.word_jump);
        exampleTwoMap.put("n", R.raw.word_panda);
        exampleTwoMap.put("ŋ", R.raw.word_think);
        exampleTwoMap.put("l", R.raw.word_cool);
        exampleTwoMap.put("w", R.raw.word_twelve);
        exampleTwoMap.put("j", R.raw.word_cube);
        exampleTwoMap.put("h", R.raw.word_have);
        exampleTwoMap.put("r", R.raw.word_rib);
        exampleTwoMap.put("i", R.raw.word_read);
        exampleTwoMap.put("ɪ", R.raw.word_him);
        exampleTwoMap.put("ɛ", R.raw.word_set);
        exampleTwoMap.put("æ", R.raw.word_bad);
        exampleTwoMap.put("ɑ", R.raw.word_father);
        exampleTwoMap.put("ɔ", R.raw.word_call);
        exampleTwoMap.put("ʊ", R.raw.word_good);
        exampleTwoMap.put("u", R.raw.word_shoot);
        exampleTwoMap.put("ʌ", R.raw.word_sun);
        exampleTwoMap.put("ə", R.raw.word_delicious);
        exampleTwoMap.put("eɪ", R.raw.word_safe);
        exampleTwoMap.put("aɪ", R.raw.word_bite);
        exampleTwoMap.put("aʊ", R.raw.word_loud);
        exampleTwoMap.put("ɔɪ", R.raw.word_voice);
        exampleTwoMap.put("oʊ", R.raw.word_cove);
        exampleTwoMap.put("ɝ", R.raw.word_birthday);
        exampleTwoMap.put("ɚ", R.raw.word_perform);
        exampleTwoMap.put("ɑr", R.raw.word_part);
        exampleTwoMap.put("ɛr", R.raw.word_chairs);
        exampleTwoMap.put("ɪr", R.raw.word_weird);
        exampleTwoMap.put("ɔr", R.raw.word_north);
        exampleTwoMap.put("ʔ", R.raw.word_button);
        exampleTwoMap.put("ɾ", R.raw.word_little);

    }

    private void initExampleThree() {

        exampleThreeMap = new HashMap<>();

        exampleThreeMap.put("p", R.raw.word_stop);
        exampleThreeMap.put("t", R.raw.word_it);
        exampleThreeMap.put("k", R.raw.word_week);
        exampleThreeMap.put("ʧ", R.raw.word_church);
        exampleThreeMap.put("f", R.raw.word_if);
        exampleThreeMap.put("θ", R.raw.word_earth);
        exampleThreeMap.put("s", R.raw.word_pass);
        exampleThreeMap.put("ʃ", R.raw.word_ash);
        exampleThreeMap.put("b", R.raw.word_rib);
        exampleThreeMap.put("d", R.raw.word_card);
        exampleThreeMap.put("g", R.raw.word_pig);
        exampleThreeMap.put("ʤ", R.raw.word_edge);
        exampleThreeMap.put("v", R.raw.word_have);
        exampleThreeMap.put("ð", R.raw.word_bathe);
        exampleThreeMap.put("z", R.raw.word_cars);
        exampleThreeMap.put("ʒ", R.raw.word_beige);
        exampleThreeMap.put("m", R.raw.word_arm);
        exampleThreeMap.put("n", R.raw.word_sun);
        exampleThreeMap.put("ŋ", R.raw.word_sung);
        exampleThreeMap.put("l", R.raw.word_bill);
        exampleThreeMap.put("w", R.raw.word_what);
        exampleThreeMap.put("j", R.raw.word_yellow);
        exampleThreeMap.put("h", R.raw.word_ahead);
        exampleThreeMap.put("r", R.raw.word_roar);
        exampleThreeMap.put("i", R.raw.word_me);
        exampleThreeMap.put("ɪ", R.raw.word_rib);
        exampleThreeMap.put("ɛ", R.raw.word_said);
        exampleThreeMap.put("æ", R.raw.word_bat);
        exampleThreeMap.put("ɑ", R.raw.word_ma);
        exampleThreeMap.put("ɔ", R.raw.word_saw);
        exampleThreeMap.put("ʊ", R.raw.word_should);
        exampleThreeMap.put("u", R.raw.word_blue);
        exampleThreeMap.put("ʌ", R.raw.word_uhhuh);
        exampleThreeMap.put("ə", R.raw.word_panda);
        exampleThreeMap.put("eɪ", R.raw.word_may);
        exampleThreeMap.put("aɪ", R.raw.word_fly);
        exampleThreeMap.put("aʊ", R.raw.word_cow);
        exampleThreeMap.put("ɔɪ", R.raw.word_boy);
        exampleThreeMap.put("oʊ", R.raw.word_low);
        exampleThreeMap.put("ɝ", R.raw.word_her);
        exampleThreeMap.put("ɚ", R.raw.word_wonderful);
        exampleThreeMap.put("ɑr", R.raw.word_car);
        exampleThreeMap.put("ɛr", R.raw.word_there);
        exampleThreeMap.put("ɪr", R.raw.word_here);
        exampleThreeMap.put("ɔr", R.raw.word_roar);
        exampleThreeMap.put("ʔ", R.raw.word_uhoh);
        exampleThreeMap.put("ɾ", R.raw.word_thirty);

    }
}