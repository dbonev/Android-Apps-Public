package com.bubblelake.maia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.util.Xml;

public class SettingsManager {
	public static final String GREETING = "greeting";
	public static final String PROVIDER = "provider";
	public static final String TITLE = "title";
	public static final String NAME = "name";	
	public static final String CLASS = "class";
	public static final String EXTRA_1 = "extra1";
	public static final String EXTRA_2 = "extra2";
	public static final String EXTRA_3 = "extra3";	
	public static final String FILENAME = "Settings.xml";	
	public static final String SHAKE_TO_SKIP = "shakeToSkip";
	private boolean shakeToSkip;
	private static SettingsManager instance;
	private List<Greeting> greetings;
	private Context context;
	
	public static SettingsManager getInstance(Context context){
		if (instance == null){
			instance = new SettingsManager(context);
			instance.greetings = instance.Load(context);
		} 
		
		return instance;
	}
	
	public String getString(int resId){
		return this.context.getResources().getString(resId);
	}
	
	private SettingsManager(Context context){
		this.context = context;			
	}
	
	public List<Greeting> getGreetings(){
		return this.greetings;
	}
	
	public boolean getShakeToSkip(){
		return this.shakeToSkip;
	}
	
	public void setShakeToSkip(boolean value){
		this.shakeToSkip = value;
	}
	
	private List<Greeting> Load(Context context) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Greeting> greetings = new ArrayList<Greeting>();        
        try {
        	FileInputStream input = getInputStream(context);
        	if (input == null){
        		loadDefaultGreeting(greetings);
        		return greetings;
        	}
            DocumentBuilder builder = factory.newDocumentBuilder();            
            Document dom = builder.parse(input);
            Element root = dom.getDocumentElement();
            NodeList shakeToSkipNodes = root.getElementsByTagName(SHAKE_TO_SKIP);
            if (shakeToSkipNodes.getLength() > 0){
            	Node shakeToSkipNode = shakeToSkipNodes.item(0);
            	String value = shakeToSkipNode.getAttributes().getNamedItem("value").getNodeValue();
            	if (value == null || value.equals("")){
            		this.shakeToSkip = false;
            	} else {
            		this.shakeToSkip = Boolean.parseBoolean(value);
            	}
            }
            
            NodeList items = root.getElementsByTagName(GREETING);
            for (int i=0;i<items.getLength();i++){
                Greeting greeting = new Greeting();
                greetings.add(greeting);
                Node item = items.item(i);
                greeting.setName(item.getAttributes().getNamedItem(NAME).getNodeValue());
                NodeList properties = item.getChildNodes();
                for (int j=0;j<properties.getLength();j++){                	
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    String className = property.getAttributes().getNamedItem(CLASS).getNodeValue();
                    String displayName = property.getAttributes().getNamedItem(NAME).getNodeValue();
                    Class<?> classType = Class.forName(className);
                    StringProvider provider = (StringProvider)classType.newInstance();
                    provider.setDisplayName(displayName);
                    String extra1 = property.getAttributes().getNamedItem(EXTRA_1).getNodeValue();
                    String extra2 = property.getAttributes().getNamedItem(EXTRA_2).getNodeValue();
                    String extra3 = property.getAttributes().getNamedItem(EXTRA_3).getNodeValue();
                    
                    provider.setExtra1(extra1);
                    provider.setExtra2(extra2);
                    provider.setExtra3(extra3);
                    
                    greeting.getProviders().add(provider);
                }
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		return greetings; 
	}
	
	private void loadDefaultGreeting(ArrayList<Greeting> greetings) {
		Greeting g = constructDefaultGreeting();
		greetings.add(g);
	}

	public Greeting constructDefaultGreeting() {
		Greeting g = new Greeting();
		g.getProviders().add(new TimeOfDayGreetingProvider());
		SalutationProvider salutation = new SalutationProvider();
		salutation.setExtra1("you rock star");
		g.getProviders().add(salutation);
		g.getProviders().add(new CurrentTimeProvider());
		//g.getProviders().add(new CityNameProvider());
		WeatherProvider wp = new WeatherProvider();
		wp.setExtra1("f");
		g.getProviders().add(wp);
		g.getProviders().add(new CalendarAppointmentsProvider());					
		g.getProviders().add(new DailyHoroscopeProvider());
		g.getProviders().add(new StockQuoteProvider("AAPL, GOOG, MSFT, ^IXIC, ^DJI"));
		g.getProviders().add(createCNNProvider("5"));		
		g.getProviders().add(createCNNSportsProvider());
		g.getProviders().add(new JokeOfTheDayProvider());
		//g.getProviders().add(new GenericRSSFeedProvider(PredefinedNewsConfigureActivity.NEWS_TYPE_TECH));
		g.setName("Daily Briefing");
		return g;
	}

	public static StringProvider createCNNProvider(String numberOfNews){
		StringProvider cnnProvider = new GenericRSSFeedProvider();
		cnnProvider.setExtra1(NewsFeedResolverTop.CNN_NAME);
		cnnProvider.setExtra2(NewsFeedResolverTop.CNN_FEED);
		cnnProvider.setExtra3(numberOfNews);
		return cnnProvider;
	}
	
	public static StringProvider createCNNSportProvider(String numberOfNews){
		StringProvider cnnProvider = new GenericRSSFeedProvider();
		cnnProvider.setExtra1(SportNewsFeedResolver.CNN_NAME);
		cnnProvider.setExtra2(SportNewsFeedResolver.CNN_FEED);
		cnnProvider.setExtra3(numberOfNews);
		return cnnProvider;
	}
	
	public static StringProvider createBBCProvider(String numberOfNews){
		StringProvider cnnProvider = new GenericRSSFeedProvider();
		cnnProvider.setExtra1(NewsFeedResolverTop.BBC_NAME);
		cnnProvider.setExtra2(NewsFeedResolverTop.BBC_FEED);
		cnnProvider.setExtra3(numberOfNews);
		return cnnProvider;
	}
	
	public static StringProvider createTechMemeProvider(String numberOfNews){
		StringProvider techmemeProvider = new GenericRSSFeedProvider();
		techmemeProvider.setExtra1(NewsFeedResolverTech.TECHMEME_NAME);
		techmemeProvider.setExtra2(NewsFeedResolverTech.TECHMEME_FEED);
		techmemeProvider.setExtra3(numberOfNews);
		return techmemeProvider;
	}
	
	public static StringProvider createTechCrunchProvider(){
		StringProvider techmemeProvider = new GenericRSSFeedProvider();
		techmemeProvider.setExtra1(NewsFeedResolverTech.TECHCRUNCH_NAME);
		techmemeProvider.setExtra2(NewsFeedResolverTech.TECHCRUNCH_FEED);
		techmemeProvider.setExtra3("5");
		return techmemeProvider;
	}
	
	private StringProvider createCNNSportsProvider(){
		StringProvider cnnProvider = new GenericRSSFeedProvider();
		cnnProvider.setExtra1(SportNewsFeedResolver.CNN_NAME);
		cnnProvider.setExtra2(SportNewsFeedResolver.CNN_FEED);
		cnnProvider.setExtra3("5");
		return cnnProvider;
	}
	
	private FileInputStream getInputStream(Context context) {
		try {
			return context.openFileInput(FILENAME);
		} catch (FileNotFoundException e) {			
			return null;
		}
	}

	public void Save(List<Greeting> greetings, Context context){
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", SHAKE_TO_SKIP);
	        serializer.attribute("", "value", new Boolean(shakeToSkip).toString());
	        serializer.startTag("", "greetings");
	        serializer.attribute("", "number", String.valueOf(greetings.size()));
	        for (Greeting greeting: greetings){
	            serializer.startTag("", GREETING);
	            serializer.attribute("", NAME, greeting.getName());
	            for (StringProvider sp : greeting.getProviders()){
		            serializer.startTag("", PROVIDER);
		            serializer.attribute("", CLASS, sp.getClass().getName());
		            serializer.attribute("", NAME, sp.getDisplayNameForSave());
		            serializer.attribute("", EXTRA_1, sp.getExtra1());
		            serializer.attribute("", EXTRA_2, sp.getExtra2());
		            serializer.attribute("", EXTRA_3, sp.getExtra3());
		            serializer.endTag("", PROVIDER);
	            }
	            serializer.endTag("", GREETING);
	        }
	        serializer.endTag("", "greetings");
	        serializer.endDocument();
	        String content = writer.toString();
	        
	        try {
				FileOutputStream stream = context.openFileOutput(FILENAME, Activity.MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(stream);
				osw.write(content);
				osw.flush();
				osw.close();
				stream.flush();
				stream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}

	public void Save(Context context) {
		Save(this.greetings, context);
	}
}
