package com.aepronunciation.ipa;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class KeyboardFragment extends Fragment implements OnClickListener {

	protected static String KEY_11 = null;
	protected static String KEY_12 = null;
	protected static String KEY_13 = null;
	protected static String KEY_14 = null;
	protected static String KEY_15 = null;
	protected static String KEY_16 = null;
	protected static String KEY_17 = null;
	protected static String KEY_18 = null;
	protected static String KEY_21 = null;
	protected static String KEY_22 = null;
	protected static String KEY_23 = null;
	protected static String KEY_24 = null;
	protected static String KEY_25 = null;
	protected static String KEY_26 = null;
	protected static String KEY_27 = null;
	protected static String KEY_28 = null;
	protected static String KEY_31 = null;
	protected static String KEY_32 = null;
	protected static String KEY_33 = null;
	protected static String KEY_34 = null;
	protected static String KEY_35 = null;
	protected static String KEY_36 = null;
	protected static String KEY_37 = null;
	protected static String KEY_38 = null;
	protected static String KEY_41 = null;
	protected static String KEY_42 = null;
	protected static String KEY_43 = null;
	protected static String KEY_44 = null;
	protected static String KEY_45 = null;
	protected static String KEY_46 = null;
	protected static String KEY_47 = null;
	protected static String KEY_48 = null;
	protected static String KEY_51 = null;
	protected static String KEY_52 = null;
	protected static String KEY_53 = null;
	protected static String KEY_54 = null;
	protected static String KEY_55 = null;
	protected static String KEY_56 = null;
	protected static String KEY_57 = null;
	protected static String KEY_58 = null;
	protected static String KEY_61 = null;
	protected static String KEY_62 = null;
	protected static String KEY_63 = null;
	protected static String KEY_64 = null;
	protected static String KEY_65 = null;
	protected static String KEY_66 = null;
	protected static String KEY_67 = null;

	RelativeLayout rlKey11;
	RelativeLayout rlKey12;
	RelativeLayout rlKey13;
	RelativeLayout rlKey14;
	RelativeLayout rlKey15;
	RelativeLayout rlKey16;
	RelativeLayout rlKey17;
	RelativeLayout rlKey18;
	RelativeLayout rlKey21;
	RelativeLayout rlKey22;
	RelativeLayout rlKey23;
	RelativeLayout rlKey24;
	RelativeLayout rlKey25;
	RelativeLayout rlKey26;
	RelativeLayout rlKey27;
	RelativeLayout rlKey28;
	RelativeLayout rlKey31;
	RelativeLayout rlKey32;
	RelativeLayout rlKey33;
	RelativeLayout rlKey34;
	RelativeLayout rlKey35;
	RelativeLayout rlKey36;
	RelativeLayout rlKey37;
	RelativeLayout rlKey38;
	RelativeLayout rlKey41;
	RelativeLayout rlKey42;
	RelativeLayout rlKey43;
	RelativeLayout rlKey44;
	RelativeLayout rlKey45;
	RelativeLayout rlKey46;
	RelativeLayout rlKey47;
	RelativeLayout rlKey48;
	RelativeLayout rlKey51;
	RelativeLayout rlKey52;
	RelativeLayout rlKey53;
	RelativeLayout rlKey54;
	RelativeLayout rlKey55;
	RelativeLayout rlKey56;
	RelativeLayout rlKey57;
	RelativeLayout rlKey58;
	RelativeLayout rlKey61;
	RelativeLayout rlKey62;
	RelativeLayout rlKey63;
	RelativeLayout rlKey64;
	RelativeLayout rlKey65;
	RelativeLayout rlKey66;
	RelativeLayout rlKey67;

	Communicator mCallback; // communicate with the parent activity

	// Container Activity must implement this interface
	public interface Communicator {

		public void onKeyTouched(String keyString);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize keyboard key values
		KEY_11 = getResources().getString(R.string.key_11);
		KEY_12 = getResources().getString(R.string.key_12);
		KEY_13 = getResources().getString(R.string.key_13);
		KEY_14 = getResources().getString(R.string.key_14);
		KEY_15 = getResources().getString(R.string.key_15);
		KEY_16 = getResources().getString(R.string.key_16);
		KEY_17 = getResources().getString(R.string.key_17);
		KEY_18 = getResources().getString(R.string.key_18);
		KEY_21 = getResources().getString(R.string.key_21);
		KEY_22 = getResources().getString(R.string.key_22);
		KEY_23 = getResources().getString(R.string.key_23);
		KEY_24 = getResources().getString(R.string.key_24);
		KEY_25 = getResources().getString(R.string.key_25);
		KEY_26 = getResources().getString(R.string.key_26);
		KEY_27 = getResources().getString(R.string.key_27);
		KEY_28 = getResources().getString(R.string.key_28);
		KEY_31 = getResources().getString(R.string.key_31);
		KEY_32 = getResources().getString(R.string.key_32);
		KEY_33 = getResources().getString(R.string.key_33);
		KEY_34 = getResources().getString(R.string.key_34);
		KEY_35 = getResources().getString(R.string.key_35);
		KEY_36 = getResources().getString(R.string.key_36);
		KEY_37 = getResources().getString(R.string.key_37);
		KEY_38 = getResources().getString(R.string.key_38);
		KEY_41 = getResources().getString(R.string.key_41);
		KEY_42 = getResources().getString(R.string.key_42);
		KEY_43 = getResources().getString(R.string.key_43);
		KEY_44 = getResources().getString(R.string.key_44);
		KEY_45 = getResources().getString(R.string.key_45);
		KEY_46 = getResources().getString(R.string.key_46);
		KEY_47 = getResources().getString(R.string.key_47);
		KEY_48 = getResources().getString(R.string.key_48);
		KEY_51 = getResources().getString(R.string.key_51);
		KEY_52 = getResources().getString(R.string.key_52);
		KEY_53 = getResources().getString(R.string.key_53);
		KEY_54 = getResources().getString(R.string.key_54);
		KEY_55 = getResources().getString(R.string.key_55);
		KEY_56 = getResources().getString(R.string.key_56);
		KEY_57 = getResources().getString(R.string.key_57);
		KEY_58 = getResources().getString(R.string.key_58);
		KEY_61 = getResources().getString(R.string.key_61);
		KEY_62 = getResources().getString(R.string.key_62);
		KEY_63 = getResources().getString(R.string.key_63);
		KEY_64 = getResources().getString(R.string.key_64);
		KEY_65 = getResources().getString(R.string.key_65);
		KEY_66 = getResources().getString(R.string.key_66);
		KEY_67 = getResources().getString(R.string.key_67);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_keyboard, container, false);

		// Add listeners for all keys
		rlKey11 = (RelativeLayout) v.findViewById(R.id.key_11);
		rlKey11.setOnClickListener(this);
		rlKey12 = (RelativeLayout) v.findViewById(R.id.key_12);
		rlKey12.setOnClickListener(this);
		rlKey13 = (RelativeLayout) v.findViewById(R.id.key_13);
		rlKey13.setOnClickListener(this);
		rlKey14 = (RelativeLayout) v.findViewById(R.id.key_14);
		rlKey14.setOnClickListener(this);
		rlKey15 = (RelativeLayout) v.findViewById(R.id.key_15);
		rlKey15.setOnClickListener(this);
		rlKey16 = (RelativeLayout) v.findViewById(R.id.key_16);
		rlKey16.setOnClickListener(this);
		rlKey17 = (RelativeLayout) v.findViewById(R.id.key_17);
		rlKey17.setOnClickListener(this);
		rlKey18 = (RelativeLayout) v.findViewById(R.id.key_18);
		rlKey18.setOnClickListener(this);
		rlKey21 = (RelativeLayout) v.findViewById(R.id.key_21);
		rlKey21.setOnClickListener(this);
		rlKey22 = (RelativeLayout) v.findViewById(R.id.key_22);
		rlKey22.setOnClickListener(this);
		rlKey23 = (RelativeLayout) v.findViewById(R.id.key_23);
		rlKey23.setOnClickListener(this);
		rlKey24 = (RelativeLayout) v.findViewById(R.id.key_24);
		rlKey24.setOnClickListener(this);
		rlKey25 = (RelativeLayout) v.findViewById(R.id.key_25);
		rlKey25.setOnClickListener(this);
		rlKey26 = (RelativeLayout) v.findViewById(R.id.key_26);
		rlKey26.setOnClickListener(this);
		rlKey27 = (RelativeLayout) v.findViewById(R.id.key_27);
		rlKey27.setOnClickListener(this);
		rlKey28 = (RelativeLayout) v.findViewById(R.id.key_28);
		rlKey28.setOnClickListener(this);
		rlKey31 = (RelativeLayout) v.findViewById(R.id.key_31);
		rlKey31.setOnClickListener(this);
		rlKey32 = (RelativeLayout) v.findViewById(R.id.key_32);
		rlKey32.setOnClickListener(this);
		rlKey33 = (RelativeLayout) v.findViewById(R.id.key_33);
		rlKey33.setOnClickListener(this);
		rlKey34 = (RelativeLayout) v.findViewById(R.id.key_34);
		rlKey34.setOnClickListener(this);
		rlKey35 = (RelativeLayout) v.findViewById(R.id.key_35);
		rlKey35.setOnClickListener(this);
		rlKey36 = (RelativeLayout) v.findViewById(R.id.key_36);
		rlKey36.setOnClickListener(this);
		rlKey37 = (RelativeLayout) v.findViewById(R.id.key_37);
		rlKey37.setOnClickListener(this);
		rlKey38 = (RelativeLayout) v.findViewById(R.id.key_38);
		rlKey38.setOnClickListener(this);
		rlKey41 = (RelativeLayout) v.findViewById(R.id.key_41);
		rlKey41.setOnClickListener(this);
		rlKey42 = (RelativeLayout) v.findViewById(R.id.key_42);
		rlKey42.setOnClickListener(this);
		rlKey43 = (RelativeLayout) v.findViewById(R.id.key_43);
		rlKey43.setOnClickListener(this);
		rlKey44 = (RelativeLayout) v.findViewById(R.id.key_44);
		rlKey44.setOnClickListener(this);
		rlKey45 = (RelativeLayout) v.findViewById(R.id.key_45);
		rlKey45.setOnClickListener(this);
		rlKey46 = (RelativeLayout) v.findViewById(R.id.key_46);
		rlKey46.setOnClickListener(this);
		rlKey47 = (RelativeLayout) v.findViewById(R.id.key_47);
		rlKey47.setOnClickListener(this);
		rlKey48 = (RelativeLayout) v.findViewById(R.id.key_48);
		rlKey48.setOnClickListener(this);
		rlKey51 = (RelativeLayout) v.findViewById(R.id.key_51);
		rlKey51.setOnClickListener(this);
		rlKey52 = (RelativeLayout) v.findViewById(R.id.key_52);
		rlKey52.setOnClickListener(this);
		rlKey53 = (RelativeLayout) v.findViewById(R.id.key_53);
		rlKey53.setOnClickListener(this);
		rlKey54 = (RelativeLayout) v.findViewById(R.id.key_54);
		rlKey54.setOnClickListener(this);
		rlKey55 = (RelativeLayout) v.findViewById(R.id.key_55);
		rlKey55.setOnClickListener(this);
		rlKey56 = (RelativeLayout) v.findViewById(R.id.key_56);
		rlKey56.setOnClickListener(this);
		rlKey57 = (RelativeLayout) v.findViewById(R.id.key_57);
		rlKey57.setOnClickListener(this);
		rlKey58 = (RelativeLayout) v.findViewById(R.id.key_58);
		rlKey58.setOnClickListener(this);
		rlKey61 = (RelativeLayout) v.findViewById(R.id.key_61);
		rlKey61.setOnClickListener(this);
		rlKey62 = (RelativeLayout) v.findViewById(R.id.key_62);
		rlKey62.setOnClickListener(this);
		rlKey63 = (RelativeLayout) v.findViewById(R.id.key_63);
		rlKey63.setOnClickListener(this);
		rlKey64 = (RelativeLayout) v.findViewById(R.id.key_64);
		rlKey64.setOnClickListener(this);
		rlKey65 = (RelativeLayout) v.findViewById(R.id.key_65);
		rlKey65.setOnClickListener(this);
		rlKey66 = (RelativeLayout) v.findViewById(R.id.key_66);
		rlKey66.setOnClickListener(this);
		rlKey67 = (RelativeLayout) v.findViewById(R.id.key_67);
		rlKey67.setOnClickListener(this);

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (Communicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Communicator");
		}
	}

	@Override
	public void onClick(View v) {

		String key = "";

		// Send the event to the host activity
		switch (v.getId()) {
		case R.id.key_11:
			key = KEY_11;
			break;
		case R.id.key_12:
			key = KEY_12;
			break;
		case R.id.key_13:
			key = KEY_13;
			break;
		case R.id.key_14:
			key = KEY_14;
			break;
		case R.id.key_15:
			key = KEY_15;
			break;
		case R.id.key_16:
			key = KEY_16;
			break;
		case R.id.key_17:
			key = KEY_17;
			break;
		case R.id.key_18:
			key = KEY_18;
			break;
		case R.id.key_21:
			key = KEY_21;
			break;
		case R.id.key_22:
			key = KEY_22;
			break;
		case R.id.key_23:
			key = KEY_23;
			break;
		case R.id.key_24:
			key = KEY_24;
			break;
		case R.id.key_25:
			key = KEY_25;
			break;
		case R.id.key_26:
			key = KEY_26;
			break;
		case R.id.key_27:
			key = KEY_27;
			break;
		case R.id.key_28:
			key = KEY_28;
			break;
		case R.id.key_31:
			key = KEY_31;
			break;
		case R.id.key_32:
			key = KEY_32;
			break;
		case R.id.key_33:
			key = KEY_33;
			break;
		case R.id.key_34:
			key = KEY_34;
			break;
		case R.id.key_35:
			key = KEY_35;
			break;
		case R.id.key_36:
			key = KEY_36;
			break;
		case R.id.key_37:
			key = KEY_37;
			break;
		case R.id.key_38:
			key = KEY_38;
			break;
		case R.id.key_41:
			key = KEY_41;
			break;
		case R.id.key_42:
			key = KEY_42;
			break;
		case R.id.key_43:
			key = KEY_43;
			break;
		case R.id.key_44:
			key = KEY_44;
			break;
		case R.id.key_45:
			key = KEY_45;
			break;
		case R.id.key_46:
			key = KEY_46;
			break;
		case R.id.key_47:
			key = KEY_47;
			break;
		case R.id.key_48:
			key = KEY_48;
			break;
		case R.id.key_51:
			key = KEY_51;
			break;
		case R.id.key_52:
			key = KEY_52;
			break;
		case R.id.key_53:
			key = KEY_53;
			break;
		case R.id.key_54:
			key = KEY_54;
			break;
		case R.id.key_55:
			key = KEY_55;
			break;
		case R.id.key_56:
			key = KEY_56;
			break;
		case R.id.key_57:
			key = KEY_57;
			break;
		case R.id.key_58:
				key = KEY_58;
			break;
		case R.id.key_61:
			key = KEY_61;
			break;
		case R.id.key_62:
			key = KEY_62;
			break;
		case R.id.key_63:
			key = KEY_63;
			break;
		case R.id.key_64:
			key = KEY_64;
			break;
		case R.id.key_65:
			key = KEY_65;
			break;
		case R.id.key_66:
			key = KEY_66;
			break;
		case R.id.key_67:
				key = KEY_67;
			break;
		default:
			//Log.e("AeP", "key error");
		}

		mCallback.onKeyTouched(key);

	}

	public void hideConsonants() {
		rlKey11.setVisibility(View.INVISIBLE);
		rlKey12.setVisibility(View.INVISIBLE);
		rlKey13.setVisibility(View.INVISIBLE);
		rlKey14.setVisibility(View.INVISIBLE);
		rlKey15.setVisibility(View.INVISIBLE);
		rlKey16.setVisibility(View.INVISIBLE);
		rlKey17.setVisibility(View.INVISIBLE);
		rlKey18.setVisibility(View.INVISIBLE);
		rlKey21.setVisibility(View.INVISIBLE);
		rlKey22.setVisibility(View.INVISIBLE);
		rlKey23.setVisibility(View.INVISIBLE);
		rlKey24.setVisibility(View.INVISIBLE);
		rlKey25.setVisibility(View.INVISIBLE);
		rlKey26.setVisibility(View.INVISIBLE);
		rlKey27.setVisibility(View.INVISIBLE);
		rlKey28.setVisibility(View.INVISIBLE);
		rlKey31.setVisibility(View.INVISIBLE);
		rlKey32.setVisibility(View.INVISIBLE);
		rlKey33.setVisibility(View.INVISIBLE);
		rlKey34.setVisibility(View.INVISIBLE);
		rlKey35.setVisibility(View.INVISIBLE);
		rlKey36.setVisibility(View.INVISIBLE);
		rlKey37.setVisibility(View.INVISIBLE);
		rlKey38.setVisibility(View.INVISIBLE);
	}

	public void hideVowels() {
		rlKey41.setVisibility(View.INVISIBLE);
		rlKey42.setVisibility(View.INVISIBLE);
		rlKey43.setVisibility(View.INVISIBLE);
		rlKey44.setVisibility(View.INVISIBLE);
		rlKey45.setVisibility(View.INVISIBLE);
		rlKey46.setVisibility(View.INVISIBLE);
		rlKey47.setVisibility(View.INVISIBLE);
		rlKey48.setVisibility(View.INVISIBLE);
		rlKey51.setVisibility(View.INVISIBLE);
		rlKey52.setVisibility(View.INVISIBLE);
		rlKey53.setVisibility(View.INVISIBLE);
		rlKey54.setVisibility(View.INVISIBLE);
		rlKey55.setVisibility(View.INVISIBLE);
		rlKey56.setVisibility(View.INVISIBLE);
		rlKey57.setVisibility(View.INVISIBLE);
		rlKey61.setVisibility(View.INVISIBLE);
		rlKey62.setVisibility(View.INVISIBLE);
		rlKey63.setVisibility(View.INVISIBLE);
		rlKey64.setVisibility(View.INVISIBLE);
		rlKey65.setVisibility(View.INVISIBLE);
		rlKey66.setVisibility(View.INVISIBLE);
	}

	public void showConsonants() {
		rlKey11.setVisibility(View.VISIBLE);
		rlKey12.setVisibility(View.VISIBLE);
		rlKey13.setVisibility(View.VISIBLE);
		rlKey14.setVisibility(View.VISIBLE);
		rlKey15.setVisibility(View.VISIBLE);
		rlKey16.setVisibility(View.VISIBLE);
		rlKey17.setVisibility(View.VISIBLE);
		rlKey18.setVisibility(View.VISIBLE);
		rlKey21.setVisibility(View.VISIBLE);
		rlKey22.setVisibility(View.VISIBLE);
		rlKey23.setVisibility(View.VISIBLE);
		rlKey24.setVisibility(View.VISIBLE);
		rlKey25.setVisibility(View.VISIBLE);
		rlKey26.setVisibility(View.VISIBLE);
		rlKey27.setVisibility(View.VISIBLE);
		rlKey28.setVisibility(View.VISIBLE);
		rlKey31.setVisibility(View.VISIBLE);
		rlKey32.setVisibility(View.VISIBLE);
		rlKey33.setVisibility(View.VISIBLE);
		rlKey34.setVisibility(View.VISIBLE);
		rlKey35.setVisibility(View.VISIBLE);
		rlKey36.setVisibility(View.VISIBLE);
		rlKey37.setVisibility(View.VISIBLE);
		rlKey38.setVisibility(View.VISIBLE);
	}

	public void showVowels() {
		rlKey41.setVisibility(View.VISIBLE);
		rlKey42.setVisibility(View.VISIBLE);
		rlKey43.setVisibility(View.VISIBLE);
		rlKey44.setVisibility(View.VISIBLE);
		rlKey45.setVisibility(View.VISIBLE);
		rlKey46.setVisibility(View.VISIBLE);
		rlKey47.setVisibility(View.VISIBLE);
		rlKey48.setVisibility(View.VISIBLE);
		rlKey51.setVisibility(View.VISIBLE);
		rlKey52.setVisibility(View.VISIBLE);
		rlKey53.setVisibility(View.VISIBLE);
		rlKey54.setVisibility(View.VISIBLE);
		rlKey55.setVisibility(View.VISIBLE);
		rlKey56.setVisibility(View.VISIBLE);
		rlKey57.setVisibility(View.VISIBLE);
		rlKey61.setVisibility(View.VISIBLE);
		rlKey62.setVisibility(View.VISIBLE);
		rlKey63.setVisibility(View.VISIBLE);
		rlKey64.setVisibility(View.VISIBLE);
		rlKey65.setVisibility(View.VISIBLE);
		rlKey66.setVisibility(View.VISIBLE);
	}

	public void hideFunctionKeys() {
		rlKey58.setVisibility(View.GONE);
		rlKey67.setVisibility(View.GONE);

	}

	public void showFunctionKeys() {
		rlKey58.setVisibility(View.VISIBLE);
		rlKey67.setVisibility(View.VISIBLE);
	}

	public void hideUnstressedVowels() {
		rlKey52.setVisibility(View.GONE);
		rlKey62.setVisibility(View.GONE);

	}

	public void showUnstressedVowels() {
		rlKey52.setVisibility(View.VISIBLE);
		rlKey62.setVisibility(View.VISIBLE);
	}

}
