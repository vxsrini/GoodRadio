package com.hrupin.streamingmedia;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hrupin.streamingmedia.R;

public class StreamingMp3Player extends Activity {

	public EditText editTextSongURL;
	private int position = 0;
	public ArrayList<RadioDetails> radioList = null;
	private final int ADD_STATION = 1;
	private final int DELETE_STATION = 2;
	RadioDB db = null;
	Context context = null;
	ListView listView = null;
	RadioListAdapter radioAdapter = null;
	
	PhoneStateListener phoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        if (state == TelephonyManager.CALL_STATE_RINGING) {
	        	Log.i("GoodRadioApp", "Call State Ringing");
	        	radioAdapter.getRp().stopRadio(false);
	        	radioAdapter.setCurrentId(-1);
	        	radioAdapter.setOldView(null);
	        	radioAdapter.setCurrentView(null);
	        	radioAdapter.setNewId(-1);
	        	radioAdapter.notifyDataSetChanged();
	        	Log.i("GoodRadioApp", "Call State Ringing - Stopped Radio");
	        } else if(state == TelephonyManager.CALL_STATE_IDLE) {
	           //Play music
	        	if(radioAdapter.getRp().getStatus() == 1){
	        		try {
	        			Log.i("GoodRadioApp", "Call State Idle");
	        			//radioAdapter.getRp().playRadio();
	        			Log.i("GoodRadioApp", "Call State Idle - Radio Started");
	        		} catch (Exception e){
	        			
	        		}
	        	}
	        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
	           //Pause music
	        	Log.i("GoodRadioApp", "Call State Off Hook");
	        	radioAdapter.getRp().stopRadio(false);
	        	radioAdapter.setCurrentId(-1);
	        	radioAdapter.setOldView(null);
	        	radioAdapter.setCurrentView(null);
	        	radioAdapter.setNewId(-1);
	        	radioAdapter.notifyDataSetChanged();
        		Log.i("GoodRadioApp", "Call State Off Hook - Radio Stopped");
   	
	        }
	        super.onCallStateChanged(state, incomingNumber);
	    }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getBaseContext();
	
		db = new RadioDB(this);

		listView = (ListView) findViewById(R.id.contactList);

		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> langs = new ArrayList<String>();
		ArrayList<String> urls = new ArrayList<String>();
		ArrayList<Long> ids = new ArrayList<Long>();

		radioList = db.fetchAllRows();

		for (int count = 0; count < radioList.size(); count++) {
			names.add(radioList.get(count).getRadioName());
			langs.add(radioList.get(count).getRadioLang());
			urls.add(radioList.get(count).getRadioUrl());
			ids.add(radioList.get(count).get_id());
		}

		radioAdapter = new RadioListAdapter(getBaseContext(), names, langs,
				urls, ids);
		
		TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if(mgr != null) {
			Log.i("GoodRadioApp", "Registering Phone Listener");
		    mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
		
		listView.setAdapter(radioAdapter);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Define your menu, giving each button a unique identifier numbers
		// (MENU_PAUSE, etc)
		// This is called only once, the first time the menu button is clicked
		menu.add(0, ADD_STATION, 0, "Add Station");
		menu.add(0, DELETE_STATION, 0, "Delete Station");
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		// This is called every time the menu button is pressed. In my game, I
		// use this to show or hide the pause/resume buttons depending on the
		// current state
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// and this is self explanatory
		boolean handled = false;

		switch (item.getItemId()) {
		case ADD_STATION:
			addStation();
			handled = true;
			break;

		case DELETE_STATION:
			deleteStation();
			handled = true;
			break;
		}
		return handled;
	}

	public void addStation() {
		Intent menuIntent = new Intent(StreamingMp3Player.this,
				MenuActivity.class);
		startActivity(menuIntent);
	}

	public void deleteStation() {

		((Button) findViewById(R.id.doneButton)).setVisibility(View.VISIBLE);
		((CheckBox) findViewById(R.id.checkBox1)).setVisibility(View.VISIBLE);

		ListView lv = (ListView) findViewById(R.id.contactList);

		((RadioListAdapter) lv.getAdapter()).setDataDelete(true);
		
		((RadioListAdapter) lv.getAdapter()).notifyDataSetChanged();

	}

	public void deleteInDB(View v) {
		Log.i("GoodRadioApp", " Delete in DB");
		ListView lv = (ListView) findViewById(R.id.contactList);
		((Button) findViewById(R.id.doneButton)).setVisibility(View.GONE);
		((CheckBox) findViewById(R.id.checkBox1)).setVisibility(View.GONE);
		((RadioListAdapter) lv.getAdapter()).setDataDelete(false);
		((RadioListAdapter) lv.getAdapter()).setCheckAll(false);

		if (radioAdapter.getCheckedList().size() > 0) {
			String ids = new String();
			for (int count = 0; count < radioAdapter.getCheckedList().size(); count++) {
				int position = Integer.parseInt(radioAdapter.getCheckedList()
						.get(count));

				ids = ids + radioList.get(position).get_id();

				if (count != (radioAdapter.getCheckedList().size() - 1)) {
					ids = ids + " ,";
				}
			}

			db.deleteMultipleRow(ids);
			
			radioAdapter.getCheckedList().clear();

			radioList = db.fetchAllRows();
			
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> langs = new ArrayList<String>();
			ArrayList<String> urls = new ArrayList<String>();
			ArrayList<Long> idslong = new ArrayList<Long>();

			radioList = db.fetchAllRows();

			for (int count = 0; count < radioList.size(); count++) {
				names.add(radioList.get(count).getRadioName());
				langs.add(radioList.get(count).getRadioLang());
				urls.add(radioList.get(count).getRadioUrl());
				idslong.add(radioList.get(count).get_id());
			}
			
			radioAdapter.setNames(names);
			radioAdapter.setLangs(langs);
			radioAdapter.setUrls(urls);
			radioAdapter.setIds(idslong);
			
		}

		((RadioListAdapter) lv.getAdapter()).notifyDataSetChanged();

	}

	public void checkAll(View v) {
		ListView lv = (ListView) findViewById(R.id.contactList);
		boolean checkedState = ((CheckBox) findViewById(R.id.checkBox1))
				.isChecked();
		radioAdapter.getCheckedList().clear();
		((RadioListAdapter) lv.getAdapter()).setCheckAll(checkedState);
		((RadioListAdapter) lv.getAdapter()).notifyDataSetChanged();
	}
	
}
