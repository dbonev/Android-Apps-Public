package com.bubblelake.maia;

import android.location.Address;

public class CityNameProvider extends StringProvider {
	public String getString(MainTTS context){
		Address address = context.getAddress();
		if (address == null){
			return "";
		} else {
			if (address.getLocality() != null){
				return address.getLocality();
			} else if (address.getAdminArea() != null){
				return address.getAdminArea();				
			} else if (address.getCountryName() != null){
				return address.getCountryName();
			} else {
				return "";
			}
		}
	}
	
	public String getCountryName(MainTTS context){
		Address address = context.getAddress();
		if (address == null){
			return "";
		} else {
			if (address.getCountryName() != null){
				return address.getCountryName();
			} else {
				return "";
			}
		}
	}
	
}
