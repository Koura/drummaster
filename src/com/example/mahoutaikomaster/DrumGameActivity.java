package com.example.mahoutaikomaster;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class DrumGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String song = intent.getStringExtra(LevelSelectActivity.LEVEL_NAME);

		setContentView(new GameLevelPanel(this,song));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_drum_game, menu);
		return true;
	}

}
