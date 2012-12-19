package com.example.mahoutaikomaster;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Dj {

	private MediaPlayer levelSong;
	private Context context;
	private SoundPool soundPool;
	private AudioManager audioManager;
	private HashMap<Integer, Integer> mSoundPoolMap;

	public Dj(Context context) {
		this.context = context;
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mSoundPoolMap.put(0, soundPool.load(context, R.raw.don, 1));
		mSoundPoolMap.put(1, soundPool.load(context, R.raw.katsu, 1));
	}

	public void playSong(String song) {
		song = song.replaceAll(" ", "");
		song = song.toLowerCase();
		try {
			int resID = context.getResources().getIdentifier(song, "raw",
					context.getPackageName());
			levelSong = MediaPlayer.create(context, resID);
			levelSong.start();
		} catch (Exception e) {

		}
	}

	public void stopSong() {
		try {
			if (levelSong.isPlaying()) {
				levelSong.release();
			}
		} catch (Exception e) {

		}
	}

	public void playDrum(int index) {
		float streamVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1,
				0, 1f);
	}
}
