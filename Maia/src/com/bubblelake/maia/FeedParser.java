package com.bubblelake.maia;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FeedParser {

    private static final String ITEM = "item";
	private static final String TITLE = "title";
	private static final String LINK = "link";
	private static final String DESCRIPTION = "description";
	private static final String PUB_DATE = "pubDate";
	private static final String Y_CONDITIONS = "yweather:condition";

	private boolean parseDescriptions;
	
	public FeedParser(){
		this(false);
	}
	
	public FeedParser(boolean parseDescriptions){
		this.parseDescriptions = parseDescriptions;
	}
    
    protected InputStream getInputStream(String url) {
        try {
//        	URL feedUrl = new URL(url);
//            return feedUrl.openConnection().getInputStream();
        	URL feedUrl = new URL(url);
            InputStream s = feedUrl.openConnection().getInputStream();
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    
    }
      
	public List<Message> parse(String url) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Message> messages = new ArrayList<Message>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(getInputStream(url));
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName(ITEM);
            for (int i=0;i<items.getLength();i++){
                Message message = new Message();
                Node item = items.item(i);
                NodeList properties = item.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(TITLE)){
                        message.setTitle(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(LINK)){
                        message.setLink(property.getFirstChild().getNodeValue());
                    } else if (this.parseDescriptions && name.equalsIgnoreCase(DESCRIPTION)){
                        StringBuilder text = new StringBuilder();
                        NodeList chars = property.getChildNodes();
                        for (int k=0;k<chars.getLength();k++){
                            text.append(chars.item(k).getNodeValue());
                        }
                        message.setDescription(text.toString());
                    } else if (name.equalsIgnoreCase(PUB_DATE)){
                       // message.setDate(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(Y_CONDITIONS)){
                    	String description = property.getAttributes().getNamedItem("text").getNodeValue();
                    	message.setDescription(description);
                    	String temperature = property.getAttributes().getNamedItem("temp").getNodeValue();
                    	message.setTemperature(temperature);
                    	message.setTemperatureUnit(new WeatherProvider().unit);
                    }
                }
                messages.add(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        return messages;
    }
}
