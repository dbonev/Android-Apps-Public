package com.bubblelake.maia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NewsConfigureActivity extends Activity {
	private Greeting greeting;
	private StringProvider provider;
	public static final int SAVED = 1;
	public static final int CANCELLED = 2;
	private boolean addFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_configure);
        
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
		
		TextView nameView = (TextView)findViewById(R.id.nameText);
		nameView.setText(this.provider.getDisplayName());
		
		fillNewsCount();
		
		setOKButtonListener();
		setCancelButtonListener();
    }

	private void setOKButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				provider.setExtra3(getCount());
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
	
	private void fillNewsCount(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.newsCountSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.newsCount, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(getSelectedIndex());
    }
	
	private int getSelectedIndex() {
		if (this.provider.extra3 == null || this.provider.extra3.equals("")){
			return 0;
		}
		
		try {
			int i = Integer.parseInt(this.provider.extra3);
			if (i == 5){
				return 1;
			} else if (i == 10){
				return 2;
			} else if (i == 15) {
				return 3;
			} else {
				return 0;
			}
		} catch (Exception ex){
			return 0;
		}
	}

	private String getCount(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.newsCountSpinner);
    	String asString = (String)typeSpinner.getSelectedItem();
    	if (asString.equals("All")){
    		return "-1";
    	}
    	return asString;
    }
}
