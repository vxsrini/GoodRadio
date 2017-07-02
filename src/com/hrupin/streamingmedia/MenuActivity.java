package com.hrupin.streamingmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	RadioDB db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addradiostation);
		db = new RadioDB(getBaseContext());
		
	}
	
	public void insertStation(View v){
		String name = ((TextView) findViewById(R.id.addStationName)).getText().toString();
		String uri = ((TextView) findViewById(R.id.addStationUri)).getText().toString();
		String lang = ((Spinner) findViewById(R.id.addStationLang)).getSelectedItem().toString();
		if (name == null || name.trim().length() == 0 ){
			Toast.makeText(getBaseContext() , "Radio Name Field Null", Toast.LENGTH_SHORT).show();
			return;
		}
		if (uri == null || uri.trim().length() == 0 ){
			Toast.makeText(getBaseContext() , "Radio URI Field Null", Toast.LENGTH_SHORT).show();
			return;
		}

		db.createRow(name, uri, lang);		
		Toast.makeText(getBaseContext() , "Successfully Inserted", Toast.LENGTH_SHORT).show();
		
		Intent oldIntent = new Intent(MenuActivity.this, StreamingMp3Player.class);
		startActivity(oldIntent);
	}
	
	public void clearForm(View v){
		((TextView) findViewById(R.id.addStationName)).setText("");
		((TextView) findViewById(R.id.addStationUri)).setText("");
		((Spinner) findViewById(R.id.addStationLang)).setSelection(0);
	}
}
