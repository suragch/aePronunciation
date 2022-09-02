package com.aepronunciation.ipa;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAllTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        mData = new ArrayList<>();
        adapter = new TestsAllRecyclerViewAdapter(this, mData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // get tests
        new InitializeTestList(this).execute();
    }

    private static class InitializeTestList extends AsyncTask<Void, Void, ArrayList<Test>> {

        private final WeakReference<HistoryTestsActivity> activityReference;

        // only retain a weak reference to the activity
        InitializeTestList(HistoryTestsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<Test> doInBackground(Void... params) {

            ArrayList<Test> result = new ArrayList<>();
            HistoryTestsActivity activity = activityReference.get();

            try {
                MyDatabaseAdapter dbAdapter = new MyDatabaseAdapter(activity);
                result = dbAdapter.getAllTestScores();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Test> result) {
            HistoryTestsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.mData.clear();
            activity.mData.addAll(result);
            activity.adapter.notifyDataSetChanged();
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