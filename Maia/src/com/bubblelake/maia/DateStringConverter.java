package com.bubblelake.maia;

import android.content.res.Resources;

public class DateStringConverter {
	public static String getDateString(int date, Resources r){
		switch (date){
		case 1:
			return r.getString(R.string.first);
		case 2:
			return r.getString(R.string.second);
		case 3:
			return r.getString(R.string.third);
		case 4:
			return r.getString(R.string.fourth);
		case 5:
			return r.getString(R.string.fifth);
		case 6:
			return r.getString(R.string.sixth);
		case 7:
			return r.getString(R.string.seventh);
		case 8:
			return r.getString(R.string.eight);
		case 9:
			return r.getString(R.string.nineth);
		case 10:
			return r.getString(R.string.tenth);
		case 11:
			return r.getString(R.string.eleventh);
		case 12:
			return r.getString(R.string.twelfth);
		case 13:
			return r.getString(R.string.thirteenth);
		case 14:
			return r.getString(R.string.fourteenth);
		case 15:
			return r.getString(R.string.fifteenth);
		case 16:
			return r.getString(R.string.sixteenth);
		case 17:
			return r.getString(R.string.seventeenth);
		case 18:
			return r.getString(R.string.eighteenth);
		case 19:
			return r.getString(R.string.nineteenth);
		case 20:
			return r.getString(R.string.twentieth);
		case 21:
			return r.getString(R.string.twentyFirst);
		case 22:
			return r.getString(R.string.twentySecond);
		case 23:
			return r.getString(R.string.twentyThird);
		case 24:
			return r.getString(R.string.twentyFourth);
		case 25:
			return r.getString(R.string.twentyFifth);
		case 26:
			return r.getString(R.string.twentySixth);
		case 27:
			return r.getString(R.string.twentySeventh);
		case 28:
			return r.getString(R.string.twentyEight);
		case 29:
			return r.getString(R.string.twentyNineth);
		case 30:
			return r.getString(R.string.thirtieth);
		case 31:
			return r.getString(R.string.thirtyFirst);		
		default:
			return "";
				
		}
	}
}
