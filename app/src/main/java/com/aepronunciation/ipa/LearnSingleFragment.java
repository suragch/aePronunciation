package com.aepronunciation.ipa;

import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.LinkedHashMap;
import java.util.Map;


public class LearnSingleFragment extends Fragment implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

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
    private SingleSound singleSound;
    private Map<String, Integer> ipaForVideo = createVideoMap();
    private Map<String, Integer> ipaForDescription = createDescriptionMap();
    private Map<String, Integer> ipaForExample1 = createExample1Map();
    private Map<String, Integer> ipaForExample2 = createExample2Map();
    private Map<String, Integer> ipaForExample3 = createExample3Map();

    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    private SoundPool soundPool = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_learn_single, container, false);

        tvIpaSymbol = layout.findViewById(R.id.tvLearnSingleIpaSound);
        videoView = layout.findViewById(R.id.videoview);
        tvIpaDescription = layout.findViewById(R.id.tvLearnSingleIpaDescription);
        rlExample1 = layout.findViewById(R.id.rlExample1);
        rlExample2 = layout.findViewById(R.id.rlExample2);
        rlExample3 = layout.findViewById(R.id.rlExample3);
        rlMore = layout.findViewById(R.id.rlMore);
        tvExample1 = layout.findViewById(R.id.tvExample1);
        tvExample2 = layout.findViewById(R.id.tvExample2);
        tvExample3 = layout.findViewById(R.id.tvExample3);

        // set listeners
        rlExample1.setOnClickListener(this);
        rlExample2.setOnClickListener(this);
        rlExample3.setOnClickListener(this);
        rlMore.setOnClickListener(this);

        singleSound = new SingleSound();

        return layout;
    }

    @Override
    public void onResume() {

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

        } else { // Button: "more..."

            Bundle args = new Bundle();
            args.putString(LearnDoubleDialogFragment.KEY_DIALOG_IPA, ipa);
            LearnDoubleDialogFragment dialog = new LearnDoubleDialogFragment();
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), "TagDoubleSounds");
        }
    }


    public void onNewSelection(String ipa) {
        if (getActivity() == null) return;

        int videoId = ipaForVideo.get(ipa);
        int descriptionId = ipaForDescription.get(ipa);
        int example1Id = ipaForExample1.get(ipa);
        int example2Id = ipaForExample2.get(ipa);
        int example3Id = ipaForExample3.get(ipa);

        tvIpaSymbol.setText(ipa);
        videoUri = Uri.parse("android.resource://"
                + getActivity().getPackageName()
                + "/" + videoId);
        videoView.setVideoURI(videoUri);
        tvIpaDescription.setText(getString(descriptionId));
        tvExample1.setText(getString(example1Id));
        tvExample2.setText(getString(example2Id));
        tvExample3.setText(getString(example3Id));

        videoView.start();
    }


    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {

        if (status != 0) // 0=success
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);

    }

    private static Map<String, Integer> createVideoMap() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("p", R.raw.v_p);
        result.put("t", R.raw.v_t);
        result.put("k", R.raw.v_k);
        result.put("ʧ", R.raw.v_ch);
        result.put("f", R.raw.v_f);
        result.put("θ", R.raw.v_th_voiceless);
        result.put("s", R.raw.v_s);
        result.put("ʃ", R.raw.v_sh);
        result.put("b", R.raw.v_b);
        result.put("d", R.raw.v_d);
        result.put("g", R.raw.v_g);
        result.put("ʤ", R.raw.v_dzh);
        result.put("v", R.raw.v_v);
        result.put("ð", R.raw.v_th_voiced);
        result.put("z", R.raw.v_z);
        result.put("ʒ", R.raw.v_zh);
        result.put("m", R.raw.v_m);
        result.put("n", R.raw.v_n);
        result.put("ŋ", R.raw.v_ng);
        result.put("l", R.raw.v_l);
        result.put("w", R.raw.v_w);
        result.put("j", R.raw.v_j);
        result.put("h", R.raw.v_h);
        result.put("r", R.raw.v_r);
        result.put("i", R.raw.v_i);
        result.put("ɪ", R.raw.v_i_short);
        result.put("ɛ", R.raw.v_e_short);
        result.put("æ", R.raw.v_ae);
        result.put("ɑ", R.raw.v_a);
        result.put("ɔ", R.raw.v_c_backwards);
        result.put("ʊ", R.raw.v_u_short);
        result.put("u", R.raw.v_u);
        result.put("ʌ", R.raw.v_v_upsidedown);
        result.put("ə", R.raw.v_schwa);
        result.put("eɪ", R.raw.v_ei);
        result.put("aɪ", R.raw.v_ai);
        result.put("aʊ", R.raw.v_au);
        result.put("ɔɪ", R.raw.v_oi);
        result.put("oʊ", R.raw.v_ou);
        result.put("ɾ", R.raw.v_flap_t);
        result.put("ɝ", R.raw.v_er_stressed);
        result.put("ɚ", R.raw.v_er_unstressed);
        result.put("ɑr", R.raw.v_ar);
        result.put("ɛr", R.raw.v_er);
        result.put("ɪr", R.raw.v_ir);
        result.put("ɔr", R.raw.v_or);
        result.put("ʔ", R.raw.v_glottal_stop);

        return result;
    }

    private static Map<String, Integer> createDescriptionMap() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("p", R.string.p_description);
        result.put("t", R.string.t_description);
        result.put("k", R.string.k_description);
        result.put("ʧ", R.string.ch_description);
        result.put("f", R.string.f_description);
        result.put("θ", R.string.th_voiceless_description);
        result.put("s", R.string.s_description);
        result.put("ʃ", R.string.sh_description);
        result.put("b", R.string.b_description);
        result.put("d", R.string.d_description);
        result.put("g", R.string.g_description);
        result.put("ʤ", R.string.dzh_description);
        result.put("v", R.string.v_description);
        result.put("ð", R.string.th_voiced_description);
        result.put("z", R.string.z_description);
        result.put("ʒ", R.string.zh_description);
        result.put("m", R.string.m_description);
        result.put("n", R.string.n_description);
        result.put("ŋ", R.string.ng_description);
        result.put("l", R.string.l_description);
        result.put("w", R.string.w_description);
        result.put("j", R.string.j_description);
        result.put("h", R.string.h_description);
        result.put("r", R.string.r_description);
        result.put("i", R.string.i_description);
        result.put("ɪ", R.string.i_short_description);
        result.put("ɛ", R.string.e_short_description);
        result.put("æ", R.string.ae_description);
        result.put("ɑ", R.string.a_description);
        result.put("ɔ", R.string.c_backwards_description);
        result.put("ʊ", R.string.u_short_description);
        result.put("u", R.string.u_description);
        result.put("ʌ", R.string.v_upsidedown_description);
        result.put("ə", R.string.schwa_description);
        result.put("eɪ", R.string.ei_description);
        result.put("aɪ", R.string.ai_description);
        result.put("aʊ", R.string.au_description);
        result.put("ɔɪ", R.string.oi_description);
        result.put("oʊ", R.string.ou_description);
        result.put("ɾ", R.string.flap_t_description);
        result.put("ɝ", R.string.er_stressed_description);
        result.put("ɚ", R.string.er_unstressed_description);
        result.put("ɑr", R.string.ar_description);
        result.put("ɛr", R.string.er_description);
        result.put("ɪr", R.string.ir_description);
        result.put("ɔr", R.string.or_description);
        result.put("ʔ", R.string.glottal_stop_description);

        return result;
    }



    private static Map<String, Integer> createExample1Map() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("p", R.string.p_example1);
        result.put("t", R.string.t_example1);
        result.put("k", R.string.k_example1);
        result.put("ʧ", R.string.ch_example1);
        result.put("f", R.string.f_example1);
        result.put("θ", R.string.th_voiceless_example1);
        result.put("s", R.string.s_example1);
        result.put("ʃ", R.string.sh_example1);
        result.put("b", R.string.b_example1);
        result.put("d", R.string.d_example1);
        result.put("g", R.string.g_example1);
        result.put("ʤ", R.string.dzh_example1);
        result.put("v", R.string.v_example1);
        result.put("ð", R.string.th_voiced_example1);
        result.put("z", R.string.z_example1);
        result.put("ʒ", R.string.zh_example1);
        result.put("m", R.string.m_example1);
        result.put("n", R.string.n_example1);
        result.put("ŋ", R.string.ng_example1);
        result.put("l", R.string.l_example1);
        result.put("w", R.string.w_example1);
        result.put("j", R.string.j_example1);
        result.put("h", R.string.h_example1);
        result.put("r", R.string.r_example1);
        result.put("i", R.string.i_example1);
        result.put("ɪ", R.string.i_short_example1);
        result.put("ɛ", R.string.e_short_example1);
        result.put("æ", R.string.ae_example1);
        result.put("ɑ", R.string.a_example1);
        result.put("ɔ", R.string.c_backwards_example1);
        result.put("ʊ", R.string.u_short_example1);
        result.put("u", R.string.u_example1);
        result.put("ʌ", R.string.v_upsidedown_example1);
        result.put("ə", R.string.schwa_example1);
        result.put("eɪ", R.string.ei_example1);
        result.put("aɪ", R.string.ai_example1);
        result.put("aʊ", R.string.au_example1);
        result.put("ɔɪ", R.string.oi_example1);
        result.put("oʊ", R.string.ou_example1);
        result.put("ɾ", R.string.flap_t_example1);
        result.put("ɝ", R.string.er_stressed_example1);
        result.put("ɚ", R.string.er_unstressed_example1);
        result.put("ɑr", R.string.ar_example1);
        result.put("ɛr", R.string.er_example1);
        result.put("ɪr", R.string.ir_example1);
        result.put("ɔr", R.string.or_example1);
        result.put("ʔ", R.string.glottal_stop_example1);

        return result;
    }

    private static Map<String, Integer> createExample2Map() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("p", R.string.p_example2);
        result.put("t", R.string.t_example2);
        result.put("k", R.string.k_example2);
        result.put("ʧ", R.string.ch_example2);
        result.put("f", R.string.f_example2);
        result.put("θ", R.string.th_voiceless_example2);
        result.put("s", R.string.s_example2);
        result.put("ʃ", R.string.sh_example2);
        result.put("b", R.string.b_example2);
        result.put("d", R.string.d_example2);
        result.put("g", R.string.g_example2);
        result.put("ʤ", R.string.dzh_example2);
        result.put("v", R.string.v_example2);
        result.put("ð", R.string.th_voiced_example2);
        result.put("z", R.string.z_example2);
        result.put("ʒ", R.string.zh_example2);
        result.put("m", R.string.m_example2);
        result.put("n", R.string.n_example2);
        result.put("ŋ", R.string.ng_example2);
        result.put("l", R.string.l_example2);
        result.put("w", R.string.w_example2);
        result.put("j", R.string.j_example2);
        result.put("h", R.string.h_example2);
        result.put("r", R.string.r_example2);
        result.put("i", R.string.i_example2);
        result.put("ɪ", R.string.i_short_example2);
        result.put("ɛ", R.string.e_short_example2);
        result.put("æ", R.string.ae_example2);
        result.put("ɑ", R.string.a_example2);
        result.put("ɔ", R.string.c_backwards_example2);
        result.put("ʊ", R.string.u_short_example2);
        result.put("u", R.string.u_example2);
        result.put("ʌ", R.string.v_upsidedown_example2);
        result.put("ə", R.string.schwa_example2);
        result.put("eɪ", R.string.ei_example2);
        result.put("aɪ", R.string.ai_example2);
        result.put("aʊ", R.string.au_example2);
        result.put("ɔɪ", R.string.oi_example2);
        result.put("oʊ", R.string.ou_example2);
        result.put("ɾ", R.string.flap_t_example2);
        result.put("ɝ", R.string.er_stressed_example2);
        result.put("ɚ", R.string.er_unstressed_example2);
        result.put("ɑr", R.string.ar_example2);
        result.put("ɛr", R.string.er_example2);
        result.put("ɪr", R.string.ir_example2);
        result.put("ɔr", R.string.or_example2);
        result.put("ʔ", R.string.glottal_stop_example2);

        return result;
    }

    private static Map<String, Integer> createExample3Map() {

        Map<String, Integer> result = new LinkedHashMap<>();

        result.put("p", R.string.p_example3);
        result.put("t", R.string.t_example3);
        result.put("k", R.string.k_example3);
        result.put("ʧ", R.string.ch_example3);
        result.put("f", R.string.f_example3);
        result.put("θ", R.string.th_voiceless_example3);
        result.put("s", R.string.s_example3);
        result.put("ʃ", R.string.sh_example3);
        result.put("b", R.string.b_example3);
        result.put("d", R.string.d_example3);
        result.put("g", R.string.g_example3);
        result.put("ʤ", R.string.dzh_example3);
        result.put("v", R.string.v_example3);
        result.put("ð", R.string.th_voiced_example3);
        result.put("z", R.string.z_example3);
        result.put("ʒ", R.string.zh_example3);
        result.put("m", R.string.m_example3);
        result.put("n", R.string.n_example3);
        result.put("ŋ", R.string.ng_example3);
        result.put("l", R.string.l_example3);
        result.put("w", R.string.w_example3);
        result.put("j", R.string.j_example3);
        result.put("h", R.string.h_example3);
        result.put("r", R.string.r_example3);
        result.put("i", R.string.i_example3);
        result.put("ɪ", R.string.i_short_example3);
        result.put("ɛ", R.string.e_short_example3);
        result.put("æ", R.string.ae_example3);
        result.put("ɑ", R.string.a_example3);
        result.put("ɔ", R.string.c_backwards_example3);
        result.put("ʊ", R.string.u_short_example3);
        result.put("u", R.string.u_example3);
        result.put("ʌ", R.string.v_upsidedown_example3);
        result.put("ə", R.string.schwa_example3);
        result.put("eɪ", R.string.ei_example3);
        result.put("aɪ", R.string.ai_example3);
        result.put("aʊ", R.string.au_example3);
        result.put("ɔɪ", R.string.oi_example3);
        result.put("oʊ", R.string.ou_example3);
        result.put("ɾ", R.string.flap_t_example3);
        result.put("ɝ", R.string.er_stressed_example3);
        result.put("ɚ", R.string.er_unstressed_example3);
        result.put("ɑr", R.string.ar_example3);
        result.put("ɛr", R.string.er_example3);
        result.put("ɪr", R.string.ir_example3);
        result.put("ɔr", R.string.or_example3);
        result.put("ʔ", R.string.glottal_stop_example3);

        return result;
    }
}
