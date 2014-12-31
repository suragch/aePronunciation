package com.aepronunciation.ipa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle mArgs = getArguments();
		String message = mArgs.getString("errorMessage");
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Help");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// You don't have to do anything here if you just want it dismissed when clicked
			}
		});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}