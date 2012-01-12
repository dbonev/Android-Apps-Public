package com.bubblelake.maia;

import android.content.Context;

public class SalutationProvider extends StringProvider {
	public SalutationProvider(){		
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.salutation));
	}
	@Override
	public String getString(MainTTS context) {
		return this.extra1 + ". ";
	}

	@Override
	public boolean hasExtra1(){
		return true;
	}
	
	@Override
	public String getExtra1Label(Context context){
		return context.getResources().getString(R.string.salutationExtra1);		
	}
	
	@Override
	public boolean canConfigureAdd(){
		return true;
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}
	
	@Override
	public boolean pasteToPrevious(){
		return true;
	}
	
	@Override
	public boolean skipPleaseWait(){
		return true;
	}
	
	@Override
	public String getDisplayName(){
		String suffix = "";
		if (this.extra1 != null && !this.extra1.trim().equals("")){
			suffix = " (" + this.extra1 + ") ";
		}
		return this.displayName + suffix;
	}
}
