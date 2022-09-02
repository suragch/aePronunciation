package com.aepronunciation.ipa;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;

public class KeyboardFragment extends Fragment implements OnClickListener {

    View view;

    private HashMap<Integer, String> mLayoutToString;
    private HashMap<Integer, String> mTextViewToString;

    private KeyboardListener mListener; // communicate with the parent activity

    // Container Activity must implement this interface
    public interface KeyboardListener {
        void onKeyTouched(String keyString);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeMaps();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_keyboard, container, false);

        if (getArguments() != null) {
            SoundMode testMode = SoundMode.fromString(getArguments().getString(TEST_MODE_KEY));
            updateKeysFor(testMode);
        }

        // Add listeners for all keys
        for (int id : mLayoutToString.keySet()) {
            RelativeLayout rlKey = view.findViewById(id);
            rlKey.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // check if parent Fragment implements listener
        if (getParentFragment() instanceof KeyboardListener) {
            mListener = (KeyboardListener) getParentFragment();
        } else if (getActivity() instanceof KeyboardListener) {
            mListener = (KeyboardListener) getActivity();
        } else {
            throw new RuntimeException("Parent fragment or activity must implement KeyboardListener");
        }
    }

    private void initializeMaps() {

        // Initialize keyboard key values
        mLayoutToString = new HashMap<>();

        mLayoutToString.put(R.id.key_p, getString(R.string.key_p));
        mLayoutToString.put(R.id.key_t, getString(R.string.key_t));
        mLayoutToString.put(R.id.key_k, getString(R.string.key_k));
        mLayoutToString.put(R.id.key_ch, getString(R.string.key_ch));
        mLayoutToString.put(R.id.key_f, getString(R.string.key_f));
        mLayoutToString.put(R.id.key_th_voiceless, getString(R.string.key_th_voiceless));
        mLayoutToString.put(R.id.key_s, getString(R.string.key_s));
        mLayoutToString.put(R.id.key_sh, getString(R.string.key_sh));
        mLayoutToString.put(R.id.key_b, getString(R.string.key_b));
        mLayoutToString.put(R.id.key_d, getString(R.string.key_d));
        mLayoutToString.put(R.id.key_g, getString(R.string.key_g));
        mLayoutToString.put(R.id.key_dzh, getString(R.string.key_dzh));
        mLayoutToString.put(R.id.key_v, getString(R.string.key_v));
        mLayoutToString.put(R.id.key_th_voiced, getString(R.string.key_th_voiced));
        mLayoutToString.put(R.id.key_z, getString(R.string.key_z));
        mLayoutToString.put(R.id.key_zh, getString(R.string.key_zh));
        mLayoutToString.put(R.id.key_m, getString(R.string.key_m));
        mLayoutToString.put(R.id.key_n, getString(R.string.key_n));
        mLayoutToString.put(R.id.key_ng, getString(R.string.key_ng));
        mLayoutToString.put(R.id.key_l, getString(R.string.key_l));
        mLayoutToString.put(R.id.key_w, getString(R.string.key_w));
        mLayoutToString.put(R.id.key_j, getString(R.string.key_j));
        mLayoutToString.put(R.id.key_h, getString(R.string.key_h));
        mLayoutToString.put(R.id.key_r, getString(R.string.key_r));
        mLayoutToString.put(R.id.key_flap_t, getString(R.string.key_flap_t));
        mLayoutToString.put(R.id.key_glottal_stop, getString(R.string.key_glottal_stop));

        mLayoutToString.put(R.id.key_i, getString(R.string.key_i));
        mLayoutToString.put(R.id.key_i_short, getString(R.string.key_i_short));
        mLayoutToString.put(R.id.key_e_short, getString(R.string.key_e_short));
        mLayoutToString.put(R.id.key_ae, getString(R.string.key_ae));
        mLayoutToString.put(R.id.key_a, getString(R.string.key_a));
        mLayoutToString.put(R.id.key_c_backwards, getString(R.string.key_c_backwards));
        mLayoutToString.put(R.id.key_u_short, getString(R.string.key_u_short));
        mLayoutToString.put(R.id.key_u, getString(R.string.key_u));
        mLayoutToString.put(R.id.key_v_upsidedown, getString(R.string.key_v_upsidedown));
        mLayoutToString.put(R.id.key_shwua, getString(R.string.key_shwua));
        mLayoutToString.put(R.id.key_ei, getString(R.string.key_ei));
        mLayoutToString.put(R.id.key_ai, getString(R.string.key_ai));
        mLayoutToString.put(R.id.key_au, getString(R.string.key_au));
        mLayoutToString.put(R.id.key_oi, getString(R.string.key_oi));
        mLayoutToString.put(R.id.key_ou, getString(R.string.key_ou));
        mLayoutToString.put(R.id.key_er_stressed, getString(R.string.key_er_stressed));
        mLayoutToString.put(R.id.key_er_unstressed, getString(R.string.key_er_unstressed));
        mLayoutToString.put(R.id.key_ar, getString(R.string.key_ar));
        mLayoutToString.put(R.id.key_er, getString(R.string.key_er));
        mLayoutToString.put(R.id.key_ir, getString(R.string.key_ir));
        mLayoutToString.put(R.id.key_or, getString(R.string.key_or));



        mTextViewToString = new HashMap<>();

        mTextViewToString.put(R.id.tvKey_p, getString(R.string.key_p));
        mTextViewToString.put(R.id.tvKey_t, getString(R.string.key_t));
        mTextViewToString.put(R.id.tvKey_k, getString(R.string.key_k));
        mTextViewToString.put(R.id.tvKey_ch, getString(R.string.key_ch));
        mTextViewToString.put(R.id.tvKey_f, getString(R.string.key_f));
        mTextViewToString.put(R.id.tvKey_th_voiceless, getString(R.string.key_th_voiceless));
        mTextViewToString.put(R.id.tvKey_s, getString(R.string.key_s));
        mTextViewToString.put(R.id.tvKey_sh, getString(R.string.key_sh));
        mTextViewToString.put(R.id.tvKey_b, getString(R.string.key_b));
        mTextViewToString.put(R.id.tvKey_d, getString(R.string.key_d));
        mTextViewToString.put(R.id.tvKey_g, getString(R.string.key_g));
        mTextViewToString.put(R.id.tvKey_dzh, getString(R.string.key_dzh));
        mTextViewToString.put(R.id.tvKey_v, getString(R.string.key_v));
        mTextViewToString.put(R.id.tvKey_th_voiced, getString(R.string.key_th_voiced));
        mTextViewToString.put(R.id.tvKey_z, getString(R.string.key_z));
        mTextViewToString.put(R.id.tvKey_zh, getString(R.string.key_zh));
        mTextViewToString.put(R.id.tvKey_m, getString(R.string.key_m));
        mTextViewToString.put(R.id.tvKey_n, getString(R.string.key_n));
        mTextViewToString.put(R.id.tvKey_ng, getString(R.string.key_ng));
        mTextViewToString.put(R.id.tvKey_l, getString(R.string.key_l));
        mTextViewToString.put(R.id.tvKey_w, getString(R.string.key_w));
        mTextViewToString.put(R.id.tvKey_j, getString(R.string.key_j));
        mTextViewToString.put(R.id.tvKey_h, getString(R.string.key_h));
        mTextViewToString.put(R.id.tvKey_r, getString(R.string.key_r));
        mTextViewToString.put(R.id.tvKey_flap_t, getString(R.string.key_flap_t));
        mTextViewToString.put(R.id.tvKey_glottal_stop, getString(R.string.key_glottal_stop));

        mTextViewToString.put(R.id.tvKey_i, getString(R.string.key_i));
        mTextViewToString.put(R.id.tvKey_i_short, getString(R.string.key_i_short));
        mTextViewToString.put(R.id.tvKey_e_short, getString(R.string.key_e_short));
        mTextViewToString.put(R.id.tvKey_ae, getString(R.string.key_ae));
        mTextViewToString.put(R.id.tvKey_a, getString(R.string.key_a));
        mTextViewToString.put(R.id.tvKey_c_backwards, getString(R.string.key_c_backwards));
        mTextViewToString.put(R.id.tvKey_u_short, getString(R.string.key_u_short));
        mTextViewToString.put(R.id.tvKey_u, getString(R.string.key_u));
        mTextViewToString.put(R.id.tvKey_v_upsidedown, getString(R.string.key_v_upsidedown));
        mTextViewToString.put(R.id.tvKey_shwua, getString(R.string.key_shwua));
        mTextViewToString.put(R.id.tvKey_ei, getString(R.string.key_ei));
        mTextViewToString.put(R.id.tvKey_ai, getString(R.string.key_ai));
        mTextViewToString.put(R.id.tvKey_au, getString(R.string.key_au));
        mTextViewToString.put(R.id.tvKey_oi, getString(R.string.key_oi));
        mTextViewToString.put(R.id.tvKey_ou, getString(R.string.key_ou));
        mTextViewToString.put(R.id.tvKey_er_stressed, getString(R.string.key_er_stressed));
        mTextViewToString.put(R.id.tvKey_er_unstressed, getString(R.string.key_er_unstressed));
        mTextViewToString.put(R.id.tvKey_ar, getString(R.string.key_ar));
        mTextViewToString.put(R.id.tvKey_er, getString(R.string.key_er));
        mTextViewToString.put(R.id.tvKey_ir, getString(R.string.key_ir));
        mTextViewToString.put(R.id.tvKey_or, getString(R.string.key_or));
    }

    @Override
    public void onClick(View v) {
        mListener.onKeyTouched(mLayoutToString.get(v.getId()));
    }

    void updateKeysFor(SoundMode mode) {

        if (mode == SoundMode.Single) {
            view.findViewById(R.id.key_shwua).setVisibility(View.VISIBLE);
            view.findViewById(R.id.key_er_unstressed).setVisibility(View.VISIBLE);
            view.findViewById(R.id.key_glottal_stop).setVisibility(View.VISIBLE);
            view.findViewById(R.id.key_flap_t).setVisibility(View.VISIBLE);
        } else { // Double
            view.findViewById(R.id.key_shwua).setVisibility(View.GONE);
            view.findViewById(R.id.key_er_unstressed).setVisibility(View.GONE);
            view.findViewById(R.id.key_glottal_stop).setVisibility(View.GONE);
            view.findViewById(R.id.key_flap_t).setVisibility(View.GONE);
        }
    }

    void updateKeyAppearanceForSelectedSounds(ArrayList<String> selectedSounds) {
        Context context = getContext();
        if (context == null) return;

        int selectedColor = ContextCompat.getColor(getContext(),R.color.keyboard_text_normal);
        int unselectedColor = ContextCompat.getColor(getContext(),R.color.keyboard_text_unselected);
        String currentKeyString;
        IpaTextView textView;
        boolean isSelected;

        for (int id : mTextViewToString.keySet()) {
            currentKeyString = mTextViewToString.get(id);
            isSelected = false;
            for (String sound : selectedSounds) {
                if (currentKeyString != null && currentKeyString.equals(sound)) {
                    isSelected = true;
                    break;
                }
            }
            textView = view.findViewById(id);
            if (isSelected) {
                textView.setTextColor(selectedColor);
            } else {
                textView.setTextColor(unselectedColor);
            }
        }
    }
}