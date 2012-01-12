package com.bubblelake.maia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class AboutActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes(); 
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT; 
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 
        
		setOkButtonListener();
    }
    
   
    
    private void setOkButtonListener() {
		Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				finish();
			}		
		});
	}
}
