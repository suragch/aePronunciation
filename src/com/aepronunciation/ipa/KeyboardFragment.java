package com.aepronunciation.ipa;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyboardFragment extends Fragment implements OnClickListener {
    static final int KEY_VOWEL = 1;
    static final int KEY_CONSONANT = 2;
    static final int KEY_FUNCTION = 4;
    static final int KEY_UNSTRESSED = 8;

    private class MapEntry {
        public int view_id;
        public String code;
        public int flags;

        public MapEntry(int id, String code, int flags) {
            this.view_id = id;
            this.code = code;
            this.flags = flags;
        }
    }

    private HashMap<Integer, MapEntry> mKeys;

    Communicator mCallback; // communicate with the parent activity

    ArrayList<RelativeLayout> x1Consonants;
    ArrayList<RelativeLayout> x1Vowels;
    ArrayList<RelativeLayout> x1Functions;
    ArrayList<RelativeLayout> x1Unstressed;
    boolean selectMode;

    // Container Activity must implement this interface
    public interface Communicator {

        public void onKeyTouched(String keyString);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources r = getResources();

        // Initialize keyboard key values
        mKeys = new HashMap<Integer, MapEntry>();
        mKeys.put(R.id.key_11, new MapEntry(R.id.key_11, r.getString(R.string.key_11), KEY_CONSONANT));
        mKeys.put(R.id.key_12, new MapEntry(R.id.key_12, r.getString(R.string.key_12), KEY_CONSONANT));
        mKeys.put(R.id.key_13, new MapEntry(R.id.key_13, r.getString(R.string.key_13), KEY_CONSONANT));
        mKeys.put(R.id.key_14, new MapEntry(R.id.key_14, r.getString(R.string.key_14), KEY_CONSONANT));
        mKeys.put(R.id.key_15, new MapEntry(R.id.key_15, r.getString(R.string.key_15), KEY_CONSONANT));
        mKeys.put(R.id.key_16, new MapEntry(R.id.key_16, r.getString(R.string.key_16), KEY_CONSONANT));
        mKeys.put(R.id.key_17, new MapEntry(R.id.key_17, r.getString(R.string.key_17), KEY_CONSONANT));
        mKeys.put(R.id.key_18, new MapEntry(R.id.key_18, r.getString(R.string.key_18), KEY_CONSONANT));
        mKeys.put(R.id.key_21, new MapEntry(R.id.key_21, r.getString(R.string.key_21), KEY_CONSONANT));
        mKeys.put(R.id.key_22, new MapEntry(R.id.key_22, r.getString(R.string.key_22), KEY_CONSONANT));
        mKeys.put(R.id.key_23, new MapEntry(R.id.key_23, r.getString(R.string.key_23), KEY_CONSONANT));
        mKeys.put(R.id.key_24, new MapEntry(R.id.key_24, r.getString(R.string.key_24), KEY_CONSONANT));
        mKeys.put(R.id.key_25, new MapEntry(R.id.key_25, r.getString(R.string.key_25), KEY_CONSONANT));
        mKeys.put(R.id.key_26, new MapEntry(R.id.key_26, r.getString(R.string.key_26), KEY_CONSONANT));
        mKeys.put(R.id.key_27, new MapEntry(R.id.key_27, r.getString(R.string.key_27), KEY_CONSONANT));
        mKeys.put(R.id.key_28, new MapEntry(R.id.key_28, r.getString(R.string.key_28), KEY_CONSONANT));
        mKeys.put(R.id.key_31, new MapEntry(R.id.key_31, r.getString(R.string.key_31), KEY_CONSONANT));
        mKeys.put(R.id.key_32, new MapEntry(R.id.key_32, r.getString(R.string.key_32), KEY_CONSONANT));
        mKeys.put(R.id.key_33, new MapEntry(R.id.key_33, r.getString(R.string.key_33), KEY_CONSONANT));
        mKeys.put(R.id.key_34, new MapEntry(R.id.key_34, r.getString(R.string.key_34), KEY_CONSONANT));
        mKeys.put(R.id.key_35, new MapEntry(R.id.key_35, r.getString(R.string.key_35), KEY_CONSONANT));
        mKeys.put(R.id.key_36, new MapEntry(R.id.key_36, r.getString(R.string.key_36), KEY_CONSONANT));
        mKeys.put(R.id.key_37, new MapEntry(R.id.key_37, r.getString(R.string.key_37), KEY_CONSONANT));
        mKeys.put(R.id.key_38, new MapEntry(R.id.key_38, r.getString(R.string.key_38), KEY_CONSONANT));

        mKeys.put(R.id.key_41, new MapEntry(R.id.key_41, r.getString(R.string.key_41), KEY_VOWEL));
        mKeys.put(R.id.key_42, new MapEntry(R.id.key_42, r.getString(R.string.key_42), KEY_VOWEL));
        mKeys.put(R.id.key_43, new MapEntry(R.id.key_43, r.getString(R.string.key_43), KEY_VOWEL));
        mKeys.put(R.id.key_44, new MapEntry(R.id.key_44, r.getString(R.string.key_44), KEY_VOWEL));
        mKeys.put(R.id.key_45, new MapEntry(R.id.key_45, r.getString(R.string.key_45), KEY_VOWEL));
        mKeys.put(R.id.key_46, new MapEntry(R.id.key_46, r.getString(R.string.key_46), KEY_VOWEL));
        mKeys.put(R.id.key_47, new MapEntry(R.id.key_47, r.getString(R.string.key_47), KEY_VOWEL));
        mKeys.put(R.id.key_48, new MapEntry(R.id.key_48, r.getString(R.string.key_48), KEY_VOWEL));
        mKeys.put(R.id.key_51, new MapEntry(R.id.key_51, r.getString(R.string.key_51), KEY_VOWEL));
        mKeys.put(R.id.key_52, new MapEntry(R.id.key_52, r.getString(R.string.key_52), KEY_VOWEL | KEY_UNSTRESSED));
        mKeys.put(R.id.key_53, new MapEntry(R.id.key_53, r.getString(R.string.key_53), KEY_VOWEL));
        mKeys.put(R.id.key_54, new MapEntry(R.id.key_54, r.getString(R.string.key_54), KEY_VOWEL));
        mKeys.put(R.id.key_55, new MapEntry(R.id.key_55, r.getString(R.string.key_55), KEY_VOWEL));
        mKeys.put(R.id.key_56, new MapEntry(R.id.key_56, r.getString(R.string.key_56), KEY_VOWEL));
        mKeys.put(R.id.key_57, new MapEntry(R.id.key_57, r.getString(R.string.key_57), KEY_VOWEL));
        mKeys.put(R.id.key_61, new MapEntry(R.id.key_61, r.getString(R.string.key_61), KEY_VOWEL));
        mKeys.put(R.id.key_62, new MapEntry(R.id.key_62, r.getString(R.string.key_62), KEY_VOWEL | KEY_UNSTRESSED));
        mKeys.put(R.id.key_63, new MapEntry(R.id.key_63, r.getString(R.string.key_63), KEY_VOWEL));
        mKeys.put(R.id.key_64, new MapEntry(R.id.key_64, r.getString(R.string.key_64), KEY_VOWEL));
        mKeys.put(R.id.key_65, new MapEntry(R.id.key_65, r.getString(R.string.key_65), KEY_VOWEL));
        mKeys.put(R.id.key_66, new MapEntry(R.id.key_66, r.getString(R.string.key_66), KEY_VOWEL));

        mKeys.put(R.id.key_58, new MapEntry(R.id.key_58, r.getString(R.string.key_58), KEY_FUNCTION));
        mKeys.put(R.id.key_67, new MapEntry(R.id.key_67, r.getString(R.string.key_67), KEY_FUNCTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_keyboard, container, false);
        x1Consonants = new ArrayList<RelativeLayout>();
        x1Vowels = new ArrayList<RelativeLayout>();
        x1Functions = new ArrayList<RelativeLayout>();
        x1Unstressed = new ArrayList<RelativeLayout>();

        // Add listeners for all keys
        for (MapEntry k : mKeys.values()) {
            RelativeLayout x = (RelativeLayout) v.findViewById(k.view_id);
            x.setOnClickListener(this);
            if ((k.flags & KEY_CONSONANT) != 0) {
                x1Consonants.add(x);
            }
            if ((k.flags & KEY_VOWEL) != 0) {
                x1Vowels.add(x);
            }
            if ((k.flags & KEY_FUNCTION) != 0) {
                x1Functions.add(x);
            }
            if ((k.flags & KEY_UNSTRESSED) != 0) {
                x1Unstressed.add(x);
            }
        }

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
        // Toggle selection
        if (isSelectMode()) {
            v.setSelected(!v.isSelected());
            v.refreshDrawableState();
        }

        // Send the event to the host activity
        String key = "";
        key = getKeyByViewId(v.getId());
        mCallback.onKeyTouched(key);
    }

    public void hideConsonants() {
        for (int i = 0; i < x1Consonants.size(); i++) {
            x1Consonants.get(i).setVisibility(View.INVISIBLE);
        }
    }

    public void hideVowels() {
        for (int i = 0; i < x1Vowels.size(); i++) {
            x1Vowels.get(i).setVisibility(View.INVISIBLE);
        }
    }

    public void showConsonants() {
        for (int i = 0; i < x1Consonants.size(); i++) {
            x1Consonants.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void showVowels() {
        for (int i = 0; i < x1Vowels.size(); i++) {
            x1Vowels.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void hideFunctionKeys() {
        for (RelativeLayout v : x1Functions) {
            v.setVisibility(View.GONE);
        }
    }

    public void showFunctionKeys() {
        for (RelativeLayout v : x1Functions) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public void hideUnstressedVowels() {
        for (RelativeLayout v : x1Unstressed) {
            v.setVisibility(View.GONE);
        }
    }

    public void showUnstressedVowels() {
        for (RelativeLayout v : x1Unstressed) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public void setSoundsSelected(ArrayList<String> sounds, boolean selected) {
        for (int i = 0; i < x1Consonants.size(); i++) {
            RelativeLayout v = x1Consonants.get(i);
            if (sounds.contains(getKeyByViewId(v.getId()))) {
                v.setSelected(selected);
            }
        }

        for (int i = 0; i < x1Vowels.size(); i++) {
            RelativeLayout v = x1Vowels.get(i);
            if (sounds.contains(getKeyByViewId(v.getId()))) {
                v.setSelected(selected);
            }
        }
    }

    public ArrayList<String> getSelectedSounds() {
        ArrayList<String> selected = new ArrayList<String>();
        selected.addAll(collectSelectedSounds(x1Vowels));
        selected.addAll(collectSelectedSounds(x1Consonants));
        return selected;
    }

    public ArrayList<String> getAllSounds() {
        ArrayList<String> result = new ArrayList<String>();
        for (RelativeLayout key : x1Vowels) {
            result.add(getKeyByViewId(key.getId()));
        }
        for (RelativeLayout key : x1Consonants) {
            result.add(getKeyByViewId(key.getId()));
        }
        return result;
    }

    public void setSelectMode(boolean mode) {
        selectMode = mode;
    }

    private boolean isSelectMode() {
        return selectMode;
    }

    private ArrayList<String> collectSelectedSounds(ArrayList<RelativeLayout> keys) {
        ArrayList<String> result = new ArrayList<String>();
        for (RelativeLayout key : keys) {
            if (!key.isSelected()) {
                continue;
            }
            String s = getKeyByViewId(key.getId());
            if (s.isEmpty()) {
                continue;
            }
            result.add(s);
        }
        return result;
    }

    private String getKeyByViewId(int id) {
        String key = "";
        MapEntry entry = mKeys.get(id);
        if (entry != null) {
            key = entry.code;
        }
        return key;
    }

    public void showKeysInList(ArrayList<String> keys) {
        for (RelativeLayout v : x1Vowels) {
            v.setVisibility(View.INVISIBLE);
        }
        for (RelativeLayout v : x1Consonants) {
            v.setVisibility(View.INVISIBLE);
        }
        for (String k : keys) {
            for (RelativeLayout v : x1Vowels) {
                if (k.equals(getKeyByViewId(v.getId()))) {
                    v.setVisibility(View.VISIBLE);
                }
            }
            for (RelativeLayout v : x1Consonants) {
                if (k.equals(getKeyByViewId(v.getId()))) {
                    v.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
