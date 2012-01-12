package com.bubblelake.maia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

public class GreetingViewActivity extends Activity {
	Greeting greeting;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_view);
        
        Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		
		TextView nameText = (TextView)findViewById(R.id.nameText);
		nameText.setText(greeting.getName() + "  " + getResources().getText(R.string.tapToChangeName));
		
		nameText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetingViewActivity.this, GreetingNameActivity.class);
				intent.putExtra("greeting", GreetingsFactory.getGreetings(GreetingViewActivity.this).indexOf(greeting));	
				try {
					((Activity)GreetingViewActivity.this).startActivityForResult(intent, GreetingDisplayControl.EDIT_GREETING_CODE);
				} catch(ActivityNotFoundException ex){
					throw ex;
				}
			}
		});
		fillProvidersList();
		setOKButtonListener();
//		setCancelButtonListener();
//		setAddButtonListener();
//		setPlayButtonListener();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
		this.fillProvidersList();
		TextView nameText = (TextView)findViewById(R.id.nameText);
		nameText.setText(greeting.getName() + "  " + getResources().getText(R.string.tapToChangeName));
    	if (requestCode == ProviderViewItemAdd.ADD_ACTIVITY_ID){
    		if (resultCode == ProviderConfigureActivity.SAVED){
				this.fillProvidersList();
    		}
    	}
    }
    
//    private void setAddButtonListener() {
//    	Button addButton = (Button)findViewById(R.id.addButton);
//		addButton.setOnClickListener( new View.OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				launch_add_activity();
//			}
//
//				
//		});
//	}
    
//    private void launch_add_activity() {
//    	Intent intent = new Intent(this, AddProviderToGreetingActivity.class);
//		intent.putExtra("greeting", GreetingsFactory.getGreetings(this).indexOf(greeting));	
//		try {
//			((Activity)this).startActivityForResult(intent, 10);
//		} catch(ActivityNotFoundException ex){
//			throw ex;
//		}
//	}	
	private void setOKButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.requestFocus();
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				save();
				finish();
			}		
		});
	}
    
//    private void setCancelButtonListener() {
//		Button cancelButton = (Button)findViewById(R.id.cancelButton);
//		cancelButton.setOnClickListener( new View.OnClickListener() {			
//			@Override
//			public void onClick(View v) {				
//				finish();
//			}		
//		});
//	}
    
//    private void setPlayButtonListener() {
//		final Button playButton = (Button)findViewById(R.id.playButton);
//		if (MainTTS.getInstance() != null){
//			playButton.setEnabled(true);
//			playButton.setFocusable(true);
//			playButton.setOnClickListener( new View.OnClickListener() {			
//				@Override
//				public void onClick(View v) {		
//					final TextToSpeech tts = MainTTS.getInstance().getSpeaker();					
//					if (tts.isSpeaking()){
//						greeting.stop();
//						tts.stop();						
//						playButton.setText(getResources().getString(R.string.sayIt));
//					} else {
//						greeting.play(MainTTS.getInstance());
//						playButton.setText(getResources().getString(R.string.stop));
//					}
//				}		
//			});
//		} else {
//			playButton.setEnabled(false);
//			playButton.setFocusable(false);
//		}
//	}
    
    private void save(){
//    	TextView nameText = (TextView)findViewById(R.id.nameText);
//		String name = nameText.getText().toString();
//		this.greeting.setName(name);
    	SettingsManager.getInstance(this).Save(this);
    }
	
	public void setSelectedItem(int index) {
		ListView providers = (ListView)findViewById(R.id.workPanel);
		providers.setVisibility(View.GONE);		
		providers.setSelection(index);
		providers.setVisibility(View.VISIBLE);
	}

	public void fillProvidersList() {
		ListView providers = (ListView)findViewById(R.id.workPanel);
		providers.setVisibility(View.GONE);
		StringProviderListViewAdapter adapter = new StringProviderListViewAdapter(greeting, false);
		providers.setAdapter(adapter);
		providers.setVisibility(View.VISIBLE);
	}
}
