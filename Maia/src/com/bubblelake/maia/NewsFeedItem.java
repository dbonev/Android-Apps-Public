package com.bubblelake.maia;

public class NewsFeedItem {
	private String name;
	private String feedUrl;
	private int logoId;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String value){
		this.name = value;
	}
	
	public String getFeedUrl(){
		return this.feedUrl;
	}
	
	public void setFeedUrl(String value){
		this.feedUrl = value;
	}
	
	public int getLogoId(){
		return this.logoId;
	}
	
	public void setLogoId(int value){
		this.logoId = value;
	}
}