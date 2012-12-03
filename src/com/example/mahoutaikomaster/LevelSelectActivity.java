package com.example.mahoutaikomaster;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LevelSelectActivity extends Activity {
    public static final String LEVEL_NAME = "com.MahouTaikoMaster.Level";
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		
		listView = (ListView)findViewById(R.id.game_list);
		String[] levels = new String[]{"Super Mario Bros    *****", "Donkey Kong DK   ***", "Sonic   **", "Crash Bandicoot   *******", "Super Monkey Ball   *", 
				"Picmin   **"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels); 
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{		
				Intent intent = new Intent(LevelSelectActivity.this, DrumGameActivity.class);
				startActivity(intent);
			}	                                
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_level_select, menu);
		return true;
	}
	
	public void previousMenu(View view) {
		finish();
	}

}
