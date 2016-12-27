package com.aepronunciation.ipa;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv_test_results, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Answer answer = mData.get(position);
        holder.tvNumber.setText(Integer.toString(position + 1));
        holder.tvCorrect.setText(answer.getCorrectAnswer());
        if (!answer.getCorrectAnswer().equals(answer.getUserAnswer())) {

            SpannableString userAnswer = new SpannableString(
                    answer.getUserAnswer());

            if (mTestMode == SoundMode.Double) {

                // find wrong sound(s)
                String[] parcedUser = Answer.parseDouble(userAnswer.toString());
                if (parcedUser == null) {
                    holder.tvUser.setText(userAnswer);
                    return;
                }
                boolean firstWrong = !answer.getCorrectAnswer().startsWith(
                        parcedUser[0]);
                boolean secondWrong = !answer.getCorrectAnswer().endsWith(
                        parcedUser[1]);

                // change wrong sounds to red
                if (firstWrong && secondWrong) {
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
                            userAnswer.length(), 0);
                }else if (firstWrong) {
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
                            parcedUser[0].length(), 0);
                }else{
                    userAnswer.setSpan(new ForegroundColorSpan(Color.RED),
                            parcedUser[0].length(), userAnswer.length(), 0);
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
            tvNumber = (IpaTextView) itemView.findViewById(R.id.tvNumberItem);
            tvCorrect = (IpaTextView) itemView.findViewById(R.id.tvCorrectAnswerItem);
            tvUser = (IpaTextView) itemView.findViewById(R.id.tvUserAnswerItem);
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