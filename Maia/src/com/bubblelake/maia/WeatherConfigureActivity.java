package com.bubblelake.maia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class WeatherConfigureActivity extends Activity {
	private Greeting greeting;
	private StringProvider provider;
	public static final int SAVED = 1;
	public static final int CANCELLED = 2;
	private boolean addFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_configure_activity);
        
        Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		
		int providerIndex = intent.getIntExtra("provider", 0);
		
		if (providerIndex < 0){
			String providerClass = intent.getStringExtra("providerClass");
			try {
				Class c = Class.forName(providerClass);
				provider = (StringProvider)c.newInstance();
				greeting.getProviders().add(provider);
				addFlag = true;
				
			} catch (Exception ex){
				
			}
		} else {			
			provider = greeting.getProviders().get(providerIndex);
		}
		
		fillScales();
		
		setOKButtonListener();
		setCancelButtonListener();
    }

	private void setOKButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				provider.setExtra1(getSelectedScale());
				setResult(SAVED);
				finish();
			}		
		});
	}
	
	private void setCancelButtonListener() {
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (addFlag){
					greeting.getProviders().remove(provider);
				}
				setResult(CANCELLED);
				finish();
			}		
		});
	}
	
	private void fillScales(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.scaleSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.degreeScale, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(getSelectedIndex());
    }
	
	
	private int getSelectedIndex() {
		if (this.provider.extra1 == null || this.provider.extra1.equals("")){
			return 0;
		}
		
		if (this.provider.extra1.equals("f")){
			return 1;
		}
		
		return 0;
	}

	private String getSelectedScale(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.scaleSpinner);
    	return typeSpinner.getSelectedItem().toString().toLowerCase();
    }
}
