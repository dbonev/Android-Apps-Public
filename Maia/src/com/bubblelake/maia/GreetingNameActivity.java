package com.bubblelake.maia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

public class GreetingNameActivity extends Activity {
	Greeting greeting;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_name_activity);
        
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes(); 
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT; 
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 

        
        Intent intent = this.getIntent();
		int greetingIndex = intent.getIntExtra("greeting", 0);
		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		
		EditText nameText = (EditText)findViewById(R.id.nameText);
		nameText.setText(greeting.getName());
		
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText nameText = (EditText)findViewById(R.id.nameText);
				greeting.setName(nameText.getText().toString());
				finish();
			}
		});
		
    }
}
