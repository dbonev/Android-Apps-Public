package com.bubblelake.maia;

import android.content.Context;
import android.content.Intent;

import java.lang.Class;
import java.util.ArrayList;

public abstract class StringProvider {
	protected String extra1;
	protected String extra2;
	protected String extra3;
	protected String displayName;
	
	public abstract String getString(MainTTS context);
	public String[] getExtendedString(MainTTS context){
		return new String[]{getString(context)};
	}
	
	public String finishWith(MainTTS context){
		return ". ";
	}
	
	public int getFootPrint(){
		return 1;
	}
	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}
	public String getExtra1() {
		return extra1 == null ? "" : extra1;
	}
	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}
	public String getExtra2() {
		return extra2 == null ? "" : extra2;
	}
	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}
	public String getExtra3() {
		return extra3 == null ? "" : extra3;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDisplayNameForSave() {
		return displayName;
	}
	
	public boolean hasExtra1() {
		return false;
	}
	
	public boolean hasExtra2() {
		return false;
	}
	
	public boolean hasExtra3() {
		return false;
	}
	
	public String getExtra1Label(Context context){
		return "Extra1";
	}
	
	public String getExtra2Label(Context context){
		return "Extra2";
	}
	
	public String getExtra3Label(Context context){
		return "Extra3";
	}
	
	public Class getConfigureActivityClass(){
		return ProviderConfigureActivity.class;
	}
	
	public Class getConfigureUpdateActivityClass(){
		return getConfigureActivityClass();
	}
	
	public boolean canConfigureAdd(){
		return false;
	}
	
	public boolean canConfigureUpdate(){
		return false;
	}
	
	public boolean canDuplicate(){
		return canConfigureAdd() || canConfigureUpdate();
	}
	
	public void addAdditionalInfo(Intent intent){
		
	}
	
	public boolean pasteToPrevious(){
		return false;
	}
	
	public boolean skipPleaseWait(){
		return false;
	}
	
	public String[] toStringArray(ArrayList<String> source){
		if (source == null){
			return new String[0];
		} else {
			String[] res = new String[source.size()];
			for (int i = 0; i < source.size(); i++){
				res[i] = source.get(i);
			}
			return res;
		}
	}
}
