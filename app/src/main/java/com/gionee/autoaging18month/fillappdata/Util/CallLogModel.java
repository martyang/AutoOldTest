package com.gionee.autoaging18month.fillappdata.Util;

public class CallLogModel {


	private String number;
	private int type;
	private int addTime;
	private long date;
	private int duration;
	private boolean isPrivateNumber;
	private int addModel;
	private int isClear;
	public final int INCOMING_TYPE_CODE = 1;
	public final int OUTGOING_TYPE_CODE = 2;
	public final int MISSED_TYPE_CODE = 3;
	public final int OUTGOING_FAILED_TYPE_CODE = 4;

	public final int OnlyOneNumber_Code = 2;
	public final int AutoConstruct_Code = 1;

	public final String YES = "YES";
	public final String NO = "NO";

	public final int YES_CODE = 1;
	public final int NO_CODE = 0;

	private final int DURATION = 1000;

	private final boolean ISPRiVATENUMBER = false;
	
	private int sim_id=0;

	public CallLogModel(){
		number = "";
		type = 0;
		addTime = 0;
		date = System.currentTimeMillis();
		duration = DURATION;
		isPrivateNumber = ISPRiVATENUMBER;
		addModel = 0;
		isClear = 0;
	}


	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getAddTime() {
		return addTime;
	}
	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public boolean isPrivateNumber() {
		return isPrivateNumber;
	}
	public void setPrivateNumber(boolean isPrivateNumber) {
		this.isPrivateNumber = isPrivateNumber;
	}
	public int getAddModel() {
		return addModel;
	}
	public void setAddModel(int addModel) {
		this.addModel = addModel;
	}
	public int getIsClear() {
		return isClear;
	}
	public void setIsClear(int isClear) {
		this.isClear = isClear;
	}


	public int getSim_id() {
		return sim_id;
	}


	public void setSim_id(int sim_id) {
		this.sim_id = sim_id;
	}
	
	
}
