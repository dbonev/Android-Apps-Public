package com.bubblelake.maia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GreetingDisplayControl extends LinearLayout{
	public static final int EDIT_GREETING_CODE = 10;
	public static final int PLAY_GREETING_CODE = 11;
	LinearLayout playTimePanel;
	Button playButton;
	Greeting greeting;
	Button viewButton;
	Button removeButton;
	Button skipButton;
	
	public GreetingDisplayControl(Context context, final Greeting greeting) {
		super(context);
		
		//this.setBackgroundDrawable(getResources().getDrawable(R.drawable.loc_bg));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(5, 5, 5, 5);
		this.greeting = greeting;
		TextView nameText = new TextView(context);
		nameText.setText(greeting.getName());
		nameText.setTextSize(25);
		nameText.setTextColor(Color.BLACK);
		this.addView(nameText);		
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 10);
		
		playButton = new Button(context);
		playButton.setText(context.getResources().getString(R.string.sayIt));
		playButton.setLayoutParams(lp);
		playButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startSpeaking(greeting, null);												
			}
		});
		playButton.setTextSize(25);
		playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.sayit_bg_x));
		set_play_stop_icon(true);
		this.addView(playButton);
		
		construct_playtime();
		
		LinearLayout optionsLayout = new LinearLayout(context);
		optionsLayout.setOrientation(LinearLayout.VERTICAL);
		
		this.addView(optionsLayout);
		viewButton = new Button(context);
		viewButton.setText(context.getResources().getString(R.string.viewConfigure));		
		viewButton.setTextSize(20);
		viewButton.setLayoutParams(lp);		
		Drawable icon2 = getResources().getDrawable(R.drawable.ic_menu_settings);
		//icon.setBounds(new Rect(5, 5, 37, 37));
		viewButton.setCompoundDrawablesWithIntrinsicBounds(icon2, null, null, null);
		viewButton.setCompoundDrawablePadding(16);
		optionsLayout.addView(viewButton);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		viewButton.setLayoutParams(params);
		viewButton.setOnClickListener( new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetingDisplayControl.this.getContext(), GreetingViewActivity.class);
				intent.putExtra("greeting", GreetingsFactory.getGreetings(GreetingDisplayControl.this.getContext()).indexOf(greeting));	
				try {
					((Activity)GreetingDisplayControl.this.getContext()).startActivityForResult(intent, EDIT_GREETING_CODE);
				} catch(ActivityNotFoundException ex){
					throw ex;
				}
			}
		});
		
		if (GreetingsFactory.getGreetings(getContext()).size() > 1){
			removeButton = new Button(context);
			removeButton.setText(context.getResources().getString(R.string.remove));
			Drawable icon3 = getResources().getDrawable(R.drawable.ic_menu_close_32);
			//icon.setBounds(new Rect(5, 5, 37, 37));
			removeButton.setCompoundDrawablesWithIntrinsicBounds(icon3, null, null, null);
			removeButton.setCompoundDrawablePadding(16);
			removeButton.setTextSize(20);
			removeButton.setLayoutParams(lp);
			optionsLayout.addView(removeButton);
			removeButton.setLayoutParams(params);
			removeButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog dialog = new AlertDialog.Builder(GreetingDisplayControl.this.getContext()).create();				
					dialog.setMessage("Are you sure?");
					dialog.setButton("Yes",new DialogInterface.OnClickListener(){
	
						//@Override
						public void onClick(DialogInterface dialog, int which) {
							GreetingsFactory.getGreetings(GreetingDisplayControl.this.getContext()).remove(greeting);
							SettingsManager.getInstance(GreetingDisplayControl.this.getContext()).Save(GreetingDisplayControl.this.getContext());
							MainTTS.getInstance().buildUI();
						}});
					dialog.setButton2("No", new DialogInterface.OnClickListener(){
	
						//@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}});
					dialog.show();				
				}
			});
		}
	}

	private void set_play_stop_icon(boolean play) {
		Drawable icon = play ? getResources().getDrawable(R.drawable.sayit_i) : getResources().getDrawable(R.drawable.stop_i);
		//icon.setBounds(new Rect(5, 5, 37, 37));
		playButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
	}
	
	protected void launchPlayActivity() {
		Intent intent = new Intent(GreetingDisplayControl.this.getContext(), PlayActivity.class);
		intent.putExtra("greeting", GreetingsFactory.getGreetings(GreetingDisplayControl.this.getContext()).indexOf(greeting));	
		try {
			((Activity)GreetingDisplayControl.this.getContext()).startActivityForResult(intent, PLAY_GREETING_CODE);
		} catch(ActivityNotFoundException ex){
			throw ex;
		}
	}

	private void setSpeakCompleteObserver(){
		this.greeting.addSpeakCompleteObserver(new ISpeakCompleteObserver(){
			@Override
			public void speakCompleted(Greeting g){
				//MainTTS.getInstance().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
				((Activity)GreetingDisplayControl.this.getContext()).runOnUiThread( new Runnable() { public void run(){
					refresh_playtime();
					refreshPlayButtonText();					
					Activity a = ((Activity)getContext());
					if (a instanceof PlayActivity){
						a.finish();
					}
				}});								
			}
			@Override
			public void onPause(Greeting g){
				
			}
			@Override
			public void onResume(Greeting g){
				
			}
		});
	}
		
	
	private void refresh_playtime(){		
		if (this.playTimePanel != null){
			if (greeting.isSpeaking()){
				playTimePanel.setVisibility(View.VISIBLE);
				this.viewButton.setVisibility(View.GONE);
				if (this.removeButton != null){
					this.removeButton.setVisibility(View.GONE);
				}
				this.playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.stopit_bg_x));
				set_play_stop_icon(false);
				
			} else {
				playTimePanel.setVisibility(View.GONE);
				this.viewButton.setVisibility(View.VISIBLE);
				if (this.removeButton != null){
					this.removeButton.setVisibility(View.VISIBLE);
				}
				this.playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.sayit_bg_x));
				set_play_stop_icon(true);
			}
		}
	}
	
	private void construct_playtime(){
		playTimePanel = new LinearLayout(this.getContext());
		playTimePanel.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		playTimePanel.setLayoutParams(params);
		
		Button pauseButton = new Button(this.getContext());
		pauseButton.setText(this.getContext().getResources().getString(R.string.pause));
		playTimePanel.addView(pauseButton);
		pauseButton.setLayoutParams(params);
		setPauseListener(pauseButton);
		
		skipButton = new Button(this.getContext());
		skipButton.setText(this.getContext().getResources().getString(R.string.skipThis));
		playTimePanel.addView(skipButton);
		skipButton.setLayoutParams(params);
		setSkipListener(skipButton);			
		
		playTimePanel.setVisibility(View.GONE);
		this.addView(playTimePanel);
	}
	
	private void setPauseListener(final Button pauseButton) {
		pauseButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if (greeting.isSpeaking()){
					if (greeting.isInPause()){
						greeting.resume();
						pauseButton.setText(getResources().getString(R.string.pause));
						skipButton.setVisibility(View.VISIBLE);
					} else {
						greeting.pause();
						pauseButton.setText(getResources().getString(R.string.resume));
						skipButton.setVisibility(View.GONE);
					}
				}
				/* if (!greeting.isSpeaking()){					
					refreshPlayButtonText();
					refresh_playtime();
				} */
			}
		}
		);
	}

	private void refreshPlayButtonText(){
		if (this.playButton != null){
			if (this.greeting.isSpeaking()){
				playButton.setText(getResources().getString(R.string.stop));				
			} else {
				playButton.setText(getResources().getString(R.string.sayIt));
			}
		}
	}
	
	private void setSkipListener(Button skipButton){
		skipButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				greeting.skipCurrent();
				/* if (!greeting.isSpeaking()){					
					refreshPlayButtonText();
					refresh_playtime();
				} */
			}
		}
		);
	}

	public void startSpeaking(final Greeting greeting, final StringProvider provider) {
		boolean inPlayMode = false;
		MainTTS activity;
		if (getContext() instanceof MainTTS){
			activity = (MainTTS)GreetingDisplayControl.this.getContext();
		} else {
			activity = MainTTS.getInstance();
			inPlayMode = true;
		}
		
		if (!inPlayMode){
			launchPlayActivity();
			return;
		}
		//activity.setRequestedOrientation(activity.getRequestedOrientation());
		this.setSpeakCompleteObserver();
		
		final TextToSpeech tts = activity.getSpeaker();
		
		if (greeting.isSpeaking()){
			stop_speaking(tts);					
			refreshPlayButtonText();
			refresh_playtime();
			((Activity)this.getContext()).finish();
		} 
		else {					
			PlayActivity player = (PlayActivity)this.getContext();
			GreetingDisplayControl.this.greeting.play(activity, player, provider);
			refreshPlayButtonText();
			refresh_playtime();
		}
	}

	public void stop_speaking(final TextToSpeech tts) {
		GreetingDisplayControl.this.greeting.stop();
		tts.stop();
	}	

}
