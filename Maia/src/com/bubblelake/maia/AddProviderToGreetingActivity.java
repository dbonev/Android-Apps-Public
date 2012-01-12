package com.bubblelake.maia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



public class AddProviderToGreetingActivity extends Activity {
	Greeting greeting;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_provider);
        
        Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
	
		fillProvidersList();
	
		setCancelButtonListener();
    }
    
   
    
    private void setCancelButtonListener() {
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				finish();
			}		
		});
	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	setResult(resultCode);
    	finish();
    }
    
//    private void setPlayButtonListener() {
//		Button playButton = (Button)findViewById(R.id.playButton);
//		playButton.setOnClickListener( new View.OnClickListener() {			
//			@Override
//			public void onClick(View v) {				
//				greeting.Play(this);
//			}		
//		});
//	}
//    
    private void save(){
    	SettingsManager.getInstance(this).Save(this);
    }

	public void fillProvidersList() {
		ListView providers = (ListView)findViewById(R.id.workPanel);
		StringProviderListViewAdapter adapter = new StringProviderListViewAdapter(greeting, true);
		providers.setAdapter(adapter);
	}
}
