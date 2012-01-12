package com.bubblelake.maia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class ProviderActionsActivity extends Activity {
	Greeting greeting;
	StringProvider provider;
	int providerIndex;
	Toast t;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_actions);
		
		Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		providerIndex = intent.getIntExtra("provider", 0);
		provider = greeting.getProviders().get(providerIndex);
		
		refreshDisplayName();
		
		setUpButton();
		setDownButton();
		setConfigureButton();
		setDeleteButton();
		setOKButton();
		setPlayButton();
	}
	
	@Override
	protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
		refreshDisplayName();
	}

	private void refreshDisplayName() {
		TextView nameView = (TextView)findViewById(R.id.providerName);
		nameView.setText(provider.getDisplayName());
	}
	
	private void setOKButton() {
		Button okButton= (Button)findViewById(R.id.okButton);		
		okButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				SettingsManager.getInstance(ProviderActionsActivity.this).Save(ProviderActionsActivity.this);
				refreshList();
			}
		});
	}
	
	private void setPlayButton() {
		Button playButton= (Button)findViewById(R.id.playButton);
		playButton.setTextSize(25);
		playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.sayit_bg_x));
		Drawable icon = getResources().getDrawable(R.drawable.sayit_i);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 10);
		playButton.setLayoutParams(lp);
		//icon.setBounds(new Rect(5, 5, 37, 37));
		playButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		
		playButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				launchPlayActivity();
			}
		});
	}
	
	protected void launchPlayActivity() {
		Intent intent = new Intent(this, PlayActivity.class);
		intent.putExtra("greeting", GreetingsFactory.getGreetings(this).indexOf(greeting));	
		intent.putExtra("provider", greeting.getProviders().indexOf(this.provider));
		try {
			startActivityForResult(intent, GreetingDisplayControl.PLAY_GREETING_CODE);
		} catch(ActivityNotFoundException ex){
			throw ex;
		}
	}

	private void setDeleteButton() {
		Button delButton= (Button)findViewById(R.id.removeButton);		
		delButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(ProviderActionsActivity.this).create();				
				dialog.setMessage("Are you sure?");
				dialog.setButton("Yes",new DialogInterface.OnClickListener(){

					//@Override
					public void onClick(DialogInterface dialog, int which) {
						greeting.getProviders().remove(provider);
						refreshList();
					}});
				dialog.setButton2("No", new DialogInterface.OnClickListener(){

					//@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}});
				dialog.show();
				
				
			}
		});
	}

	private void setConfigureButton(){
		Button configureButton = (Button)findViewById(R.id.configureButton);
		if (showConfigureButton()){		
			configureButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					launch_configure_activity(ProviderViewItem.ADD_ACTIVITY_ID);
				}			
			});		
		} else {
			configureButton.setVisibility(View.GONE);
		}
		
		if (showConfigureButton()){
			TextView nameView = (TextView)findViewById(R.id.providerName);
			nameView.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					launch_configure_activity(ProviderViewItem.ADD_ACTIVITY_ID);
				}
			});
		}
	}
	
	protected boolean showConfigureButton(){
		return this.provider.canConfigureUpdate();
	}
	
	protected void launch_configure_activity(int id) {
		ConfigureActivityStarter.startConfigureActivity(this.provider, this, id, this.greeting);		
	}

	private void setUpButton() {
		Button upButton = (Button)findViewById(R.id.upButton);
		int providerIndex = this.greeting.getProviders().indexOf(this.provider);
		
		if (providerIndex <= 0){
			upButton.setVisibility(View.GONE);
		} 
		
		upButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveProviderUp();
			}
		
		});		
	}
	
	private void setDownButton() {
		Button downButton = (Button)findViewById(R.id.downButton);
		int providerIndex = getProviderIndex();
		
		if (providerIndex >= this.greeting.getProviders().size() - 1){
			downButton.setVisibility(View.GONE);
		} 
		
		downButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveProviderDown();
			}
		
		});		
	}
	
	private int getProviderIndex(){
		int providerIndex = this.greeting.getProviders().indexOf(this.provider);		
		return providerIndex;
	}

	protected void moveProviderUp() {
		int providerIndex = getProviderIndex();
		if (providerIndex <= 0){
			return;
		}
		
		int prevIndex = providerIndex - 1;
		StringProvider temp = this.greeting.getProviders().get(prevIndex);
		this.greeting.getProviders().set(prevIndex, this.provider);
		this.greeting.getProviders().set(providerIndex, temp);
		
		refresh_up_down_buttons(prevIndex);
		if (t != null){
			t.cancel();
		}
		String text = String.format(getResources().getString(R.string.wasMovedUpToPosition), provider.getDisplayNameForSave(), prevIndex + 1, temp.getDisplayNameForSave());
		t = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		t.show();
		
		//refreshList();
	}

	private void refresh_up_down_buttons(int index) {
		Button vb = (Button)findViewById(R.id.upButton);
		if (index == 0){			
			vb.setVisibility(View.GONE);
		} else {
			vb.setVisibility(View.VISIBLE);
		}
		
		Button db = (Button)findViewById(R.id.downButton);
		if (index >= greeting.getProviders().size() - 1){			
			db.setVisibility(View.GONE);
		}
		else {
			db.setVisibility(View.VISIBLE);
		}
	}
	
	protected void moveProviderDown() {
		int providerIndex = this.greeting.getProviders().indexOf(this.provider);
		if (providerIndex >= this.greeting.getProviders().size() - 1){
			return;
		}
		
		int nextIndex = providerIndex + 1;
		StringProvider temp = this.greeting.getProviders().get(nextIndex);
		this.greeting.getProviders().set(nextIndex, this.provider);
		this.greeting.getProviders().set(providerIndex, temp);
		
		refresh_up_down_buttons(nextIndex);
		
		if (t != null){
			t.cancel();
		}
		String text = String.format(getResources().getString(R.string.wasMovedDownToPosition), provider.getDisplayNameForSave(), nextIndex + 1, temp.getDisplayNameForSave());
		t = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		t.show();
	}
	
	private void refreshList(){
		finish();
	}
}
