package com.hrupin.streamingmedia;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class RadioDB {


	private static final String DATABASE_CREATE = "create table RadioDetails(_id integer primary key autoincrement, "
			+ "name text not null,"
			+ "uri text not null,"
			+ "lang text not null" + ");";
	
	private static String DBPath = Environment.getDataDirectory().getName()+"/data/com.hrupin.streamingmedia/";

	private static final String DATABASE_NAME = "RadioDB";
	
	private static String myPath = DBPath + DATABASE_NAME;

	private static final String DATABASE_TABLE = "RadioDetails";


	private SQLiteDatabase db;

	public RadioDB(Context ctx) {
		try {
			db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteCantOpenDatabaseException e) {
			try {
				db = SQLiteDatabase.openOrCreateDatabase(myPath, null);
				
				db.execSQL(DATABASE_CREATE);
			} catch (Exception e1) {
				Log.i("GoodRadioApp", e1.toString());
				db = null;
			}
		}
	}
	
	public void close() {
		db.close();
	}

	public void createRow(String name, String uri, String lang) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", name);
		initialValues.put("uri", uri);
		initialValues.put("lang", lang);
		db.insert(DATABASE_TABLE, null, initialValues);
	}

	public void deleteRow(long rowId) {
		db.delete(DATABASE_TABLE, "_id=" + rowId, null);
	}
	
	public void deleteMultipleRow(String ids) {
		Log.i("GoodRadioApp ", ids);
		db.delete(DATABASE_TABLE, "_id in (" + ids + ")", null);
	}


	public ArrayList<RadioDetails> fetchAllRows() {
		ArrayList<RadioDetails> ret = new ArrayList<RadioDetails>();
		try {
			Log.i("GoodRadioApp", "Created Successfully " + db.isOpen() );

			Cursor c = db.query(DATABASE_TABLE, new String[] { "_id", "name",
					"uri", "lang" }, null, null, null, null, " lang, name desc");
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				ret.add(new RadioDetails(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("GoodRadioApp", e.toString());
		}
		return ret;
	}

	public RadioDetails fetchRow(long rowId) {
		//Row row = new Row();
		RadioDetails rd = new RadioDetails(-1, null, null, null);
		Cursor c = db.query(true, DATABASE_TABLE, new String[] { "_id", "name",
				"name", "lang" }, "_id=" + rowId, null, null, null, " lang, name desc", null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			rd.set_id(c.getLong(0));
			rd.setRadioName(c.getString(1));
			rd.setRadioUrl(c.getString(2));
			rd.setRadioLang(c.getString(3));
			
		} 
		return rd;
	}

	public void updateRow(long rowId, String name, String uri, String lang) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("uri", uri);
		args.put("lang", lang);
		db.update(DATABASE_TABLE, args, "_id=" + rowId, null);
	}

	public Cursor GetAllRows() {
		try {
			return db.query(DATABASE_TABLE, new String[] { "_id", "name", "uri",
					"lang" }, null, null, null, null, " lang, name desc");
		} catch (SQLException e) {
			Log.e("GoodRadioApp", e.toString());
			return null;
		}
	}
}