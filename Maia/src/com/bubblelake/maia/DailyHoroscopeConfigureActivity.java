package com.bubblelake.maia;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class DailyHoroscopeConfigureActivity extends Activity {
	private Greeting greeting;
	private StringProvider provider;
	public static final int SAVED = 1;
	public static final int CANCELLED = 2;
	private boolean addFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_horoscope_configure);
        
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
		
		fillCategories();
		fillProviders();
		
		setOKButtonListener();
		setCancelButtonListener();
    }

	private void setOKButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				provider.setExtra1(getSelectedSign());
				provider.setExtra2(getSelectedSource());
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
	
	private void fillCategories(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.signSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.signs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(getSelectedIndex());
    }
	
	private void fillProviders(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.sourceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.astrologySites, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(getSelectedIndexSource());
    }
	
	private int getSelectedIndexSource() {
		if (this.provider.getExtra2().equals(DailyHoroscopeProvider.ASTROSAGE_COM)){
			return 1;
		} else {
			return 0;
		}
	}
	
	private int getSelectedIndex() {
		if (this.provider.extra1 == null || this.provider.extra1.equals("")){
			return 0;
		}
		
		String sign = this.provider.extra1.toLowerCase().trim();
		
		if (sign.equals("aries")){
			return 0;
		} else if (sign.equals("taurus")){
			return 1;
		} else if (sign.equals("gemini")){
			return 2;
		}else if (sign.equals("cancer")){
			return 3;
		}else if (sign.equals("leo")){
			return 4;
		}else if (sign.equals("virgo")){
			return 5;
		}else if (sign.equals("libra")){
			return 6;
		}else if (sign.equals("scorpio")){
			return 7;
		}else if (sign.equals("sagittarius")){
			return 8;
		}else if (sign.equals("capricorn")){
			return 9;
		}else if (sign.equals("aquarius")){
			return 10;
		}else if (sign.equals("pisces")){
			return 11;
		}
		
		return 0;
	}

	private String getSelectedSign(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.signSpinner);
    	return (String)typeSpinner.getSelectedItem();
    }
	
	private String getSelectedSource(){
    	Spinner typeSpinner = (Spinner)findViewById(R.id.sourceSpinner);
    	return (String)typeSpinner.getSelectedItem();
    }
}
