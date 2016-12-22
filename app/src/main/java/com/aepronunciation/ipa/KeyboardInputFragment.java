package com.aepronunciation.ipa;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Resources;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.aepronunciation.ipa.MainActivity.TEST_MODE_KEY;

public class KeyboardInputFragment extends Fragment implements OnClickListener, View.OnLongClickListener {

    View view;

    private HashMap<Integer, String> mClickMap;
    private HashMap<Integer, String> mLongClickMap;

    KeyboardInputListener mListener; // communicate with the parent activity

    // Container Activity must implement this interface
    public interface KeyboardInputListener {
        public void onKeyTouched(String keyString);
        public void onBackspace();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeMaps();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_keyboard_input, container, false);



        // Add listeners for all keys
        for (int id : mClickMap.keySet()) {
            RelativeLayout rlKey = (RelativeLayout) view.findViewById(id);
            rlKey.setOnClickListener(this);
        }

        for (int id : mLongClickMap.keySet()) {
            RelativeLayout rlKey = (RelativeLayout) view.findViewById(id);
            rlKey.setOnLongClickListener(this);
        }

        RelativeLayout rlBackspace = (RelativeLayout) view.findViewById(R.id.key_backspace);
        rlBackspace.setOnClickListener(backspaceClick);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if parent Fragment implements listener
        if (getActivity() instanceof KeyboardInputListener) {
            mListener = (KeyboardInputFragment.KeyboardInputListener) getActivity();
        } else {
            throw new RuntimeException("Parent activity must implement KeyboardInputListener");
        }
    }

    private void initializeMaps() {

        // Initialize keyboard key values
        mClickMap = new HashMap<Integer, String>();

        mClickMap.put(R.id.key_i, getString(R.string.key_i));
        mClickMap.put(R.id.key_i_short, getString(R.string.key_i_short));
        mClickMap.put(R.id.key_e_short, getString(R.string.key_e_short));
        mClickMap.put(R.id.key_ae, getString(R.string.key_ae));
        mClickMap.put(R.id.key_a, getString(R.string.key_a));
        mClickMap.put(R.id.key_c_backwards, getString(R.string.key_c_backwards));
        mClickMap.put(R.id.key_u_short, getString(R.string.key_u_short));
        mClickMap.put(R.id.key_u, getString(R.string.key_u));
        mClickMap.put(R.id.key_v_upsidedown, getString(R.string.key_v_upsidedown));
        mClickMap.put(R.id.key_shwua, getString(R.string.key_shwua));
        mClickMap.put(R.id.key_ei, getString(R.string.key_ei));
        mClickMap.put(R.id.key_ai, getString(R.string.key_ai));
        mClickMap.put(R.id.key_au, getString(R.string.key_au));
        mClickMap.put(R.id.key_oi, getString(R.string.key_oi));
        mClickMap.put(R.id.key_ou, getString(R.string.key_ou));
        mClickMap.put(R.id.key_er_stressed, getString(R.string.key_er_stressed));
        mClickMap.put(R.id.key_er_unstressed, getString(R.string.key_er_unstressed));
        mClickMap.put(R.id.key_ar, getString(R.string.key_ar));
        mClickMap.put(R.id.key_er, getString(R.string.key_er));
        mClickMap.put(R.id.key_ir, getString(R.string.key_ir));
        mClickMap.put(R.id.key_or, getString(R.string.key_or));
        mClickMap.put(R.id.key_p, getString(R.string.key_p));
        mClickMap.put(R.id.key_t, getString(R.string.key_t));
        mClickMap.put(R.id.key_k, getString(R.string.key_k));
        mClickMap.put(R.id.key_ch, getString(R.string.key_ch));
        mClickMap.put(R.id.key_f, getString(R.string.key_f));
        mClickMap.put(R.id.key_th_voiceless, getString(R.string.key_th_voiceless));
        mClickMap.put(R.id.key_s, getString(R.string.key_s));
        mClickMap.put(R.id.key_sh, getString(R.string.key_sh));
        mClickMap.put(R.id.key_b, getString(R.string.key_b));
        mClickMap.put(R.id.key_d, getString(R.string.key_d));
        mClickMap.put(R.id.key_g, getString(R.string.key_g));
        mClickMap.put(R.id.key_dzh, getString(R.string.key_dzh));
        mClickMap.put(R.id.key_v, getString(R.string.key_v));
        mClickMap.put(R.id.key_th_voiced, getString(R.string.key_th_voiced));
        mClickMap.put(R.id.key_z, getString(R.string.key_z));
        mClickMap.put(R.id.key_zh, getString(R.string.key_zh));
        mClickMap.put(R.id.key_m, getString(R.string.key_m));
        mClickMap.put(R.id.key_n, getString(R.string.key_n));
        mClickMap.put(R.id.key_ng, getString(R.string.key_ng));
        mClickMap.put(R.id.key_l, getString(R.string.key_l));
        mClickMap.put(R.id.key_w, getString(R.string.key_w));
        mClickMap.put(R.id.key_j, getString(R.string.key_j));
        mClickMap.put(R.id.key_h, getString(R.string.key_h));
        mClickMap.put(R.id.key_r, getString(R.string.key_r));
        mClickMap.put(R.id.key_flap_t, getString(R.string.key_flap_t));
        mClickMap.put(R.id.key_glottal_stop, getString(R.string.key_glottal_stop));
        //mClickMap.put(R.id.key_backspace, "");
        mClickMap.put(R.id.key_brackets, getString(R.string.key_left_bracket));
        mClickMap.put(R.id.key_slash, getString(R.string.key_slash));
        mClickMap.put(R.id.key_space, getString(R.string.key_space));
        mClickMap.put(R.id.key_stress, getString(R.string.key_primary_stress));
        mClickMap.put(R.id.key_long_vowel, getString(R.string.key_long_vowel));
        mClickMap.put(R.id.key_return, getString(R.string.key_return));

        mLongClickMap = new HashMap<Integer, String>();

        mLongClickMap.put(R.id.key_i, getString(R.string.key_alt_i));
        mLongClickMap.put(R.id.key_e_short, getString(R.string.key_alt_e_short));
        mLongClickMap.put(R.id.key_a, getString(R.string.key_alt_a));
        mLongClickMap.put(R.id.key_c_backwards, getString(R.string.key_alt_c_backwards));
        mLongClickMap.put(R.id.key_u, getString(R.string.key_alt_u));
        mLongClickMap.put(R.id.key_ou, getString(R.string.key_alt_ou));
        mLongClickMap.put(R.id.key_er_stressed, getString(R.string.key_alt_er_stressed));
        mLongClickMap.put(R.id.key_er_unstressed, getString(R.string.key_alt_er_unstressed));
        mLongClickMap.put(R.id.key_ar, getString(R.string.key_alt_ar));
        mLongClickMap.put(R.id.key_er, getString(R.string.key_alt_er));
        mLongClickMap.put(R.id.key_ir, getString(R.string.key_alt_ir));
        mLongClickMap.put(R.id.key_or, getString(R.string.key_alt_or));
        mLongClickMap.put(R.id.key_l, getString(R.string.key_alt_l));
        mLongClickMap.put(R.id.key_w, getString(R.string.key_alt_w));
        mLongClickMap.put(R.id.key_h, getString(R.string.key_alt_h));
        mLongClickMap.put(R.id.key_r, getString(R.string.key_alt_r));
        mLongClickMap.put(R.id.key_brackets, getString(R.string.key_right_bracket));
        mLongClickMap.put(R.id.key_stress, getString(R.string.key_secondary_stress));



    }

    private OnClickListener backspaceClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.onBackspace();
        }
    };

    @Override
    public void onClick(View v) {
        mListener.onKeyTouched(mClickMap.get(v.getId()));
    }


    @Override
    public boolean onLongClick(View view) {
        mListener.onKeyTouched(mLongClickMap.get(view.getId()));
        return true;
    }
}