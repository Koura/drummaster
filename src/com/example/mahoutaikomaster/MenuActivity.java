package com.example.mahoutaikomaster;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);		
		setContentView(R.layout.activity_menu);
		blinkText();
	}
	
	public void blinkText() {
	    TextView blinkText = (TextView)findViewById(R.id.blinkInstruction);
	    
	    Animation anim = new AlphaAnimation(0.0f, 1.0f);
	    anim.setDuration(500);
	    anim.setStartOffset(20);
	    anim.setRepeatMode(Animation.REVERSE);
	    anim.setRepeatCount(Animation.INFINITE);
	    blinkText.startAnimation(anim);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

}
