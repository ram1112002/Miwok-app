package com.example.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FamilyFragment#} factory method to
 * create an instance of this fragment.
 */
public class FamilyFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListner=
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                    {
                        // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                        // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                        // our app is allowed to continue playing sound but at a lower volume. We'll treat
                        // both cases the same way because our app is playing short sound files.

                        // Pause playback and reset player to the start of the file. That way, we can
                        // play the word from the beginning when we resume playback.
                        mMediaPlayer.pause();
                        mMediaPlayer.stop();
                        mMediaPlayer.seekTo(0);
                    }else if (focusChange== AudioManager.AUDIOFOCUS_GAIN){

                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                        mMediaPlayer.start();
                    }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback and clean up resources
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mComplitionListner=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.word_list, container, false);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Father", "әpә",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("Mother", "әṭa",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("Son", "angsi",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("Daughter", "tune",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("Older Brother", "taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("Younger Brother", "chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("Older Sister", "teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("Younger Sister", "kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("GrandMother", "ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("GrandFather", "paapa",R.drawable.family_grandfather,R.raw.family_grandfather));
        //There are two methords to set data on screen from java to xml
        // 1st is ListView and ArrayAdapter methord which code is written below

        WordAdapter Adapter = new WordAdapter(getActivity(), words,R.color.category_family);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word=words.get(position);
                releaseMediaPlayer();

                int result=mAudioManager.requestAudioFocus(mOnAudioFocusChangeListner,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {



                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourseId());
                    mMediaPlayer.start();

                    //Setup a Listener on Media Player,so that we can stop and release the media player once the sound is finished playing
                    mMediaPlayer.setOnCompletionListener(mComplitionListner);
                }
            }
        });
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
   }
    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;


        }
    }
}