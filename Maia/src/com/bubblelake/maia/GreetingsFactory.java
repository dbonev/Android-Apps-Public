package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class GreetingsFactory {
	private static Greeting temporaryGreeting;
	public static List<Greeting> getGreetings(Context context){
		ArrayList<Greeting> res = (ArrayList<Greeting>) SettingsManager.getInstance(context).getGreetings();		
		return res;
	}
	public static void setTemporaryGreeting(Greeting temporaryGreeting) {
		GreetingsFactory.temporaryGreeting = temporaryGreeting;
	}
	public static Greeting getTemporaryGreeting() {
		return temporaryGreeting;
	}
}
