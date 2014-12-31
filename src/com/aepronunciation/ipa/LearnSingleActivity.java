package com.aepronunciation.ipa;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LearnSingleActivity extends BaseActivity implements
		SoundPool.OnLoadCompleteListener {
	
	static final String STATE_FIRST_LOAD = "firstLoad";
	static final String STATE_IPA = "ipaSymbol";
	static final String STATE_DESCRIPTION = "description";
	static final String STATE_EXAMPLE_1 = "example1";
	static final String STATE_EXAMPLE_2 = "example2";
	static final String STATE_EXAMPLE_3 = "example3";


	LinearLayout topHalf;
	RelativeLayout rlIntroMessage;
	TextView tvIpaSymbol;
	TextView tvIpaDescription;
	RelativeLayout rlExample1;
	RelativeLayout rlExample2;
	RelativeLayout rlExample3;
	TextView tvExample1;
	TextView tvExample2;
	TextView tvExample3;
	boolean firstLoad = true;
	private SingleSound singleSound;
	long startTime;
	SharedPreferences settings;

	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;

	// private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_single);

		// initialize views
		topHalf =  (LinearLayout) findViewById(R.id.llLearnSingleTopHalf);
		rlIntroMessage = (RelativeLayout) findViewById(R.id.rlInstructions);
		tvIpaSymbol = (TextView) findViewById(R.id.tvLearnSingleIpaSound);
		tvIpaDescription = (TextView) findViewById(R.id.tvLearnSingleIpaDescription);
		rlExample1 = (RelativeLayout) findViewById(R.id.rlExample1);
		rlExample2 = (RelativeLayout) findViewById(R.id.rlExample2);
		rlExample3 = (RelativeLayout) findViewById(R.id.rlExample3);
		tvExample1 = (TextView) findViewById(R.id.tvExample1);
		tvExample2 = (TextView) findViewById(R.id.tvExample2);
		tvExample3 = (TextView) findViewById(R.id.tvExample3);

		tvIpaSymbol.setVisibility(View.INVISIBLE);
		rlExample1.setVisibility(View.INVISIBLE);
		rlExample2.setVisibility(View.INVISIBLE);
		rlExample3.setVisibility(View.INVISIBLE);
		firstLoad = true;

		// Set up fragment
		fragmentManager = getSupportFragmentManager();
		keyboardFragment = (KeyboardFragment) fragmentManager
				.findFragmentByTag(KEYBOARD_FRAGMENT_TAG);
		if (keyboardFragment == null) {
			keyboardFragment = new KeyboardFragment();
			fragmentManager
					.beginTransaction()
					.add(R.id.keyboardContainer, keyboardFragment,
							KEYBOARD_FRAGMENT_TAG).commit();
		}

		singleSound = new SingleSound();

	}

	@Override
	protected void onResume() {

		// start timing
		startTime = System.nanoTime();

		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
		soundPool.setOnLoadCompleteListener(this);

		super.onResume();
	}

	@Override
	protected void onPause() {

		// Increment stored time by elapsed time
		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		long formerTime = settings
				.getLong(TIME_LEARN_SINGLE_KEY, TIME_DEFAULT);
		long elapsedTime = System.nanoTime() - startTime;
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(TIME_LEARN_SINGLE_KEY, formerTime + elapsedTime);
		editor.commit();

		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}

		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
	    // Save the user's current game state
		savedInstanceState.putBoolean(STATE_FIRST_LOAD, firstLoad);
	    savedInstanceState.putString(STATE_IPA, tvIpaSymbol.getText().toString());
	    savedInstanceState.putString(STATE_DESCRIPTION, tvIpaDescription.getText().toString());
	    savedInstanceState.putString(STATE_EXAMPLE_1, tvExample1.getText().toString());
	    savedInstanceState.putString(STATE_EXAMPLE_2, tvExample2.getText().toString());
	    savedInstanceState.putString(STATE_EXAMPLE_3, tvExample3.getText().toString());
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    firstLoad = savedInstanceState.getBoolean(STATE_FIRST_LOAD);
	    
	    if (!firstLoad) {
	    	
	    	// make views visible again
			rlIntroMessage.setVisibility(View.GONE);
			topHalf.setVisibility(View.VISIBLE);
			tvIpaSymbol.setVisibility(View.VISIBLE);
			tvIpaDescription.setVisibility(View.VISIBLE);
			rlExample1.setVisibility(View.VISIBLE);
			rlExample2.setVisibility(View.VISIBLE);
			rlExample3.setVisibility(View.VISIBLE);

			// add the text back
			tvIpaSymbol.setText(savedInstanceState.getString(STATE_IPA));
			tvIpaDescription.setText(savedInstanceState.getString(STATE_DESCRIPTION));
			tvExample1.setText(savedInstanceState.getString(STATE_EXAMPLE_1));
			tvExample2.setText(savedInstanceState.getString(STATE_EXAMPLE_2));
			tvExample3.setText(savedInstanceState.getString(STATE_EXAMPLE_3));

		}
	    
	}

	@Override
	public void onKeyTouched(String keyString) {
		
		if (firstLoad) {
			rlIntroMessage.setVisibility(View.GONE);
			topHalf.setVisibility(View.VISIBLE);
			tvIpaSymbol.setVisibility(View.VISIBLE);
			tvIpaDescription.setVisibility(View.VISIBLE);
			rlExample1.setVisibility(View.VISIBLE);
			rlExample2.setVisibility(View.VISIBLE);
			rlExample3.setVisibility(View.VISIBLE);
			firstLoad = false;
		}

		if (keyString.equals("p")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.p_description));
			tvExample1.setText(getResources().getString(R.string.p_example1));
			tvExample2.setText(getResources().getString(R.string.p_example2));
			tvExample3.setText(getResources().getString(R.string.p_example3));

			soundPool.load(this, R.raw.single_p, PRIORITY);

		} else if (keyString.equals("t")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.t_description));
			tvExample1.setText(getResources().getString(R.string.t_example1));
			tvExample2.setText(getResources().getString(R.string.t_example2));
			tvExample3.setText(getResources().getString(R.string.t_example3));

			soundPool.load(this, R.raw.single_t, PRIORITY);

		} else if (keyString.equals("k")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.k_description));
			tvExample1.setText(getResources().getString(R.string.k_example1));
			tvExample2.setText(getResources().getString(R.string.k_example2));
			tvExample3.setText(getResources().getString(R.string.k_example3));

			soundPool.load(this, R.raw.single_k, PRIORITY);

		} else if (keyString.equals("ʧ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ch_description));
			tvExample1.setText(getResources().getString(R.string.ch_example1));
			tvExample2.setText(getResources().getString(R.string.ch_example2));
			tvExample3.setText(getResources().getString(R.string.ch_example3));

			soundPool.load(this, R.raw.single_ch, PRIORITY);

		} else if (keyString.equals("f")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.f_description));
			tvExample1.setText(getResources().getString(R.string.f_example1));
			tvExample2.setText(getResources().getString(R.string.f_example2));
			tvExample3.setText(getResources().getString(R.string.f_example3));

			soundPool.load(this, R.raw.single_f, PRIORITY);

		} else if (keyString.equals("θ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.th_voiceless_description));
			tvExample1.setText(getResources().getString(
					R.string.th_voiceless_example1));
			tvExample2.setText(getResources().getString(
					R.string.th_voiceless_example2));
			tvExample3.setText(getResources().getString(
					R.string.th_voiceless_example3));

			soundPool.load(this, R.raw.single_th_voiceless, PRIORITY);

		} else if (keyString.equals("s")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.s_description));
			tvExample1.setText(getResources().getString(R.string.s_example1));
			tvExample2.setText(getResources().getString(R.string.s_example2));
			tvExample3.setText(getResources().getString(R.string.s_example3));

			soundPool.load(this, R.raw.single_s, PRIORITY);

		} else if (keyString.equals("ʃ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.sh_description));
			tvExample1.setText(getResources().getString(R.string.sh_example1));
			tvExample2.setText(getResources().getString(R.string.sh_example2));
			tvExample3.setText(getResources().getString(R.string.sh_example3));

			soundPool.load(this, R.raw.single_sh, PRIORITY);

		} else if (keyString.equals("b")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.b_description));
			tvExample1.setText(getResources().getString(R.string.b_example1));
			tvExample2.setText(getResources().getString(R.string.b_example2));
			tvExample3.setText(getResources().getString(R.string.b_example3));

			soundPool.load(this, R.raw.single_b, PRIORITY);

		} else if (keyString.equals("d")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.d_description));
			tvExample1.setText(getResources().getString(R.string.d_example1));
			tvExample2.setText(getResources().getString(R.string.d_example2));
			tvExample3.setText(getResources().getString(R.string.d_example3));

			soundPool.load(this, R.raw.single_d, PRIORITY);

		} else if (keyString.equals("g")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.g_description));
			tvExample1.setText(getResources().getString(R.string.g_example1));
			tvExample2.setText(getResources().getString(R.string.g_example2));
			tvExample3.setText(getResources().getString(R.string.g_example3));

			soundPool.load(this, R.raw.single_g, PRIORITY);

		} else if (keyString.equals("ʤ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.dzh_description));
			tvExample1.setText(getResources().getString(R.string.dzh_example1));
			tvExample2.setText(getResources().getString(R.string.dzh_example2));
			tvExample3.setText(getResources().getString(R.string.dzh_example3));

			soundPool.load(this, R.raw.single_dzh, PRIORITY);

		} else if (keyString.equals("v")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.v_description));
			tvExample1.setText(getResources().getString(R.string.v_example1));
			tvExample2.setText(getResources().getString(R.string.v_example2));
			tvExample3.setText(getResources().getString(R.string.v_example3));

			soundPool.load(this, R.raw.single_v, PRIORITY);

		} else if (keyString.equals("ð")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.th_voiced_description));
			tvExample1.setText(getResources().getString(
					R.string.th_voiced_example1));
			tvExample2.setText(getResources().getString(
					R.string.th_voiced_example2));
			tvExample3.setText(getResources().getString(
					R.string.th_voiced_example3));

			soundPool.load(this, R.raw.single_th_voiced, PRIORITY);

		} else if (keyString.equals("z")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.z_description));
			tvExample1.setText(getResources().getString(R.string.z_example1));
			tvExample2.setText(getResources().getString(R.string.z_example2));
			tvExample3.setText(getResources().getString(R.string.z_example3));

			soundPool.load(this, R.raw.single_z, PRIORITY);

		} else if (keyString.equals("ʒ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.zh_description));
			tvExample1.setText(getResources().getString(R.string.zh_example1));
			tvExample2.setText(getResources().getString(R.string.zh_example2));
			tvExample3.setText(getResources().getString(R.string.zh_example3));

			soundPool.load(this, R.raw.single_zh, PRIORITY);

		} else if (keyString.equals("m")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.m_description));
			tvExample1.setText(getResources().getString(R.string.m_example1));
			tvExample2.setText(getResources().getString(R.string.m_example2));
			tvExample3.setText(getResources().getString(R.string.m_example3));

			soundPool.load(this, R.raw.single_m, PRIORITY);

		} else if (keyString.equals("n")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.n_description));
			tvExample1.setText(getResources().getString(R.string.n_example1));
			tvExample2.setText(getResources().getString(R.string.n_example2));
			tvExample3.setText(getResources().getString(R.string.n_example3));

			soundPool.load(this, R.raw.single_n, PRIORITY);

		} else if (keyString.equals("ŋ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ng_description));
			tvExample1.setText(getResources().getString(R.string.ng_example1));
			tvExample2.setText(getResources().getString(R.string.ng_example2));
			tvExample3.setText(getResources().getString(R.string.ng_example3));

			soundPool.load(this, R.raw.single_ng, PRIORITY);

		} else if (keyString.equals("l")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.l_description));
			tvExample1.setText(getResources().getString(R.string.l_example1));
			tvExample2.setText(getResources().getString(R.string.l_example2));
			tvExample3.setText(getResources().getString(R.string.l_example3));

			soundPool.load(this, R.raw.single_l, PRIORITY);

		} else if (keyString.equals("w")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.w_description));
			tvExample1.setText(getResources().getString(R.string.w_example1));
			tvExample2.setText(getResources().getString(R.string.w_example2));
			tvExample3.setText(getResources().getString(R.string.w_example3));

			soundPool.load(this, R.raw.single_w, PRIORITY);

		} else if (keyString.equals("j")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.j_description));
			tvExample1.setText(getResources().getString(R.string.j_example1));
			tvExample2.setText(getResources().getString(R.string.j_example2));
			tvExample3.setText(getResources().getString(R.string.j_example3));

			soundPool.load(this, R.raw.single_j, PRIORITY);

		} else if (keyString.equals("h")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.h_description));
			tvExample1.setText(getResources().getString(R.string.h_example1));
			tvExample2.setText(getResources().getString(R.string.h_example2));
			tvExample3.setText(getResources().getString(R.string.h_example3));

			soundPool.load(this, R.raw.single_h, PRIORITY);

		} else if (keyString.equals("r")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.r_description));
			tvExample1.setText(getResources().getString(R.string.r_example1));
			tvExample2.setText(getResources().getString(R.string.r_example2));
			tvExample3.setText(getResources().getString(R.string.r_example3));

			soundPool.load(this, R.raw.single_r, PRIORITY);

		} else if (keyString.equals("i")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.i_description));
			tvExample1.setText(getResources().getString(R.string.i_example1));
			tvExample2.setText(getResources().getString(R.string.i_example2));
			tvExample3.setText(getResources().getString(R.string.i_example3));

			soundPool.load(this, R.raw.single_i_long, PRIORITY);

		} else if (keyString.equals("ɪ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.i_short_description));
			tvExample1.setText(getResources().getString(
					R.string.i_short_example1));
			tvExample2.setText(getResources().getString(
					R.string.i_short_example2));
			tvExample3.setText(getResources().getString(
					R.string.i_short_example3));

			soundPool.load(this, R.raw.single_i_short, PRIORITY);

		} else if (keyString.equals("ɛ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.e_short_description));
			tvExample1.setText(getResources().getString(
					R.string.e_short_example1));
			tvExample2.setText(getResources().getString(
					R.string.e_short_example2));
			tvExample3.setText(getResources().getString(
					R.string.e_short_example3));

			soundPool.load(this, R.raw.single_e_short, PRIORITY);

		} else if (keyString.equals("æ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ae_description));
			tvExample1.setText(getResources().getString(R.string.ae_example1));
			tvExample2.setText(getResources().getString(R.string.ae_example2));
			tvExample3.setText(getResources().getString(R.string.ae_example3));

			soundPool.load(this, R.raw.single_ae, PRIORITY);

		} else if (keyString.equals("ɑ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.a_description));
			tvExample1.setText(getResources().getString(R.string.a_example1));
			tvExample2.setText(getResources().getString(R.string.a_example2));
			tvExample3.setText(getResources().getString(R.string.a_example3));

			soundPool.load(this, R.raw.single_a, PRIORITY);

		} else if (keyString.equals("ɔ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.c_backwards_description));
			tvExample1.setText(getResources().getString(
					R.string.c_backwards_example1));
			tvExample2.setText(getResources().getString(
					R.string.c_backwards_example2));
			tvExample3.setText(getResources().getString(
					R.string.c_backwards_example3));

			soundPool.load(this, R.raw.single_c_backwards, PRIORITY);

		} else if (keyString.equals("ʊ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.u_short_description));
			tvExample1.setText(getResources().getString(
					R.string.u_short_example1));
			tvExample2.setText(getResources().getString(
					R.string.u_short_example2));
			tvExample3.setText(getResources().getString(
					R.string.u_short_example3));

			soundPool.load(this, R.raw.single_u_short, PRIORITY);

		} else if (keyString.equals("u")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.u_description));
			tvExample1.setText(getResources().getString(R.string.u_example1));
			tvExample2.setText(getResources().getString(R.string.u_example2));
			tvExample3.setText(getResources().getString(R.string.u_example3));

			soundPool.load(this, R.raw.single_u, PRIORITY);

		} else if (keyString.equals("ʌ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.v_upsidedown_description));
			tvExample1.setText(getResources().getString(
					R.string.v_upsidedown_example1));
			tvExample2.setText(getResources().getString(
					R.string.v_upsidedown_example2));
			tvExample3.setText(getResources().getString(
					R.string.v_upsidedown_example3));

			soundPool.load(this, R.raw.single_v_upsidedown, PRIORITY);

		} else if (keyString.equals("ə")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.schwa_description));
			tvExample1.setText(getResources()
					.getString(R.string.schwa_example1));
			tvExample2.setText(getResources()
					.getString(R.string.schwa_example2));
			tvExample3.setText(getResources()
					.getString(R.string.schwa_example3));

			soundPool.load(this, R.raw.single_schwa, PRIORITY);

		} else if (keyString.equals("e")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.e_description));
			tvExample1.setText(getResources().getString(R.string.e_example1));
			tvExample2.setText(getResources().getString(R.string.e_example2));
			tvExample3.setText(getResources().getString(R.string.e_example3));

			soundPool.load(this, R.raw.single_e, PRIORITY);

		} else if (keyString.equals("aɪ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ai_description));
			tvExample1.setText(getResources().getString(R.string.ai_example1));
			tvExample2.setText(getResources().getString(R.string.ai_example2));
			tvExample3.setText(getResources().getString(R.string.ai_example3));

			soundPool.load(this, R.raw.single_ai, PRIORITY);

		} else if (keyString.equals("aʊ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.au_description));
			tvExample1.setText(getResources().getString(R.string.au_example1));
			tvExample2.setText(getResources().getString(R.string.au_example2));
			tvExample3.setText(getResources().getString(R.string.au_example3));

			soundPool.load(this, R.raw.single_au, PRIORITY);

		} else if (keyString.equals("ɔɪ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.oi_description));
			tvExample1.setText(getResources().getString(R.string.oi_example1));
			tvExample2.setText(getResources().getString(R.string.oi_example2));
			tvExample3.setText(getResources().getString(R.string.oi_example3));

			soundPool.load(this, R.raw.single_oi, PRIORITY);

		} else if (keyString.equals("o")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.o_description));
			tvExample1.setText(getResources().getString(R.string.o_example1));
			tvExample2.setText(getResources().getString(R.string.o_example2));
			tvExample3.setText(getResources().getString(R.string.o_example3));

			soundPool.load(this, R.raw.single_o, PRIORITY);

		} else if (keyString.equals("ɝ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.er_stressed_description));
			tvExample1.setText(getResources().getString(
					R.string.er_stressed_example1));
			tvExample2.setText(getResources().getString(
					R.string.er_stressed_example2));
			tvExample3.setText(getResources().getString(
					R.string.er_stressed_example3));

			soundPool.load(this, R.raw.single_er_stressed, PRIORITY);

		} else if (keyString.equals("ɚ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.er_unstressed_description));
			tvExample1.setText(getResources().getString(
					R.string.er_unstressed_example1));
			tvExample2.setText(getResources().getString(
					R.string.er_unstressed_example2));
			tvExample3.setText(getResources().getString(
					R.string.er_unstressed_example3));

			soundPool.load(this, R.raw.single_er_unstressed, PRIORITY);

		} else if (keyString.equals("ɑr")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ar_description));
			tvExample1.setText(getResources().getString(R.string.ar_example1));
			tvExample2.setText(getResources().getString(R.string.ar_example2));
			tvExample3.setText(getResources().getString(R.string.ar_example3));

			soundPool.load(this, R.raw.single_ar, PRIORITY);

		} else if (keyString.equals("ɛr")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.er_description));
			tvExample1.setText(getResources().getString(R.string.er_example1));
			tvExample2.setText(getResources().getString(R.string.er_example2));
			tvExample3.setText(getResources().getString(R.string.er_example3));

			soundPool.load(this, R.raw.single_er, PRIORITY);

		} else if (keyString.equals("ɪr")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.ir_description));
			tvExample1.setText(getResources().getString(R.string.ir_example1));
			tvExample2.setText(getResources().getString(R.string.ir_example2));
			tvExample3.setText(getResources().getString(R.string.ir_example3));

			soundPool.load(this, R.raw.single_ir, PRIORITY);

		} else if (keyString.equals("ɔr")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.or_description));
			tvExample1.setText(getResources().getString(R.string.or_example1));
			tvExample2.setText(getResources().getString(R.string.or_example2));
			tvExample3.setText(getResources().getString(R.string.or_example3));

			soundPool.load(this, R.raw.single_or, PRIORITY);

		} else if (keyString.equals("ʔ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.glottal_stop_description));
			tvExample1.setText(getResources().getString(
					R.string.glottal_stop_example1));
			tvExample2.setText(getResources().getString(
					R.string.glottal_stop_example2));
			tvExample3.setText(getResources().getString(
					R.string.glottal_stop_example3));

			soundPool.load(this, R.raw.single_glottal_stop, PRIORITY);

		} else if (keyString.equals("ɾ")) {

			tvIpaSymbol.setText(keyString);
			tvIpaDescription.setText(getResources().getString(
					R.string.flap_t_description));
			tvExample1.setText(getResources().getString(
					R.string.flap_t_example1));
			tvExample2.setText(getResources().getString(
					R.string.flap_t_example2));
			tvExample3.setText(getResources().getString(
					R.string.flap_t_example3));

			soundPool.load(this, R.raw.single_flap_t, PRIORITY);

		}

	}

	public void exampleOneClick(View v) {

		// get the ipa string
		String ipa = tvIpaSymbol.getText().toString();

		// use the string to look up the audio resource id
		int soundId = singleSound.getExampleOneResourceId(ipa);

		// use the id to load (and play) the sound
		soundPool.load(this, soundId, PRIORITY);

	}

	public void exampleTwoClick(View v) {

		// get the ipa string
		String ipa = tvIpaSymbol.getText().toString();

		// use the string to look up the audio resource id
		int soundId = singleSound.getExampleTwoResourceId(ipa);

		// use the id to load (and play) the sound
		soundPool.load(this, soundId, PRIORITY);

	}

	public void exampleThreeClick(View v) {

		// get the ipa string
		String ipa = tvIpaSymbol.getText().toString();

		// use the string to look up the audio resource id
		int soundId = singleSound.getExampleThreeResourceId(ipa);

		// use the id to load (and play) the sound
		soundPool.load(this, soundId, PRIORITY);

	}

	@Override
	public void onLoadComplete(SoundPool sPool, int sid, int status) {

		if (status != 0) // 0=success
			return;

		soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
		soundPool.unload(sid);

	}

}
