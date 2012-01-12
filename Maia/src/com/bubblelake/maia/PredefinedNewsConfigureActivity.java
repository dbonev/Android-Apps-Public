package com.bubblelake.maia;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;

public class PredefinedNewsConfigureActivity extends Activity {
    public static final String NEWS_TYPE_TECH = "tech";
    public static final String NEWS_TYPE_TOP = "top";
    public static final String NEWS_TYPE_SPORT = "sport";
    private String newsType;
    private NewsFeedResolver resolver;
    private Greeting greeting;
    @Override
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_predefined__news_add);
		Intent intent = this.getIntent();
		this.newsType = intent.getStringExtra("type");
		this.resolver = createResolver();
		
		int greetingIndex = intent.getIntExtra("greeting", 0);		
		greeting = GreetingsFactory.getGreetings(this).get(greetingIndex);
		    
		buildUI();
    }
    
    private void buildUI(){
		ListView itemsList = (ListView)findViewById(R.id.workPanel);
		NewsFeedProviderListViewAdapter adapter = new NewsFeedProviderListViewAdapter(greeting, this.resolver);
		itemsList.setAdapter(adapter);
    }
    
    private NewsFeedResolver createResolver(){
		//TODO: add conditional logic here
    	if (NEWS_TYPE_TECH.equals(this.newsType)){
    		return new NewsFeedResolverTech();
    	} else if (NEWS_TYPE_TOP.equals(this.newsType)){
    		return new NewsFeedResolverTop();
    	} else if (NEWS_TYPE_SPORT.equals(this.newsType)){
    		return new SportNewsFeedResolver();
    	}
    	return new NewsFeedResolverTop();
    }
}
