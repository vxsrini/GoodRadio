package com.hrupin.streamingmedia;

import java.util.ArrayList;
import java.util.Locale;

import com.hrupin.streamingmedia.R.id;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class RadioListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private ArrayList<String> names;
	private ArrayList<String> langs;
	private ArrayList<String> urls;
	private ArrayList<Long> ids;
	RadioMediaPlayer rp = new RadioMediaPlayer();
	private boolean dataDelete = false;
	private boolean checkAll = false;
	int currentId = -1;
	int newId = -1;
	ArrayList<String> checkedList = new ArrayList<String>();
	View oldView = null;
	View currentView = null;

	public RadioListAdapter(Context context, ArrayList<String> names,
			ArrayList<String> langs, ArrayList<String> urls, ArrayList<Long> ids) {
		super(context, R.layout.radiolistview, names);
		this.context = context;
		this.names = names;
		this.langs = langs;
		this.urls = urls;
		this.ids = ids;
		rp.setContext(context);
	}

	public ArrayList<String> getCheckedList() {
		return checkedList;
	}

	public void setCheckedList(ArrayList<String> checkedList) {
		this.checkedList = checkedList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}

	public boolean isDataDelete() {
		return dataDelete;
	}

	public void setDataDelete(boolean dataDelete) {
		this.dataDelete = dataDelete;
	}

	public RadioMediaPlayer getRp() {
		return rp;
	}

	public void setRp(RadioMediaPlayer rp) {
		this.rp = rp;
	}

	public int getCurrentId() {
		return currentId;
	}

	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public ArrayList<String> getLangs() {
		return langs;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public ArrayList<Long> getIds() {
		return ids;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}

	public void setLangs(ArrayList<String> langs) {
		this.langs = langs;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public void setIds(ArrayList<Long> ids) {
		this.ids = ids;
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.radiolistview, parent, false);
		TextView name = (TextView) rowView.findViewById(R.id.radio_name);
		Log.i("GoodRadioApp", "" + position);
		// Log.i("In getView - ", String.valueOf(number.size()) + " : " +
		// String.valueOf(numberTmp.size()));
		name.setText(names.get(position));
		TextView lang = (TextView) rowView.findViewById(R.id.radio_lang);
		lang.setText(langs.get(position));

		TextView url = (TextView) rowView.findViewById(R.id.hidden_url);
		url.setText(urls.get(position));

		TextView id = (TextView) rowView.findViewById(R.id.hiddenId);
		id.setText(ids.get(position).toString());
		if (Integer.parseInt(id.getText().toString()) == currentId && currentId != -1){
			name.setTextColor(Color.BLUE);
			oldView = rowView;
		}
		rp.setRlA(this);

		if (dataDelete || checkAll) {
			CheckBox cb = (CheckBox) rowView.findViewById(R.id.indCheckBox);
			cb.setVisibility(View.VISIBLE);
			cb.setTag(String.valueOf(position));
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						Log.i("GoodRadioApp", (String) buttonView.getTag());
						if (!checkedList.contains((String)buttonView.getTag())){
							checkedList.add((String) buttonView.getTag());
						}
						Log.i("GoodRadioApp", "Add - " + checkedList.size());
					} else {
						checkedList.remove((String) buttonView.getTag());
						Log.i("GoodRadioApp", "Remove - " + checkedList.size());
					}

				}
			});

			if (checkAll || checkedList.contains(String.valueOf(position))) {
				((CheckBox) rowView.findViewById(R.id.indCheckBox))
						.setChecked(true);
			}

		}
		rowView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView urlView = (TextView) v.findViewById(R.id.hidden_url);
				TextView idView = (TextView) v.findViewById(R.id.hiddenId);
				newId = Integer.valueOf(idView.getText().toString());
				Log.i("GoodRadioApp", urlView.getText().toString());
				Log.i("GoodRadioApp", "Current Id = " + newId + " : Old Id = " + currentId);
				// ROW ** rp.setRowView(v);

				rp.setUri(urlView.getText().toString());
				rp.setContext(context);
				// ** ROW ** rp.decideOperAndExecute();

				try {
					if (currentId == -1) {
						rp.playRadio();
						currentId = newId;
						TextView name = (TextView) v
								.findViewById(R.id.radio_name);
						Log.i("GoodRadioApp", name.getText().toString());
						oldView = v;
						name.setTextColor(Color.BLUE);
					} else if (currentId == newId) {
						currentId = -1;
						rp.stopRadio(false);
						TextView name = (TextView) v
								.findViewById(R.id.radio_name);
						
						name.setTextColor(Color.parseColor("#212121"));
					} else if (currentId != newId) {
						Log.i("GoodRadioApp", " Inside current id not new id");
						rp.stopRadio(false);
						TextView name = (TextView) oldView
								.findViewById(R.id.radio_name);
						name.setTextColor(Color.parseColor("#212121"));
						currentId = -1;

						rp.playRadio();
						currentId = newId;
						name = (TextView) v.findViewById(R.id.radio_name);
						Log.i("GoodRadioApp", name.getText().toString());
						name.setTextColor(Color.BLUE);
						oldView = v;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					rp.stopRadio(false);
					notifyDataSetChanged();
				}

			}
		});
		return rowView;
	}

	public int getNewId() {
		return newId;
	}

	public void setNewId(int newId) {
		this.newId = newId;
	}

	public View getOldView() {
		return oldView;
	}

	public void setOldView(View oldView) {
		this.oldView = oldView;
	}

	public View getCurrentView() {
		return currentView;
	}

	public void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

}
