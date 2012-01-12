package com.bubblelake.maia;

import android.telephony.PhoneStateListener;

public class CallReceiver extends PhoneStateListener {
	Greeting greeting;
	public CallReceiver(Greeting greeting){
		this.greeting = greeting;
	}
	
	 @Override
	 public void onCallStateChanged(int state, java.lang.String incomingNumber){
		 if (state == android.telephony.TelephonyManager.CALL_STATE_RINGING && this.greeting != null && this.greeting.isSpeaking()){
			 this.greeting.stop();
		 }
	 }
}
