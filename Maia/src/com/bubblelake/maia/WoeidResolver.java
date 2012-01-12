package com.bubblelake.maia;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WoeidResolver {
	public static String getWoeid(double latitude, double longitude){
		String url = String.format("http://www.geomojo.org/cgi-bin/reversegeocoder.cgi?long=%s&lat=%s", longitude, latitude);
		String webPage = PageDownloader.getPage(url);
		if (webPage != null){
			int startIndex = webPage.indexOf("<woeid>");
			int finalIndex = webPage.indexOf("</woeid>");
			if (startIndex > -1 && finalIndex > -1){
				startIndex += 7;
				String result = webPage.substring(startIndex, finalIndex);
				return result;
			}
		}
		
		return null;
	}
	
	
	public static String getWoeidFlickr(double latitude, double longitude){
		String url = "http://query.yahooapis.com/v1/public/yql?q=select%20place.woeid%20from%20flickr.places%20where%20lat%3D" + latitude;
		url += "%20and%20lon%3D" + longitude;
		url += "&format=xml";
		String webPage = PageDownloader.getPage(url);
		if (webPage != null){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
            Document dom = null;
			try {
				dom = builder.parse(new ByteArrayInputStream(webPage.getBytes("utf-8")));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Element root = dom.getDocumentElement();
            Element placeElement = (Element)root.getFirstChild().getFirstChild().getFirstChild();
            String result = placeElement.getAttribute("woeid");
            return result;
		} else {
			return null;
		}
		
	}
	
	private static double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
	
	public static String getWoeidYahoo(double latitude, double longitude){		
		String url = "http://where.yahooapis.com/geocode?location=%s,%s&appid=VpMjMK7i&gflags=R";
		latitude = roundTwoDecimals(latitude);
		longitude = roundTwoDecimals(longitude);
		url = String.format(url, latitude, longitude);
		String webPage = PageDownloader.getPage(url);
		if (webPage != null){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
            Document dom = null;
			try {
				dom = builder.parse(new ByteArrayInputStream(webPage.getBytes("utf-8")));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Element root = dom.getDocumentElement();
            NodeList resultElements = root.getElementsByTagName("Result");
            if (resultElements.getLength() == 0){
            	return null;
            }
            Element resultNode = (Element)resultElements.item(0);
            NodeList woeidNodeList = resultNode.getElementsByTagName("woeid");
            if (woeidNodeList.getLength() == 0){
            	return null;
            }
            Element woeidNode = (Element)woeidNodeList.item(0);
            String result = woeidNode.getTextContent();
            return result;
		} else {
			return null;
		}
		
	}
	
	
}
