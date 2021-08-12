package com.example.miwok;

import android.app.Activity;
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
 * Use the {@link NumbersFragment#} factory method to
 * create an instance of this fragment.
 */
public class NumbersFragment extends Fragment {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListner =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                        // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                        // our app is allowed to continue playing sound but at a lower volume. We'll treat
                        // both cases the same way because our app is playing short sound files.

                        // Pause playback and reset player to the start of the file. That way, we can
                        // play the word from the beginning when we resume playback.
                        mMediaPlayer.pause();
                        mMediaPlayer.stop();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback and clean up resources
                        releaseMediaPlayer();
                    }
                }
            };
    private MediaPlayer.OnCompletionListener mComplitionListner = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    public NumbersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);



        final ArrayList<Word> words = new ArrayList<Word>();//final key word is added to reference to OnItenClick
        words.add(new Word("One", "Lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("Two", "Ottiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("Three", "Tolloksu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("Four", "Oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("Five", "Massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("Six", "Temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("Seven", "Kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("Eight", "Kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("Nine", "Wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("Ten", "Na'aacha", R.drawable.number_ten, R.raw.number_ten));
        //There are two methords to set data on screen from java to xml
        // 1st is ListView and ArrayAdapter methord which code is written below

        WordAdapter Adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                //Release the mediaPlayer if it is currently exits because we are about to play a different sound file.
                //If user clicks on different sound file the playing soundFile releases and a new sound file will create and
                // it starts playing with help of this code of release
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListner, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


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

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListner);
        }
    }
}