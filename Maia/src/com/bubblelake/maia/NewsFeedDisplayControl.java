package com.bubblelake.maia;

import android.content.Context;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.app.Activity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class NewsFeedDisplayControl extends LinearLayout {
	private NewsFeedItem item;
	private Greeting greeting;
	
	public NewsFeedDisplayControl(Context context){
		super(context);
	}
	
	public NewsFeedDisplayControl(Context context, NewsFeedItem item, Greeting greeting){
		this(context);
		this.item = item;
		this.greeting = greeting;
	}
	
	public void inflate() {		
		LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
		View res = inflater.inflate(R.layout.news_feed_display_item, null);		
		this.addView(res);
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.loc_bg));
		
		TextView nameText = (TextView)findViewById(R.id.nameText);
		String text = this.item.getName();
		nameText.setText(text);	
		
		ImageView imgView = (ImageView)findViewById(R.id.logoImg);
		if (this.item.getLogoId() > 0){
			imgView.setVisibility(View.VISIBLE);
			imgView.setImageResource(this.item.getLogoId());
			
		} else {
			imgView.setVisibility(View.GONE);
		}
		
		subscribeButtonsListeners();
		
	}
	
	private void subscribeButtonsListeners() {
		//ImageButton addButton = (ImageButton)findViewById(R.id.addButton);
		this.setOnClickListener(
					new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							GenericRSSFeedProvider p = new GenericRSSFeedProvider(PredefinedNewsConfigureActivity.ACCESSIBILITY_SERVICE);
							p.setExtra1(item.getName());
							p.setExtra2(item.getFeedUrl());
							greeting.getProviders().add(p);
							Context context = NewsFeedDisplayControl.this.getContext();
							//SettingsManager.getInstance(context).Save(context);
							((Activity)context).setResult(ProviderConfigureActivity.SAVED);
							((Activity)context).finish();
						}
					}
				);
	}

	public NewsFeedItem getItem(){
		return this.item;
	}
}