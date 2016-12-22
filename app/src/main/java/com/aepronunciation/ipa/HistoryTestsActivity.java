package com.aepronunciation.ipa;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryTestsActivity extends AppCompatActivity implements
        TestsAllRecyclerViewAdapter.ItemClickListener {

    static final String STATE_SCROLL_POSITION = "scrollPosition";

    // ArrayList<TestList> tests;
    private TestsAllRecyclerViewAdapter adapter;
    private ArrayList<Test> mData;
    int savedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvAllTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<Test>();
        adapter = new TestsAllRecyclerViewAdapter(this, mData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // get tests
        new InitializeTestList().execute();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

//        // Save the user's current game state
//        int currentPosition = lvTests.getFirstVisiblePosition();
//        savedInstanceState.putInt(STATE_SCROLL_POSITION,
//                currentPosition);

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

            mData.clear();
            mData.addAll(result);
            adapter.notifyDataSetChanged();

//            adapter = new MyTestsSummaryListAdapter(getApplicationContext(),
//                    result);
//            lvTests.setAdapter(adapter);
//            lvTests.setSelection(savedPosition);
//
//            //ArrayList<Test> emptyDataSet = new ArrayList<Test>();
//            adapter = new TestsAllRecyclerViewAdapter(this, result);
//            adapter.setClickListener(this);
//            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onItemClick(View view, int position) {

        // Start test results activity
//        TextView tvTestNumber = (TextView) view
//                .findViewById(R.id.tvLvTestNumber);
//        int testNumber = Integer.parseInt(tvTestNumber.getText().toString());
//        long index = testNumber;

        // use test results activity


        Intent intent = new Intent(this, HistoryTestDetailsActivity.class);
        intent.putExtra("id", adapter.getItem(position).getId());
        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)

        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_copy:
//                //copyText();
//                return true;
//            case R.id.action_clear:
//                //clearText();
//                return true;
//            case R.id.action_share:
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//                return true;
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}