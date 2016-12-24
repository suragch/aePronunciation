package com.aepronunciation.ipa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.EXTRA_EMAIL;
import static com.aepronunciation.ipa.MainActivity.TIME_DEFAULT;
import static com.aepronunciation.ipa.MainActivity.PREFS_NAME;
import static com.aepronunciation.ipa.MainActivity.TIME_LEARN_SINGLE_KEY;


public class LearnSingleFragment extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    static final String STATE_FIRST_LOAD = "firstLoad";
    static final String STATE_IPA = "ipaSymbol";
    static final String STATE_VIDEO_URI = "videoUri";
    static final String STATE_DESCRIPTION = "description";
    static final String STATE_EXAMPLE_1 = "example1";
    static final String STATE_EXAMPLE_2 = "example2";
    static final String STATE_EXAMPLE_3 = "example3";

    TextView tvIpaSymbol;
    VideoView videoView;
    Uri videoUri;
    TextView tvIpaDescription;
    RelativeLayout rlExample1;
    RelativeLayout rlExample2;
    RelativeLayout rlExample3;
    RelativeLayout rlMore;
    TextView tvExample1;
    TextView tvExample2;
    TextView tvExample3;
    //boolean firstLoad = true;
    private SingleSound singleSound;
    //long startTime;
    //SharedPreferences settings;

    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;

    //LearnListener mCallback; // communicate with the parent activity



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_learn_single, container, false);

        tvIpaSymbol = (TextView) layout.findViewById(R.id.tvLearnSingleIpaSound);
        videoView = (VideoView) layout.findViewById(R.id.videoview);
        tvIpaDescription = (TextView) layout.findViewById(R.id.tvLearnSingleIpaDescription);
        rlExample1 = (RelativeLayout) layout.findViewById(R.id.rlExample1);
        rlExample2 = (RelativeLayout) layout.findViewById(R.id.rlExample2);
        rlExample3 = (RelativeLayout) layout.findViewById(R.id.rlExample3);
        rlMore = (RelativeLayout) layout.findViewById(R.id.rlMore);
        tvExample1 = (TextView) layout.findViewById(R.id.tvExample1);
        tvExample2 = (TextView) layout.findViewById(R.id.tvExample2);
        tvExample3 = (TextView) layout.findViewById(R.id.tvExample3);

        // set listeners
        rlExample1.setOnClickListener(this);
        rlExample2.setOnClickListener(this);
        rlExample3.setOnClickListener(this);
        rlMore.setOnClickListener(this);

        singleSound = new SingleSound();
        //firstLoad = true;

        // retain instance state so that it isn't lost on rotation ext.
        //setRetainInstance(true);

        return layout;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }

    @Override
    public void onResume() {

        // start timing
        //startTime = System.nanoTime();
        if (videoView != null) {
            videoView.seekTo(1);
        }



        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // FIXME this is getting called twice on an orientation change and the second time savedInstanceState is null
        // http://stackoverflow.com/questions/10983396/fragment-oncreateview-and-onactivitycreated-called-twice#
        // The result is that state is not saved on tablets for an orientation change

        if (savedInstanceState != null) {

            // add the text back
            tvIpaSymbol.setText(savedInstanceState.getString(STATE_IPA));
            videoUri = savedInstanceState.getParcelable(STATE_VIDEO_URI);
            videoView.setVideoURI(videoUri);
            videoView.seekTo(1);
            tvIpaDescription.setText(savedInstanceState.getString(STATE_DESCRIPTION));
            tvExample1.setText(savedInstanceState.getString(STATE_EXAMPLE_1));
            tvExample2.setText(savedInstanceState.getString(STATE_EXAMPLE_2));
            tvExample3.setText(savedInstanceState.getString(STATE_EXAMPLE_3));


        } else {

            tvIpaSymbol.setText(getString(R.string.key_ae));
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ae);
            videoView.setVideoURI(videoUri);
            videoView.seekTo(1);
            tvIpaDescription.setText(getString(
                    R.string.ae_description));
            tvExample1.setText(getString(R.string.ae_example1));
            tvExample2.setText(getString(R.string.ae_example2));
            tvExample3.setText(getString(R.string.ae_example3));
        }
    }

    @Override
    public void onPause() {

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Save the fragment's state here
        savedInstanceState.putString(STATE_IPA, tvIpaSymbol.getText().toString());
        savedInstanceState.putParcelable(STATE_VIDEO_URI, videoUri);
        savedInstanceState.putString(STATE_DESCRIPTION, tvIpaDescription.getText().toString());
        savedInstanceState.putString(STATE_EXAMPLE_1, tvExample1.getText().toString());
        savedInstanceState.putString(STATE_EXAMPLE_2, tvExample2.getText().toString());
        savedInstanceState.putString(STATE_EXAMPLE_3, tvExample3.getText().toString());
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//
//        // Save the user's current game state
//        savedInstanceState.putBoolean(STATE_FIRST_LOAD, firstLoad);
//        savedInstanceState.putString(STATE_IPA, tvIpaSymbol.getText().toString());
//        savedInstanceState.putString(STATE_DESCRIPTION, tvIpaDescription.getText().toString());
//        savedInstanceState.putString(STATE_EXAMPLE_1, tvExample1.getText().toString());
//        savedInstanceState.putString(STATE_EXAMPLE_2, tvExample2.getText().toString());
//        savedInstanceState.putString(STATE_EXAMPLE_3, tvExample3.getText().toString());
//
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }




    @Override
    public void onClick(View v) {

        // get the ipa string
        String ipa = tvIpaSymbol.getText().toString();

        if (v.getId() == R.id.rlExample1) {

            int soundId = singleSound.getExampleOneResourceId(ipa);
            soundPool.load(getContext(), soundId, PRIORITY);

        } else if (v.getId() == R.id.rlExample2) {

            int soundId = singleSound.getExampleTwoResourceId(ipa);
            soundPool.load(getContext(), soundId, PRIORITY);

        } else if (v.getId() == R.id.rlExample3) {

            int soundId = singleSound.getExampleThreeResourceId(ipa);
            soundPool.load(getContext(), soundId, PRIORITY);

        } else { // more ...

            Bundle args = new Bundle();
            args.putString(LearnDoubleDialogFragment.KEY_DIALOG_IPA, tvIpaSymbol.getText().toString());
            LearnDoubleDialogFragment dialog = new LearnDoubleDialogFragment();
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), "TagDoubleSounds");

        }


    }


    public void onNewSelection(String keyString) {
        Log.i("TAG", "onNewSelection: " + keyString);

        // update ipa lable

        // update video

        // update ipa description

        // update example buttons

        Context context = getContext();

        if (keyString.equals("i")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_i);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.i_description));
            tvExample1.setText(getString(R.string.i_example1));
            tvExample2.setText(getString(R.string.i_example2));
            tvExample3.setText(getString(R.string.i_example3));

            //soundPool.load(context, R.raw.single_i_long, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɪ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_i_short);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.i_short_description));
            tvExample1.setText(getString(
                    R.string.i_short_example1));
            tvExample2.setText(getString(
                    R.string.i_short_example2));
            tvExample3.setText(getString(
                    R.string.i_short_example3));

            //soundPool.load(context, R.raw.single_i_short, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɛ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_e_short);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.e_short_description));
            tvExample1.setText(getString(
                    R.string.e_short_example1));
            tvExample2.setText(getString(
                    R.string.e_short_example2));
            tvExample3.setText(getString(
                    R.string.e_short_example3));

            //soundPool.load(context, R.raw.single_e_short, PRIORITY);
            videoView.start();

        } else if (keyString.equals("æ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ae);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.ae_description));
            tvExample1.setText(getString(R.string.ae_example1));
            tvExample2.setText(getString(R.string.ae_example2));
            tvExample3.setText(getString(R.string.ae_example3));

            //soundPool.load(context, R.raw.single_ae, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɑ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_a);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.a_description));
            tvExample1.setText(getString(R.string.a_example1));
            tvExample2.setText(getString(R.string.a_example2));
            tvExample3.setText(getString(R.string.a_example3));

            //soundPool.load(context, R.raw.single_a, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɔ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_c_backwards);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.c_backwards_description));
            tvExample1.setText(getString(
                    R.string.c_backwards_example1));
            tvExample2.setText(getString(
                    R.string.c_backwards_example2));
            tvExample3.setText(getString(
                    R.string.c_backwards_example3));

            //soundPool.load(context, R.raw.single_c_backwards, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʊ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_u_short);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.u_short_description));
            tvExample1.setText(getString(
                    R.string.u_short_example1));
            tvExample2.setText(getString(
                    R.string.u_short_example2));
            tvExample3.setText(getString(
                    R.string.u_short_example3));

            //soundPool.load(context, R.raw.single_u_short, PRIORITY);
            videoView.start();

        } else if (keyString.equals("u")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_u);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.u_description));
            tvExample1.setText(getString(R.string.u_example1));
            tvExample2.setText(getString(R.string.u_example2));
            tvExample3.setText(getString(R.string.u_example3));

            //soundPool.load(context, R.raw.single_u, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʌ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_v_upsidedown);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.v_upsidedown_description));
            tvExample1.setText(getString(
                    R.string.v_upsidedown_example1));
            tvExample2.setText(getString(
                    R.string.v_upsidedown_example2));
            tvExample3.setText(getString(
                    R.string.v_upsidedown_example3));

            //soundPool.load(context, R.raw.single_v_upsidedown, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ə")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_shwua);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.schwa_description));
            tvExample1.setText(getResources()
                    .getString(R.string.schwa_example1));
            tvExample2.setText(getResources()
                    .getString(R.string.schwa_example2));
            tvExample3.setText(getResources()
                    .getString(R.string.schwa_example3));

            //soundPool.load(context, R.raw.single_schwa, PRIORITY);
            videoView.start();

        } else if (keyString.equals("eɪ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ei);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.ei_description));
            tvExample1.setText(getString(R.string.ei_example1));
            tvExample2.setText(getString(R.string.ei_example2));
            tvExample3.setText(getString(R.string.ei_example3));

            //soundPool.load(context, R.raw.single_ei, PRIORITY);
            videoView.start();

        } else if (keyString.equals("aɪ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ai);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.ai_description));
            tvExample1.setText(getString(R.string.ai_example1));
            tvExample2.setText(getString(R.string.ai_example2));
            tvExample3.setText(getString(R.string.ai_example3));

            //soundPool.load(context, R.raw.single_ai, PRIORITY);
            videoView.start();

        } else if (keyString.equals("aʊ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_au);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.au_description));
            tvExample1.setText(getString(R.string.au_example1));
            tvExample2.setText(getString(R.string.au_example2));
            tvExample3.setText(getString(R.string.au_example3));

            //soundPool.load(context, R.raw.single_au, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɔɪ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_oi);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.oi_description));
            tvExample1.setText(getString(R.string.oi_example1));
            tvExample2.setText(getString(R.string.oi_example2));
            tvExample3.setText(getString(R.string.oi_example3));

            //soundPool.load(context, R.raw.single_oi, PRIORITY);
            videoView.start();

        } else if (keyString.equals("oʊ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ou);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.ou_description));
            tvExample1.setText(getString(R.string.ou_example1));
            tvExample2.setText(getString(R.string.ou_example2));
            tvExample3.setText(getString(R.string.ou_example3));

            //soundPool.load(context, R.raw.single_ou, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɝ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_er_stressed);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(R.string.er_stressed_description));
            tvExample1.setText(getString(R.string.er_stressed_example1));
            tvExample2.setText(getString(R.string.er_stressed_example2));
            tvExample3.setText(getString(R.string.er_stressed_example3));

            //soundPool.load(context, R.raw.single_er_stressed, PRIORITY);
            videoView.start();
            //videoView.start();

        } else if (keyString.equals("ɚ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_er_unstressed);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.er_unstressed_description));
            tvExample1.setText(getString(
                    R.string.er_unstressed_example1));
            tvExample2.setText(getString(
                    R.string.er_unstressed_example2));
            tvExample3.setText(getString(
                    R.string.er_unstressed_example3));

            //soundPool.load(context, R.raw.single_er_unstressed, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɑr")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ar);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.ar_description));
            tvExample1.setText(getString(R.string.ar_example1));
            tvExample2.setText(getString(R.string.ar_example2));
            tvExample3.setText(getString(R.string.ar_example3));

            //soundPool.load(context, R.raw.single_ar, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɛr")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_er);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.er_description));
            tvExample1.setText(getString(R.string.er_example1));
            tvExample2.setText(getString(R.string.er_example2));
            tvExample3.setText(getString(R.string.er_example3));

            //soundPool.load(context, R.raw.single_er, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɪr")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ir);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.ir_description));
            tvExample1.setText(getString(R.string.ir_example1));
            tvExample2.setText(getString(R.string.ir_example2));
            tvExample3.setText(getString(R.string.ir_example3));

            //soundPool.load(context, R.raw.single_ir, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɔr")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_or);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.or_description));
            tvExample1.setText(getString(R.string.or_example1));
            tvExample2.setText(getString(R.string.or_example2));
            tvExample3.setText(getString(R.string.or_example3));

            //soundPool.load(context, R.raw.single_or, PRIORITY);
            videoView.start();

        } else if (keyString.equals("p")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_p);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.p_description));
            tvExample1.setText(getString(R.string.p_example1));
            tvExample2.setText(getString(R.string.p_example2));
            tvExample3.setText(getString(R.string.p_example3));

            //soundPool.load(context, R.raw.single_p, PRIORITY);
            videoView.start();

        } else if (keyString.equals("t")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_t);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.t_description));
            tvExample1.setText(getString(R.string.t_example1));
            tvExample2.setText(getString(R.string.t_example2));
            tvExample3.setText(getString(R.string.t_example3));

            //soundPool.load(context, R.raw.single_t, PRIORITY);
            videoView.start();

        } else if (keyString.equals("k")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_k);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.k_description));
            tvExample1.setText(getString(R.string.k_example1));
            tvExample2.setText(getString(R.string.k_example2));
            tvExample3.setText(getString(R.string.k_example3));

            //soundPool.load(context, R.raw.single_k, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʧ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ch);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.ch_description));
            tvExample1.setText(getString(R.string.ch_example1));
            tvExample2.setText(getString(R.string.ch_example2));
            tvExample3.setText(getString(R.string.ch_example3));

            //soundPool.load(context, R.raw.single_ch, PRIORITY);
            videoView.start();

        } else if (keyString.equals("f")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_f);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.f_description));
            tvExample1.setText(getString(R.string.f_example1));
            tvExample2.setText(getString(R.string.f_example2));
            tvExample3.setText(getString(R.string.f_example3));

            //soundPool.load(context, R.raw.single_f, PRIORITY);
            videoView.start();

        } else if (keyString.equals("θ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_th_voiceless);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.th_voiceless_description));
            tvExample1.setText(getString(
                    R.string.th_voiceless_example1));
            tvExample2.setText(getString(
                    R.string.th_voiceless_example2));
            tvExample3.setText(getString(
                    R.string.th_voiceless_example3));

            //soundPool.load(context, R.raw.single_th_voiceless, PRIORITY);
            videoView.start();

        } else if (keyString.equals("s")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_s);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.s_description));
            tvExample1.setText(getString(R.string.s_example1));
            tvExample2.setText(getString(R.string.s_example2));
            tvExample3.setText(getString(R.string.s_example3));

            //soundPool.load(context, R.raw.single_s, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʃ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_sh);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.sh_description));
            tvExample1.setText(getString(R.string.sh_example1));
            tvExample2.setText(getString(R.string.sh_example2));
            tvExample3.setText(getString(R.string.sh_example3));

            //soundPool.load(context, R.raw.single_sh, PRIORITY);
            videoView.start();

        } else if (keyString.equals("b")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_b);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.b_description));
            tvExample1.setText(getString(R.string.b_example1));
            tvExample2.setText(getString(R.string.b_example2));
            tvExample3.setText(getString(R.string.b_example3));

            //soundPool.load(context, R.raw.single_b, PRIORITY);
            videoView.start();

        } else if (keyString.equals("d")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_d);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.d_description));
            tvExample1.setText(getString(R.string.d_example1));
            tvExample2.setText(getString(R.string.d_example2));
            tvExample3.setText(getString(R.string.d_example3));

            //soundPool.load(context, R.raw.single_d, PRIORITY);
            videoView.start();

        } else if (keyString.equals("g")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_g);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.g_description));
            tvExample1.setText(getString(R.string.g_example1));
            tvExample2.setText(getString(R.string.g_example2));
            tvExample3.setText(getString(R.string.g_example3));

            //soundPool.load(context, R.raw.single_g, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʤ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_dzh);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.dzh_description));
            tvExample1.setText(getString(R.string.dzh_example1));
            tvExample2.setText(getString(R.string.dzh_example2));
            tvExample3.setText(getString(R.string.dzh_example3));

            //soundPool.load(context, R.raw.single_dzh, PRIORITY);
            videoView.start();

        } else if (keyString.equals("v")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_v);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.v_description));
            tvExample1.setText(getString(R.string.v_example1));
            tvExample2.setText(getString(R.string.v_example2));
            tvExample3.setText(getString(R.string.v_example3));

            //soundPool.load(context, R.raw.single_v, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ð")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_th_voiced);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.th_voiced_description));
            tvExample1.setText(getString(
                    R.string.th_voiced_example1));
            tvExample2.setText(getString(
                    R.string.th_voiced_example2));
            tvExample3.setText(getString(
                    R.string.th_voiced_example3));

            //soundPool.load(context, R.raw.single_th_voiced, PRIORITY);
            videoView.start();

        } else if (keyString.equals("z")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_z);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.z_description));
            tvExample1.setText(getString(R.string.z_example1));
            tvExample2.setText(getString(R.string.z_example2));
            tvExample3.setText(getString(R.string.z_example3));

            //soundPool.load(context, R.raw.single_z, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ʒ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_zh);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.zh_description));
            tvExample1.setText(getString(R.string.zh_example1));
            tvExample2.setText(getString(R.string.zh_example2));
            tvExample3.setText(getString(R.string.zh_example3));

            //soundPool.load(context, R.raw.single_zh, PRIORITY);
            videoView.start();

        } else if (keyString.equals("m")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_m);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.m_description));
            tvExample1.setText(getString(R.string.m_example1));
            tvExample2.setText(getString(R.string.m_example2));
            tvExample3.setText(getString(R.string.m_example3));

            //soundPool.load(context, R.raw.single_m, PRIORITY);
            videoView.start();

        } else if (keyString.equals("n")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_n);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.n_description));
            tvExample1.setText(getString(R.string.n_example1));
            tvExample2.setText(getString(R.string.n_example2));
            tvExample3.setText(getString(R.string.n_example3));

            //soundPool.load(context, R.raw.single_n, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ŋ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_ng);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.ng_description));
            tvExample1.setText(getString(R.string.ng_example1));
            tvExample2.setText(getString(R.string.ng_example2));
            tvExample3.setText(getString(R.string.ng_example3));

            //soundPool.load(context, R.raw.single_ng, PRIORITY);
            videoView.start();

        } else if (keyString.equals("l")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_l);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.l_description));
            tvExample1.setText(getString(R.string.l_example1));
            tvExample2.setText(getString(R.string.l_example2));
            tvExample3.setText(getString(R.string.l_example3));

            //soundPool.load(context, R.raw.single_l, PRIORITY);
            videoView.start();

        } else if (keyString.equals("w")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_w);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.w_description));
            tvExample1.setText(getString(R.string.w_example1));
            tvExample2.setText(getString(R.string.w_example2));
            tvExample3.setText(getString(R.string.w_example3));

            //soundPool.load(context, R.raw.single_w, PRIORITY);
            videoView.start();

        } else if (keyString.equals("j")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_j);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.j_description));
            tvExample1.setText(getString(R.string.j_example1));
            tvExample2.setText(getString(R.string.j_example2));
            tvExample3.setText(getString(R.string.j_example3));

            //soundPool.load(context, R.raw.single_j, PRIORITY);
            videoView.start();

        } else if (keyString.equals("h")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_h);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.h_description));
            tvExample1.setText(getString(R.string.h_example1));
            tvExample2.setText(getString(R.string.h_example2));
            tvExample3.setText(getString(R.string.h_example3));

            //soundPool.load(context, R.raw.single_h, PRIORITY);
            videoView.start();

        } else if (keyString.equals("r")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_r);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.r_description));
            tvExample1.setText(getString(R.string.r_example1));
            tvExample2.setText(getString(R.string.r_example2));
            tvExample3.setText(getString(R.string.r_example3));

            //soundPool.load(context, R.raw.single_r, PRIORITY);
            videoView.start();


        } else if (keyString.equals("ʔ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_glottal_stop);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.glottal_stop_description));
            tvExample1.setText(getString(
                    R.string.glottal_stop_example1));
            tvExample2.setText(getString(
                    R.string.glottal_stop_example2));
            tvExample3.setText(getString(
                    R.string.glottal_stop_example3));

            //soundPool.load(context, R.raw.single_glottal_stop, PRIORITY);
            videoView.start();

        } else if (keyString.equals("ɾ")) {

            tvIpaSymbol.setText(keyString);
            videoUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v_t_flap);
            videoView.setVideoURI(videoUri);
            tvIpaDescription.setText(getString(
                    R.string.flap_t_description));
            tvExample1.setText(getString(
                    R.string.flap_t_example1));
            tvExample2.setText(getString(
                    R.string.flap_t_example2));
            tvExample3.setText(getString(
                    R.string.flap_t_example3));

            //soundPool.load(context, R.raw.single_flap_t, PRIORITY);
            videoView.start();

        }

    }


    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {

        if (status != 0) // 0=success
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);

    }
}
