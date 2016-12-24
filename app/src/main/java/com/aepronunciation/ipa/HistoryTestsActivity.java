package com.aepronunciation.ipa;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class HistoryTestsActivity extends AppCompatActivity implements
        TestsAllRecyclerViewAdapter.ItemClickListener {

    private TestsAllRecyclerViewAdapter adapter;
    private ArrayList<Test> mData;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        mData = new ArrayList<>();
        adapter = new TestsAllRecyclerViewAdapter(this, mData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // get tests
        new InitializeTestList().execute();
    }

    private class InitializeTestList extends AsyncTask<Void, Void, ArrayList<Test>> {

        @Override
        protected ArrayList<Test> doInBackground(Void... params) {

            ArrayList<Test> result = new ArrayList<>();

            try {
                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(getApplicationContext());
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
        }
    }

    @Override
    public void onItemClick(View view, int position) {

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
}