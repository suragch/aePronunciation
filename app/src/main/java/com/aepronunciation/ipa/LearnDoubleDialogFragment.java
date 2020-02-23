package com.aepronunciation.ipa;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;


public class LearnDoubleDialogFragment extends DialogFragment implements
        LearnDoubleRecyclerViewAdapter.ItemClickListener, SoundPool.OnLoadCompleteListener {

    private DoubleSound doubleSound;
    private LearnDoubleRecyclerViewAdapter adapter;

    String ipa;

    private static final int SRC_QUALITY = 0;
    private static final int PRIORITY = 1;
    static final String KEY_DIALOG_IPA = "ipa";
    private SoundPool soundPool = null;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_learn_double, null);

        Bundle mArgs = getArguments();
        ipa = mArgs.getString(KEY_DIALOG_IPA);

        doubleSound = new DoubleSound();
        doubleSound.restrictListToAllPairsContaining(ipa);
        adapter = new LearnDoubleRecyclerViewAdapter(getActivity(), doubleSound.getSounds());

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rvLearnDouble);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getActivity(), R.dimen.grid_item_spacing);
        recyclerView.addItemDecoration(itemDecoration);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // build the alert dialog
        String title = String.format(getString(R.string.title_activity_learn_double), ipa);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(R.string.select_sounds_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {

        StudyTimer timer = StudyTimer.getInstance();
        timer.start(getActivity(), StudyTimer.StudyType.LearnDouble);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, SRC_QUALITY);
        soundPool.setOnLoadCompleteListener(this);

        super.onResume();
    }

    @Override
    public void onPause() {

        // update time
        StudyTimer timer = StudyTimer.getInstance();
        timer.start(getActivity(), StudyTimer.StudyType.LearnSingle);

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        super.onPause();
    }

    @Override
    public void onLoadComplete(SoundPool sPool, int sid, int status) {
        if (status != 0) // 0=success
            return;

        soundPool.play(sid, 1, 1, PRIORITY, 0, 1.0f);
        soundPool.unload(sid);
    }

    @Override
    public void onItemClick(View view, int position) {

        // get the ipa string
        String clickedIpaString = adapter.getItem(position);

        // use the string to look up the audio resource id
        int soundId = doubleSound.getSoundResourceId(clickedIpaString);

        // use the id to load (and play) the sound
        soundPool.load(getActivity(), soundId, PRIORITY);
    }
}
