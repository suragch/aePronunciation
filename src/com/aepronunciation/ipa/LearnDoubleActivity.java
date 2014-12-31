package com.aepronunciation.ipa;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class LearnDoubleActivity extends BaseActivity implements
		OnItemClickListener, SoundPool.OnLoadCompleteListener {
	
	private TextView tvIpa;
	private DoubleSound doubleSound;

	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;
	long startTime;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_double);

		String[] myStringArray = getResources().getStringArray(
				R.array.double_sounds);
		MyGridAdapter adapter = new MyGridAdapter(this, myStringArray);
		GridView gridview = (GridView) findViewById(R.id.gvLearnDouble);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);

		doubleSound = new DoubleSound();

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
				.getLong(TIME_LEARN_DOUBLE_KEY, TIME_DEFAULT);
		long elapsedTime = System.nanoTime() - startTime;
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(TIME_LEARN_DOUBLE_KEY, formerTime + elapsedTime);
		editor.commit();


		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}

		super.onPause();
	}

	@Override
	public void onKeyTouched(String keyString) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		// get the ipa string
		tvIpa = (TextView) v.findViewById(R.id.tvGridDoubleSound);
		String ipa = tvIpa.getText().toString();

		// use the string to look up the audio resource id
		int soundId = doubleSound.getSoundResourceId(ipa);

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
