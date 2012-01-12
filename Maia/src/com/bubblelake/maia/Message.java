package com.bubblelake.maia;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Message implements Comparable<Message>{
    static SimpleDateFormat FORMATTER = 
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    private String title;
    private URL link;
    private String description;
    private Date date;
    private String temperature;
    private String temperatureUnit;

      // getters and setters omitted for brevity
    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
        // pad the date if necessary
        while (!date.endsWith("00")){
            date += "0";
        }
        try {
            this.date = FORMATTER.parse(date.trim());
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        } 
    }
    
    @Override
    public String toString() {
         return "";
    }

    @Override
    public int hashCode() {
         return 0;
    }
    
//    @Override
//    public boolean equals(Object obj) {
//        
//    }
//      // sort by date
    public int compareTo(Message another) {
        if (another == null) return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
    }

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public String getStrippedTitle(){
		int lastDash = title.lastIndexOf(" - ");
		if (lastDash > -1){
			String result = title.substring(0, lastDash);
			return result;
		}
		return title;
	}

	public void setLink(URL link) {
		this.link = link;
	}

	public URL getLink() {
		return link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public String getTemperatureUnit() {
		return temperatureUnit;
	}
}