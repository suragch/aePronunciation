package com.aepronunciation.ipa;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


// This could be an inner class if it is only used by one activity

class MyGridAdapter extends ArrayAdapter<String> {

	private Context mContext;
	private String[] sounds;

	MyGridAdapter(Context c, String[] doubleSoundArray) {
		super(c, R.layout.grid_item, R.id.tvGridDoubleSound, doubleSoundArray);
		this.mContext = c;
		this.sounds = doubleSoundArray;
	}

	class MyViewHolder {
		// This is an optimization to avoid expensive findViewById calls
		TextView tvSounds;

		MyViewHolder(View v) {
			tvSounds = (TextView) v.findViewById(R.id.tvGridDoubleSound);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MyViewHolder holder = null;

		if (row == null) {
			// Expensive LayoutInflator calls only done once
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.grid_item, parent, false);
			holder = new MyViewHolder(row);
			row.setTag(holder);
		} else { // recycling
			holder = (MyViewHolder) row.getTag();
		}

		holder.tvSounds.setText(sounds[position]);

		return row;
	}
}