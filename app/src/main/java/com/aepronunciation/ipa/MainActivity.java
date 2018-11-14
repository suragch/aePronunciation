package com.aepronunciation.ipa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String TEST_NAME_KEY = "testName";
    public static final String NUMBER_OF_QUESTIONS_KEY = "numberOfQuestions";
    public static final String TEST_MODE_KEY = "testMode";
    public static final String VOWEL_ARRAY_KEY = "vowelArray";
    public static final String CONSONANT_ARRAY_KEY = "consonantArray";
    public static final String PRACTICE_MODE_IS_SINGLE_KEY = "practiceMode";
    public static final String TIME_LEARN_SINGLE_KEY = "timeLearnSingle";
    public static final String TIME_LEARN_DOUBLE_KEY = "timeLearnDouble";
    public static final String TIME_PRACTICE_SINGLE_KEY = "timePracticeSingle";
    public static final String TIME_PRACTICE_DOUBLE_KEY = "timePracticeDouble";
    public static final String TIME_TEST_SINGLE_KEY = "timeTestSingle";
    public static final String TIME_TEST_DOUBLE_KEY = "timeTestDouble";
    public static final long TIME_DEFAULT = 0;

    private static final int LEARN_TAB_POSITION = 0;
    private static final int PRACTICE_TAB_POSITION = 1;
    //private static final int TEST_TAB_POSITION = 2;

    public static final int TEST_RESULTS_RESULT = 0;


    private LearnFragment mLearnFragment;
    private PracticeFragment mPracticeFragment;
    private TestFragment mTestFragment;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_tab_learn)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_tab_practice)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_tab_test)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.addOnTabSelectedListener(tabListener);

        // disable rotation for smaller devices
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());

            // restart the timer if on the learn or practice tab
            StudyTimer timer = StudyTimer.getInstance();
            if (tabLayout.getSelectedTabPosition() == LEARN_TAB_POSITION) {
                timer.start(getApplicationContext(), StudyTimer.StudyType.LearnSingle);
            } else if (tabLayout.getSelectedTabPosition() == PRACTICE_TAB_POSITION) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean isSingle = settings.getBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
                if (isSingle) {
                    timer.start(getApplicationContext(), StudyTimer.StudyType.PracticeSingle);
                } else {
                    timer.start(getApplicationContext(), StudyTimer.StudyType.PracticeDouble);
                }
            } else {
                timer.stop(getApplicationContext());
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.i("TAG", "onTabUnselected: " + tab.getPosition());
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            Log.i("TAG", "onTabReselected: " + tab.getPosition());
        }
    };

    @Override
    protected void onResume() {

        // restart the timer if on the learn or practice tab
        StudyTimer timer = StudyTimer.getInstance();
        if (tabLayout.getSelectedTabPosition() == LEARN_TAB_POSITION) {
            timer.start(this, StudyTimer.StudyType.LearnSingle);
        } else if (tabLayout.getSelectedTabPosition() == PRACTICE_TAB_POSITION) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isSingle = settings.getBoolean(PRACTICE_MODE_IS_SINGLE_KEY, true);
            if (isSingle) {
                timer.start(this, StudyTimer.StudyType.PracticeSingle);
            } else {
                timer.start(this, StudyTimer.StudyType.PracticeDouble);
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        // stop recording time when user leaves
        StudyTimer timer = StudyTimer.getInstance();
        timer.stop(this);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_keyboard:
                intent = new Intent(this, KeyboardInputActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_personal_evaluation:
                intent = new Intent(this, PersonalEvaluationActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        if (requestCode == TEST_RESULTS_RESULT) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                SoundMode mode = (SoundMode) data.getSerializableExtra(TEST_MODE_KEY);
                ArrayList<String> vowels = data.getStringArrayListExtra(VOWEL_ARRAY_KEY);
                ArrayList<String> consonants = data.getStringArrayListExtra(CONSONANT_ARRAY_KEY);

                // select practice tab
                viewPager.setCurrentItem(PRACTICE_TAB_POSITION);

                // set up UI to practice the difficult sounds
                if (mPracticeFragment != null) {
                    mPracticeFragment.updateUiForSelectedSounds(mode, vowels, consonants);
                }

            }
        }

    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new LearnFragment();
                case 1:
                    return new PracticeFragment();
                case 2:
                    return new TestFragment();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            switch (position) {
                case 0:
                    mLearnFragment = (LearnFragment) createdFragment;
                    break;
                case 1:
                    mPracticeFragment = (PracticeFragment) createdFragment;
                    break;
                case 2:
                    mTestFragment = (TestFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }


        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}














