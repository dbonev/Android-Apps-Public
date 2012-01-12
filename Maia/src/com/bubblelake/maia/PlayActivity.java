package com.bubblelake.maia;

import java.security.Provider;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PlayActivity extends Activity {
	Greeting greeting;
	LinearLayout mWorkPanel;
	StringProvider provider;
	CallReceiver phoneListener;
	
	 private SensorManager mSensorManager;
	 private boolean isListenerRegistered;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	  GreetingDisplayControl dcc;
	  
	  CheckBox cb;
	  private SensorEventListener mSensorListener;
	  
	  private void create_new_listener(boolean forceNew){
		  if (mSensorListener != null){
			  return;
		  }
		  mSensorListener = new SensorEventListener() {
	
		    public synchronized void onSensorChanged(SensorEvent se) {
		      if (greeting != null && greeting.isSpeaking()){
			      float x = se.values[0];
			      float y = se.values[1];
			      float z = se.values[2];
			      mAccelLast = mAccelCurrent;
			      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			      float delta = mAccelCurrent - mAccelLast;		      		    
			      
			      //mAccel = mAccel * 0.9f + delta; // perform low-cut filter
			      if (Math.abs(delta) >= 2.9f){
			    	  Log.i("Maia", "Accelerometer delta detected (potential shake) " + delta);
			    	  if (greeting.getStartMeasure() == 0){
			    		  greeting.setStartMeasure(System.currentTimeMillis());		 
			    		  greeting.setCounter(1);
			    		  return;
			    	  }		    	 		    	 
			    	  
			    	  long curr = System.currentTimeMillis();
			    	  if (curr - greeting.getStartMeasure() < 3000){
			    		  greeting.setCounter(greeting.getCounter() + 1);
				    	  if (curr - greeting.getShakeTime() > 5000 && greeting.getCounter() >= 4){				    		  
				    		  Log.i("Maia", "Accelerometer delta detected performing skip " + delta + " curr - st: " + (curr - greeting.getShakeTime()) + " counter " + greeting.getCounter());
				    		  greeting.setShakeTime(curr);				    		  
				    		  greeting.setStartMeasure(0);
				    		  greeting.setCounter(0);
				    		  greeting.skipCurrent();
				    	  } else if (greeting.getCounter() >= 4){
				    		  greeting.setCounter(0);
				    	  }
			    	  }  else {
			    		  greeting.setStartMeasure(curr);
			    		  greeting.setCounter(0);
			    	  }
			    	  
			    	  
			      }
		      }
		    }
		    
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		  };
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    registerListener();
	  }

	  @Override
	  protected void onStop() {
		  if (isListenerRegistered){
			  mSensorManager.unregisterListener(mSensorListener);
		  }
		  TelephonyManager telephonyManager=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		  telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);	
		  
		  if (this.greeting.isSpeaking()){
			  this.dcc.stop_speaking(MainTTS.getInstance().getSpeaker());
		  }
		  
		  super.onStop();
	  }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        create_new_listener(true);
        registerListener();
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes(); 
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT; 
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 
        
        Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		if (greetingIndex >= 0){
			greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		} else {
			greeting = GreetingsFactory.getTemporaryGreeting();
		}
		
		int providerIndex = intent.getIntExtra("provider", -1);
		if (providerIndex >= 0){
			provider = greeting.getProviders().get(providerIndex);
		}
		
		buildUI();
		
		phoneListener = new CallReceiver(this.greeting);
		TelephonyManager telephonyManager=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);		
	}
	
	private void setSpeakCompleteObserver(){
		this.greeting.addSpeakCompleteObserver(new ISpeakCompleteObserver(){
			@Override
			public void speakCompleted(Greeting g){														
			}
			@Override
			public void onPause(Greeting g){
				cb.setVisibility(View.GONE);
				if (isListenerRegistered){
					mSensorManager.unregisterListener(mSensorListener);
					isListenerRegistered = false;
				}
			}
			@Override
			public void onResume(Greeting g){
				cb.setVisibility(View.VISIBLE);
				registerListener();
			}
		});
	}

	private void registerListener() {
		if (SettingsManager.getInstance(this).getShakeToSkip()){
			if (!this.isListenerRegistered){
				create_new_listener(false);
				this.isListenerRegistered = mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
			}
		} else {
			if (this.isListenerRegistered){
				 mSensorManager.unregisterListener(mSensorListener);
			}
			
			this.isListenerRegistered = false;
		}
	}
	
	private void buildUI(){
		mWorkPanel = (LinearLayout)this.findViewById(R.id.workPanel);
		mWorkPanel.removeAllViews();        
    	dcc = new GreetingDisplayControl(this, greeting);
    	mWorkPanel.addView(dcc);
    	mWorkPanel.addView(makeShakeToSkipCheckbox());
    	setSpeakCompleteObserver();
    	dcc.startSpeaking(greeting, this.provider);
	}

	private View makeShakeToSkipCheckbox() {
		cb = new CheckBox(this);
		cb.setText(getResources().getString(R.string.shakeToSkip));
		cb.setChecked(SettingsManager.getInstance(this).getShakeToSkip());
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SettingsManager.getInstance(PlayActivity.this).setShakeToSkip(isChecked);
				SettingsManager.getInstance(PlayActivity.this).Save(PlayActivity.this);
				registerListener();
			}
		});
		return cb;
	}
}
