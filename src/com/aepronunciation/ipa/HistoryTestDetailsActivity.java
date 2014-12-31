package com.aepronunciation.ipa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryTestDetailsActivity extends BaseActivity implements
		OnItemClickListener, SoundPool.OnLoadCompleteListener {

	static final String STATE_SCROLL_POSITION = "scrollPosition";
	
	int savedPosition = 0;
	private static String userName;
	private static long timeLength;
	public static String testMode;
	private static int score;

	//private static StringBuilder wrong;
	private ListView listView;

	private static final int SRC_QUALITY = 0;
	private static final int PRIORITY = 1;
	private SoundPool soundPool = null;
	private DoubleSound doubleSound;
	private SingleSound singleSound;

	TextView tvName;
	TextView tvDate;
	TextView tvPercent;
	TextView tvTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_test_details);

		// Get extras from Test Activity
		Bundle bundle = getIntent().getExtras();
		long id = bundle.getLong("id", 0);

		new GetTest().execute(id);

		// create objects
		tvName = (TextView) findViewById(R.id.tvResultName);
		tvDate = (TextView) findViewById(R.id.tvResultDate);
		tvPercent = (TextView) findViewById(R.id.tvResultPercent);
		tvTime = (TextView) findViewById(R.id.tvResultTime);
		listView = (ListView) findViewById(R.id.lvTestResults);
		listView.setOnItemClickListener(this);
		singleSound = new SingleSound();
		doubleSound = new DoubleSound();
		//wrong = new StringBuilder();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// hide the menu
		return false;
	}

	@Override
	protected void onResume() {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
		soundPool.setOnLoadCompleteListener(this);

		super.onResume();
	}

	@Override
	protected void onPause() {

		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}

		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save the user's current game state
		int currentPosition = listView.getFirstVisiblePosition();
		savedInstanceState.putInt(STATE_SCROLL_POSITION,
				currentPosition);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		savedPosition = savedInstanceState
				.getInt(STATE_SCROLL_POSITION);
		//listView.setSelection(savedPosition);

	}

	public void tryAgainClick(View v) {

		// Start test activity
		Intent intent = new Intent(this, TestSetupActivity.class);
		startActivity(intent);
	}

	@Override
	public void onKeyTouched(String keyString) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowId) {

		// Get the text from the listview item
		TextView tvCorrect = (TextView) view
				.findViewById(R.id.tvCorrectAnswerItem);
		TextView tvUser = (TextView) view.findViewById(R.id.tvUserAnswerItem);
		String correctIpa = tvCorrect.getText().toString();
		final String userIpa = tvUser.getText().toString();

		// play the sounds
		playSound(correctIpa);
		if (!TextUtils.isEmpty(userIpa)) {
			int delay = 1000;
			if (correctIpa.equals("l")) {
				delay = 2000; // this sound needs a longer delay
			}
			// delay playing second sound
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					playSound(userIpa);
				}
			}, delay);
		}

	}

	// call: new GetTest().execute();
	private class GetTest extends AsyncTask<Long, Void, Test> {

		Context context = getApplicationContext();

		@Override
		protected Test doInBackground(Long... params) {

			// android.os.Debug.waitForDebugger();

			long id = params[0];

			Test test = new Test();

			try {

				MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
						getApplicationContext());
				test = dbAdapter.getTest(id);
			} catch (Exception e) {
				//Log.e("app", e.toString());
			} finally {

			}
			return test;

		}

		@Override
		protected void onPostExecute(Test test) {

			// Get answers from test
			userName = test.getUserName();
			long dateMilliseconds = test.getDate();
			timeLength = test.getTimeLength();
			testMode = test.getMode();
			score = test.getScore();
			String[] correctAnswers = test.getCorrectAnswers().split(",");
			String[] userAnswers = test.getUserAnswers().split(",");

			// error checking
			int length = correctAnswers.length;
			if (length != userAnswers.length) {
				//Log.e("app", "HistorTestDetails wrong length");
				if (length > userAnswers.length) {
					length = userAnswers.length;
				}
			}

			// make answer array object to update listview
			ArrayList<Answer> answers = new ArrayList<Answer>();
			for (int i = 0; i < length; i++) {
				Answer answer = new Answer();
				answer.setCorrectAnswer(correctAnswers[i]);
				answer.setUserAnswer(userAnswers[i]);
				answers.add(answer);
			}
			MyTestDetailsListAdapter adapter = new MyTestDetailsListAdapter(
					context, answers, testMode);
			listView.setAdapter(adapter);
			listView.setSelection(savedPosition);
			
			// get date/time
			Date date = new Date(dateMilliseconds);
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy, h:mma",
					Locale.US);
			String formattedDate = sdf.format(date);
			formattedDate = formattedDate.replace("AM", "am").replace("PM",
					"pm");

			// update textviews
			tvName.setText(userName);
			tvDate.setText(formattedDate);
			tvPercent.setText(Integer.toString(score) + "%");
			tvTime.setText("Time:\n" + getTimeString(timeLength));

		}

	}

	private void playSound(String ipaSound) {

		int soundId = -1;

		// look up audio resource id for that sound
		if (testMode.equals("double")) {
			soundId = doubleSound.getSoundResourceId(ipaSound);
		} else { // single
			soundId = singleSound.getSoundResourceId(ipaSound);
		}

		if (soundId == -1) {

			// do error checking on input
			Bundle args = new Bundle();
			args.putString("errorMessage",
					Answer.getErrorMessage(ipaSound));
			DialogFragment dialog = new ErrorDialogFragment();
			dialog.setArguments(args);
			dialog.show(getSupportFragmentManager(), "ErrorDialogFragmentTag");

		} else {
			// load (and play) sound
			soundPool.load(this, soundId, PRIORITY);
		}
	}

	@Override
	public void onLoadComplete(SoundPool sPool, int sid, int status) {

		if (status != 0) // 0=success
			return;

		soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
		soundPool.unload(sid);

	}

}
