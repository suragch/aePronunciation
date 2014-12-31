package com.aepronunciation.ipa;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;

public class SingleSound {

	// private class variables
	private HashMap<String, Integer> soundMap;
	private HashMap<String, Integer> exampleOneMap;
	private HashMap<String, Integer> exampleTwoMap;
	private HashMap<String, Integer> exampleThreeMap;
	private static Random random = new Random();
	private String[] singleSounds;

	// constructor
	public SingleSound() {
		// this.privateClassVariable = something
		initSound();
	}

	// public methods to return values
	public int getSoundCount() {

		return soundMap.size();
	}

	public String getRandomIpa(Context context) {

		// get single sounds string array
		// this does not include schwa and unstressed er
		if (singleSounds == null) {
			singleSounds = context.getResources().getStringArray(
					R.array.single_sounds);
		}

		// get random integer (0 <= x < numberOfSounds)
		int soundIndex = random.nextInt(singleSounds.length);

		// translate integer to ipa string
		String ipa = singleSounds[soundIndex];

		return ipa;
	}

	public String getRandomVowelIpa(Context context) {

		// this is where the vowels start in single_sounds array
		int VOWEL_INDEX_START = 24;

		// get single sounds string array
		// this does not include schwa and unstressed er
		if (singleSounds == null) {
			singleSounds = context.getResources().getStringArray(
					R.array.single_sounds);
		}

		// formula: rand.nextInt((max - min) + 1) + min
		int soundIndex = random
				.nextInt(singleSounds.length - VOWEL_INDEX_START)
				+ VOWEL_INDEX_START;

		// translate integer to ipa string
		String ipa = singleSounds[soundIndex];

		return ipa;
	}

	public String getRandomConsonantIpa(Context context) {

		// this is where the vowels start in single_sounds array
		// The consonants are before that
		int VOWEL_INDEX_START = 24;

		// get single sounds string array
		// this does not include schwa and unstressed er
		if (singleSounds == null) {
			singleSounds = context.getResources().getStringArray(
					R.array.single_sounds);
		}

		// formula: rand.nextInt((max - min) + 1) + min
		int soundIndex = random.nextInt(VOWEL_INDEX_START);

		// translate integer to ipa string
		String ipa = singleSounds[soundIndex];

		return ipa;
	}

	public int getSoundResourceId(String singleSoundIpa) {

		if (soundMap == null) {
			initSound();
		}
		
		// returns null if no value found
		return soundMap.get(singleSoundIpa);
	}

	public int getExampleOneResourceId(String singleSoundIpa) {

		if (exampleOneMap == null) {
			initExampleOne();
		}

		// returns null if no value found
		return exampleOneMap.get(singleSoundIpa);
	}

	public int getExampleTwoResourceId(String singleSoundIpa) {

		if (exampleTwoMap == null) {
			initExampleTwo();
		}

		// returns null if no value found
		return exampleTwoMap.get(singleSoundIpa);
	}

	public int getExampleThreeResourceId(String singleSoundIpa) {

		if (exampleThreeMap == null) {
			initExampleThree();
		}

		// returns null if no value found
		return exampleThreeMap.get(singleSoundIpa);
	}

	// initialize the hashmap when new object created
	private void initSound() {

		soundMap = new HashMap<String, Integer>();

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
		soundMap.put("i", R.raw.single_i_long);
		soundMap.put("ɪ", R.raw.single_i_short);
		soundMap.put("ɛ", R.raw.single_e_short);
		soundMap.put("æ", R.raw.single_ae);
		soundMap.put("ɑ", R.raw.single_a);
		soundMap.put("ɔ", R.raw.single_c_backwards);
		soundMap.put("ʊ", R.raw.single_u_short);
		soundMap.put("u", R.raw.single_u);
		soundMap.put("ʌ", R.raw.single_v_upsidedown);
		soundMap.put("ə", R.raw.single_schwa);
		soundMap.put("e", R.raw.single_e);
		soundMap.put("aɪ", R.raw.single_ai);
		soundMap.put("aʊ", R.raw.single_au);
		soundMap.put("ɔɪ", R.raw.single_oi);
		soundMap.put("o", R.raw.single_o);
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

		exampleOneMap = new HashMap<String, Integer>();

		exampleOneMap.put("p", R.raw.pass);
		exampleOneMap.put("t", R.raw.toad);
		exampleOneMap.put("k", R.raw.cool);
		exampleOneMap.put("ʧ", R.raw.chairs);
		exampleOneMap.put("f", R.raw.father);
		exampleOneMap.put("θ", R.raw.think);
		exampleOneMap.put("s", R.raw.set);
		exampleOneMap.put("ʃ", R.raw.shoot);
		exampleOneMap.put("b", R.raw.bill);
		exampleOneMap.put("d", R.raw.delicious);
		exampleOneMap.put("g", R.raw.good);
		exampleOneMap.put("ʤ", R.raw.jump);
		exampleOneMap.put("v", R.raw.very);
		exampleOneMap.put("ð", R.raw.there);
		exampleOneMap.put("z", R.raw.zoo);
		exampleOneMap.put("ʒ", R.raw.usually);
		exampleOneMap.put("m", R.raw.me);
		exampleOneMap.put("n", R.raw.word_new);
		exampleOneMap.put("ŋ", R.raw.ringing);
		exampleOneMap.put("l", R.raw.loud);
		exampleOneMap.put("w", R.raw.wary);
		exampleOneMap.put("j", R.raw.year);
		exampleOneMap.put("h", R.raw.her);
		exampleOneMap.put("r", R.raw.real);
		exampleOneMap.put("i", R.raw.each);
		exampleOneMap.put("ɪ", R.raw.it);
		exampleOneMap.put("ɛ", R.raw.edge);
		exampleOneMap.put("æ", R.raw.ash);
		exampleOneMap.put("ɑ", R.raw.on);
		exampleOneMap.put("ɔ", R.raw.ought);
		exampleOneMap.put("ʊ", R.raw.put);
		exampleOneMap.put("u", R.raw.ooze);
		exampleOneMap.put("ʌ", R.raw.up);
		exampleOneMap.put("ə", R.raw.above);
		exampleOneMap.put("e", R.raw.age);
		exampleOneMap.put("aɪ", R.raw.im);
		exampleOneMap.put("aʊ", R.raw.out);
		exampleOneMap.put("ɔɪ", R.raw.oil);
		exampleOneMap.put("o", R.raw.oath);
		exampleOneMap.put("ɝ", R.raw.earth);
		exampleOneMap.put("ɚ", R.raw.mother);
		exampleOneMap.put("ɑr", R.raw.arm);
		exampleOneMap.put("ɛr", R.raw.aired);
		exampleOneMap.put("ɪr", R.raw.ear);
		exampleOneMap.put("ɔr", R.raw.oars);
		exampleOneMap.put("ʔ", R.raw.put_glottal);
		exampleOneMap.put("ɾ", R.raw.water);

	}

	private void initExampleTwo() {

		exampleTwoMap = new HashMap<String, Integer>();

		exampleTwoMap.put("p", R.raw.speak);
		exampleTwoMap.put("t", R.raw.sting);
		exampleTwoMap.put("k", R.raw.skill);
		exampleTwoMap.put("ʧ", R.raw.eachof);
		exampleTwoMap.put("f", R.raw.gift);
		exampleTwoMap.put("θ", R.raw.jonathan);
		exampleTwoMap.put("s", R.raw.raced);
		exampleTwoMap.put("ʃ", R.raw.wished);
		exampleTwoMap.put("b", R.raw.above);
		exampleTwoMap.put("d", R.raw.cards);
		exampleTwoMap.put("g", R.raw.again);
		exampleTwoMap.put("ʤ", R.raw.agile);
		exampleTwoMap.put("v", R.raw.every);
		exampleTwoMap.put("ð", R.raw.father);
		exampleTwoMap.put("z", R.raw.raised);
		exampleTwoMap.put("ʒ", R.raw.pleasure);
		exampleTwoMap.put("m", R.raw.jump);
		exampleTwoMap.put("n", R.raw.panda);
		exampleTwoMap.put("ŋ", R.raw.think);
		exampleTwoMap.put("l", R.raw.cool);
		exampleTwoMap.put("w", R.raw.twelve);
		exampleTwoMap.put("j", R.raw.cube);
		exampleTwoMap.put("h", R.raw.have);
		exampleTwoMap.put("r", R.raw.rib);
		exampleTwoMap.put("i", R.raw.read);
		exampleTwoMap.put("ɪ", R.raw.him);
		exampleTwoMap.put("ɛ", R.raw.set);
		exampleTwoMap.put("æ", R.raw.bad);
		exampleTwoMap.put("ɑ", R.raw.father);
		exampleTwoMap.put("ɔ", R.raw.call);
		exampleTwoMap.put("ʊ", R.raw.good);
		exampleTwoMap.put("u", R.raw.shoot);
		exampleTwoMap.put("ʌ", R.raw.sun);
		exampleTwoMap.put("ə", R.raw.delicious);
		exampleTwoMap.put("e", R.raw.safe);
		exampleTwoMap.put("aɪ", R.raw.bite);
		exampleTwoMap.put("aʊ", R.raw.loud);
		exampleTwoMap.put("ɔɪ", R.raw.voice);
		exampleTwoMap.put("o", R.raw.cove);
		exampleTwoMap.put("ɝ", R.raw.birthday);
		exampleTwoMap.put("ɚ", R.raw.perform);
		exampleTwoMap.put("ɑr", R.raw.part);
		exampleTwoMap.put("ɛr", R.raw.chairs);
		exampleTwoMap.put("ɪr", R.raw.weird);
		exampleTwoMap.put("ɔr", R.raw.north);
		exampleTwoMap.put("ʔ", R.raw.button);
		exampleTwoMap.put("ɾ", R.raw.little);

	}

	private void initExampleThree() {

		exampleThreeMap = new HashMap<String, Integer>();

		exampleThreeMap.put("p", R.raw.stop);
		exampleThreeMap.put("t", R.raw.it);
		exampleThreeMap.put("k", R.raw.week);
		exampleThreeMap.put("ʧ", R.raw.church);
		exampleThreeMap.put("f", R.raw.word_if);
		exampleThreeMap.put("θ", R.raw.earth);
		exampleThreeMap.put("s", R.raw.pass);
		exampleThreeMap.put("ʃ", R.raw.ash);
		exampleThreeMap.put("b", R.raw.rib);
		exampleThreeMap.put("d", R.raw.card);
		exampleThreeMap.put("g", R.raw.pig);
		exampleThreeMap.put("ʤ", R.raw.edge);
		exampleThreeMap.put("v", R.raw.have);
		exampleThreeMap.put("ð", R.raw.bathe);
		exampleThreeMap.put("z", R.raw.cars);
		exampleThreeMap.put("ʒ", R.raw.beige);
		exampleThreeMap.put("m", R.raw.arm);
		exampleThreeMap.put("n", R.raw.sun);
		exampleThreeMap.put("ŋ", R.raw.sung);
		exampleThreeMap.put("l", R.raw.bill);
		exampleThreeMap.put("w", R.raw.what);
		exampleThreeMap.put("j", R.raw.yellow);
		exampleThreeMap.put("h", R.raw.ahead);
		exampleThreeMap.put("r", R.raw.roar);
		exampleThreeMap.put("i", R.raw.me);
		exampleThreeMap.put("ɪ", R.raw.rib);
		exampleThreeMap.put("ɛ", R.raw.said);
		exampleThreeMap.put("æ", R.raw.bat);
		exampleThreeMap.put("ɑ", R.raw.ma);
		exampleThreeMap.put("ɔ", R.raw.saw);
		exampleThreeMap.put("ʊ", R.raw.should);
		exampleThreeMap.put("u", R.raw.blue);
		exampleThreeMap.put("ʌ", R.raw.uhhuh);
		exampleThreeMap.put("ə", R.raw.panda);
		exampleThreeMap.put("e", R.raw.may);
		exampleThreeMap.put("aɪ", R.raw.fly);
		exampleThreeMap.put("aʊ", R.raw.cow);
		exampleThreeMap.put("ɔɪ", R.raw.boy);
		exampleThreeMap.put("o", R.raw.low);
		exampleThreeMap.put("ɝ", R.raw.her);
		exampleThreeMap.put("ɚ", R.raw.wonderful);
		exampleThreeMap.put("ɑr", R.raw.car);
		exampleThreeMap.put("ɛr", R.raw.there);
		exampleThreeMap.put("ɪr", R.raw.here);
		exampleThreeMap.put("ɔr", R.raw.roar);
		exampleThreeMap.put("ʔ", R.raw.uhoh);
		exampleThreeMap.put("ɾ", R.raw.thirty);

	}

}
