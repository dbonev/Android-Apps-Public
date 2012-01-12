package com.bubblelake.maia;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.HashMap;
import java.util.regex.Pattern;


public class Greeting {
	private String name;
	private ArrayList<StringProvider> providers = new ArrayList<StringProvider>();
	private boolean isStoped;
	private ArrayList<String> playable;
	private int current = 0;
	private MainTTS context;
	private PlayActivity player;
	private boolean mIsSpeaking;
	private ArrayList<ISpeakCompleteObserver> speakCompleteObservers;
	private boolean skipping = false;
	private StringProvider provider;
	private boolean inPause = false;
	
	private long shakeTime = 0;
	private long startMeasure = 0;
	private int counter = 0;
	
	ProgressDialog dialog;
	
	private class FetchThread extends Thread{
		@Override
        public void run() {		
			StringBuilder sb = new StringBuilder();
			int counter = 0;
			for (StringProvider prov : providers){
				if (provider != null && prov != provider){
					continue;
				}
				if (isStoped){
					return;
				}
				try {			
					String[] split = prov.getExtendedString(context);
					int tempCounter = 0;
					for (String s : split){												
						if (tempCounter++ == split.length - 1){
							playable.add(s + prov.finishWith(context));
						} else {
							playable.add(s);
						}
					}
					
//					//sb.append(prov.finishWith(context));
//					//totalFootprint += prov.getFootPrint();
//					boolean pasteToPrevious = provider == null && counter < providers.size() - 1 && providers.get(counter + 1).pasteToPrevious(); 
//					if (!pasteToPrevious){
//						playable.add(sb.toString());
//						sb = new StringBuilder();
//					} 
//					counter++;
					Log.i("Maia", "Taken from " + prov.getDisplayName() + " is the text " + sb.toString());
					
					
//					sb.append(prov.getExtendedString(context));
//					sb.append(prov.finishWith(context));
//					//totalFootprint += prov.getFootPrint();
//					boolean pasteToPrevious = provider == null && counter < providers.size() - 1 && providers.get(counter + 1).pasteToPrevious(); 
//					if (!pasteToPrevious){
//						playable.add(sb.toString());
//						sb = new StringBuilder();
//					} 
//					counter++;
//					Log.i("Maia", "Taken from " + prov.getDisplayName() + " is the text " + sb.toString());
				} catch (Exception ex){
					Log.i("Maia", "Exception while getting text from " + prov.getDisplayName() + " ex: " + ex.toString());
					if (provider != null){
						playable.add(String.format(SettingsManager.getInstance(null).getString(R.string.fallbackOne), provider.getDisplayNameForSave()));
					}
				}			
			}
			
			handler.sendEmptyMessage(0);
		}
		
		private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	if (dialog != null){
        			try {
        				dialog.dismiss();
        			} catch (Exception ex) {
        				Log.e("Maia", ex.toString());
        			}
        		}
            	
            	playCurrent();
            }
		};
	}
	
	//~ public void Play(MainTTS context){
		//~ isStoped = false;
		//~ StringBuilder sb = new StringBuilder();
		//~ int totalFootprint = 0;
		//~ for (StringProvider prov : providers){
			//~ if (isStoped){
				//~ return;
			//~ }
			//~ try {
			//~ sb.append(prov.getExtendedString(context));
			//~ sb.append(prov.finishWith(context));
			//~ totalFootprint += prov.getFootPrint();
			
//~ //			if (totalFootprint >= 5){
//~ //				if (isStoped){
//~ //					return;
//~ //				}
//~ //				if (speak(context, sb)){
//~ //					sb = new StringBuilder();
//~ //					totalFootprint = 0;
//~ //				}
//~ //			}
			//~ } catch (Exception ex){
				//~ //intentionally do nothing
			//~ }
			
		//~ }
		
		//~ if (sb.length() > 0){
			//~ speak(context, sb);
		//~ }
	//~ }	
	
	public boolean isSpeaking(){
		return this.mIsSpeaking;
	}
	
	public void play(MainTTS context, PlayActivity player, StringProvider provider){
		TextToSpeech tts = context.getSpeaker();
		mIsSpeaking = true;
		this.provider = provider;		
		tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener(){
			@Override
			public void onUtteranceCompleted(String utteranceId) {
				if (dialog != null && dialog.isShowing()){
					return;
				}
				
				Log.i("Maia", "Listener fired with " + utteranceId + "; Current is " + new Integer(current).toString());
				if (skipping){
					Log.i("Maia", "Skipping is true, leaving...");
				}
				int uId = new Integer(utteranceId);					
				
				if (uId < current - 1 || uId >= current){
					return;
				}								
				playCurrent();
			}
						
		});
					
		this.context = context;
		this.player = player;	
		isStoped = false;
		playable = new ArrayList<String>();
		current = 0;
		
		this.counter = 0;
		this.shakeTime = 0;
		this.startMeasure = 0;
		
		int totalFootprint = 0;
		getDataString(context);
		
		
		
	/* 	if (playable.size() > 0){
			for (String s : playable){				
				try {
					speak(context, s);
				} catch (Exception ex){
					//intentionally do nothing
				}
			}		
		} */
	}

	private void getDataString(MainTTS context) {
		if (this.dialog != null){
			try {
				this.dialog.dismiss();
			} catch (Exception ex) {
				Log.e("Maia", ex.toString());
			}
		}
				
		dialog = ProgressDialog.show(this.player, "", 
                this.player.getResources().getString(R.string.pleaseWait), true);	
		if (this.provider == null || !this.provider.skipPleaseWait()){
			speak(context, context.getResources().getString(R.string.pleaseWait));
		}
		
		FetchThread ft = new FetchThread();
		ft.start();		
	}
	
	private void playCurrent(){
		if (this.inPause){
			return;
		}
		skipping = false;
		Log.i("Maia", "playCurrent called ");
		if (isStoped || this.playable == null || current >= this.playable.size()){
			Log.i("Maia", "exiting");
			if (playable == null){
				Log.i("Maia", "playable is null");
			} else if (this.playable.size() <= current){
				Log.i("Maia", "playable size reached");
			}
			mIsSpeaking = false;
			callSpeakCompletedObservers();
			return;
		}
		String s = this.playable.get(current);		
		current++;
		if (s == null){
			playCurrent();
		}
		Log.i("Maia", String.format("calling speak with %s", s));
		speak(this.context, s);
	}
	
	private void callSpeakCompletedObservers(){
		if (this.speakCompleteObservers == null){
			return;
		}
		
		for (ISpeakCompleteObserver observer : this.speakCompleteObservers){
			observer.speakCompleted(this);
		}
	}
	
	private void callSpeakCompletedObserversPause(){
		if (this.speakCompleteObservers == null){
			return;
		}
		
		for (ISpeakCompleteObserver observer : this.speakCompleteObservers){
			observer.onPause(this);
		}
	}
	
	private void callSpeakCompletedObserversResume(){
		if (this.speakCompleteObservers == null){
			return;
		}
		
		for (ISpeakCompleteObserver observer : this.speakCompleteObservers){
			observer.onResume(this);
		}
	}
	
	private boolean speak(MainTTS context, StringBuilder sb){
		return speak(context, sb.toString());
	}
	
	
	private boolean speak(MainTTS context, String s){
		inPause = false;
		Log.i("Maia", String.format("Entering speak with %s", s));
		if (context.isReady()){	
			Log.i("Maia", String.format("Context found to be OK"));			
			TextToSpeech tts = context.getSpeaker();	
			if (isStoped){
				Log.i("Maia", String.format("isStoped flag is ON. Stopping"));
				tts.stop();
			}		
		
			int queueMode = TextToSpeech.QUEUE_FLUSH;						
			HashMap<String,String> map = new HashMap<String,String>();
			map.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, new Integer(current - 1).toString());
			Log.i("Maia", String.format("calling tts speak with %s", s));
			tts.speak(s, queueMode, map);			
			//context.showMessage(s);
			return true;
		}
		return false;
	}
	
	public ArrayList<StringProvider> getProviders (){
		return this.providers;
	}
	
	public void setProviders(ArrayList<StringProvider> providers){
		this.providers = providers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void stop() {
		this.isStoped = true;
		this.playable = null;
		this.mIsSpeaking = false;
		this.inPause = false;
		this.current = 0;
		Log.i("Maia", String.format("Stop called"));
		this.context.getSpeaker().stop();
		callSpeakCompletedObservers();
	}
	
	public void skipCurrent(){
		Log.i("Maia", "Skip current called");
		if (this.mIsSpeaking){
			if (current == 0 && !this.context.getSpeaker().isSpeaking()){
				return;
			}
			this.playable.add(this.current, "Skipping.");
			//skipping = true;
			this.context.getSpeaker().stop();
			//playCurrent();
		}
	}
	
	public void pause(){
		Log.i("Maia", "Skip current called");
		if (this.mIsSpeaking){	
			this.inPause = true;
			this.current -= 1;
			this.context.getSpeaker().stop();
			callSpeakCompletedObserversPause();
		}
	}
	
	public void resume(){
		Log.i("Maia", "Skip current called");
		if (this.mIsSpeaking){	
			this.inPause = false;
			playCurrent();
			callSpeakCompletedObserversResume();
		}
	}
	
	public boolean isInPause(){
		return this.inPause;
	}
	
	public void addSpeakCompleteObserver(ISpeakCompleteObserver observer){
		if (observer == null){
			return;
		}
		ensureSpeakCompleteObserverList();
		if (!this.speakCompleteObservers.contains(observer)){
			this.speakCompleteObservers.add(observer);
		}
	}
	
	private void ensureSpeakCompleteObserverList(){
		if (this.speakCompleteObservers == null){
			this.speakCompleteObservers = new ArrayList<ISpeakCompleteObserver>();			
		}
	}

	public void setShakeTime(long shakeTime) {
		this.shakeTime = shakeTime;
	}

	public long getShakeTime() {
		return shakeTime;
	}

	public void setStartMeasure(long startMeasure) {
		this.startMeasure = startMeasure;
	}

	public long getStartMeasure() {
		return startMeasure;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}
}
