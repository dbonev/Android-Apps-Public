package com.bubblelake.maia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainTTS extends Activity implements OnInitListener {
	private TextToSpeech mTts;
	private static final int MY_DATA_CHECK_CODE = 1;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 2;
	private Location mCurrentLocation;
	private Address mCurrentAddress;
	private long addressTimestamp;
	private boolean canPlay = false;
	private LinearLayout mWorkPanel;
	private static MainTTS instance;
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		instance = this;
        setContentView(R.layout.main);
                
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
        ImageButton speakButton = (ImageButton) findViewById(R.id.speakButton);
        ImageButton speakAboutkButton = (ImageButton) findViewById(R.id.speakAboutButton);
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {	
					startVoiceRecognitionActivity();
				}
			});
            
            speakAboutkButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainTTS.this, SpeakHelpActivity.class);			
					startActivityForResult(intent, 0);
				}
			});
        } else {
            speakButton.setVisibility(View.GONE);
            speakAboutkButton.setVisibility(View.GONE);        		
        }
        
        mCurrentLocation = getCurrentLocation();  
        mCurrentAddress = getCurrentAddress(mCurrentLocation);

		buildUI();		
    }
    
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Example: Tell me the news.");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }   
	
	public void buildUI(){		
		mWorkPanel = (LinearLayout)this.findViewById(R.id.workPanel);
		mWorkPanel.removeAllViews();
        List<Greeting> greetings = GreetingsFactory.getGreetings(this);
        for (Greeting g : greetings){
        	GreetingDisplayControl dcc = new GreetingDisplayControl(this, g);
        	mWorkPanel.addView(dcc);
        }
	}
    
    public Location getLocation(){
    	return this.mCurrentLocation;
    }
    
    public Address getAddress(){
    	return this.mCurrentAddress;
    }
    
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        } else if (requestCode == GreetingDisplayControl.EDIT_GREETING_CODE){
			buildUI();
		} else if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {            
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS); 
            Greeting temp = SpeechRequestResolver.GetTempGreeting(matches);
            GreetingsFactory.setTemporaryGreeting(temp);
            Intent intent = new Intent(this, PlayActivity.class);
    		intent.putExtra("greeting", -1);	    		
    		try {
    			startActivityForResult(intent, GreetingDisplayControl.PLAY_GREETING_CODE);
    		} catch(ActivityNotFoundException ex){
    			throw ex;
    		}        
    	}

    }
    
    private Address getCurrentAddress(Location location){
    	if (location == null){
    		return null;
    	}
    	if (this.mCurrentAddress != null && location.getTime() <= addressTimestamp){
    		return this.mCurrentAddress;
    	}
    	Geocoder gc = new Geocoder(this, Locale.getDefault());
    	try {
			List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses.size() >= 1){
				addressTimestamp = location.getTime();
				return addresses.get(0);
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
    	
    }
    
    private Location getCurrentLocation() {       	
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lNET = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        //temp
        if (lGPS == null){
        	lGPS = new Location(LocationManager.GPS_PROVIDER);
        	lGPS.setLatitude(42.7);
        	lGPS.setLongitude(23.333);
        }
        
        if (lGPS == null){
        	return lNET;
        }
        else if (lNET == null){
        	return lGPS;
        }
        
        //both not null
        if (lGPS.getTime() > lNET.getTime()){
        	return lGPS;
        } else {
        	return lNET;
        }        		
	}
    
    public TextToSpeech getSpeaker(){
    	return this.mTts;
    }

	@Override
	public void onInit(int status) {
		mTts.setLanguage(Locale.US);
		this.canPlay = true;		
//		String myText1 = "Did you sleep well?";
//		String myText2 = "I hope so, because it's time to wake up.";
//		mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
//		mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);
	}


	public boolean isReady() {
		return canPlay;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		instance = null;
		if (this.mTts != null){
			try {
				this.mTts.shutdown();
			} catch (Exception ex) {
				//intentionally do nothing
			}
		}
	}
	
	public static MainTTS getInstance(){
		return instance;
	}
	
	public void showMessage(String message){
		if (message != null && !message.equals("")){
			Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			t.show();
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuItem item;
    	item = menu.add(0, 1, 1, getText(R.string.addGreeting));   
    	item.setIcon(R.drawable.ic_menu_add);
    	item = menu.add(0, 2, 2, getText(R.string.aboutMenu));
    	item.setIcon(R.drawable.ic_menu_help);
    	return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item){
    	int i = item.getItemId();    	
    	if (i == 1){
    		Greeting g = new Greeting(); //SettingsManager.getInstance(this).constructDefaultGreeting();
    		g.setName("My Say List");
    		GreetingsFactory.getGreetings(this).add(g);
    		
    		Intent intent = new Intent(this, GreetingViewActivity.class);
			intent.putExtra("greeting", GreetingsFactory.getGreetings(this).indexOf(g));	
			try {
				startActivityForResult(intent, GreetingDisplayControl.EDIT_GREETING_CODE);
			} catch(ActivityNotFoundException ex){
				throw ex;
			}
    	}
    	
    	if (i == 2){
    		Intent intent = new Intent(this, AboutActivity.class);			
			try {
				startActivityForResult(intent, 0);
			} catch(ActivityNotFoundException ex){
				throw ex;
			}
    	}
    	
    	return true;
	}
}