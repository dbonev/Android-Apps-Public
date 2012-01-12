package com.bubblelake.maia;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PageDownloader {
	public static String getPage(String urlString){
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
		    url = new URL(urlString);
		    is = url.openStream();  // throws an IOException
		    dis = new DataInputStream(new BufferedInputStream(is));
		    
		    while ((line = dis.readLine()) != null) {
		        sb.append(line);
		    }
		    
		} catch (MalformedURLException mue) {
		     mue.printStackTrace();
		} catch (IOException ioe) {
		     ioe.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException ioe) {
		        // nothing to see here
		    }			    
		}
		return sb.toString();
	}
}
