package com.aepronunciation.ipa;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyTestDetailsListAdapter extends ArrayAdapter<Answer> {

	private String testMode;
	
	// View lookup cache
	private static class ViewHolder {
		TextView tvNumber;
		TextView tvCorrect;
		TextView tvUser;
	}

	public MyTestDetailsListAdapter(Context context, ArrayList<Answer> answers, String testMode) {
		super(context, R.layout.listview_item, answers);
		this.testMode=testMode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for this position
		Answer answer = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.listview_item, parent,
					false);
			viewHolder.tvNumber = (TextView) convertView
					.findViewById(R.id.tvNumberItem);
			viewHolder.tvCorrect = (TextView) convertView
					.findViewById(R.id.tvCorrectAnswerItem);
			viewHolder.tvUser = (TextView) convertView
					.findViewById(R.id.tvUserAnswerItem);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// Populate the data into the template view using the data object
		viewHolder.tvNumber.setText(Integer.toString(position + 1));
		viewHolder.tvCorrect.setText(answer.getCorrectAnswer());
		if (!answer.getCorrectAnswer().equals(answer.getUserAnswer())) {
			
			SpannableString userAnswer = new SpannableString(
						answer.getUserAnswer());

			if (testMode.equals("double")) {
				
				// find wrong sound(s)
				String[] parcedUser = Answer.parseDouble(userAnswer.toString());
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
				
				viewHolder.tvUser.setText(userAnswer);
				
			} else {
				userAnswer.setSpan(new ForegroundColorSpan(Color.RED), 0,
						userAnswer.length(), 0);
				viewHolder.tvUser.setText(userAnswer);
			}

		}else{
			viewHolder.tvUser.setText("");
		}
		

		// Return the completed view to render on screen
		return convertView;
	}
}