package com.bubblelake.maia;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;

public class ProviderViewItemAdd extends ProviderViewItem {
	public static final int ADD_ACTIVITY_ID = 11;
	public ProviderViewItemAdd(Context context) {
		super(context);
		
	}
	
	public ProviderViewItemAdd(Context context, StringProvider provider, Greeting greeting) {
		this(context);	
		this.provider = provider;
		this.greeting = greeting;
	}
	
	@Override
	protected void subscribeButtonsListeners() {		
		setAddButton();
	}
	
	@Override
	protected  int get_layout_id(){
		return R.layout.provider_view_item_add;
	}	
	
	private void setAddButton() {
//		ImageButton configureButton = (ImageButton)findViewById(R.id.addButton);
//		if (!provider.canConfigureAdd()){
//			configureButton.setImageResource(R.drawable.add);
//		}
		
		this.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					if (provider.canConfigureAdd()){
						launch_configure_activity(ADD_ACTIVITY_ID);
					} else {
						greeting.getProviders().add(provider);
						refreshList();
						Toast t = Toast.makeText(getContext(), provider.getDisplayName() + " was added to " + greeting.getName(), Toast.LENGTH_LONG);
						t.show();
					}
				}			
			});
	}
	
	private void refreshList(){
		((GreetingViewActivity)this.getContext()).fillProvidersList();
		((GreetingViewActivity)this.getContext()).setSelectedItem(getProviderIndex());
	}
	
	private int getProviderIndex(){
		int providerIndex = this.greeting.getProviders().indexOf(this.provider);		
		return providerIndex;
	}

}
