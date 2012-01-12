package com.bubblelake.maia;

import java.util.ArrayList;

import android.content.Context;

public class StockQuoteProvider extends StringProvider {
	private final static String YAHOO_FINANCE_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=%s&f=nl1p2";
	private String prelude;
	public StockQuoteProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.stockQuotes));
		this.prelude = SettingsManager.getInstance(null).getString(R.string.latestStockQuotes);
	}
	
	public StockQuoteProvider(String symbols){
		this();
		this.extra1 = symbols;
	}
	
	@Override
	public String getString(MainTTS context) {
		return GenericRSSFeedProvider.makeString(getExtendedString(context));
	}
	
	@Override
	public String[] getExtendedString(MainTTS context) {
		String quotes = this.extra1.toUpperCase().trim().replace(" ", "").replace(",", "+").replace(";", "+");
		String url = String.format(YAHOO_FINANCE_URL, quotes);
		String result = PageDownloader.getPage(url);
		String processed = process(result);
		processed = processed.replace(",", ", ");
		
		ArrayList<String> res = new ArrayList<String>();
		res.add(prelude);
		res.add(processed);
		return toStringArray(res);		
	}

	private String process(String result) {
		StringBuilder res = new StringBuilder();
		String[] splitted = result.split("\"\"");
		for (String element : splitted){
			String sub = element.startsWith("\"") ? element.substring(1) : element;
			
			String name = sub.substring(0, sub.indexOf("\""));
			if (name.endsWith(" ") || name.endsWith(".")){
				res.append(element.startsWith("\"") ? element : "\"" + element);
				continue;
			}
			String rest = element.substring(sub.indexOf("\""));
			int lastSpace = name.lastIndexOf(" ");
			if (lastSpace > -1){
				name = name.substring(0, lastSpace);
				res.append("\"" + name + rest);
			} else {
				res.append(element);
			}						
		}
		return res.toString();
	}

	@Override
	public boolean hasExtra1(){
		return true;
	}
	
	@Override
	public String getExtra1Label(Context context){
		return context.getResources().getString(R.string.stockQuoteLabel);		
	}
	
	@Override
	public boolean canConfigureAdd(){
		return true;
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}
	
	@Override
	public String getDisplayName(){
		String suffix = "";
		if (this.extra1 != null && !this.extra1.trim().equals("")){
			suffix = " (" + this.extra1 + ") ";
		}
		return this.displayName + suffix;
	}
}
