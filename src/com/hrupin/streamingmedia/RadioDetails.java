package com.hrupin.streamingmedia;

public class RadioDetails {
	private long _id = -1;
	private String radioName;
	private String radioUrl;
	private String radioLang;
	
	public RadioDetails(long _id, String radioName, String radioUrl, String radioLang) {
		super();
		this.radioName = radioName;
		this.radioUrl = radioUrl;
		this.radioLang = radioLang;
		this._id = _id;
	}

	
	public Long get_id() {
		return _id;
	}


	public void set_id(Long _id) {
		this._id = _id;
	}


	public String getRadioName() {
		return radioName;
	}

	public void setRadioName(String radioName) {
		this.radioName = radioName;
	}

	public String getRadioUrl() {
		return radioUrl;
	}

	public void setRadioUrl(String radioUrl) {
		this.radioUrl = radioUrl;
	}

	public String getRadioLang() {
		return radioLang;
	}

	public void setRadioLang(String radioLang) {
		this.radioLang = radioLang;
	}
}
