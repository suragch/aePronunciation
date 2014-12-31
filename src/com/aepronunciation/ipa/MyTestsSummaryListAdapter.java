package com.aepronunciation.ipa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyTestsSummaryListAdapter extends ArrayAdapter<Test> {

	// View lookup cache
	private static class ViewHolder {
		TextView tvTestNumber;
		TextView tvScore;
		TextView tvDate;
		TextView tvWrong;
	}

	public MyTestsSummaryListAdapter(Context context, ArrayList<Test> tests) {
		super(context, R.layout.listview_test_summary_item, tests);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//android.os.Debug.waitForDebugger();
		Log.i("app", "Entered test listview maker");
		
		// Get the data item for this position
		Test test = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.listview_test_summary_item, parent,
					false);
			viewHolder.tvTestNumber = (TextView) convertView
					.findViewById(R.id.tvLvTestNumber);
			viewHolder.tvScore = (TextView) convertView
					.findViewById(R.id.tvLvTestScore);
			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tvLvTestDate);
			viewHolder.tvWrong = (TextView) convertView
					.findViewById(R.id.tvLvTestWrong);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// Populate the data into the template view using the data object
		viewHolder.tvTestNumber.setText(Long.toString(test.getId()));
		viewHolder.tvScore.setText(Integer.toString(test.getScore()) + "%");
		viewHolder.tvDate.setText(convertDate(test.getDate()));
		viewHolder.tvWrong.setText(test.getWrong());
		
		// Return the completed view to render on screen
		return convertView;
	}

	private String convertDate(long unixMilliseconds) {
		
		//long unixSeconds = 1372339860;
		Date date = new Date(unixMilliseconds); 
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy'\n'h:mma", Locale.US);
		String formattedDate = sdf.format(date);
		formattedDate = formattedDate.replace("AM", "am").replace("PM","pm");
		return formattedDate;
	}
}