package com.aepronunciation.ipa;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TestsAllRecyclerViewAdapter extends RecyclerView.Adapter<TestsAllRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Test> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public TestsAllRecyclerViewAdapter(Context context, ArrayList<Test> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv_all_tests, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Test test = mData.get(position);
        holder.tvNumber.setText(Integer.toString(position + 1));
        holder.tvUserName.setText(test.getUserName());
        holder.tvScore.setText(String.format(mContext.getString(R.string.history_tests_item_score), test.getScore()));

        // get date
        Locale locale = Locale.US; // default
        if (mContext.getString(R.string.locale).equals(Locale.CHINESE.toString())) {
            locale = Locale.CHINESE;
        }
        Date date = new Date(test.getDate());
        DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, locale);
        String formattedDate = df.format(date);
        holder.tvDateTime.setText(formattedDate);

        // Get the mode (single or double) and the number of questions
        String[] correctAnswers = test.getCorrectAnswers().split(",");
        int numberOfQuestions = correctAnswers.length;
        //holder.tvNumberOfQuestions.setText(Integer.toString(numberOfQuestions));
        SoundMode testMode = test.getMode();
        if (testMode == SoundMode.Double) {
            holder.tvType.setText(String.format(mContext.getString(R.string.history_test_details_type_double), numberOfQuestions));
        } else {
            holder.tvType.setText(String.format(mContext.getString(R.string.history_test_details_type_single), numberOfQuestions));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IpaTextView tvNumber;
        IpaTextView tvUserName;
        IpaTextView tvDateTime;
        IpaTextView tvScore;
        IpaTextView tvType;
        //IpaTextView tvNumberOfQuestions;
        //IpaTextView tvWrong;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestNumber);
            tvUserName = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestUserName);
            tvDateTime = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestDate);
            tvScore = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestScore);
            tvType = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestType);
            //tvNumberOfQuestions = (IpaTextView) itemView.findViewById(R.id.tvRvItemTestNumberOfQuestions);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Test getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}