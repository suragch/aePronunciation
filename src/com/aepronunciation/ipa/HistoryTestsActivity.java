package com.aepronunciation.ipa;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryTestsActivity extends BaseActivity implements
		OnItemClickListener {

	static final String STATE_SCROLL_POSITION = "scrollPosition";
	
	// ArrayList<TestList> tests;
	MyTestsSummaryListAdapter adapter;
	ListView lvTests;
	int savedPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_test);

		// create objects
		lvTests = (ListView) findViewById(R.id.lvAllTests);

		// get tests
		new InitializeTestList().execute();
		lvTests.setOnItemClickListener(this);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// hide the menu
		return false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save the user's current game state
		int currentPosition = lvTests.getFirstVisiblePosition();
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
		//lvTests.setSelection(savedPosition);

	}

	private class InitializeTestList extends
			AsyncTask<Void, Void, ArrayList<Test>> {

		// AsyncTask<Params, Progress, Result>.
		// Params – the input. what you pass to the AsyncTask
		// Progress – if you have any updates, passed to onProgressUpdate()
		// Result – the output. what returns doInBackground()

		// Context context = getApplicationContext();

		@Override
		protected ArrayList<Test> doInBackground(Void... params) {

			// android.os.Debug.waitForDebugger();

			// Log.i("app", "Entered InitializeTestList");
			ArrayList<Test> result = new ArrayList<Test>();

			try {

				MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(
						getApplicationContext());
				result = dbAdapter.getAllTestScores();
			} catch (Exception e) {
				//Log.i("app", e.toString());
			}

			return result;

		}

		@Override
		protected void onPostExecute(ArrayList<Test> result) {

			adapter = new MyTestsSummaryListAdapter(getApplicationContext(),
					result);
			lvTests.setAdapter(adapter);
			lvTests.setSelection(savedPosition);

		}
	}

	@Override
	public void onKeyTouched(String keyString) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowId) {

		// Start test results activity
		TextView tvTestNumber = (TextView) view
				.findViewById(R.id.tvLvTestNumber);
		int testNumber = Integer.parseInt(tvTestNumber.getText().toString());
		long index = testNumber;
		
		Intent intent = new Intent(this, HistoryTestDetailsActivity.class);
		intent.putExtra("id", index);
		startActivity(intent);
	}

}
