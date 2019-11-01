package com.aepronunciation.ipa;


import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

class TestResultsRecyclerViewAdapter extends RecyclerView.Adapter<TestResultsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Answer> mData;
    private SoundMode mTestMode;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    TestResultsRecyclerViewAdapter(Context context, ArrayList<Answer> data, SoundMode testMode) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mTestMode = testMode;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv_test_results, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Answer answer = mData.get(position);
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvCorrect.setText(answer.getCorrectAnswer());
        if (!answer.getCorrectAnswer().equals(answer.getUserAnswer())) {

            SpannableString userAnswer = new SpannableString(
                    answer.getUserAnswer());

            if (mTestMode == SoundMode.Double) {

                // find wrong sound(s)
                Pair<String, String> parsedUser = DoubleSound.parse(userAnswer.toString());
                if (parsedUser == null) {
                    holder.tvUser.setText(userAnswer);
                    return;
                }
                boolean firstWrong = !answer.getCorrectAnswer().startsWith(
                        parsedUser.first);
                boolean secondWrong = !answer.getCorrectAnswer().endsWith(
                        parsedUser.second);

                // change wrong sounds to red
                if (firstWrong && secondWrong) {
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
                            userAnswer.length(), 0);
                }else if (firstWrong) {
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
                            parsedUser.first.length(), 0);
                }else{
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED),
                            parsedUser.first.length(), userAnswer.length(), 0);
                }

                holder.tvUser.setText(userAnswer);

            } else {
                userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
                        userAnswer.length(), 0);
                holder.tvUser.setText(userAnswer);
            }

        }else{
            holder.tvUser.setText("");
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IpaTextView tvNumber;
        IpaTextView tvCorrect;
        IpaTextView tvUser;

        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumberItem);
            tvCorrect = itemView.findViewById(R.id.tvCorrectAnswerItem);
            tvUser = itemView.findViewById(R.id.tvUserAnswerItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Answer getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}