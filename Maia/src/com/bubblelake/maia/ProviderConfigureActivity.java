package com.bubblelake.maia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProviderConfigureActivity extends Activity {
	private Greeting greeting;
	private StringProvider provider;
	public static final int SAVED = 1;
	public static final int CANCELLED = 2;
	private boolean addFlag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_configure);
        
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
		
		TextView nameText = (TextView)findViewById(R.id.nameText);
		nameText.setText(provider.getDisplayName());
		
		setExtra1();
		setExtra2();
		setExtra3();
		
		setOKButtonListener();
		setCancelButtonListener();
    }

	private void setOKButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				save();
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
	
	private void save() {
		if (provider.hasExtra1()){			
			EditText eet = (EditText)findViewById(R.id.extra1Text);
			provider.extra1 = eet.getText().toString();
		}
		if (provider.hasExtra2()){			
			EditText eet = (EditText)findViewById(R.id.extra2Text);
			provider.extra2 = eet.getText().toString();
		}
		if (provider.hasExtra3()){			
			EditText eet = (EditText)findViewById(R.id.extra3Text);
			provider.extra3 = eet.getText().toString();
		}	
		
		this.setResult(SAVED);
	}

	private void setExtra1() {
		TextView etv = (TextView)findViewById(R.id.extra1Label);
		EditText eet = (EditText)findViewById(R.id.extra1Text);
		if (this.provider.hasExtra1()){
			etv.setText(provider.getExtra1Label(this));
			eet.setText(provider.getExtra1());
			etv.setVisibility(View.VISIBLE);
			eet.setVisibility(View.VISIBLE);
		} else {
			etv.setVisibility(View.GONE);
			eet.setVisibility(View.GONE);
		}
	}
	
	private void setExtra2() {
		TextView etv = (TextView)findViewById(R.id.extra2Label);
		EditText eet = (EditText)findViewById(R.id.extra2Text);
		if (this.provider.hasExtra2()){
			etv.setText(provider.getExtra2Label(this));
			eet.setText(provider.getExtra2());
			etv.setVisibility(View.VISIBLE);
			eet.setVisibility(View.VISIBLE);
		} else {
			etv.setVisibility(View.GONE);
			eet.setVisibility(View.GONE);
		}
	}

	private void setExtra3() {
		TextView etv = (TextView)findViewById(R.id.extra3Label);
		EditText eet = (EditText)findViewById(R.id.extra3Text);
		if (this.provider.hasExtra3()){
			etv.setText(provider.getExtra3Label(this));
			eet.setText(provider.getExtra3());
			etv.setVisibility(View.VISIBLE);
			eet.setVisibility(View.VISIBLE);
		} else {
			etv.setVisibility(View.GONE);
			eet.setVisibility(View.GONE);
		}
	}
}
