package com.hrupin.streamingmedia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import wseemann.media.FFmpegMediaPlayer;
import wseemann.media.FFmpegMediaPlayer.OnBufferingUpdateListener;
import wseemann.media.FFmpegMediaPlayer.OnCompletionListener;
import wseemann.media.FFmpegMediaPlayer.OnErrorListener;
import wseemann.media.FFmpegMediaPlayer.OnPreparedListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RadioMediaPlayer implements OnCompletionListener,
		OnBufferingUpdateListener, OnErrorListener, OnPreparedListener{
		private FFmpegMediaPlayer mediaPlayer = new FFmpegMediaPlayer();
	private String uri;
	RadioListAdapter rlA = null;
	Context context = null;
	int status = 0; /* 0 = Stopped; 1 = Playing; 2 = Paused */

	public RadioMediaPlayer() {
		super();
	}

	public RadioMediaPlayer(String uri, View rowView, Context context) {
		super();
		this.uri = uri;
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public RadioListAdapter getRlA() {
		return rlA;
	}

	public void setRlA(RadioListAdapter rlA) {
		this.rlA = rlA;
	}


	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean stopRadio(boolean inCall) {
		mediaPlayer.reset();
		if (!inCall){
			status = 0;
		}
		return true;
	}

	public boolean playRadio() throws Exception {
		
		if (uri == null){
			return false;
		}
		
		
		try {
			Log.i("GoodRadioApp", "Setting the data source");
			mediaPlayer.setDataSource(uri);
			Log.i("GoodRadioApp", "Data Source successfully set");
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.prepareAsync();
			return true;
		}
		catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			mediaPlayer.reset();
			throw e;
		}
		
	}

	@Override
	public void onCompletion(FFmpegMediaPlayer mp) {
		// buttonPlayPause.setImageResource(R.drawable.button_play);
		// Toast.makeText(context, "Playing ", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onBufferingUpdate(FFmpegMediaPlayer mp, int percent) {
		// seekBarProgress.setSecondaryProgress(percent);
		Toast.makeText(context, "Buffering " + String.valueOf(percent) + "%",
		Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onError(FFmpegMediaPlayer mp, int what, int extra){
		Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
		mediaPlayer.reset();
		rlA.notifyDataSetChanged();
		return true;
	}
	
	@Override
	public void onPrepared(FFmpegMediaPlayer mp) {
		Log.i("GoodRadioApp", "Suucessfully Prepeared");
		mp.start();
		status = 1;
	}
}
