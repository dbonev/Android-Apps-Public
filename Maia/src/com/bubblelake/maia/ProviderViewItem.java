package com.bubblelake.maia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProviderViewItem extends LinearLayout {
	StringProvider provider;
	Greeting greeting;
	public static final int ADD_ACTIVITY_ID = 10;
	public static final int ACTIONS_ACTIVITY_ID = 13;
	public ProviderViewItem(Context context) {
		super(context);		
	}
	
	public ProviderViewItem(Context context, StringProvider provider, Greeting greeting) {
		this(context);	
		this.provider = provider;
		this.greeting = greeting;
	}
	
	public StringProvider getProvider(){
		return this.provider;
	}
	
	protected  int get_layout_id(){
		return R.layout.provider_view_item;
	}
	
	public void inflate() {		
		LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
		View res = inflater.inflate(get_layout_id(), null);		
		this.addView(res);
		//this.setBackgroundDrawable(getResources().getDrawable(R.drawable.loc_bg));
		
		TextView nameText = (TextView)findViewById(R.id.providerNameText);
		int index = (this.greeting.getProviders().indexOf(this.provider) + 1);
		String number = index > 0 ? index + ". " : "";
		String text =  number + this.provider.getDisplayName();
		nameText.setText(text);	
		
		subscribeButtonsListeners();
		
	}

	protected void subscribeButtonsListeners() {
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchActionsActivity();
			}
		});
	}
	
	protected void launchActionsActivity() {
		Intent intent = new Intent(this.getContext(), ProviderActionsActivity.class);
		intent.putExtra("greeting", GreetingsFactory.getGreetings(this.getContext()).indexOf(greeting));
		intent.putExtra("provider", greeting.getProviders().indexOf(this.provider));
		try {
			((Activity)this.getContext()).startActivityForResult(intent, ACTIONS_ACTIVITY_ID);
		} catch(ActivityNotFoundException ex){
			throw ex;
		}
	}	
		
	
	protected void launch_configure_activity(int id) {
		ConfigureActivityStarter.startConfigureActivity(this.provider, this.getContext(), id, this.greeting);
		/* Intent intent = new Intent(this.getContext(), ProviderConfigureActivity.class);
		intent.putExtra("greeting", GreetingsFactory.getGreetings(this.getContext()).indexOf(greeting));
		intent.putExtra("provider", greeting.getProviders().indexOf(this.provider));
		try {
			((Activity)this.getContext()).startActivityForResult(intent, id);
		} catch(ActivityNotFoundException ex){
			throw ex;
		} */
	}
	
}
