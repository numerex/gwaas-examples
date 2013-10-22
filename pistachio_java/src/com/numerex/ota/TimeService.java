package com.numerex.ota;

/*
 * @author:  byron appelt, 2008.09
 * 			 (c) 2008 Ublip
 * 
 *  NOTES:  andrew wolverton, 2008, 09
 *  		1.  added getDBDateTime functions
 */

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;

public class TimeService {
	
	public long getTimeInMillis() {
		return System.currentTimeMillis();
	}

	public long getTimeInSecs() {
		return System.currentTimeMillis() / 1000;
	}
/*
	public String getDBDateTimeYYYYMMDDHHMMSS() throws Exception {
		
		return getDBDateTime().replace(':'), '').replace(('-'), '').trim();
	
	}
*/	
	public String getDBDateTimeFromMillis(long millis) throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date(millis));
		return getDBDateTime(cal);
	}
	
	public String getDBDateTimeFromSeconds(long secs) throws Exception {
		return getDBDateTimeFromMillis(secs * 1000);
	}
	
	public String getDBDateTimeFromYYYYMMDDHHMMSS(String s) throws Exception {
		if (s == null || s.length() < 1) throw new Exception("invalid input parameters");
		
		String year = s.substring(0, 4);
		String month = s.substring(4, 6);
		String day = s.substring(6, 8);
		
		String hour = s.substring(8, 10);
		String minutes = s.substring(10, 12);
		String seconds = s.substring(12, 14);
		
		return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
	}
/*
	public long getMillisFromYYYYMMDDHHMMSS(String s) throws Exception {
		if (s == null || s.length() < 1) throw new Exception("invalid input parameters");
		
		String dbDateTime = getDBDateTimeFromYYYYMMDDHHMMSS(s);
		Calendar cal = getDBDateTimeToCal(dbDateTime);
		return cal.getTime().getTime();
	}

	public long getSecondsFromFromYYYYMMDDHHMMSS(String s) throws Exception {
		return getMillisFromYYYYMMDDHHMMSS(s) / 1000;
	}
*/	
	public String getDBDateTime() {
		return getDBDateTime(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
	}
	
	public String getDBDateTime(Calendar cal) {
		String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
		if (month.length() < 2) month = "0" + month;
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)); 
		if (day.length() < 2) day = "0" + day;
		String year = Integer.toString(cal.get(Calendar.YEAR)); 

		String hours = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if (hours.length() < 2) hours = "0" + hours;
		String minutes = Integer.toString(cal.get(Calendar.MINUTE));
		if (minutes.length() < 2) minutes = "0" + minutes;
		String seconds = Integer.toString(cal.get(Calendar.SECOND));
		if (seconds.length() < 2) seconds = "0" + seconds;

		//'05/29/2008 18:30:38'
		String date = "" 
			+ (year) + "-" 
			+ (month) + "-" 
			+ (day) + " "
			
			+ (hours) + ":" 
			+ (minutes) + ":" 
			+ (seconds)
			+ "";
		return date;
	}
	
	public String getDBDateTime(int year, int month, int day, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		return getDBDateTime(cal);
	}

//	public Calendar getDBDateTimeToCal(String dbDateTime)  {
	
//		if (dbDateTime == null || dbDateTime.length() < 5 /*|| minutes < 0*/) throw new RuntimeException("getDBDateTimeToCal invalid parameters");
		
		//'05/29/2008 18:30:38'
	/*
		String date = dbDateTime.split(" ")[0];
		String time = dbDateTime.split(" ")[1];
		
		int year = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[0]) : Integer.parseInt(date.split("-")[0]);
		int month = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[1]) : Integer.parseInt(date.split("-")[1]);
		int day = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[2]) : Integer.parseInt(date.split("-")[2]);
		
		int hour = Integer.parseInt(time.split("[:\\.]")[0]);
		int minute = Integer.parseInt(time.split("[:\\.]")[1]);
		int second = Integer.parseInt(time.split("[:\\.]")[2]);
		
		Calendar then = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		then.set(Calendar.HOUR_OF_DAY, hour);
		then.set(Calendar.MINUTE, minute);
		then.set(Calendar.SECOND, second);
		then.set(Calendar.DAY_OF_MONTH, day);
		then.set(Calendar.MONTH, month - 1);
		then.set(Calendar.YEAR, year);
		then.set(Calendar.MILLISECOND, 0);
		
		return then;
	}
*/
	
	/*
	public boolean isMinOldYYYY_MM_DD_(String dbDateTime, int minutes) throws Exception {
		if (dbDateTime == null || dbDateTime.length() < 5) throw new Exception("isMinOld invalid parameters");
		
		Calendar then = getDBDateTimeToCal(dbDateTime);
		
		long comparator = minutes * 60000;
		long now = System.currentTimeMillis() - comparator;
		
		Calendar nowC = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		nowC.setTimeInMillis(now);
		
		System.out.println("now=<" + nowC.getTime() + ">");
		System.out.println("then=<" + then.getTime() + ">");
		System.out.println();
		
		if (nowC.after(then)) return true;
		else return false;
	}
*/
	
	/*
	public boolean isMinFutureYYYY_MM_DD_(String dbDateTime, int minutes) throws Exception {
		if (dbDateTime == null || dbDateTime.length() < 5 ) throw new Exception("isMinOld invalid parameters");
		
		//'05/29/2008 18:30:38'
		String date = dbDateTime.split(" ")[0];
		String time = dbDateTime.split(" ")[1];
		
		int year = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[0]) : Integer.parseInt(date.split("-")[0]);
		int month = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[1]) : Integer.parseInt(date.split("-")[1]);
		int day = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[2]) : Integer.parseInt(date.split("-")[2]);
		
		int hour = Integer.parseInt(time.split(":")[0]);
		int minute = Integer.parseInt(time.split(":")[1]);
		int second = Integer.parseInt(time.split(":")[2]);
		
		Calendar then = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		then.set(Calendar.HOUR_OF_DAY, hour);
		then.set(Calendar.MINUTE, minute + minutes);
		then.set(Calendar.SECOND, second);
		then.set(Calendar.DAY_OF_MONTH, day);
		then.set(Calendar.MONTH, month - 1);
		then.set(Calendar.YEAR, year);
		
		Calendar nowC = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		
		System.out.println("now=<" + nowC.getTime() + ">");
		System.out.println("then=<" + then.getTime() + ">");
		System.out.println();
		
		if (nowC.before(then)) return true;
		else return false;
	}
*/
	
	/*
	public boolean isMinOld(String dbDateTime, int minutes) throws Exception {
		if (dbDateTime == null || dbDateTime.length() < 5 || minutes < 0) throw new Exception("isMinOld invalid parameters");
		
		//'05/29/2008 18:30:38'
		String date = dbDateTime.split(" ")[0];
		String time = dbDateTime.split(" ")[1];
		
		int month = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[0]) : Integer.parseInt(date.split("-")[0]);
		int day = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[1]) : Integer.parseInt(date.split("-")[1]);
		int year = (date.indexOf("/")) > 0 ? Integer.parseInt(date.split("/")[2]) : Integer.parseInt(date.split("-")[2]);
		
		int hour = Integer.parseInt(time.split(":")[0]);
		int minute = Integer.parseInt(time.split(":")[1]);
		int second = Integer.parseInt(time.split(":")[2]);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		
		long then = cal.getTimeInMillis();
		long now = System.currentTimeMillis();
		long comparator = minutes * 60000;
		
		if ((now - comparator) > then) return true;
		else return false;
	}
	*/
}
