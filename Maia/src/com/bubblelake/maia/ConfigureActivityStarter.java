package com.bubblelake.maia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;


public class ConfigureActivityStarter {
	public static void startConfigureActivity(StringProvider provider, Context parent, int requestId, Greeting greeting){
		int index = greeting.getProviders().indexOf(provider);
		Class c = index > -1 ? provider.getConfigureUpdateActivityClass() : provider.getConfigureActivityClass();
		Intent intent = new Intent(parent, c);
		intent.putExtra("greeting", GreetingsFactory.getGreetings(parent).indexOf(greeting));
		intent.putExtra("provider", index);
		intent.putExtra("providerClass", provider.getClass().getName());
		provider.addAdditionalInfo(intent);
		try {
			((Activity)parent).startActivityForResult(intent, requestId);
		} catch(ActivityNotFoundException ex){
			throw ex;
		}
	}
}